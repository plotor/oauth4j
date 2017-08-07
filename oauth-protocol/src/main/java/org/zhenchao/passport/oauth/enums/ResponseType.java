package org.zhenchao.passport.oauth.enums;

import java.util.Optional;

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
    public static boolean isValid(String responseType) {
        return AUTHORIZATION_CODE.getType().equals(responseType) || IMPLICIT.getType().equals(responseType);
    }

    /**
     * change string param to {@code ResponseType} object
     *
     * @param responseType
     * @return
     */
    public static Optional<ResponseType> resolve(String responseType) {
        for (final ResponseType type : ResponseType.values()) {
            if (type.getType().equals(responseType)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    /**
     * check if is allowed response type
     *
     * @param type
     * @return
     */
    public static boolean isAllowed(String type) {
        return AUTHORIZATION_CODE.getType().equals(type) || IMPLICIT.getType().equals(type);
    }

}
