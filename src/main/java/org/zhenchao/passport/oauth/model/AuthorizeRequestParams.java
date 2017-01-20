package org.zhenchao.passport.oauth.model;

/**
 * 授权请求参数
 *
 * @author zhenchao.wang 2017-01-20 17:24
 * @version 1.0.0
 */
public class AuthorizeRequestParams implements RequestParams {

    private String responseType;

    private long clientId;

    private String redirectUri;

    private String scope;

    private String state;

    public AuthorizeRequestParams() {
    }

    public AuthorizeRequestParams(String responseType, long clientId, String redirectUri) {
        this.responseType = responseType;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
    }

    public AuthorizeRequestParams(String responseType, long clientId, String redirectUri, String scope, String state) {
        this.responseType = responseType;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.scope = scope;
        this.state = state;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
