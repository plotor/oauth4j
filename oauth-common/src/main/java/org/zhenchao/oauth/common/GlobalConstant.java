package org.zhenchao.oauth.common;

/**
 * 全局常量
 *
 * @author zhenchao.wang 2016-12-28 17:35
 * @version 1.0.0
 */
public interface GlobalConstant {

    String COOKIE_KEY_USER_LOGIN_SIGN = "ck_sign_ul";

    /** 盐 */
    String SALT = "U2FsdGVkX18jBB+UX3z4J1Qy9z24/JX5U28ELFYb+ehc8WH/QetV6bbbxUhhM3zvkY/M";

    String CACHE_NAMESPACE_AUTHORIZATION_CODE = "passport-oauth-authorization-code";

    /** 响应参数 */
    String CALLBACK = "callback";

    /** 分隔符 */

    String SEPARATOR_REDIRECT_URI = ";";

    String SEPARATOR_REDIRECT_SCOPE = " ";

    String JSON_SAFE_PREFIX = "&&&START&&&";
}
