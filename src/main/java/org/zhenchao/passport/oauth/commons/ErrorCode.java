package org.zhenchao.passport.oauth.commons;

/**
 * 错误代码
 *
 * @author zhenchao.wang 2016-12-28 17:52
 * @version 1.0.0
 */
public enum ErrorCode {

    /** 基础错误 **/

    NO_ERROR(0, "no error"),

    SYSTEM_ERROR(100000, "system error"),

    SERVICE_ERROR(100050, "service error"),

    SERVICE_TEMPORARILY_UNAVAILABLE(100051, "service temporarily unavailable"),

    LOCAL_SERVICE_ERROR(100100, "local service error"),

    LOCAL_SERVICE_TEMPORARILY_UNAVAILABLE(100101, "local service temporarily unavailable"),

    REMOTE_SERVICE_ERROR(100150, "remote service error"),

    REMOTE_SERVICE_TEMPORARILY_UNAVAILABLE(100151, "remote service temporarily unavailable"),

    PARAMETER_ERROR(100200, "parameter error"),

    ENCRYPT_ERROR(100300, "encrypt error"),

    AES_ENCRYPT_ERROR(100301, "aes encrypt error"),

    PBKDF2_ENCRYPT_ERROR(100302, "pbkdf2 encrypt error"),

    DECRYPT_ERROR(100400, "decrypt error"),

    AES_DECRYPT_ERROR(100401, "aes decrypt error"),

    REDIRECT_TO_DEST_URI_ERROR(100402, "redirect to dest url error"),

    /** OAuth相关错误 **/

    INVALID_REQUEST(200000, "invalid request"),

    UNAUTHORIZED_CLIENT(200001, "unauthorized client"),

    ACCESS_DENIED(200002, "access denied"),

    UNSUPPORTED_RESPONSE_TYPE(200003, "unsupported response type"),

    INVALID_SCOPE(200004, "invalid scope"),

    CLIENT_NOT_EXIST(200005, "unknown client"),

    INVALID_REDIRECT_URI(200006, "invalid redirect uri"),

    /** 业务相关错误 **/

    VALIDATE_USER_ERROR(300000, "validate user error"),

    ILLEGAL_USER(300001, "illegal user");

    /** 错误码 */
    private int code;

    /** 错误描述信息 */
    private String desc;

    ErrorCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
