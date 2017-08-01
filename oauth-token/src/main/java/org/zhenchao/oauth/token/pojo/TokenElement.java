package org.zhenchao.oauth.token.pojo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.zhenchao.oauth.token.enums.TokenType;

/**
 * @author zhenchao.wang 2017-08-01 21:22
 * @version 1.0.0
 */
public class TokenElement {

    private TokenType tokenType;

    private String responseType;

    private Long clientId;

    private Long userId;

    private String scope;

    private Long validity;

    private boolean issueRefreshToken;

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

    public String getResponseType() {
        return responseType;
    }

    public TokenElement setResponseType(String responseType) {
        this.responseType = responseType;
        return this;
    }

    public Long getClientId() {
        return clientId;
    }

    public TokenElement setClientId(Long clientId) {
        this.clientId = clientId;
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

    public Long getValidity() {
        return validity;
    }

    public TokenElement setValidity(Long validity) {
        this.validity = validity;
        return this;
    }

    public boolean isIssueRefreshToken() {
        return issueRefreshToken;
    }

    public TokenElement setIssueRefreshToken(boolean issueRefreshToken) {
        this.issueRefreshToken = issueRefreshToken;
        return this;
    }
}
