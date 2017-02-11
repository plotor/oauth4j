package org.zhenchao.passport.oauth.domain;

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

    private String state;

    public ErrorInformation() {
    }

    public ErrorInformation(int code, String desc, String state) {
        this.code = code;
        this.desc = desc;
        this.uri = StringUtils.EMPTY;
        this.state = StringUtils.trimToEmpty(state);
    }

    public ErrorInformation(int code, String desc, String uri, String state) {
        this.code = code;
        this.desc = desc;
        this.uri = uri;
        this.state = StringUtils.trimToEmpty(state);
    }

    public ErrorInformation(ErrorCode errorCode, String state) {
        this.code = errorCode.getCode();
        this.desc = errorCode.getDesc();
        this.uri = StringUtils.EMPTY;
        this.state = StringUtils.trimToEmpty(state);
    }

    public ErrorInformation(ErrorCode errorCode, String uri, String state) {
        this.code = errorCode.getCode();
        this.desc = errorCode.getDesc();
        this.uri = uri;
        this.state = StringUtils.trimToEmpty(state);
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
