package org.zhenchao.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhenchao.wang 2017-01-09 21:34
 * @version 1.0.0
 */
@Controller
public class ErrorHandleController {

    @RequestMapping("/error")
    public String error() {
        return "error";
    }

}
