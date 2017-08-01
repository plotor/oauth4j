package org.zhenchao.oauth.token;

import org.zhenchao.oauth.token.enums.TokenType;

/**
 * bearer type access token
 *
 * @author zhenchao.wang 2017-01-23 17:55
 * @version 1.0.0
 */
public class BearerAccessToken extends AbstractAccessToken {

    public static final TokenType TYPE = TokenType.BEARER;

    @Override
    public TokenType getType() {
        return TYPE;
    }
}
