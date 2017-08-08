package org.zhenchao.oauth.common.exception;

import org.zhenchao.oauth.common.ErrorCode;

/**
 * @author zhenchao.wang 2017-08-08 22:37
 * @version 1.0.0
 */
public class ParameterVerificationException extends VerificationException {

    public ParameterVerificationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ParameterVerificationException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public ParameterVerificationException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause, errorCode);
    }

    public ParameterVerificationException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }

    public ParameterVerificationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace, errorCode);
    }
}
