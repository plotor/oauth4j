package org.zhenchao.oauth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.zhenchao.oauth.common.ErrorCode;
import static org.zhenchao.oauth.common.GlobalConstant.COOKIE_KEY_USER_LOGIN_SIGN;
import org.zhenchao.oauth.common.RequestPath;
import org.zhenchao.oauth.common.exception.VerificationException;
import org.zhenchao.oauth.entity.AppInfo;
import org.zhenchao.oauth.entity.AuthorizeRelation;
import org.zhenchao.oauth.entity.Scope;
import org.zhenchao.oauth.entity.UserInfo;
import org.zhenchao.oauth.pojo.AuthorizeRequestParams;
import org.zhenchao.oauth.pojo.ResultInfo;
import org.zhenchao.oauth.pojo.TokenRelevantRequestParams;
import org.zhenchao.oauth.service.AuthorizeRelationService;
import org.zhenchao.oauth.service.ScopeService;
import org.zhenchao.oauth.token.AbstractAccessToken;
import org.zhenchao.oauth.token.TokenGeneratorFactory;
import org.zhenchao.oauth.token.generator.AbstractAccessTokenGenerator;
import org.zhenchao.oauth.util.CookieUtils;
import org.zhenchao.oauth.util.JsonView;
import org.zhenchao.oauth.util.ResponseUtils;
import org.zhenchao.oauth.util.SessionUtils;

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
@RequestMapping(RequestPath.PATH_ROOT_OAUTH)
public class ImplicitGrantController {

    private static final Logger log = LoggerFactory.getLogger(ImplicitGrantController.class);

    @Resource
    private AuthorizeRelationService authorizeRelationService;

    @Resource
    private ScopeService scopeService;

    /**
     * implicit grant
     *
     * @return
     */
    @RequestMapping(value = RequestPath.PATH_OAUTH_IMPLICIT_TOKEN, method = {GET, POST}, params = "response_type=token")
    public ModelAndView authorize(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                                  @RequestParam("response_type") String responseType,
                                  @RequestParam("client_id") long clientId,
                                  @RequestParam("redirect_uri") String redirectUri,
                                  @RequestParam(name = "scope", required = false) String scope,
                                  @RequestParam(name = "state", required = false) String state,
                                  @RequestParam(name = "token_type", required = false) String tokenType,
                                  @RequestParam(name = "force_login", required = false, defaultValue = "false") boolean forceLogin,
                                  @RequestParam(name = "skip_confirm", required = false, defaultValue = "false") boolean skipConfirm)
            throws VerificationException {

        log.debug("Request implicit token, appId[{}]", clientId);
        ModelAndView mav = new ModelAndView();

        AuthorizeRequestParams requestParams = new AuthorizeRequestParams(responseType, clientId, redirectUri, scope, state);
        ErrorCode validateResult = requestParams.validate();
        if (ErrorCode.NO_ERROR != validateResult) {
            // 请求参数有误
            log.error("Request implicit params error, appId[{}], code[{}]", clientId, validateResult);
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
            return ResponseUtils.buildLoginResponse(request, appInfo);
        }

        Optional<AuthorizeRelation> opt =
                authorizeRelationService.getAuthorizeRelation(user.getId(), requestParams.getClientId(), requestParams.getScope());
        if (opt.isPresent() && skipConfirm) {
            // 用户已授权，下发token
            TokenRelevantRequestParams tokenParams = new TokenRelevantRequestParams(redirectUri, clientId, tokenType, state);
            tokenParams.setUserId(user.getId()).setScope(requestParams.getScope()).setAppInfo(appInfo).setAuthorizeRelation(opt.get());

            // 验证通过，下发accessToken
            AbstractAccessTokenGenerator accessTokenGenerator =
                    (AbstractAccessTokenGenerator) TokenGeneratorFactory.getGenerator(tokenParams.toTokenElement());
            Optional<AbstractAccessToken> optAccessToken = accessTokenGenerator.create();
            if (!optAccessToken.isPresent()) {
                log.error("Generate access token failed, appId[{}], userId[{}]", appInfo.getAppId(), tokenParams.getUserId(), tokenParams.getScope());
                return ResponseUtils.buildImplicitErrorResponse(redirectUri, ErrorCode.INVALID_REQUEST, state);
            }

            AbstractAccessToken accessToken = optAccessToken.get();
            try {
                log.info("Issue implicit access token, appId[{}], userId[{}]", accessToken.getClientId(), accessToken.getUserId());
                // not cache
                response.setHeader("Cache-Control", "no-store");
                response.setHeader("Pragma", "no-cache");
                return ResponseUtils.buildImplicitTokenResponse(accessToken, tokenParams);
            } catch (IOException e) {
                log.error("Issue implicit access token error, appId[{}], userId[{}]", accessToken.getClientId(), accessToken.getUserId(), e);
                return ResponseUtils.buildImplicitErrorResponse(redirectUri, ErrorCode.SERVICE_ERROR, state);
            }
        }
        // 用户未授权该APP，跳转到授权页面
        List<Scope> scopes = scopeService.getScopes(requestParams.getScope());
        return ResponseUtils.buildAuthorizeResponse(request, scopes, user, appInfo, state);
    }

}
