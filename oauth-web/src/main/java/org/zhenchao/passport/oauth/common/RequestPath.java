package org.zhenchao.passport.oauth.common;

/**
 * @author zhenchao.wang 2017-07-31 22:47
 * @version 1.0.0
 */
public interface RequestPath {

    String PATH_ROOT = "/";

    String PATH_ROOT_LOGIN = "/login";

    String PATH_ROOT_OAUTH = "/oauth";

    String PATH_OAUTH_AUTHORIZE_CODE = "/authorize/code";

    String PATH_OAUTH_AUTHORIZE_TOKEN = "/authorize/token";

    String PATH_OAUTH_IMPLICIT_TOKEN = "/implicit/token";

    String PATH_OAUTH_USER_AUTHORIZE = "/user/authorize";

}
