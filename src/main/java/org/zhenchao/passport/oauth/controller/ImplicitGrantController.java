package org.zhenchao.passport.oauth.controller;

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
import org.zhenchao.passport.oauth.common.ErrorCode;
import org.zhenchao.passport.oauth.common.GlobalConstant;
import static org.zhenchao.passport.oauth.common.GlobalConstant.COOKIE_KEY_USER_LOGIN_SIGN;
import static org.zhenchao.passport.oauth.common.GlobalConstant.PATH_OAUTH_IMPLICIT_TOKEN;
import static org.zhenchao.passport.oauth.common.GlobalConstant.PATH_ROOT_LOGIN;
import static org.zhenchao.passport.oauth.common.GlobalConstant.PATH_ROOT_OAUTH;
import org.zhenchao.passport.oauth.domain.AuthorizeRequestParams;
import org.zhenchao.passport.oauth.domain.ResultInfo;
import org.zhenchao.passport.oauth.domain.TokenRequestParams;
import org.zhenchao.oauth.model.OAuthAppInfo;
import org.zhenchao.oauth.model.Scope;
import org.zhenchao.oauth.model.User;
import org.zhenchao.oauth.model.UserAppAuthorization;
import org.zhenchao.passport.oauth.service.OAuthAppInfoService;
import org.zhenchao.passport.oauth.service.ParamsValidateService;
import org.zhenchao.passport.oauth.service.ScopeService;
import org.zhenchao.passport.oauth.service.UserAppAuthorizationService;
import org.zhenchao.passport.oauth.token.AbstractAccessToken;
import org.zhenchao.passport.oauth.token.MacAccessToken;
import org.zhenchao.passport.oauth.token.generator.AbstractAccessTokenGenerator;
import org.zhenchao.passport.oauth.token.generator.AbstractTokenGenerator;
import org.zhenchao.passport.oauth.token.generator.TokenGeneratorFactory;
import org.zhenchao.passport.oauth.util.CommonUtils;
import org.zhenchao.passport.oauth.util.CookieUtils;
import org.zhenchao.passport.oauth.util.HttpRequestUtils;
import org.zhenchao.passport.oauth.util.JsonView;
import org.zhenchao.passport.oauth.util.SessionUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
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

    /**
     * implicit grant
     *
     * must not automatically redirect the user-agent to the invalid redirect uri :
     * 1. missing, invalid, or mismatching redirect uri
     * 2. client id is missing or invalid
     *
     * @return
     */
    @RequestMapping(value = PATH_OAUTH_IMPLICIT_TOKEN, method = {GET, POST}, params = "response_type=token")
    public ModelAndView authorize(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                                  @RequestParam("response_type") String responseType,
                                  @RequestParam("client_id") long clientId,
                                  @RequestParam("redirect_uri") String redirectUri,
                                  @RequestParam(value = "scope", required = false) String scope,
                                  @RequestParam(value = "state", required = false) String state,
                                  @RequestParam(value = "token_type", required = false) String tokenType,
                                  @RequestParam(value = "force_login", required = false, defaultValue = "false") boolean forceLogin,
                                  @RequestParam(value = "skip_confirm", required = false, defaultValue = "false") boolean skipConfirm) {

        log.debug("Entering implicit grant method...");

        ModelAndView mav = new ModelAndView();

        try {
            redirectUri = URLDecoder.decode(redirectUri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // never happen
        }

        AuthorizeRequestParams requestParams = new AuthorizeRequestParams();
        requestParams.setResponseType(responseType).setClientId(clientId).setRedirectUri(redirectUri).setScope(scope).setState(StringUtils.trimToEmpty(state));

        ErrorCode validateResult = paramsValidateService.validateAuthorizeRequestParams(requestParams);
        if (ErrorCode.NO_ERROR != validateResult) {
            // 请求参数有误
            log.error("Request authorize params error, params[{}], errorCode[{}]", requestParams, validateResult);
            return JsonView.render(new ResultInfo(validateResult, state), response, false);
        }

        // 获取APP信息
        Optional<OAuthAppInfo> optAppInfo = appInfoService.getAppInfo(clientId);
        if (!optAppInfo.isPresent()) {
            log.error("Client[id={}] is not exist!", clientId);
            return JsonView.render(new ResultInfo(ErrorCode.CLIENT_NOT_EXIST, state), response, false);
        }

        OAuthAppInfo appInfo = optAppInfo.get();
        User user = SessionUtils.getUser(session, CookieUtils.get(request, COOKIE_KEY_USER_LOGIN_SIGN));
        if (null == user || forceLogin) {
            try {
                // 用户未登录或需要强制登录，跳转到登录页面
                UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
                builder.path(PATH_ROOT_LOGIN)
                        .queryParam(GlobalConstant.CALLBACK, HttpRequestUtils.getEncodeRequestUrl(request))
                        .queryParam("app_name", URLEncoder.encode(appInfo.getAppName(), "UTF-8"));
                mav.setViewName("redirect:" + builder.toUriString());
                return mav;
            } catch (UnsupportedEncodingException e) {
                // never happen
            }
        }

        Optional<UserAppAuthorization> authorization =
                authorizeRelationService.getUserAndAppAuthorizationInfo(
                        user.getId(), requestParams.getClientId(), CommonUtils.genScopeSign(requestParams.getScope()));
        if (authorization.isPresent() && skipConfirm) {
            // 用户已授权，下发token
            UserAppAuthorization uaa = authorization.get();
            TokenRequestParams trp = new TokenRequestParams();
            trp.setRedirectUri(redirectUri).setClientId(clientId).setTokenType(StringUtils.defaultString(tokenType, AbstractAccessToken.TokenType.MAC.getValue()));
            trp.setResponseType(responseType).setIrt(false).setUserId(user.getId()).setScope(requestParams.getScope()).setAppInfo(appInfo).setUserAppAuthorization(uaa);
            // 验证通过，下发accessToken
            Optional<AbstractTokenGenerator> optTokenGenerator = TokenGeneratorFactory.getGenerator(trp);
            if (!optTokenGenerator.isPresent()) {
                log.error("Generate implicit access token failed, unknown tokenType[{}]", trp.getTokenType());
                return this.buildErrorResponse(mav, redirectUri, ErrorCode.UNSUPPORTED_GRANT_TYPE, state);
            }

            AbstractAccessTokenGenerator accessTokenGenerator = (AbstractAccessTokenGenerator) optTokenGenerator.get();
            Optional<AbstractAccessToken> optAccessToken = accessTokenGenerator.create();
            if (!optAccessToken.isPresent()) {
                log.error("Generate access token failed, params[{}]", requestParams);
                return this.buildErrorResponse(mav, redirectUri, ErrorCode.INVALID_REQUEST, state);
            }

            // not cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");

            List<String> params = new ArrayList<>();
            AbstractAccessToken accessToken = optAccessToken.get();
            try {
                params.add(String.format("access_token=%s", accessToken.getValue()));
                params.add(String.format("token_type=%s", accessToken.getType().getValue()));
                if (AbstractAccessToken.TokenType.MAC.equals(accessToken.getType())) {
                    // mac类型token
                    params.add(String.format("mac_key=%s", accessToken.getKey()));
                    params.add(String.format("mac_algorithm=%s", ((MacAccessToken) accessToken).getAlgorithm().getValue()));
                }
                params.add(String.format("expires_in=%d", accessToken.getExpirationTime()));
                if (!CommonUtils.checkScopeIsSame(scope, accessToken.getScope())) {
                    // 最终授权的scope与请求的scope不一致，返回说明
                    params.add(String.format("scope=%s", URLEncoder.encode(accessToken.getScope(), "UTF-8")));
                }
                if (StringUtils.isNotBlank(state)) {
                    params.add(String.format("state=%s", state));
                }
                mav.setViewName("redirect:" + String.format("%s#%s", redirectUri, StringUtils.join(params, "&")));
                return mav;
            } catch (IOException e) {
                log.error("Get string access token error by [{}]!", accessToken, e);
                return this.buildErrorResponse(mav, redirectUri, ErrorCode.SERVICE_TEMPORARILY_UNAVAILABLE, state);
            }
        } else {
            // 用户未授权该APP，跳转到授权页面
            List<Scope> scopes = scopeService.getScopes(requestParams.getScope());
            mav.setViewName("user-authorize");
            mav.addObject(GlobalConstant.CALLBACK, HttpRequestUtils.getEncodeRequestUrl(request)).addObject("scopes", scopes)
                    .addObject("user", user).addObject("app", appInfo).addObject("state", StringUtils.trimToEmpty(state));
            return mav;
        }
    }

    /**
     * build error response
     *
     * @param mav
     * @param redirectUri
     * @param errorCode
     * @param state
     * @return
     */
    private ModelAndView buildErrorResponse(ModelAndView mav, String redirectUri, ErrorCode errorCode, String state) {
        List<String> params = new ArrayList<>();
        params.add(String.format("error=%s", errorCode.getCode()));
        if (StringUtils.isNotBlank(errorCode.getDesc())) {
            try {
                params.add(String.format("error_description=%s", URLEncoder.encode(errorCode.getDesc(), "UTF-8")));
            } catch (UnsupportedEncodingException e) {
                // never happen
            }
        }
        if (StringUtils.isNotBlank(state)) {
            params.add(String.format("state=%s", state));
        }
        mav.setViewName("redirect:" + String.format("%s#%s", redirectUri, StringUtils.join(params, "&")));
        return mav;
    }

}
