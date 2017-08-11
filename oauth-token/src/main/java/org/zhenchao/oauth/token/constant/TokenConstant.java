package org.zhenchao.oauth.token.constant;

/**
 * @author zhenchao.wang 2017-08-10 22:46
 * @version 1.0.0
 */
public interface TokenConstant {

    /** 访问令牌默认有效期（秒） */
    long DEFAULT_ACCESS_TOKEN_VALIDITY = 7200;

    /** 刷新令牌默认有效期（秒） */
    long DEFAULT_REFRESH_TOKEN_VALIDITY = 90 * 24 * 3600;

}
