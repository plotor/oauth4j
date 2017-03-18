package org.zhenchao.passport.oauth.exception;

import org.zhenchao.passport.oauth.common.ErrorCode;

/**
 * 加解密相关错误
 *
 * @author zhenchao.wang 2017-01-02 13:59
 * @version 1.0.0
 */
public class CryptException extends Exception {

    private ErrorCode errorCode;

    public CryptException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public CryptException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CryptException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public CryptException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    protected CryptException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
