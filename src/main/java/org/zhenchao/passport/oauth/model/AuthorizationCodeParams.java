package org.zhenchao.passport.oauth.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 授权请求参数
 *
 * @author zhenchao.wang 2017-01-20 17:24
 * @version 1.0.0
 */
public class AuthorizationCodeParams implements RequestParams {

    private String responseType;

    private long clientId;

    private String redirectUri;

    private String scope;

    private String state;

    public AuthorizationCodeParams() {
    }

    public AuthorizationCodeParams(String responseType, long clientId, String redirectUri) {
        this.responseType = responseType;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
    }

    public AuthorizationCodeParams(String responseType, long clientId, String redirectUri, String scope, String state) {
        this.responseType = responseType;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.scope = scope;
        this.state = state;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    public String getResponseType() {
        return responseType;
    }

    public AuthorizationCodeParams setResponseType(String responseType) {
        this.responseType = responseType;
        return this;
    }

    public long getClientId() {
        return clientId;
    }

    public AuthorizationCodeParams setClientId(long clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public AuthorizationCodeParams setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public AuthorizationCodeParams setScope(String scope) {
        this.scope = scope;
        return this;
    }

    public String getState() {
        return state;
    }

    public AuthorizationCodeParams setState(String state) {
        this.state = state;
        return this;
    }
}
