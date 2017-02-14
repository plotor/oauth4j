package org.zhenchao.passport.oauth.controllers;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;
import org.zhenchao.passport.oauth.commons.ErrorCode;
import org.zhenchao.passport.oauth.commons.GlobalConstant;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.COOKIE_KEY_USER_LOGIN_SIGN;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.PATH_OAUTH_IMPLICIT_TOKEN;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.PATH_OAUTH_USER_AUTHORIZE;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.PATH_ROOT_LOGIN;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.PATH_ROOT_OAUTH;
import org.zhenchao.passport.oauth.domain.AuthorizeRequestParams;
import org.zhenchao.passport.oauth.domain.Error;
import org.zhenchao.passport.oauth.domain.TokenRequestParams;
import org.zhenchao.passport.oauth.model.OAuthAppInfo;
import org.zhenchao.passport.oauth.model.Scope;
import org.zhenchao.passport.oauth.model.User;
import org.zhenchao.passport.oauth.model.UserAppAuthorization;
import org.zhenchao.passport.oauth.service.OAuthAppInfoService;
import org.zhenchao.passport.oauth.service.ParamsValidateService;
import org.zhenchao.passport.oauth.service.ScopeService;
import org.zhenchao.passport.oauth.service.UserAppAuthorizationService;
import org.zhenchao.passport.oauth.token.AbstractAccessToken;
import org.zhenchao.passport.oauth.token.MacAccessToken;
import org.zhenchao.passport.oauth.token.generator.AbstractAccessTokenGenerator;
import org.zhenchao.passport.oauth.token.generator.AbstractTokenGenerator;
import org.zhenchao.passport.oauth.token.generator.TokenGeneratorFactory;
import org.zhenchao.passport.oauth.utils.CommonUtils;
import org.zhenchao.passport.oauth.utils.CookieUtils;
import org.zhenchao.passport.oauth.utils.HttpRequestUtils;
import org.zhenchao.passport.oauth.utils.JsonView;
import org.zhenchao.passport.oauth.utils.SessionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * implicit grant
 *
 * @author zhenchao.wang 2017-02-14 13:42
 * @version 1.0.0
 */
@Controller
@RequestMapping(PATH_ROOT_OAUTH)
public class ImplicitGrantController {

    private static final Logger log = LoggerFactory.getLogger(ImplicitGrantController.class);

    @Resource
    private ParamsValidateService paramsValidateService;

    @Resource
    private OAuthAppInfoService appInfoService;

    @Resource
    private UserAppAuthorizationService authorizeRelationService;

    @Resource
    private ScopeService scopeService;

