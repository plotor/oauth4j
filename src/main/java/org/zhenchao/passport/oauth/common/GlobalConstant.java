package org.zhenchao.passport.oauth.common;

import org.zhenchao.passport.oauth.token.AbstractAccessToken;

import java.util.HashSet;
import java.util.Set;

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

    String PATH_OAUTH_AUTHORIZE_TOKEN = "/authorize/token";

    String PATH_OAUTH_IMPLICIT_TOKEN = "/implicit/token";

    String PATH_OAUTH_USER_AUTHORIZE = "/user/authorize";

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

    /** allowed response type */
    Set<String> ALLOWED_RESPONSE_TYPE = new HashSet<String>(2) {
        private static final long serialVersionUID = -7941095619455398619L;

        {
            add(ResponseType.AUTHORIZATION_CODE.getType()); // Authorization Code
            add(ResponseType.IMPLICIT.getType()); // implicit
        }
    };

    /** allowed token type */
    Set<String> ALLOWED_TOKEN_TYPE = new HashSet<String>(2) {
        private static final long serialVersionUID = -7941095619455398619L;

        {
            add(AbstractAccessToken.TokenType.BEARER.getValue()); // bearer access token
            add(AbstractAccessToken.TokenType.MAC.getValue()); // mac access token
        }
    };
}
