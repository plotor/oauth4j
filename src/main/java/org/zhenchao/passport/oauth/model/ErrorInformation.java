package org.zhenchao.passport.oauth.model;

import org.apache.commons.lang3.StringUtils;
import org.zhenchao.passport.oauth.commons.ErrorCode;

/**
 * 错误信息
 *
 * @author zhenchao.wang 2017-01-23 13:52
 * @version 1.0.0
 */
public class ErrorInformation {

    private int code;

    private String desc;

    private String uri;

    public ErrorInformation() {
    }

    public ErrorInformation(int code, String desc) {
        this.code = code;
        this.desc = desc;
        this.uri = StringUtils.EMPTY;
    }

    public ErrorInformation(int code, String desc, String uri) {
        this.code = code;
        this.desc = desc;
        this.uri = uri;
    }

    public ErrorInformation(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.desc = errorCode.getDesc();
        this.uri = StringUtils.EMPTY;
    }

    public ErrorInformation(ErrorCode errorCode, String uri) {
        this.code = errorCode.getCode();
        this.desc = errorCode.getDesc();
        this.uri = uri;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