    @RequestMapping(value = PATH_OAUTH_IMPLICIT_TOKEN, method = {GET, POST}, params = "response_type=token")
    public ModelAndView authorize(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                                  @RequestParam("response_type") String responseType,
                                  @RequestParam("client_id") long clientId,
                                  @RequestParam(value = "redirect_uri", required = false) String redirectUri,
                                  @RequestParam(value = "scope", required = false) String scope,
                                  @RequestParam(value = "state", required = false) String state,
                                  @RequestParam(value = "token_type", required = false) String tokenType,
                                  @RequestParam(value = "force_login", required = false, defaultValue = "false") boolean forceLogin,
                                  @RequestParam(value = "skip_confirm", required = false, defaultValue = "false") boolean skipConfirm) {

        log.debug("Entering implicit grant method...");

        ModelAndView mav = new ModelAndView();

        AuthorizeRequestParams requestParams = new AuthorizeRequestParams();
        requestParams.setResponseType(responseType).setClientId(clientId).setRedirectUri(redirectUri).setScope(scope).setState(StringUtils.trimToEmpty(state));

        ErrorCode validateResult = paramsValidateService.validateAuthorizeRequestParams(requestParams);
        if (ErrorCode.NO_ERROR != validateResult) {
            // 请求参数有误
            log.error("Request authorize params error, params[{}], errorCode[{}]", requestParams, validateResult);
            return JsonView.render(new Error(validateResult, state), response, false);
        }

        // 获取APP信息
        Optional<OAuthAppInfo> optAppInfo = appInfoService.getAppInfo(clientId);
        if (!optAppInfo.isPresent()) {
            log.error("Client[id={}] is not exist!", clientId);
            return JsonView.render(new Error(ErrorCode.CLIENT_NOT_EXIST, state), response, false);
        }

        OAuthAppInfo appInfo = optAppInfo.get();
        User user = SessionUtils.getUser(session, CookieUtils.get(request, COOKIE_KEY_USER_LOGIN_SIGN));
        if (null == user || forceLogin) {
            // 用户未登录或需要强制登录，跳转到登录页面
            UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
            builder.path(PATH_ROOT_LOGIN)
                    .queryParam(GlobalConstant.CALLBACK, HttpRequestUtils.getEncodeRequestUrl(request))
                    .queryParam("app_name", appInfo.getAppName());
            mav.setViewName("redirect:" + builder.toUriString());
            return mav;
        }

        Optional<UserAppAuthorization> authorization =
                authorizeRelationService.getUserAndAppAuthorizationInfo(
                        user.getId(), requestParams.getClientId(), CommonUtils.genScopeSign(requestParams.getScope()));
        if (authorization.isPresent() && !skipConfirm) {
            // 用户已授权，下发token
            UserAppAuthorization uaa = authorization.get();
            TokenRequestParams trp = new TokenRequestParams();
            trp.setRedirectUri(redirectUri).setClientId(clientId).setTokenType(StringUtils.defaultString(tokenType, AbstractAccessToken.TokenType.MAC.getValue()));
            trp.setIrt(false).setUserId(user.getId()).setScope(requestParams.getScope()).setAppInfo(appInfo).setUserAppAuthorization(uaa);
            // 验证通过，下发accessToken
            Optional<AbstractTokenGenerator> optTokenGenerator = TokenGeneratorFactory.getGenerator(trp);
            if (!optTokenGenerator.isPresent()) {
                log.error("Unknown grantType[{}] or tokenType[{}]", requestParams.getGrantType(), requestParams.getTokenType());
                return JsonView.render(new Error(ErrorCode.UNSUPPORTED_GRANT_TYPE, StringUtils.EMPTY), response, false);
            }

            AbstractAccessTokenGenerator accessTokenGenerator = (AbstractAccessTokenGenerator) optTokenGenerator.get();
            Optional<AbstractAccessToken> optAccessToken = accessTokenGenerator.create();
            if (!optAccessToken.isPresent()) {
                log.error("Generate access token failed, params[{}]", requestParams);
                return JsonView.render(new Error(ErrorCode.INVALID_REQUEST, StringUtils.EMPTY), response, false);
            }

            // no cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");

            AbstractAccessToken accessToken = optAccessToken.get();
            try {
                JSONObject result = new JSONObject();
                result.put("access_token", accessToken.getValue());
                result.put("expires_in", accessToken.getExpirationTime());
                result.put("refresh_token", refresh ? accessToken.getRefreshToken() : StringUtils.EMPTY);
                if (accessToken instanceof MacAccessToken) {
                    result.put("mac_key", accessToken.getKey());
                    result.put("mac_algorithm", ((MacAccessToken) accessToken).getAlgorithm().getValue());
                }
                return JsonView.render(result, response, true);
            } catch (IOException e) {
                log.error("Get string access token error by [{}]!", accessToken);
            }
        } else {
            // 用户未授权该APP，跳转到授权页面
            List<Scope> scopes = scopeService.getScopes(requestParams.getScope());
            UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
            builder.path(PATH_ROOT_OAUTH + PATH_OAUTH_USER_AUTHORIZE).queryParam(GlobalConstant.CALLBACK, HttpRequestUtils.getEncodeRequestUrl(request));
            mav.setViewName("user-authorize");
            mav.addObject(GlobalConstant.CALLBACK, builder.build(true))
                    .addObject("scopes", scopes).addObject("user", user).addObject("app", appInfo).addObject("state", StringUtils.trimToEmpty(state));
            return mav;
        }
        return mav;
    }

}
