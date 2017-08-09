package org.zhenchao.oauth.controller;

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
import org.zhenchao.oauth.common.RequestPath;
import org.zhenchao.oauth.common.exception.VerificationException;
import org.zhenchao.oauth.entity.AppInfo;
import org.zhenchao.oauth.entity.AuthorizeRelation;
import org.zhenchao.oauth.entity.Scope;
import org.zhenchao.oauth.entity.UserInfo;
import org.zhenchao.oauth.enums.ResponseType;
import org.zhenchao.oauth.handler.AuthCodeCacheHandler;
import org.zhenchao.oauth.pojo.AuthorizationCode;
import org.zhenchao.oauth.pojo.AuthorizeRequestParams;
import org.zhenchao.oauth.pojo.ResultInfo;
import org.zhenchao.oauth.pojo.TokenRelevantRequestParams;
import org.zhenchao.oauth.service.AuthorizeRelationService;
import org.zhenchao.oauth.service.ScopeService;
import org.zhenchao.oauth.token.AbstractAccessToken;
import org.zhenchao.oauth.token.MacAccessToken;
import org.zhenchao.oauth.token.TokenGeneratorFactory;
import org.zhenchao.oauth.token.enums.TokenType;
import org.zhenchao.oauth.token.generator.AbstractAccessTokenGenerator;
import org.zhenchao.oauth.token.generator.AbstractTokenGenerator;
import org.zhenchao.oauth.util.CookieUtils;
import org.zhenchao.oauth.util.HttpRequestUtils;
import org.zhenchao.oauth.util.JsonView;
import org.zhenchao.oauth.util.ResponseUtils;
import org.zhenchao.oauth.util.ScopeUtils;
import org.zhenchao.oauth.util.SessionUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
public class AuthCodeGrantController {

    private static final Logger log = LoggerFactory.getLogger(AuthCodeGrantController.class);

    @Resource
    private AuthorizeRelationService authorizeRelationService;

