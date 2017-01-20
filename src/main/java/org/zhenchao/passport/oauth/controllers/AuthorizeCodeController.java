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
import org.zhenchao.passport.oauth.model.AuthorizeRequestParams;
import org.zhenchao.passport.oauth.model.Scope;
import org.zhenchao.passport.oauth.model.User;
import org.zhenchao.passport.oauth.model.UserAppAuthorization;
import org.zhenchao.passport.oauth.service.ScopeService;
import org.zhenchao.passport.oauth.service.UserAppAuthorizationService;
import org.zhenchao.passport.oauth.utils.CommonUtils;
import org.zhenchao.passport.oauth.utils.CookieUtils;
import org.zhenchao.passport.oauth.utils.HttpRequestUtils;
import org.zhenchao.passport.oauth.utils.ResultUtils;
import org.zhenchao.passport.oauth.utils.SessionUtils;
import org.zhenchao.passport.oauth.validate.AuthorizeParamsValidator;
import org.zhenchao.passport.oauth.validate.ParamsValidator;

import java.io.IOException;
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
    private UserAppAuthorizationService authorizationService;

    @Resource
    private ScopeService scopeService;

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
        ParamsValidator paramsValidator = new AuthorizeParamsValidator();
        // 校验授权请求参数
        ErrorCode validateResult = paramsValidator.validate(params);
        if (!ErrorCode.NO_ERROR.equals(validateResult)) {
            // 请求参数有误
            log.error("Request authorize params error, params[{}], errorCode[{}]", params, validateResult);
            return ResultUtils.genFailedStringResult(validateResult, StringUtils.EMPTY);
        }

        User user = SessionUtils.getUser(session, CookieUtils.get(request, COOKIE_KEY_USER_LOGIN_SIGN));
        if (null == user) {
            // 用户未登录，跳转到登录页面
            UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
            builder.path("/login").queryParam("callback", HttpRequestUtils.getEncodeRequestUrl(request));
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
            builder.path(PATH_OAUTH_AUTHORIZE_CODE).queryParam("callback", "callback", HttpRequestUtils.getEncodeRequestUrl(request));
            request.setAttribute("callback", builder.build(true));
            request.setAttribute("scopes", scopes);
            request.setAttribute("user", user);
            return "user-authorize";
        }

        return "redirect:/error";
    }

}
