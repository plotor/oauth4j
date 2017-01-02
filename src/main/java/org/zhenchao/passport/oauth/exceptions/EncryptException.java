package org.zhenchao.passport.oauth.exceptions;

import org.zhenchao.passport.oauth.commons.ErrorCode;

/**
 * 加密相关错误
 *
 * @author zhenchao.wang 2017-01-02 13:59
 * @version 1.0.0
 */
public class EncryptException extends Exception {

    private ErrorCode errorCode;

    public EncryptException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public EncryptException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public EncryptException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public EncryptException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    protected EncryptException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

}
