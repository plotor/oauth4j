package org.zhenchao.passport.oauth.validate;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.zhenchao.passport.oauth.commons.ErrorCode;
import org.zhenchao.passport.oauth.model.RequestParams;

import java.util.List;

/**
 * params validator
 *
 * @author zhenchao.wang 2017-01-20 17:22
 * @version 1.0.0
 */
public interface ParamsValidator {

    /**
     * 请求参数验证
     *
     * @param params
     * @return
     */
    ErrorCode validate(RequestParams params);

    /**
     * redirect uri 合法性校验
     *
     * @param redirectUris
     * @param input
     * @return
     */
    default boolean validateRedirectUri(List<String> redirectUris, String input) {
        if (CollectionUtils.isEmpty(redirectUris) || StringUtils.isBlank(input)) {
            return false;
        }

        for (final String redirectUri : redirectUris) {
            if (StringUtils.equalsIgnoreCase(input, redirectUri)) {
                return true;
            }
            // TODO 回调地址验证
        }

        return false;
    }

}
