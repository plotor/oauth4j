package org.zhenchao.oauth.common.exception;

import org.zhenchao.oauth.common.ErrorCode;

/**
 * service exception
 *
 * @author zhenchao.wang 2017-08-05 12:48
 * @version 1.0.0
 */
public abstract class ServiceException extends Exception {

    private static final long serialVersionUID = -7359473116626228099L;

    protected ErrorCode errorCode = ErrorCode.UNKNOWN_ERROR;

    public ServiceException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ServiceException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ServiceException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ServiceException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
