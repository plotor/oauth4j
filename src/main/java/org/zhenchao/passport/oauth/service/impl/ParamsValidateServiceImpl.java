package org.zhenchao.passport.oauth.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zhenchao.passport.oauth.commons.ErrorCode;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.ALLOWED_RESPONSE_TYPE;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.ALLOWED_TOKEN_TYPE;
import org.zhenchao.passport.oauth.commons.GrantType;
import org.zhenchao.passport.oauth.domain.AuthorizationCode;
import org.zhenchao.passport.oauth.domain.AuthorizeRequestParams;
import org.zhenchao.passport.oauth.domain.RequestParams;
import org.zhenchao.passport.oauth.domain.TokenRequestParams;
import org.zhenchao.passport.oauth.model.OAuthAppInfo;
import org.zhenchao.passport.oauth.service.AuthorizeService;
import org.zhenchao.passport.oauth.service.OAuthAppInfoService;
import org.zhenchao.passport.oauth.service.ParamsValidateService;

import java.util.Optional;
import javax.annotation.Resource;

/**
 * {@link ParamsValidateService} 实现类
 *
 * @author zhenchao.wang 2017-01-20 17:37
 * @version 1.0.0
 */
@Service
public class ParamsValidateServiceImpl implements ParamsValidateService {

    private static final Logger log = LoggerFactory.getLogger(ParamsValidateServiceImpl.class);

    @Resource
    private OAuthAppInfoService appInfoService;

    @Resource
    private AuthorizeService authorizeService;

    @Override
    public ErrorCode validateAuthorizeRequestParams(RequestParams params) {
        if (!(params instanceof AuthorizeRequestParams)) {
            return ErrorCode.INVALID_REQUEST;
        }

        AuthorizeRequestParams requestParams = (AuthorizeRequestParams) params;

        // 校验response type
        if (!ALLOWED_RESPONSE_TYPE.contains(requestParams.getResponseType())) {
            log.error("The response type [{}] is not allowed!", requestParams.getResponseType());
            return ErrorCode.UNSUPPORTED_RESPONSE_TYPE;
        }

        Optional<OAuthAppInfo> optAppInfo = appInfoService.getAppInfo(requestParams.getClientId());
        if (optAppInfo.isPresent()) {
            /*
             * 客户端存在
             */
            OAuthAppInfo appInfo = optAppInfo.get();

            // 回调地址校验
            // TODO 请求授权码时回调地址不是必须的
            if (StringUtils.isBlank(requestParams.getRedirectUri()) || StringUtils.isBlank(appInfo.getRedirectUri())) {
                return ErrorCode.INVALID_REDIRECT_URI;
            }
            if (!this.validateRedirectUri(appInfo.getRedirectUri(), requestParams.getRedirectUri())) {
                log.error("The input redirect uri [{}] is illegal, app redirect uri [{}]", requestParams.getRedirectUri(), appInfo.getRedirectUri());
                return ErrorCode.INVALID_REDIRECT_URI;
            }

            // TODO 如果授权的scope与客户端请求的scope不一致，那么需要在下发token的时候说明真实下发的scope列表，参见3.3
            // scope校验，如果没有传递则设置为当前允许的所有权限
            if (StringUtils.isBlank(requestParams.getScope())) {
                log.info("The app[{}] not set scope, use default scope[{}].", requestParams.getClientId(), appInfo.getScope());
                requestParams.setScope(appInfo.getScope());
            } else {
                // 校验传递的scope是否是许可scope的子集
                return this.isSubScopes(appInfo.getScope(), requestParams.getScope()) ? ErrorCode.NO_ERROR : ErrorCode.INVALID_SCOPE;
            }
        }
        log.error("Client[id={}] is not exist!", requestParams.getClientId());
        return ErrorCode.CLIENT_NOT_EXIST;
    }

    @Override
    public ErrorCode validateTokenRequestParams(RequestParams params) {
        if (!(params instanceof TokenRequestParams)) {
            return ErrorCode.INVALID_REQUEST;
        }

        TokenRequestParams requestParams = (TokenRequestParams) params;

        // 检测grant type
        if (!GrantType.AUTHORIZATION_CODE.getType().equals(requestParams.getGrantType())) {
            log.error("The grant type [{}] is not expected, need [{}]!", requestParams.getGrantType(), GrantType.AUTHORIZATION_CODE);
            return ErrorCode.UNSUPPORTED_GRANT_TYPE;
        }

        if (StringUtils.isBlank(requestParams.getCode())) {
            log.error("Authorization code is null or empty when request access token!");
            return ErrorCode.INVALID_AUTHORIZATION_CODE;
        }

        // 验证token类型
        if (StringUtils.isNotBlank(requestParams.getTokenType())) {
            if (!ALLOWED_TOKEN_TYPE.contains(requestParams.getTokenType())) {
                log.error("The token type [{}] is unsupported!");
                return ErrorCode.UNSUPPORTED_TOKEN_TYPE;
            }
        }

        Optional<AuthorizationCode> optCode = authorizeService.getAuthorizationCodeFromCache(requestParams.getCode());
        if (optCode.isPresent()) {

            // 从缓存中删除授权码，一个授权码只能被使用一次
            if (!authorizeService.deleteAuthorizationCodeFromCache(requestParams.getCode())) {
                log.error("Delete authorization code [{}] from cache error!", requestParams.getCode());
                return ErrorCode.INVALID_AUTHORIZATION_CODE;
            }

            AuthorizationCode code = optCode.get();
            //记录部分信息，创建token时需要
            requestParams.setUserId(code.getUserId()).setScope(code.getScopes()).setAppInfo(code.getAppInfo());
            // 回调地址验证
            String redirectUri = code.getRedirectUri();
            if (StringUtils.isNotBlank(redirectUri) && !redirectUri.equals(requestParams.getRedirectUri())) {
                // 如果请求code时带redirectUri参数，那么请求token时的redirectUri必须与之前一致
                log.error("The redirect uri [{}] is not equals to code request [redirect uri = {}]",
                        requestParams.getRedirectUri(), redirectUri);
                return ErrorCode.INVALID_GRANT;
            }
            if (!this.validateRedirectUri(code.getAppInfo().getRedirectUri(), requestParams.getRedirectUri())) {
                log.error("The input redirect uri [{}] is illegal, app redirect uri [{}]",
                        requestParams.getRedirectUri(), code.getAppInfo().getRedirectUri());
                return ErrorCode.INVALID_REDIRECT_URI;
            }

            // 验证clientId
            // FIXME clientId的验证还需要进一步研究一下
            if (requestParams.getClientId() != code.getAppInfo().getAppId()) {
                log.error("The input client id [{}] is not equals to code request [client id = {}]",
                        requestParams.getClientId(), code.getAppInfo().getAppId());
                return ErrorCode.UNAUTHORIZED_CLIENT;
            }
            // TODO client secret 验证
            return ErrorCode.NO_ERROR;
        }
        return ErrorCode.INVALID_AUTHORIZATION_CODE;
    }
}
