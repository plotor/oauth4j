package org.zhenchao.oauth.common.exception;

import org.zhenchao.oauth.common.ErrorCode;

/**
 * 加解密相关异常
 *
 * @author zhenchao.wang 2017-01-02 13:59
 * @version 1.0.0
 */
public class CodecException extends Exception {

    private static final long serialVersionUID = 729009559555889799L;

    protected ErrorCode errorCode = ErrorCode.UNKNOWN_ERROR;

    public CodecException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public CodecException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CodecException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public CodecException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    protected CodecException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
