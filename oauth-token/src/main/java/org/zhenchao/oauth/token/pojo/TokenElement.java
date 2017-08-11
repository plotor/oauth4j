package org.zhenchao.oauth.token.pojo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.zhenchao.oauth.common.enums.GrantType;
import org.zhenchao.oauth.entity.AppInfo;
import org.zhenchao.oauth.token.enums.TokenType;

/**
 * @author zhenchao.wang 2017-08-01 21:22
 * @version 1.0.0
 */
public class TokenElement {

    private TokenType tokenType;

    private Long userId;

    private String scope;

    private AppInfo appInfo;

    private GrantType grantType;

    private boolean issueRefreshToken;

    private String tokenKey;

    public TokenElement() {
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public TokenElement setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public TokenElement setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public TokenElement setScope(String scope) {
        this.scope = scope;
        return this;
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public TokenElement setAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
        return this;
    }

    public GrantType getGrantType() {
        return grantType;
    }

    public TokenElement setGrantType(GrantType grantType) {
        this.grantType = grantType;
        return this;
    }

    public boolean isIssueRefreshToken() {
        return issueRefreshToken;
    }

    public TokenElement setIssueRefreshToken(boolean issueRefreshToken) {
        this.issueRefreshToken = issueRefreshToken;
        return this;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public TokenElement setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
        return this;
    }
}
