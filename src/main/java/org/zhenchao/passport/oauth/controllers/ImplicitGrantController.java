package org.zhenchao.passport.oauth.controllers;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.zhenchao.passport.oauth.commons.ErrorCode;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.PATH_OAUTH_IMPLICIT_TOKEN;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.PATH_ROOT_OAUTH;
import org.zhenchao.passport.oauth.domain.AuthorizeRequestParams;
import org.zhenchao.passport.oauth.domain.Error;
import org.zhenchao.passport.oauth.service.ParamsValidateService;
import org.zhenchao.passport.oauth.utils.JsonView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * implicit grant
 *
 * @author zhenchao.wang 2017-02-14 13:42
 * @version 1.0.0
 */
@Controller
@RequestMapping(PATH_ROOT_OAUTH)
public class ImplicitGrantController {

    private static final Logger log = LoggerFactory.getLogger(ImplicitGrantController.class);

    @Resource
    private ParamsValidateService paramsValidateService;

    @RequestMapping(value = PATH_OAUTH_IMPLICIT_TOKEN, method = {GET, POST}, params = "response_type=token")
    public ModelAndView authorize(
            HttpServletResponse response,
            @RequestParam("response_type") String responseType,
            @RequestParam("client_id") long clientId,
            @RequestParam(value = "redirect_uri", required = false) String redirectUri,
            @RequestParam(value = "scope", required = false) String scope,
            @RequestParam(value = "state", required = false) String state) {

        log.debug("Entering implicit grant method...");

        ModelAndView mav = new ModelAndView();

        AuthorizeRequestParams requestParams = new AuthorizeRequestParams();
        requestParams.setResponseType(responseType).setClientId(clientId).setRedirectUri(redirectUri).setScope(scope).setState(StringUtils.trimToEmpty(state));

        ErrorCode validateResult = paramsValidateService.validateAuthorizeRequestParams(requestParams);
        if (ErrorCode.NO_ERROR != validateResult) {
            // 请求参数有误
            log.error("Request authorize params error, params[{}], errorCode[{}]", requestParams, validateResult);
            return JsonView.render(new Error(validateResult, state), response, false);
        }
        // TODO 2017-02-14 14:32:00
        return mav;
    }

}
