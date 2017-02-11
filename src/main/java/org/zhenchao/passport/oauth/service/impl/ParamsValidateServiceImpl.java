package org.zhenchao.passport.oauth.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zhenchao.passport.oauth.commons.ErrorCode;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.ALLOWED_TOKEN_TYPE;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.GRANT_TYPE_CODE;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.RESPONSE_TYPE_CODE;
import org.zhenchao.passport.oauth.domain.AuthorizationCode;
import org.zhenchao.passport.oauth.domain.AuthorizationCodeParams;
import org.zhenchao.passport.oauth.domain.AuthorizationTokenParams;
import org.zhenchao.passport.oauth.domain.RequestParams;
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
    public ErrorCode validateCodeRequestParams(RequestParams params) {
        if (!(params instanceof AuthorizationCodeParams)) {
            return ErrorCode.INVALID_REQUEST;
        }

        AuthorizationCodeParams codeParams = (AuthorizationCodeParams) params;

        // 校验response type
        if (!RESPONSE_TYPE_CODE.equals(codeParams.getResponseType())) {
            log.error("The response type [{}] is not expected, need [{}]!", codeParams.getResponseType(), RESPONSE_TYPE_CODE);
            return ErrorCode.UNSUPPORTED_RESPONSE_TYPE;
        }

        Optional<OAuthAppInfo> optAppInfo = appInfoService.getAppInfo(codeParams.getClientId());
        if (optAppInfo.isPresent()) {
            /*
             * 客户端存在
             */
            OAuthAppInfo appInfo = optAppInfo.get();

            // 回调地址校验
            // TODO 请求授权码时回调地址不是必须的
            if (StringUtils.isBlank(codeParams.getRedirectUri()) || StringUtils.isBlank(appInfo.getRedirectUri())) {
                return ErrorCode.INVALID_REDIRECT_URI;
            }
            if (!this.validateRedirectUri(appInfo.getRedirectUri(), codeParams.getRedirectUri())) {
                log.error("The input redirect uri [{}] is illegal, app redirect uri [{}]", codeParams.getRedirectUri(), appInfo.getRedirectUri());
                return ErrorCode.INVALID_REDIRECT_URI;
            }

            // scope校验，如果没有传递则设置为当前允许的所有权限
            if (StringUtils.isBlank(codeParams.getScope())) {
                log.info("The app[{}] not set scope, use app default scope[{}].", codeParams.getClientId(), appInfo.getScope());
                codeParams.setScope(appInfo.getScope());
            } else {
                // 校验传递的scope是否是许可scope的子集
                return this.isSubScopes(appInfo.getScope(), codeParams.getScope()) ? ErrorCode.NO_ERROR : ErrorCode.INVALID_SCOPE;
            }
        }
        log.error("Client[id={}] is not exist!", codeParams.getClientId());
        return ErrorCode.CLIENT_NOT_EXIST;
    }

    @Override
    public ErrorCode validateTokenRequestParams(RequestParams params) {
        if (!(params instanceof AuthorizationTokenParams)) {
            return ErrorCode.INVALID_REQUEST;
        }

        AuthorizationTokenParams tokenParams = (AuthorizationTokenParams) params;

        // 检测grant type
        if (!GRANT_TYPE_CODE.equals(tokenParams.getGrantType())) {
            log.error("The grant type [{}] is not expected, need [{}]!", tokenParams.getGrantType(), GRANT_TYPE_CODE);
            return ErrorCode.UNSUPPORTED_GRANT_TYPE;
        }

        if (StringUtils.isBlank(tokenParams.getCode())) {
            log.error("Authorization code is null or empty when request access token!");
            return ErrorCode.INVALID_AUTHORIZATION_CODE;
        }

        // 验证token类型
        if (StringUtils.isNotBlank(tokenParams.getTokenType())) {
            if (!ALLOWED_TOKEN_TYPE.contains(tokenParams.getTokenType())) {
                log.error("The token type [{}] is unsupported!");
                return ErrorCode.UNSUPPORTED_TOKEN_TYPE;
            }
        }

        Optional<AuthorizationCode> optCode = authorizeService.getAuthorizationCodeFromCache(tokenParams.getCode());
        if (optCode.isPresent()) {

            // 从缓存中删除授权码，一个授权码只能被使用一次
            if (!authorizeService.deleteAuthorizationCodeFromCache(tokenParams.getCode())) {
                log.error("Delete authorization code [{}] from cache error!", tokenParams.getCode());
                return ErrorCode.INVALID_AUTHORIZATION_CODE;
            }

            AuthorizationCode code = optCode.get();
            tokenParams.setAuthorizationCode(code); // 记录一下，创建token时需要部分信息
            // 回调地址验证
            String redirectUri = code.getRedirectUri();
            if (StringUtils.isNotBlank(redirectUri) && !redirectUri.equals(tokenParams.getRedirectUri())) {
                // 如果请求code时带redirectUri参数，那么请求token时的redirectUri必须与之前一致
                log.error("The redirect uri [{}] is not equals to code request [redirect uri = {}]",
                        tokenParams.getRedirectUri(), redirectUri);
                return ErrorCode.INVALID_GRANT;
            }
            if (!this.validateRedirectUri(code.getAppInfo().getRedirectUri(), tokenParams.getRedirectUri())) {
                log.error("The input redirect uri [{}] is illegal, app redirect uri [{}]",
                        tokenParams.getRedirectUri(), code.getAppInfo().getRedirectUri());
                return ErrorCode.INVALID_REDIRECT_URI;
            }

            // 验证clientId
            // FIXME clientId的验证还需要进一步研究一下
            if (tokenParams.getClientId() != code.getAppInfo().getAppId()) {
                log.error("The input client id [{}] is not equals to code request [client id = {}]",
                        tokenParams.getClientId(), code.getAppInfo().getAppId());
                return ErrorCode.UNAUTHORIZED_CLIENT;
            }
            // TODO client secret 验证
            return ErrorCode.NO_ERROR;
        }
        return ErrorCode.INVALID_AUTHORIZATION_CODE;
    }
}
