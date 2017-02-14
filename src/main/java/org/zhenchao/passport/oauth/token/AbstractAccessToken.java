package org.zhenchao.passport.oauth.token;

import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * abstract access token
 *
 * @author zhenchao.wang 2017-01-23 17:53
 * @version 1.0.0
 */
public abstract class AbstractAccessToken implements Token {

    public enum TokenVersion {

        V_1_0_0("1.0.0");

        private String value;

        TokenVersion(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum TokenType {

        MAC("mac"),

        BEARER("bearer");

        private String value;

        TokenType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        /**
         * tell whether {@param tokenType} is valid
         *
         * @param tokenType
         * @return
         */
        public static boolean isValid(TokenType tokenType) {
            return MAC.equals(tokenType) || BEARER.equals(tokenType);
        }
    }

    protected TokenVersion version;

    protected long userId;

    protected long clientId;

    protected String scope;

    protected long issueTime;

    protected long expirationTime;

    protected TokenType type;

    protected String refreshToken;

    private String key;

    protected String value;

    /**
     * encode token
     *
     * @return
     * @throws IOException
     */
    protected byte[] encode() throws IOException {
        byte[] result;
        DataOutputStream dos = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            dos.writeBytes(this.version.getValue()); // version
            dos.writeChar('-');
            dos.writeLong(this.expirationTime); // expirationTime
            dos.writeLong(this.issueTime);
            dos.writeLong(this.userId);
            dos.writeLong(this.clientId);
            dos.writeInt(this.scope.getBytes().length);
            dos.writeBytes(this.scope);
            dos.writeInt(this.type.getValue().length());
            dos.writeBytes(this.type.getValue());
            dos.flush();
            result = baos.toByteArray();
        } finally {
            if (null != dos) {
                dos.close();
            }
        }
        return result;
    }

    /**
     * get access token string value
     *
     * @return
     * @throws IOException
     */
    public String getValue() throws IOException {
        if (StringUtils.isBlank(this.value)) {
            byte[] encodeValue = this.encode();
            byte[] hmacValue = Base64.getEncoder().encode(HmacUtils.hmacSha1(this.key.getBytes(), encodeValue));

            DataOutputStream dos = null;
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                dos = new DataOutputStream(baos);
                dos.writeInt(hmacValue.length);
                dos.write(hmacValue);
                dos.write(encodeValue);
                dos.flush();
                this.value = Base64.getEncoder().encodeToString(baos.toByteArray());
            } finally {
                if (null != dos) {
                    dos.close();
                }
            }

        }
        return this.value;
    }

    public TokenVersion getVersion() {
        return version;
    }

    public AbstractAccessToken setVersion(TokenVersion version) {
        this.version = version;
        return this;
    }

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

    public TokenType getType() {
        return type;
    }

    public AbstractAccessToken setType(TokenType type) {
        this.type = type;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public AbstractAccessToken setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getKey() {
        return key;
    }

    public AbstractAccessToken setKey(String key) {
        this.key = key;
        return this;
    }
}
