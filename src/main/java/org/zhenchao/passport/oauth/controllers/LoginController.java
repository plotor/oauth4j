package org.zhenchao.passport.oauth.controllers;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zhenchao.passport.oauth.commons.ErrorCode;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.PATH_ROOT;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.PATH_ROOT_LOGIN;
import org.zhenchao.passport.oauth.exceptions.EncryptException;
import org.zhenchao.passport.oauth.model.User;
import org.zhenchao.passport.oauth.service.UserService;
import org.zhenchao.passport.oauth.utils.ResultUtils;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;

/**
 * 登录相关控制器
 *
 * @author zhenchao.wang 2016-12-28 17:24
 * @version 1.0.0
 */
@Controller
@RequestMapping(PATH_ROOT)
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = PATH_ROOT_LOGIN, method = RequestMethod.POST)
    public JSONObject login(
            @RequestParam(value = "username") String username,
            @RequestParam("password") String password,
            @RequestParam(value = "callback", required = false) String callback) {

        log.debug("Entering login method...");

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            log.error("Login params error, username or password is null or empty!");
            return ResultUtils.genFailedJsonResult(ErrorCode.PARAMETER_ERROR, callback);
        }

        User user;
        try {
            user = userService.validatePassword(username, password);
        } catch (EncryptException e) {
            log.error("Validate user[{}] error", username);
            return ResultUtils.genFailedJsonResult(ErrorCode.VALIDATE_USER_ERROR, callback);
        }

        if (null == user) {
            log.info("User[{}] password[{}] is wrong!", username, password);
            return ResultUtils.genFailedJsonResult(ErrorCode.ILLEGAL_USER, callback);
        }

        Map<String, Object> data = new HashMap<>();
        data.put(ResultUtils.CALLBACK, callback);
        return ResultUtils.genSuccessJsonResult(data);
    }

}
