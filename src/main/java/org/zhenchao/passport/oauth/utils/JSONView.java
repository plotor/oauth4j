package org.zhenchao.passport.oauth.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
 * return json data
 *
 * @author zhenchao.wang 2017-01-23 13:42
 * @version 1.0.0
 */
public class JSONView {

    private static final Logger log = LoggerFactory.getLogger(JSONView.class);

    /**
     * 以json视图返回
     *
     * @param model
     * @param response
     * @return
     */
    public static ModelAndView render(Object model, HttpServletResponse response) {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        MediaType jsonMimeType = MediaType.APPLICATION_JSON;

        try {
            jsonConverter.write(model, jsonMimeType, new ServletServerHttpResponse(response));
        } catch (IOException e) {
            log.error("Write response data as json view error!", e);
        }

        return new ModelAndView("error");
    }

}
