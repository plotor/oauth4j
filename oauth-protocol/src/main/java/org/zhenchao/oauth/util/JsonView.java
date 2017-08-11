package org.zhenchao.oauth.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.ModelAndView;
import org.zhenchao.oauth.common.GlobalConstant;
import org.zhenchao.oauth.pojo.ResultInfo;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
 * return json data
 *
 * @author zhenchao.wang 2017-01-23 13:42
 * @version 1.0.0
 */
public class JsonView {

    private static final Logger log = LoggerFactory.getLogger(JsonView.class);

    /**
     * 以json视图返回
     *
     * @param model
     * @param response
     * @return
     */
    public static ModelAndView render(Object model, HttpServletResponse response, boolean jsonSafe) {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        try {
            Object json = jsonSafe ? StringUtils.join(GlobalConstant.JSON_SAFE_PREFIX, model) : model;
            if(model instanceof ResultInfo) {
                json = ((ResultInfo) model).toJson();
            }
            jsonConverter.write(json, MediaType.APPLICATION_JSON, new ServletServerHttpResponse(response));
        } catch (IOException e) {
            log.error("Write response data [{}] as json view error!", model, e);
        }
        return null;
    }

}
