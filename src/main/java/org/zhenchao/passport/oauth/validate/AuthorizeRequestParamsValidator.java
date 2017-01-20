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
public class AuthorizeRequestParamsValidator implements ParamsValidator {

    @Resource
    private OAuthAppInfoService appInfoService;

    @Override
    public ErrorCode validate(RequestParams params) {
        if (!(params instanceof AuthorizeRequestParams)) {
            return ErrorCode.INVALID_REQUEST;
        }

        AuthorizeRequestParams authorizeParams = (AuthorizeRequestParams) params;

        if (!RESPONSE_TYPE_CODE.equals(authorizeParams.getResponseType())) {
            return ErrorCode.UNSUPPORTED_RESPONSE_TYPE;
        }

        Optional<OAuthAppInfo> optAppInfo = appInfoService.getAppInfo(authorizeParams.getClientId());
        if (optAppInfo.isPresent()) {
            OAuthAppInfo appInfo = optAppInfo.get();
            String redirectUri = appInfo.getRedirectUri();
            if (StringUtils.isBlank(redirectUri)) {
                return ErrorCode.INVALID_REDIRECT_URI;
            }
        }

        return ErrorCode.CLIENT_NOT_EXIST;
    }

}
