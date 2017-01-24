package org.zhenchao.passport.oauth.token;

/**
 * abstract access token
 *
 * @author zhenchao.wang 2017-01-23 17:53
 * @version 1.0.0
 */
public abstract class AbstractAccessToken implements Token {

    protected long userId;

    protected long clientId;

    protected String scope;

    protected long issueTime;

    protected long expirationTime;

    protected String tokenType;


    public long getUserId() {
        return userId;
    }

    public AbstractAccessToken setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public long getClientId() {
        return clientId;
    }

    public AbstractAccessToken setClientId(long clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public AbstractAccessToken setScope(String scope) {
        this.scope = scope;
        return this;
    }

    public long getIssueTime() {
        return issueTime;
    }

    public AbstractAccessToken setIssueTime(long issueTime) {
        this.issueTime = issueTime;
        return this;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public AbstractAccessToken setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
        return this;
    }

    public String getTokenType() {
        return tokenType;
    }
}
