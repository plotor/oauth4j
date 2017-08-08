package org.zhenchao.oauth.pojo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.zhenchao.oauth.entity.AppInfo;
import org.zhenchao.oauth.entity.AuthorizeRelation;
import org.zhenchao.oauth.token.enums.TokenType;
import org.zhenchao.oauth.token.pojo.TokenElement;

/**
 * 令牌请求参数
 *
 * @author zhenchao.wang 2017-01-23 15:36
 * @version 1.0.0
 */
public class TokenRequestParams implements RequestParams {

    private String responseType;

    private String grantType;

    private String code;

    private String redirectUri;

    private long clientId;

    /** token类型，默认为mac类型 */
    private String tokenType = TokenType.MAC.getValue();

    private String clientSecret;

    private boolean irt;

    private long userId;

    private String scope;

    private AppInfo appInfo;

    private AuthorizeRelation authorizeRelation;

    public TokenRequestParams() {
    }

    public TokenRequestParams(String grantType, String code, String redirectUri, long clientId) {
        this.grantType = grantType;
        this.code = code;
        this.redirectUri = redirectUri;
        this.clientId = clientId;
    }

    public TokenRequestParams(
            String grantType, String code, String redirectUri, long clientId, String tokenType, String clientSecret, boolean irt) {
        this.grantType = grantType;
        this.code = code;
        this.redirectUri = redirectUri;
        this.clientId = clientId;
        this.tokenType = tokenType;
        this.clientSecret = clientSecret;
        this.irt = irt;
    }

    @Override
    public TokenElement toTokenElement() {
        // TODO 2017-08-08 18:11:29
        return null;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    public String getResponseType() {
        return responseType;
    }

    public TokenRequestParams setResponseType(String responseType) {
        this.responseType = responseType;
        return this;
    }

    public String getGrantType() {
        return grantType;
    }

    public TokenRequestParams setGrantType(String grantType) {
        this.grantType = grantType;
        return this;
    }

    public String getCode() {
        return code;
    }

    public TokenRequestParams setCode(String code) {
        this.code = code;
        return this;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public TokenRequestParams setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    public long getClientId() {
        return clientId;
    }

    public TokenRequestParams setClientId(long clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getTokenType() {
        return tokenType;
    }

    public TokenRequestParams setTokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public TokenRequestParams setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public boolean isIrt() {
        return irt;
    }

    public TokenRequestParams setIrt(boolean irt) {
        this.irt = irt;
        return this;
    }

    public long getUserId() {
        return userId;
    }

    public TokenRequestParams setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public TokenRequestParams setScope(String scope) {
        this.scope = scope;
        return this;
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public TokenRequestParams setAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
        return this;
    }

    public AuthorizeRelation getAuthorizeRelation() {
        return authorizeRelation;
    }

    public TokenRequestParams setAuthorizeRelation(AuthorizeRelation authorizeRelation) {
        this.authorizeRelation = authorizeRelation;
        return this;
    }
}
