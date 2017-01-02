package org.zhenchao.passport.oauth.commons;

/**
 * 全局常量
 *
 * @author zhenchao.wang 2016-12-28 17:35
 * @version 1.0.0
 */
public interface GlobalConstant {

    /** 路径相关 **/
    String PATH_ROOT = "/";

    String PATH_ROOT_LOGIN = "/login";

    String PATH_ROOT_OAUTH = "/oauth";

    String PATH_OAUTH_AUTHORIZE_CODE = "/authorize/code";

    String PATH_OAUTH_AUTHORIZE_token = "/authorize/token";

    String PATH_OAUTH_IMPLICIT_token = "/implicit/token";

    /** 盐 */
    String SALT = "U2FsdGVkX18jBB+UX3z4J1Qy9z24/JX5U28ELFYb+ehc8WH/QetV6bbbxUhhM3zvkY/M";
}
