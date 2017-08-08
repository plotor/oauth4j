package org.zhenchao.oauth.common.exception;

import org.zhenchao.oauth.common.ErrorCode;

/**
 * @author zhenchao.wang 2017-08-08 22:35
 * @version 1.0.0
 */
public abstract class VerificationException extends Exception {

    protected ErrorCode errorCode = ErrorCode.UNKNOWN_ERROR;

    public VerificationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public VerificationException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public VerificationException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public VerificationException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public VerificationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
