package org.zhenchao.oauth.pojo;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhenchao.oauth.common.ErrorCode;
import org.zhenchao.oauth.common.exception.VerificationException;
import org.zhenchao.oauth.entity.AppInfo;
import org.zhenchao.oauth.entity.UserInfo;
import org.zhenchao.oauth.enums.ResponseType;
import org.zhenchao.oauth.service.factory.SpringBeanFactory;
import org.zhenchao.oauth.token.pojo.TokenElement;
import org.zhenchao.oauth.util.RedirectUriUtils;
import org.zhenchao.oauth.common.util.ScopeUtils;

import java.util.Optional;

/**
 * 授权请求参数
 *
 * @author zhenchao.wang 2017-01-20 17:24
 * @version 1.0.0
 */
public class AuthorizeRequestParams implements RequestParams {

    private static final Logger log = LoggerFactory.getLogger(AuthorizeRequestParams.class);

    private String responseType;

    private long clientId;

    private String redirectUri;

    private String scope;

    private String state;

    private AppInfo appInfo;

    private UserInfo userInfo;

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

    @Override
    public ErrorCode validate() throws VerificationException {
        if (!ResponseType.isAllowed(responseType)) {
            log.error("Authorize request params error, response type is not allowed, appId[{}], responseType[{}]", clientId, responseType);
            return ErrorCode.UNSUPPORTED_RESPONSE_TYPE;
        }

        Optional<AppInfo> opt = SpringBeanFactory.getAppInfoService().getAppInfo(clientId);
        if (!opt.isPresent()) {
            log.error("No app info found, appId[{}]", clientId);
            return ErrorCode.CLIENT_NOT_EXIST;
        }
        this.appInfo = opt.get();

        // 回调地址校验
        // FIXME 请求授权码时回调地址不是必须的
        if (StringUtils.isBlank(redirectUri) || StringUtils.isBlank(appInfo.getRedirectUri())) {
            return ErrorCode.INVALID_REDIRECT_URI;
        }

        if (!RedirectUriUtils.isValid(redirectUri, appInfo.getRedirectUri())) {
            log.error("Illegal redirect uri, appId[{}], input[{}], config[{}]", clientId, redirectUri, appInfo.getRedirectUri());
            return ErrorCode.INVALID_REDIRECT_URI;
        }

        /*
         * scope校验，如果没有传递则设置为当前允许的所有权限
         * 如果授权的scope与客户端请求的scope不一致，那么需要在下发token的时候说明真实下发的scope列表
         */
        if (StringUtils.isBlank(scope)) {
            log.info("No request scope set and use default scope[{}], appId[{}]", appInfo.getScope(), clientId);
            this.scope = appInfo.getScope();
            return ErrorCode.NO_ERROR;
        } else {
            // 校验传递的scope是否是许可scope的子集
            if (ScopeUtils.isSubScopes(appInfo.getScope(), scope)) {
                log.error("Illegal request scope, appId[{}], input[{}], config[{}]", clientId, scope, appInfo.getScope());
                return ErrorCode.INVALID_SCOPE;
            }
        }
        log.info("Validate authorize request params success, appId[{}]", clientId);
        return ErrorCode.NO_ERROR;
    }

    @Override
    public TokenElement toTokenElement() {
        // TODO 2017-08-08 18:11:05
        return null;
    }

    public AppInfo getAppInfo() {
        return this.appInfo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    public String getResponseType() {
        return responseType;
    }

    public AuthorizeRequestParams setResponseType(String responseType) {
        this.responseType = responseType;
        return this;
    }

    public long getClientId() {
        return clientId;
    }

    public AuthorizeRequestParams setClientId(long clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public AuthorizeRequestParams setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public AuthorizeRequestParams setScope(String scope) {
        this.scope = scope;
        return this;
    }

    public String getState() {
        return state;
    }

    public AuthorizeRequestParams setState(String state) {
        this.state = state;
        return this;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public AuthorizeRequestParams setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        return this;
    }
}
