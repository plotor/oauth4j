package org.zhenchao.passport.oauth.exceptions;

import org.zhenchao.passport.oauth.commons.ErrorCode;

/**
 * oauth service relative exception
 *
 * @author zhenchao.wang 2017-01-22 15:58
 * @version 1.0.0
 */
public class OAuthServiceException extends Exception {

    private ErrorCode errorCode;

    public OAuthServiceException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public OAuthServiceException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public OAuthServiceException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public OAuthServiceException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    protected OAuthServiceException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
