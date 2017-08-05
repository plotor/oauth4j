package org.zhenchao.passport.oauth.controller;

import com.alibaba.fastjson.JSONObject;
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
import org.zhenchao.oauth.common.ErrorCode;
import org.zhenchao.oauth.common.GlobalConstant;
import static org.zhenchao.oauth.common.GlobalConstant.COOKIE_KEY_USER_LOGIN_SIGN;
import org.zhenchao.oauth.common.exception.OAuthServiceException;
import org.zhenchao.oauth.model.OAuthAppInfo;
import org.zhenchao.oauth.model.Scope;
import org.zhenchao.oauth.model.User;
import org.zhenchao.oauth.model.UserAppAuthorization;
import org.zhenchao.oauth.protocol.enums.ResponseType;
import org.zhenchao.oauth.token.enums.TokenType;
import org.zhenchao.passport.oauth.common.RequestPath;
import org.zhenchao.passport.oauth.pojo.AuthorizationCode;
import org.zhenchao.passport.oauth.pojo.AuthorizeRequestParams;
import org.zhenchao.passport.oauth.pojo.ResultInfo;
import org.zhenchao.passport.oauth.pojo.TokenRequestParams;
import org.zhenchao.passport.oauth.service.AuthorizeService;
import org.zhenchao.oauth.service.AppInfoService;
import org.zhenchao.passport.oauth.service.ParamsValidateService;
import org.zhenchao.oauth.service.ScopeService;
import org.zhenchao.oauth.service.AuthorizeRelationService;
import org.zhenchao.oauth.token.AbstractAccessToken;
import org.zhenchao.oauth.token.MacAccessToken;
import org.zhenchao.oauth.token.generator.AbstractAccessTokenGenerator;
import org.zhenchao.oauth.token.generator.AbstractTokenGenerator;
import org.zhenchao.oauth.token.TokenGeneratorFactory;
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
 * authorization code grant
 *
 * @author zhenchao.wang 2016-12-19 21:19
 * @version 1.0.0
 */
