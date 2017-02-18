package org.zhenchao.passport.oauth.interceptors;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;
import org.zhenchao.passport.oauth.commons.GlobalConstant;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.COOKIE_KEY_USER_LOGIN_SIGN;
import org.zhenchao.passport.oauth.model.OAuthAppInfo;
import org.zhenchao.passport.oauth.service.OAuthAppInfoService;
import org.zhenchao.passport.oauth.utils.CookieUtils;
import org.zhenchao.passport.oauth.utils.HttpRequestUtils;
import org.zhenchao.passport.oauth.utils.SessionUtils;

import java.net.URLEncoder;
import java.util.Optional;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录验证拦截器
 *
 * @author zhenchao.wang 2017-01-02 15:25
 * @version 1.0.0
 */
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

    @Resource
    private OAuthAppInfoService oAuthAppInfoService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String userId = request.getParameter("user_id");
        String key = StringUtils.isBlank(userId) ? CookieUtils.get(request, COOKIE_KEY_USER_LOGIN_SIGN) : DigestUtils.md5Hex(userId);

        if (StringUtils.isNotBlank(key) && null != SessionUtils.getUser(request.getSession(), key)) {
            return true;
        }

        long clientId = NumberUtils.toLong(request.getParameter("client_id"), -1L);
        log.info("User authorize on app[{}] but not login, redirect to login page!", clientId);
        Optional<OAuthAppInfo> appInfo = oAuthAppInfoService.getAppInfo(clientId);
        String appName = appInfo.isPresent() ? appInfo.get().getAppName() : "unknown";
        // 用户未登录，跳转到登录页
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder.path("/login")
                .queryParam(GlobalConstant.CALLBACK, HttpRequestUtils.getEncodeRequestUrl(request))
                .queryParam("app_name", URLEncoder.encode(appName, "UTF-8"));
        response.sendRedirect(builder.build(true).toUriString());
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
