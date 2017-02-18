package org.zhenchao.passport.oauth.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.zhenchao.passport.oauth.model.OAuthAppInfo;
import org.zhenchao.passport.oauth.model.UserAppAuthorization;
import org.zhenchao.passport.oauth.token.AbstractAccessToken;

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
    private String tokenType = AbstractAccessToken.TokenType.MAC.getValue();

    private String clientSecret;

    private boolean irt;

    private long userId;

    private String scope;

    private OAuthAppInfo appInfo;

    private UserAppAuthorization userAppAuthorization;

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

    public OAuthAppInfo getAppInfo() {
        return appInfo;
    }

    public TokenRequestParams setAppInfo(OAuthAppInfo appInfo) {
        this.appInfo = appInfo;
        return this;
    }

    public UserAppAuthorization getUserAppAuthorization() {
        return userAppAuthorization;
    }

    public TokenRequestParams setUserAppAuthorization(UserAppAuthorization userAppAuthorization) {
        this.userAppAuthorization = userAppAuthorization;
        return this;
    }
}