@Controller
@RequestMapping(RequestPath.PATH_ROOT_OAUTH)
public class AuthorizationCodeGrantController {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationCodeGrantController.class);

    @Resource
    private AppInfoService appInfoService;

    @Resource
    private AuthorizeRelationService authorizeRelationService;

    @Resource
    private ScopeService scopeService;

    @Resource
    private ParamsValidateService paramsValidateService;

    @Resource
    private AuthorizeService authorizeService;

    /**
     * issue authorization code
     *
     * @return
     */
    @RequestMapping(value = RequestPath.PATH_OAUTH_AUTHORIZE_CODE, method = {GET, POST}, params = "response_type=code")
    public ModelAndView authorize(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                                  @RequestParam("response_type") String responseType,
                                  @RequestParam("client_id") long clientId,
                                  @RequestParam("redirect_uri") String redirectUri,
                                  @RequestParam(value = "scope", required = false) String scope,
                                  @RequestParam(value = "state", required = false) String state,
                                  @RequestParam(value = "skip_confirm", required = false, defaultValue = "false") boolean skipConfirm,
                                  @RequestParam(value = "force_login", required = false, defaultValue = "false") boolean forceLogin) {

        log.debug("Entering authorize code method...");

        ModelAndView mav = new ModelAndView();

        try {
            redirectUri = URLDecoder.decode(redirectUri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // never happen
        }

        AuthorizeRequestParams requestParams = new AuthorizeRequestParams();
        requestParams.setResponseType(responseType).setClientId(clientId).setRedirectUri(redirectUri).setScope(scope).setState(StringUtils.trimToEmpty(state));
        // 校验授权请求参数
        ErrorCode validateResult = paramsValidateService.validateAuthorizeRequestParams(requestParams);
        if (!ErrorCode.NO_ERROR.equals(validateResult)) {
            // 请求参数有误
            log.error("Request authorize params error, params[{}], errorCode[{}]", requestParams, validateResult);
            if (ErrorCode.INVALID_CLIENT.equals(validateResult) || ErrorCode.INVALID_REDIRECT_URI.equals(validateResult)) {
                /*
                 * If the request fails due to a missing, invalid, or mismatching redirection URI,
                 * or if the client identifier is missing or invalid, the authorization server SHOULD inform the resource owner of the
                 * error and MUST NOT automatically redirect the user-agent to the invalid redirection URI.
                 */
                return JsonView.render(new ResultInfo(validateResult, state), response, false);
            }
            return this.buildErrorResponse(mav, redirectUri, validateResult, state);
        }

        // 获取APP信息
        Optional<OAuthAppInfo> opt = appInfoService.getAppInfo(clientId);
        if (!opt.isPresent()) {
            log.error("Client[id={}] is not exist!", clientId);
            return JsonView.render(new ResultInfo(ErrorCode.CLIENT_NOT_EXIST, state), response, false);
        }

        OAuthAppInfo appInfo = opt.get();
        User user = SessionUtils.getUser(session, CookieUtils.get(request, COOKIE_KEY_USER_LOGIN_SIGN));
        if (null == user || forceLogin) {
            try {
                // 用户未登录或需要强制登录，跳转到登录页面
                UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
                builder.path(RequestPath.PATH_ROOT_LOGIN)
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
            // 用户已授权该APP，下发授权码
            try {
                Optional<AuthorizationCode> optCode = authorizeService.generateAndCacheAuthorizationCode(authorization.get(), requestParams);
                if (optCode.isPresent()) {
                    // 下发授权码
                    String code = optCode.get().getValue();
                    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(appInfo.getRedirectUri());
                    builder.queryParam("code", code);
                    if (StringUtils.isNotEmpty(state)) {
                        builder.queryParam("state", state);
                    }
                    mav.setViewName("redirect:" + builder.toUriString());
                    return mav;
                }
                return this.buildErrorResponse(mav, redirectUri, ErrorCode.GENERATE_CODE_ERROR, state);
            } catch (OAuthServiceException e) {
                log.error("Generate authorization code error!", e);
                return JsonView.render(new ResultInfo(e.getErrorCode(), state), response, false);
            }
        } else {
            // 用户未授权该APP，跳转到授权页面
            List<Scope> scopes = scopeService.getScopes(requestParams.getScope());
            mav.setViewName("user-authorize");
            mav.addObject(GlobalConstant.CALLBACK, HttpRequestUtils.getEncodeRequestUrl(request))
                    .addObject("scopes", scopes).addObject("user", user).addObject("app", appInfo).addObject("state", StringUtils.trimToEmpty(state));
            return mav;
        }

    }

    /**
     * issue accessToken (and refreshToken)
     *
     * @return
     */
    @RequestMapping(value = RequestPath.PATH_OAUTH_AUTHORIZE_TOKEN, method = {GET, POST})
    public ModelAndView issueToken(HttpServletResponse response,
                                   @RequestParam("grant_type") String grantType,
                                   @RequestParam("code") String code,
                                   @RequestParam("redirect_uri") String redirectUri,
                                   @RequestParam("client_id") long clientId,
                                   @RequestParam(value = "client_secret", required = false) String clientSecret,
                                   @RequestParam(value = "token_type", required = false) String tokenType,
                                   @RequestParam(value = "issue_refresh_token", required = false, defaultValue = "true") boolean refresh) {

        log.debug("Entering authorize code method...");

        try {
            redirectUri = URLDecoder.decode(redirectUri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // never happen
        }

        TokenRequestParams requestParams = new TokenRequestParams();
        requestParams.setResponseType(ResponseType.AUTHORIZATION_CODE.getType()).setGrantType(grantType).setCode(code)
                .setRedirectUri(redirectUri).setClientId(clientId).setTokenType(StringUtils.defaultString(tokenType, TokenType.MAC.getValue()))
                .setClientSecret(clientSecret).setIrt(refresh);

        ErrorCode validateResult = paramsValidateService.validateTokenRequestParams(requestParams);
        if (!ErrorCode.NO_ERROR.equals(validateResult)) {
            log.error("Params error when request token, params [{}], error code [{}]", requestParams, validateResult);
            return JsonView.render(new ResultInfo(validateResult, StringUtils.EMPTY), response, false);
        }

        // 校验用户与APP之间是否存在授权关系
        Optional<UserAppAuthorization> opt = authorizeRelationService.getUserAndAppAuthorizationInfo(
                requestParams.getUserId(), requestParams.getAppInfo().getAppId(), CommonUtils.genScopeSign(requestParams.getScope()));
        if (!opt.isPresent()) {
            // 用户与APP之间不存在指定的授权关系
            log.error("No authorization between user[{}] and app[{}] on scope[{}]!",
                    requestParams.getUserId(), requestParams.getAppInfo().getAppId(), requestParams.getScope());
            return JsonView.render(new ResultInfo(ErrorCode.UNAUTHORIZED_CLIENT, StringUtils.EMPTY), response, false);
        }
        requestParams.setUserAppAuthorization(opt.get());

        // 验证通过，下发accessToken
        Optional<AbstractTokenGenerator> optTokenGenerator = TokenGeneratorFactory.getGenerator(requestParams);
        if (!optTokenGenerator.isPresent()) {
            log.error("Unknown grantType[{}] or tokenType[{}]", requestParams.getGrantType(), requestParams.getTokenType());
            return JsonView.render(new ResultInfo(ErrorCode.UNSUPPORTED_GRANT_TYPE, StringUtils.EMPTY), response, false);
        }

        AbstractAccessTokenGenerator accessTokenGenerator = (AbstractAccessTokenGenerator) optTokenGenerator.get();
        Optional<AbstractAccessToken> optAccessToken = accessTokenGenerator.create();
        if (!optAccessToken.isPresent()) {
            log.error("Generate access token failed, params[{}]", requestParams);
            return JsonView.render(new ResultInfo(ErrorCode.INVALID_REQUEST, StringUtils.EMPTY), response, false);
        }

        // not cache
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");

        AbstractAccessToken accessToken = optAccessToken.get();
        try {
            JSONObject result = new JSONObject();
            result.put("access_token", accessToken.getValue());
            result.put("expires_in", accessToken.getExpirationTime());
            if (refresh) {
                // 客户端指定下发refreshToken
                result.put("refresh_token", accessToken.getRefreshToken());
            }
            if (!CommonUtils.checkScopeIsSame(requestParams.getScope(), accessToken.getScope())) {
                // 如果最终下发的scope与请求时不一致，需要说明
                result.put("scope", accessToken.getScope());
            }
            if (TokenType.MAC.equals(accessToken.getType())) {
                // MAC类型token需要指定key和algorithm
                result.put("mac_key", accessToken.getKey());
                result.put("mac_algorithm", ((MacAccessToken) accessToken).getAlgorithm().getValue());
            }
            return JsonView.render(result, response, true);
        } catch (IOException e) {
            log.error("Get string access token error by [{}]!", accessToken, e);
        }

        return JsonView.render(new ResultInfo(ErrorCode.SERVICE_ERROR, StringUtils.EMPTY), response, false);
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
        mav.setViewName("redirect:" + String.format("%s?%s", redirectUri, StringUtils.join(params, "&")));
        return mav;
    }

}
