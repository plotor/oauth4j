package org.zhenchao.passport.oauth.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;

/**
 * http request 工具类
 *
 * @author zhenchao.wang 2017-01-05 17:28
 * @version 1.0.0
 */
public class HttpRequestUtils {

    /**
     * 获取请求绝对路径
     *
     * @param request
     * @return
     */
    public static String getRequestUrl(HttpServletRequest request) {
        StringBuffer requestUrl = request.getRequestURL();
        String qs = request.getQueryString();
        return StringUtils.isBlank(qs) ? requestUrl.toString() : requestUrl.append('?').append(qs).toString();
    }

    /**
     * 获取编码后的请求绝对路径
     *
     * @param request
     * @return
     */
    public static String getEncodeRequestUrl(HttpServletRequest request) {
        String requestUrl = getRequestUrl(request);
        try {
            URLEncoder.encode(requestUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // never happen
        }
        return requestUrl;
    }

}
