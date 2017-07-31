package org.zhenchao.oauth.token.enums;

/**
 * access (refresh) token type
 *
 * @author zhenchao.wang 2017-07-31 22:53
 * @version 1.0.0
 */
public enum TokenType {

    MAC("mac"),

    BEARER("bearer");

    private String value;

    TokenType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * tell whether {@param tokenType} is valid
     *
     * @param tokenType
     * @return
     */
    public static boolean isValid(TokenType tokenType) {
        return MAC.equals(tokenType) || BEARER.equals(tokenType);
    }

    /**
     * tell whether {@param tokenType} is valid
     *
     * @param tokenType
     * @return
     */
    public static boolean isValid(String tokenType) {
        return MAC.getValue().equals(tokenType) || BEARER.getValue().equals(tokenType);
    }

}
