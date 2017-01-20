package org.zhenchao.passport.oauth.validate;

import org.apache.commons.lang3.StringUtils;
import org.zhenchao.passport.oauth.commons.ErrorCode;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.RESPONSE_TYPE_CODE;
import org.zhenchao.passport.oauth.model.AuthorizeRequestParams;
import org.zhenchao.passport.oauth.model.OAuthAppInfo;
import org.zhenchao.passport.oauth.model.RequestParams;
import org.zhenchao.passport.oauth.service.OAuthAppInfoService;

import java.util.Optional;
import javax.annotation.Resource;

/**
 * authorize code request validator
 *
 * @author zhenchao.wang 2017-01-20 17:37
 * @version 1.0.0
 */
public class AuthorizeParamsValidator implements ParamsValidator {

    @Resource
    private OAuthAppInfoService appInfoService;

    @Override
    public ErrorCode validate(RequestParams params) {
        if (!(params instanceof AuthorizeRequestParams)) {
            return ErrorCode.INVALID_REQUEST;
        }

        AuthorizeRequestParams authorizeParams = (AuthorizeRequestParams) params;

        // 校验response type
        if (!RESPONSE_TYPE_CODE.equals(authorizeParams.getResponseType())) {
            return ErrorCode.UNSUPPORTED_RESPONSE_TYPE;
        }

        Optional<OAuthAppInfo> optAppInfo = appInfoService.getAppInfo(authorizeParams.getClientId());
        if (optAppInfo.isPresent()) {
            /*
             * 客户端存在
             */
            OAuthAppInfo appInfo = optAppInfo.get();

            // 回调地址校验
            if (StringUtils.isBlank(authorizeParams.getRedirectUri()) || StringUtils.isBlank(appInfo.getRedirectUri())) {
                return ErrorCode.INVALID_REDIRECT_URI;
            }
            if (!this.validateRedirectUri(appInfo.getRedirectUri(), authorizeParams.getRedirectUri())) {
                return ErrorCode.INVALID_REDIRECT_URI;
            }

            // scope校验，如果没有传递则设置为当前允许的所有权限
            if (StringUtils.isBlank(authorizeParams.getScope())) {
                authorizeParams.setScope(appInfo.getScope());
            } else {
                // 校验传递的scope是否是许可scope的子集
                return this.isSubScopes(appInfo.getScope(), authorizeParams.getScope()) ? ErrorCode.NO_ERROR : ErrorCode.INVALID_SCOPE;
            }
        }

        return ErrorCode.CLIENT_NOT_EXIST;
    }

}
