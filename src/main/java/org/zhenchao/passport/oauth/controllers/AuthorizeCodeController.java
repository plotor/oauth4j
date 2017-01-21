package org.zhenchao.passport.oauth.controllers;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;
import org.zhenchao.passport.oauth.commons.ErrorCode;
import org.zhenchao.passport.oauth.commons.GlobalConstant;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.COOKIE_KEY_USER_LOGIN_SIGN;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.PATH_OAUTH_AUTHORIZE_CODE;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.PATH_OAUTH_USER_AUTHORIZE;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.PATH_ROOT_LOGIN;
import org.zhenchao.passport.oauth.model.AuthorizeRequestParams;
import org.zhenchao.passport.oauth.model.OAuthAppInfo;
import org.zhenchao.passport.oauth.model.Scope;
import org.zhenchao.passport.oauth.model.User;
import org.zhenchao.passport.oauth.model.UserAppAuthorization;
import org.zhenchao.passport.oauth.service.OAuthAppInfoService;
import org.zhenchao.passport.oauth.service.ParamsValidateService;
import org.zhenchao.passport.oauth.service.ScopeService;
import org.zhenchao.passport.oauth.service.UserAppAuthorizationService;
import org.zhenchao.passport.oauth.utils.CommonUtils;
import org.zhenchao.passport.oauth.utils.CookieUtils;
import org.zhenchao.passport.oauth.utils.HttpRequestUtils;
import org.zhenchao.passport.oauth.utils.ResultUtils;
import org.zhenchao.passport.oauth.utils.SessionUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 授权码模式授权控制器
 *
 * @author zhenchao.wang 2016-12-19 21:19
 * @version 1.0.0
 */
@Controller
@RequestMapping(GlobalConstant.PATH_ROOT_OAUTH)
public class AuthorizeCodeController {

    private static final Logger log = LoggerFactory.getLogger(AuthorizeCodeController.class);

    @Resource
    private OAuthAppInfoService appInfoService;

    @Resource
    private UserAppAuthorizationService authorizationService;

    @Resource
    private ScopeService scopeService;

    @Resource
    private ParamsValidateService paramsValidateService;

    @RequestMapping(value = PATH_OAUTH_AUTHORIZE_CODE, method = {GET, POST}, params = "response_type=code")
    public String authorize(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session,
            @RequestParam("response_type") String responseType,
            @RequestParam("client_id") long clientId,
            @RequestParam(value = "redirect_uri", required = false) String redirectUri,
            @RequestParam(value = "scope", required = false) String scope,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "skip_confirm", required = false, defaultValue = "false") boolean skipConfirm,
            @RequestParam(value = "force_login", required = false, defaultValue = "false") boolean forceLogin,
            @RequestParam(value = "issue_refresh_token", required = false, defaultValue = "true") boolean issueRefreshToken,
            @RequestParam(value = "_json", required = false, defaultValue = "false") boolean jsonResponse) {

        log.debug("Entering authorize code method...");

        AuthorizeRequestParams params = new AuthorizeRequestParams().setResponseType(responseType).setClientId(clientId)
                .setRedirectUri(redirectUri).setScope(scope).setState(StringUtils.isBlank(state) ? StringUtils.EMPTY : state);
        // 校验授权请求参数
        ErrorCode validateResult = paramsValidateService.validateAuthorizeCodeRequestParams(params);
        if (!ErrorCode.NO_ERROR.equals(validateResult)) {
            // 请求参数有误
            log.error("Request authorize params error, params[{}], errorCode[{}]", params, validateResult);
            return ResultUtils.genFailedStringResult(validateResult, StringUtils.EMPTY);
        }

        // 获取APP信息
        OAuthAppInfo appInfo = appInfoService.getAppInfo(clientId).get();

        User user = SessionUtils.getUser(session, CookieUtils.get(request, COOKIE_KEY_USER_LOGIN_SIGN));
        if (null == user) {
            // 用户未登录，跳转到登录页面
            UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
            builder.path("/login")
                    .queryParam("callback", HttpRequestUtils.getEncodeRequestUrl(request))
                    .queryParam("app_name", appInfo.getAppName());
            try {
                response.sendRedirect(builder.build().toUriString());
            } catch (IOException e) {
                // never happen
            }
        }

        Optional<UserAppAuthorization> authorization =
                authorizationService.getUserAndAppAuthorizationInfo(
                        user.getId(), params.getClientId(), CommonUtils.genScopeSign(params.getScope()));

        if (authorization.isPresent()) {
            // 用户已授权该APP
        } else {
            // 用户未授权该APP，跳转到授权页面
            List<Scope> scopes = scopeService.getScopes(params.getScope());
            UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
            builder.path(PATH_OAUTH_USER_AUTHORIZE).queryParam("callback", "callback", HttpRequestUtils.getEncodeRequestUrl(request));
            request.setAttribute("callback", builder.build(true));
            request.setAttribute("scopes", scopes);
            request.setAttribute("user", user);
            request.setAttribute("app", appInfo);
            return "user-authorize";
        }

        return "redirect:/error";
    }

    @RequestMapping(value = PATH_OAUTH_USER_AUTHORIZE, method = {POST})
    public String userAuthorize(
            HttpServletResponse response,
            @RequestParam("user_id") long userId,
            @RequestParam("client_id") long clientId,
            @RequestParam("scope") String scope,
            @RequestParam("callback") String callback) {

        log.debug("Entering user authorize method...");

        if (userId < 0 || StringUtils.isBlank(callback)) {
            log.error("User authorize request params error, userId[{}], callback[{}]", userId, callback);
            UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
            builder.path(PATH_ROOT_LOGIN);
            builder.queryParam("callback", callback);
            try {
                response.sendRedirect(builder.toUriString());
            } catch (IOException e) {
                // never happen
            }
        }

        // 添加用户授权关系
        UserAppAuthorization authorization = new UserAppAuthorization();
        authorization.setUserId(userId);
        authorization.setAppId(clientId);
        authorization.setScope(scope);
        authorization.setScopeSign(CommonUtils.genScopeSign(scope));
        authorization.setCreateTime(new Date());
        authorization.setCreateTime(authorization.getCreateTime());
        if (authorizationService.replaceUserAndAppAuthorizationInfo(authorization)) {
            // 更新用户授权关系成功
            try {
                response.sendRedirect(callback);
            } catch (IOException e) {
                // never happen
            }
        }
        return ResultUtils.genFailedStringResult(ErrorCode.LOCAL_SERVICE_ERROR);
    }

}
