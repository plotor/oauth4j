package org.zhenchao.passport.oauth.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;
import org.zhenchao.passport.oauth.common.ErrorCode;
import static org.zhenchao.passport.oauth.common.GlobalConstant.PATH_OAUTH_USER_AUTHORIZE;
import static org.zhenchao.passport.oauth.common.GlobalConstant.PATH_ROOT_LOGIN;
import static org.zhenchao.passport.oauth.common.GlobalConstant.PATH_ROOT_OAUTH;
import org.zhenchao.passport.oauth.domain.ResultInfo;
import org.zhenchao.oauth.model.UserAppAuthorization;
import org.zhenchao.passport.oauth.service.UserAppAuthorizationService;
import org.zhenchao.passport.oauth.util.CommonUtils;
import org.zhenchao.passport.oauth.util.JsonView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * User Authorize
 *
 * @author zhenchao.wang 2017-02-14 17:32
 * @version 1.0.0
 */
@Controller
@RequestMapping(PATH_ROOT_OAUTH)
public class UserAuthorizeController {

    private static final Logger log = LoggerFactory.getLogger(UserAuthorizeController.class);

    @Resource
    private UserAppAuthorizationService authorizeRelationService;

    /**
     * user authorize on app
     *
     * @return
     */
    @RequestMapping(value = PATH_OAUTH_USER_AUTHORIZE, method = {POST})
    public ModelAndView userAuthorize(HttpServletResponse response,
                                      @RequestParam("user_id") long userId,
                                      @RequestParam("client_id") long clientId,
                                      @RequestParam("scope") String scope,
                                      @RequestParam(value = "state", required = false) String state,
                                      @RequestParam("callback") String callback) {

        log.debug("Entering user authorize method...");

        ModelAndView mav = new ModelAndView();

        if (userId < 0 || StringUtils.isBlank(callback)) {
            log.error("User authorize request params error, userId[{}], callback[{}]", userId, callback);
            UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
            builder.path(PATH_ROOT_LOGIN);
            builder.queryParam("callback", callback);
            mav.setViewName("redirect:" + builder.toUriString());
            return mav;
        }

        // 添加用户授权关系
        UserAppAuthorization authorization = new UserAppAuthorization();
        authorization.setUserId(userId);
        authorization.setAppId(clientId);
        authorization.setScope(scope);
        authorization.setScopeSign(CommonUtils.genScopeSign(scope));
        authorization.setCreateTime(new Date());
        authorization.setCreateTime(authorization.getCreateTime());
        authorization.setTokenKey(RandomStringUtils.randomAlphanumeric(64));  // 随机生成key
        authorization.setRefreshTokenKey(RandomStringUtils.randomAlphanumeric(64));  // 随机生成刷新令牌key
        authorization.setRefreshTokenExpirationTime(365 * 24 * 3600L);  // 设置刷新令牌有效期
        if (authorizeRelationService.replaceUserAndAppAuthorizationInfo(authorization)) {
            // 更新用户授权关系成功
            try {
                mav.setViewName(String.format("redirect:%s&skip_confirm=true", URLDecoder.decode(callback, "UTF-8")));
                return mav;
            } catch (UnsupportedEncodingException e) {
                // never happen
            }
        }
        return JsonView.render(new ResultInfo(ErrorCode.LOCAL_SERVICE_ERROR, state), response, false);
    }

}
