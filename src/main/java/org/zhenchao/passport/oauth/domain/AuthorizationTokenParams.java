package org.zhenchao.passport.oauth.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.zhenchao.passport.oauth.model.UserAppAuthorization;
import org.zhenchao.passport.oauth.token.AbstractAccessToken;

/**
 * 令牌请求参数
 *
 * @author zhenchao.wang 2017-01-23 15:36
 * @version 1.0.0
 */
public class AuthorizationTokenParams implements RequestParams {

    private String grantType;

    private String code;

    private String redirectUri;

    private long clientId;

    /** token类型，默认为mac类型 */
    private String tokenType = AbstractAccessToken.TokenType.MAC.getValue();

    private String clientSecret;

    private boolean irt;

    private AuthorizationCode authorizationCode;

    private UserAppAuthorization userAppAuthorization;

    public AuthorizationTokenParams() {
    }

    public AuthorizationTokenParams(String grantType, String code, String redirectUri, long clientId) {
        this.grantType = grantType;
        this.code = code;
        this.redirectUri = redirectUri;
        this.clientId = clientId;
    }

    public AuthorizationTokenParams(
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

    public String getGrantType() {
        return grantType;
    }

    public AuthorizationTokenParams setGrantType(String grantType) {
        this.grantType = grantType;
        return this;
    }

    public String getCode() {
        return code;
    }

    public AuthorizationTokenParams setCode(String code) {
        this.code = code;
        return this;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public AuthorizationTokenParams setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    public long getClientId() {
        return clientId;
    }

    public AuthorizationTokenParams setClientId(long clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getTokenType() {
        return tokenType;
    }

    public AuthorizationTokenParams setTokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public AuthorizationTokenParams setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public boolean isIrt() {
        return irt;
    }

    public AuthorizationTokenParams setIrt(boolean irt) {
        this.irt = irt;
        return this;
    }

    public AuthorizationCode getAuthorizationCode() {
        return authorizationCode;
    }

    public AuthorizationTokenParams setAuthorizationCode(AuthorizationCode authorizationCode) {
        this.authorizationCode = authorizationCode;
        return this;
    }

    public UserAppAuthorization getUserAppAuthorization() {
        return userAppAuthorization;
    }

    public AuthorizationTokenParams setUserAppAuthorization(UserAppAuthorization userAppAuthorization) {
        this.userAppAuthorization = userAppAuthorization;
        return this;
    }
}
