package org.zhenchao.passport.oauth.commons;

/**
 * response type
 *
 * @author zhenchao.wang 2017-02-14 18:07
 * @version 1.0.0
 */
public enum ResponseType {

    AUTHORIZATION_CODE("code"),

    IMPLICIT("token");

    private String type;

    ResponseType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    /**
     * tell whether {@param responseType} is valid
     *
     * @param responseType
     * @return
     */
    public static boolean isValid(ResponseType responseType) {
        return AUTHORIZATION_CODE.equals(responseType) || IMPLICIT.equals(responseType);
    }

}
