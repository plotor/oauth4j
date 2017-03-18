package org.zhenchao.passport.oauth.util;

import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhenchao.wang 2017-02-25 19:19
 * @version 1.0.0
 */
public class ResultUtils {

    /**
     * 解析Location字段中的url参数列表
     *
     * @param response
     * @return
     */
    public static Map<String, String> getLocationUrlParamsValue(Response response) {
        String location = response.getHeader("Location");
        if (StringUtils.isBlank(location)) {
            return new HashMap<>();
        }
        return getUrlParamsValue(location);
    }

    /**
     * 解析url参数列表
     *
     * @param url
     * @return
     */
    public static Map<String, String> getUrlParamsValue(String url) {
        Map<String, String> result = new HashMap<>();
        try {
            String query = new URI(url).getQuery();
            if (StringUtils.isNotBlank(query)) {
                String[] pairs = query.split("&");
                for (final String pair : pairs) {
                    int index = pair.indexOf('=');
                    result.put(pair.substring(0, index), pair.substring(index + 1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
