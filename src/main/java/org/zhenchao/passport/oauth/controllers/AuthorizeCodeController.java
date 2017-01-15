package org.zhenchao.passport.oauth.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import org.zhenchao.passport.oauth.commons.GlobalConstant;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.PATH_OAUTH_AUTHORIZE_CODE;

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

    @RequestMapping(value = PATH_OAUTH_AUTHORIZE_CODE, method = {GET, POST}, params = "response_type=code")
    public String authorize(
            @RequestParam("response_type") String responseType,
            @RequestParam("client_id") String clientId,
            @RequestParam(value = "redirect_uri", required = false) String redirectUri,
            @RequestParam(value = "scope", required = false) String scope,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "skip_confirm", required = false, defaultValue = "false") boolean skipConfirm,
            @RequestParam(value = "force_login", required = false, defaultValue = "false") boolean forceLogin,
            @RequestParam(value = "issue_refresh_token", required = false, defaultValue = "true")  boolean issueRefreshToken,
            @RequestParam(value = "json", required = false, defaultValue = "false") boolean json ) {

        log.debug("Entering authorize code method...");


        return "redirect:/error";
    }

}
