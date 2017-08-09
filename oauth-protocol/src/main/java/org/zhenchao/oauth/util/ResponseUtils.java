package org.zhenchao.oauth.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.zhenchao.oauth.common.ErrorCode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhenchao.wang 2017-08-09 09:44
 * @version 1.0.0
 */
public abstract class ResponseUtils {

    /**
     * build error response
     *
     * @param mav
     * @param redirectUri
     * @param errorCode
     * @param state
     * @return
     */
    public static ModelAndView buildErrorResponse(ModelAndView mav, String redirectUri, ErrorCode errorCode, String state) {
        List<String> params = new ArrayList<>();
        params.add(String.format("error=%s", errorCode.getCode()));
        if (StringUtils.isNotBlank(errorCode.getDescription())) {
            try {
                params.add(String.format("error_description=%s", URLEncoder.encode(errorCode.getDescription(), "UTF-8")));
            } catch (UnsupportedEncodingException e) {
                // never happen
            }
        }
        if (StringUtils.isNotBlank(state)) {
            params.add(String.format("state=%s", state));
        }
        mav.setViewName("redirect:" + String.format("%s?%s", redirectUri, StringUtils.join(params, "&")));
        return mav;
    }
}