    @Resource
    private ScopeService scopeService;

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
                                  @RequestParam(name = "scope", required = false) String scope,
                                  @RequestParam(name = "state", required = false) String state,
                                  @RequestParam(name = "skip_confirm", required = false, defaultValue = "false") boolean skipConfirm,
                                  @RequestParam(name = "force_login", required = false, defaultValue = "false") boolean forceLogin)
            throws VerificationException {
        ModelAndView mav = new ModelAndView();
        log.info("Request authorize code, appId[{}]", clientId);

        // 请求参数封装与校验
        AuthorizeRequestParams requestParams = new AuthorizeRequestParams(responseType, clientId, redirectUri, scope, state);
        ErrorCode validateResult = requestParams.validate();
        if (!ErrorCode.NO_ERROR.equals(validateResult)) {
            // 请求参数有误
            log.error("Request authorize params error, appId[{}], errorCode[{}], params[{}]", clientId, validateResult, requestParams);
            if (ErrorCode.INVALID_CLIENT.equals(validateResult) || ErrorCode.INVALID_REDIRECT_URI.equals(validateResult)) {
                /*
                 * If the request fails due to a missing, invalid, or mismatching redirection URI,
                 * or if the client identifier is missing or invalid, the authorization server SHOULD inform the resource owner of the
                 * error and MUST NOT automatically redirect the user-agent to the invalid redirection URI.
                 */
                return JsonView.render(new ResultInfo(validateResult, state), response, false);
            }
            return ResponseUtils.buildErrorResponse(mav, redirectUri, validateResult, state);
        }

        AppInfo appInfo = requestParams.getAppInfo();
        UserInfo user = SessionUtils.getUser(session, CookieUtils.get(request, COOKIE_KEY_USER_LOGIN_SIGN));
        if (null == user || forceLogin) {
            try {
                // 用户未登录或需要强制登录，跳转到登录页面
                log.info("User not login and redirect to login page, appId[{}]", clientId);
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
        requestParams.setUserInfo(user);

        // 获取用户与APP之间的授权关系记录
        Optional<AuthorizeRelation> relation = authorizeRelationService.getAuthorizeRelation(
                user.getId(), requestParams.getClientId(), ScopeUtils.getScopeSign(requestParams.getScope()));

        if (relation.isPresent() && skipConfirm) {
            // 用户已授权该APP，下发授权码
            AuthorizationCode code = new AuthorizationCode(
                    requestParams.getAppInfo(), user.getId(), relation.get().getScope(), requestParams.getRedirectUri());
            String key = code.getValue();
            if (StringUtils.isBlank(key)) {
                log.error("Generate auth code error, appId[{}], userId[{}], scope[{}]", clientId, user.getId(), requestParams.getScope());
                return ResponseUtils.buildErrorResponse(mav, redirectUri, ErrorCode.AUTHORIZATION_CODE_GENERATE_ERROR, state);
            }
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(requestParams.getRedirectUri());
            builder.queryParam("code", code);
            if (StringUtils.isNotEmpty(state)) {
                builder.queryParam("state", state);
            }
            AuthCodeCacheHandler.getInstance().put(key, code);
            mav.setViewName("redirect:" + builder.toUriString());
            return mav;
        } else {
            // 用户未授权该APP，跳转到授权页面
            // TODO 页面跳转需要添加参数签名，防止请求被更改或伪造
            List<Scope> scopes = scopeService.getScopes(requestParams.getScope());
            mav.setViewName("user-authorize");
            mav.addObject(GlobalConstant.CALLBACK, HttpRequestUtils.getEncodeRequestUrl(request))
                    .addObject("scopes", scopes)
                    .addObject("user", user)
                    .addObject("app", appInfo);
            if (StringUtils.isNotBlank(state)) {
                mav.addObject("state", StringUtils.trimToEmpty(state));
            }
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
                                   @RequestParam(name = "client_secret", required = false) String clientSecret,
                                   @RequestParam(name = "token_type", required = false) String tokenType,
                                   @RequestParam(name = "issue_refresh_token", required = false, defaultValue = "true") boolean refresh)
            throws VerificationException {

        log.info("Request authorize token, appId[{}]", clientId);

        TokenRelevantRequestParams requestParams = new TokenRelevantRequestParams();
        requestParams.setResponseType(ResponseType.AUTHORIZATION_CODE.getType()).setGrantType(grantType).setCode(code)
                .setRedirectUri(redirectUri).setClientId(clientId).setTokenType(StringUtils.defaultString(tokenType, TokenType.MAC.getValue()))
                .setClientSecret(clientSecret).setIrt(refresh);

        ErrorCode validateResult = requestParams.validate();
        if (!ErrorCode.NO_ERROR.equals(validateResult)) {
            log.error("Request authorize token with params error, appId[{}], code[{}]", clientId, validateResult);
            return JsonView.render(new ResultInfo(validateResult, StringUtils.EMPTY), response, false);
        }

        // 校验用户与APP之间是否存在授权关系
        Optional<AuthorizeRelation> opt = authorizeRelationService.getAuthorizeRelation(
                requestParams.getUserId(), requestParams.getAppInfo().getAppId(), ScopeUtils.getScopeSign(requestParams.getScope()));
        if (!opt.isPresent()) {
            // 用户与APP之间不存在指定的授权关系
            log.error("No authorization between user[{}] and app[{}] on scope[{}]!",
                    requestParams.getUserId(), requestParams.getAppInfo().getAppId(), requestParams.getScope());
            return JsonView.render(new ResultInfo(ErrorCode.UNAUTHORIZED_CLIENT, StringUtils.EMPTY), response, false);
        }
        requestParams.setAuthorizeRelation(opt.get());

        // 验证通过，下发accessToken
        Optional<AbstractTokenGenerator> optTokenGenerator = TokenGeneratorFactory.getGenerator(requestParams.toTokenElement());
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
            if (!ScopeUtils.isSame(requestParams.getScope(), accessToken.getScope())) {
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

}
