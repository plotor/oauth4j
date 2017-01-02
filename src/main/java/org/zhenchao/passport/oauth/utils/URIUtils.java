package org.zhenchao.passport.oauth.utils;

/**
 * URI工具类
 *
 * @author zhenchao.wang 2017-01-02 16:17
 * @version 1.0.0
 */
public class URIUtils {

    /*public static URIBuilder genCallback(HttpServletRequest request) {
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme(request.getScheme());
        uriBuilder.setHost(request.getRemoteHost());
        uriBuilder.setPort(request.getRemotePort());
        Map<String, String[]> params = request.getParameterMap();
        if (MapUtils.isNotEmpty(params)) {
            params.forEach((key, values) -> {
                if (values.length == 1) {
                    uriBuilder.setParameter(key, values[0]);
                } else {
                    for (final String value : values) {
                        uriBuilder.setParameter(key, value);
                    }
                }
            });
        }
    }*/

}
