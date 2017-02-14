package org.zhenchao.passport.oauth.commons;

/**
 * grant type
 *
 * @author zhenchao.wang 2017-02-14 18:11
 * @version 1.0.0
 */
public enum GrantType {

    AUTHORIZATION_CODE("authorization_code"),

    PASSWORD_CREDENTIALS("password"),

    CLIENT_CREDENTIALS("client_credentials");

    private String type;

    GrantType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    /**
     * tell whether {@param grantType} is valid
     *
     * @param grantType
     * @return
     */
    public static boolean isValid(GrantType grantType) {
        return AUTHORIZATION_CODE.equals(grantType) || PASSWORD_CREDENTIALS.equals(grantType) || CLIENT_CREDENTIALS.equals(grantType);
    }
}
