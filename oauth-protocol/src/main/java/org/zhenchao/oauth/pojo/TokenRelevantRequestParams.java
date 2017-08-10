package org.zhenchao.oauth.pojo;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhenchao.oauth.common.ErrorCode;
import org.zhenchao.oauth.common.exception.VerificationException;
import org.zhenchao.oauth.entity.AppInfo;
import org.zhenchao.oauth.entity.AuthorizeRelation;
import org.zhenchao.oauth.handler.AuthCodeCacheHandler;
import org.zhenchao.oauth.token.enums.TokenType;
import org.zhenchao.oauth.token.pojo.TokenElement;
import org.zhenchao.oauth.common.util.RedirectUriUtils;

/**
 * 令牌请求参数
 *
 * @author zhenchao.wang 2017-01-23 15:36
 * @version 1.0.0
 */
public class TokenRelevantRequestParams implements RequestParams {

    private static final Logger log = LoggerFactory.getLogger(TokenRelevantRequestParams.class);

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

    private String state;

    private AppInfo appInfo;

    private AuthorizeRelation authorizeRelation;

    public TokenRelevantRequestParams() {
    }

    public TokenRelevantRequestParams(String redirectUri, long clientId, String tokenType, String state) {
        this.redirectUri = redirectUri;
        this.clientId = clientId;
        this.tokenType = StringUtils.defaultIfBlank(tokenType, TokenType.MAC.getValue());
        this.state = StringUtils.trimToEmpty(state);
    }

    public TokenRelevantRequestParams(String grantType, String code, String redirectUri, long clientId,
                                      String tokenType, String clientSecret, boolean irt) {
        this.grantType = grantType;
        this.code = code;
        this.redirectUri = redirectUri;
        this.clientId = clientId;
        this.tokenType = StringUtils.defaultIfBlank(tokenType, TokenType.MAC.getValue());
        this.clientSecret = clientSecret;
        this.irt = irt;
    }

    @Override
    public ErrorCode validate() throws VerificationException {
        // 验证token类型
        if (StringUtils.isNotBlank(tokenType) && !TokenType.isValid(tokenType)) {
            log.error("Unsupported token type, appId[{}], tokenType[{}]", clientId, tokenType);
            return ErrorCode.UNSUPPORTED_TOKEN_TYPE;
        }

        if (StringUtils.isBlank(code)) {
            log.error("Auth code missing when request access token, appId[{}]", clientId);
            return ErrorCode.INVALID_AUTHORIZATION_CODE;
        }
        AuthorizationCode authCode = AuthCodeCacheHandler.getInstance().get(code);
        if (null == authCode) {
            log.error("No cache auth code found, appId[{}], key[{}]", clientId, code);
            return ErrorCode.INVALID_AUTHORIZATION_CODE;
        }
        // 从缓存中删除授权码，一个授权码只能被使用一次
        AuthCodeCacheHandler.getInstance().remove(code);
        this.appInfo = authCode.getAppInfo();

        if (this.clientId != appInfo.getAppId()) {
            log.error("Client id mismatch, input[{}], in code [{}]", clientId, appInfo.getAppId());
            return ErrorCode.UNAUTHORIZED_CLIENT;
        }

        // 记录部分信息，创建 token 时需要
        this.setScope(authCode.getScopes());
        this.setAppInfo(authCode.getAppInfo());

        // 回调地址验证
        String redirectUri = authCode.getRedirectUri();
        if (StringUtils.isNotBlank(redirectUri) && !redirectUri.equals(authCode.getRedirectUri())) {
            // 如果请求code时带redirectUri参数，那么请求token时的redirectUri必须与之前一致
            log.error("Illegal redirect uri, appId[{}], input[{}], in code [{}]", clientId, redirectUri, authCode.getRedirectUri());
            return ErrorCode.INVALID_GRANT;
        }
        if (!RedirectUriUtils.isValid(redirectUri, appInfo.getRedirectUri())) {
            log.error("Illegal redirect uri, appId[{}], input[{}], config[{}]", clientId, redirectUri, appInfo.getRedirectUri());
            return ErrorCode.INVALID_REDIRECT_URI;
        }

        /*
         * TODO client secret 验证
         *
         * client_secret属于创建APP时下发，这一块可以调用开放平台接口进行验证
         * client_secret由APP自己保管，这个主要用来验证当前请求授权的APP是持有对应ID的APP自己
         * 如果client_secret遭到泄露，那么相关责任应该由APP自己承担
         */
        return ErrorCode.NO_ERROR;
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

    public String getGrantType() {
        return grantType;
    }

    public TokenRelevantRequestParams setGrantType(String grantType) {
        this.grantType = grantType;
        return this;
    }

    public String getCode() {
        return code;
    }

    public TokenRelevantRequestParams setCode(String code) {
        this.code = code;
        return this;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public TokenRelevantRequestParams setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    public long getClientId() {
        return clientId;
    }

    public TokenRelevantRequestParams setClientId(long clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getTokenType() {
        return tokenType;
    }

    public TokenRelevantRequestParams setTokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public TokenRelevantRequestParams setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public boolean isIrt() {
        return irt;
    }

    public TokenRelevantRequestParams setIrt(boolean irt) {
        this.irt = irt;
        return this;
    }

    public long getUserId() {
        return userId;
    }

    public TokenRelevantRequestParams setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public TokenRelevantRequestParams setScope(String scope) {
        this.scope = scope;
        return this;
    }

    public String getState() {
        return state;
    }

    public TokenRelevantRequestParams setState(String state) {
        this.state = state;
        return this;
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public TokenRelevantRequestParams setAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
        return this;
    }

    public AuthorizeRelation getAuthorizeRelation() {
        return authorizeRelation;
    }

    public TokenRelevantRequestParams setAuthorizeRelation(AuthorizeRelation authorizeRelation) {
        this.authorizeRelation = authorizeRelation;
        return this;
    }
}
