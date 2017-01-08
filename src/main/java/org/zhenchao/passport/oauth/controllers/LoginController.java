package org.zhenchao.passport.oauth.controllers;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.zhenchao.passport.oauth.commons.ErrorCode;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.COOKIE_KEY_USER_LOGIN_SIGN;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.PATH_ROOT_LOGIN;
import org.zhenchao.passport.oauth.exceptions.EncryptOrDecryptException;
import org.zhenchao.passport.oauth.model.User;
import org.zhenchao.passport.oauth.service.UserService;
import org.zhenchao.passport.oauth.utils.EncryptAndDecryptUtils;
import org.zhenchao.passport.oauth.utils.ResultUtils;
import org.zhenchao.passport.oauth.utils.SessionUtils;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录相关控制器
 *
 * @author zhenchao.wang 2016-12-28 17:24
 * @version 1.0.0
 */
@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private UserService userService;

    /**
     * 跳转登录页
     *
     * @return
     */
    @RequestMapping(value = PATH_ROOT_LOGIN, method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "callback", required = false) String callback) {
        ModelAndView mav = new ModelAndView();
        if (StringUtils.isNotBlank(callback)) {
            mav.addObject("callback", callback);
        }
        mav.setViewName("login");
        return mav;
    }

    /**
     * 用户登录验证
     *
     * @param session
     * @param username
     * @param password
     * @param callback
     * @return
     */
    @ResponseBody
    @RequestMapping(value = PATH_ROOT_LOGIN, method = RequestMethod.POST)
    public String login(
            HttpSession session,
            HttpServletResponse response,
            @RequestParam(value = "username") String username,
            @RequestParam("password") String password,
            @RequestParam(value = "callback", required = false) String callback) {

        log.debug("Entering login method...");

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            log.error("Login params error, username or password is null or empty!");
            return ResultUtils.genFailedStringResult(ErrorCode.PARAMETER_ERROR, callback);
        }

        User user;
        try {
            user = userService.validatePassword(username, password);
        } catch (EncryptOrDecryptException e) {
            log.error("Validate user[{}] error", username);
            return ResultUtils.genFailedStringResult(ErrorCode.VALIDATE_USER_ERROR, callback);
        }

        if (null == user) {
            log.info("User[{}] password[{}] is wrong!", username, password);
            return ResultUtils.genFailedStringResult(ErrorCode.ILLEGAL_USER, callback);
        }

        // session user
        SessionUtils.putUser(session, user);

        try {
            // cookie user
            Cookie cookie = new Cookie(COOKIE_KEY_USER_LOGIN_SIGN, new String(EncryptAndDecryptUtils.aesEncrypt(username)));
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(24 * 3600);
            response.addCookie(cookie);
        } catch (EncryptOrDecryptException e) {
            log.error("Put user[{}] info to cookie error!", username, e);
            return ResultUtils.genFailedStringResult(ErrorCode.ENCRYPT_ERROR, callback);
        }

        Map<String, Object> data = new HashMap<>();
        data.put(ResultUtils.CALLBACK, callback);
        return ResultUtils.genSuccessStringResult(data);

    }

}
