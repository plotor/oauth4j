package org.zhenchao.passport.oauth.token;

import org.zhenchao.oauth.token.enums.TokenType;

/**
 * mac type access token
 *
 * @author zhenchao.wang 2017-01-23 17:56
 * @version 1.0.0
 */
public class MacAccessToken extends AbstractAccessToken {

    public enum ALGORITHM {
        HMAC_SHA_1("hmac-sha-1"),

        HMAC_SHA_256("hmac-sha-256");

        private String value;

        ALGORITHM(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static final TokenType TYPE = TokenType.MAC;

    /** 目前仅支持hmac-sha-1 */
    private ALGORITHM algorithm = ALGORITHM.HMAC_SHA_1;

    @Override
    public TokenType getType() {
        return TYPE;
    }

    public ALGORITHM getAlgorithm() {
        return algorithm;
    }

    public MacAccessToken setAlgorithm(ALGORITHM algorithm) {
        this.algorithm = algorithm;
        return this;
    }
}
