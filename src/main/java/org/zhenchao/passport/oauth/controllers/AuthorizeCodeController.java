package org.zhenchao.passport.oauth.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.zhenchao.passport.oauth.commons.GlobalConstant;

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

    @RequestMapping(value = GlobalConstant.PATH_OAUTH_AUTHORIZE_CODE, method = {RequestMethod.GET, RequestMethod.POST})
    public String authorize(
            @RequestParam("response_type") String responseType,
            @RequestParam("client_id") String clientId,
            @RequestParam(value = "redirect_uri", required = false) String redirectUri,
            @RequestParam(value = "scope", required = false) String scope,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "skip_confirm", required = false, defaultValue = "false") boolean skipConfirm,
            @RequestParam(value = "force_login", required = false, defaultValue = "false") boolean forceLogin,
            @RequestParam(value = "json", required = false, defaultValue = "false") boolean json ) {

        log.debug("Entering authorize code method...");



        return "";
    }

}
