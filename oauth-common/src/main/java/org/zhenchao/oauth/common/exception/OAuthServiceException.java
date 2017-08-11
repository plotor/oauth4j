package org.zhenchao.oauth.common.exception;

import org.zhenchao.oauth.common.ErrorCode;

/**
 * oauth service relative exception
 *
 * @author zhenchao.wang 2017-01-22 15:58
 * @version 1.0.0
 */
public class OAuthServiceException extends ServiceException {

    private static final long serialVersionUID = 4727143238486804703L;

    public OAuthServiceException(ErrorCode errorCode) {
        super(errorCode);
    }

    public OAuthServiceException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public OAuthServiceException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause, errorCode);
    }

    public OAuthServiceException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }

    public OAuthServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace, errorCode);
    }
}
