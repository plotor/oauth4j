package org.zhenchao.passport.oauth.exceptions;

import org.zhenchao.passport.oauth.commons.ErrorCode;

/**
 * 加解密相关错误
 *
 * @author zhenchao.wang 2017-01-02 13:59
 * @version 1.0.0
 */
public class EncryptOrDecryptException extends Exception {

    private ErrorCode errorCode;

    public EncryptOrDecryptException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public EncryptOrDecryptException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public EncryptOrDecryptException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public EncryptOrDecryptException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    protected EncryptOrDecryptException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

}
