package org.zhenchao.passport.oauth.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhenchao.oauth.common.ErrorCode;
import static org.zhenchao.oauth.common.GlobalConstant.SEPARATOR_REDIRECT_URI;
import org.zhenchao.passport.oauth.pojo.RequestParams;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 * 参数验证服务
 *
 * @author zhenchao.wang 2017-01-20 17:22
 * @version 1.0.0
 */
public interface ParamsValidateService {

    Logger log = LoggerFactory.getLogger(ParamsValidateService.class);

    /**
     * 授权码请求参数验证
     *
     * @param params
     * @return
     */
    ErrorCode validateAuthorizeRequestParams(RequestParams params);

    /**
     * 访问令牌请求参数验证
     *
     * @param params
     * @return
     */
    ErrorCode validateTokenRequestParams(RequestParams params);

    /**
     * redirect uri 合法性校验
     *
     * @param redirectUriStr
     * @param input
     * @return
     */
    default boolean validateRedirectUri(String redirectUriStr, String input) {
        if (StringUtils.isBlank(redirectUriStr) || StringUtils.isBlank(input)) {
            return false;
        }

        if (!UrlValidator.getInstance().isValid(input)) {
            log.error("The input url [{}] is not a legal url!", input);
            return false;
        }

        String[] redirectUris = StringUtils.trim(redirectUriStr).split(SEPARATOR_REDIRECT_URI);
        try {
            URI inputUri = new URI(input);
            String inputUriPath = inputUri.getPath().endsWith("/") ? inputUri.getPath() : inputUri.getPath() + "/";
            for (final String redirectUri : redirectUris) {
                if (StringUtils.equalsIgnoreCase(input, redirectUri)) {
                    return true;
                }
                try {
                    URI uri = new URI(redirectUri);
                    if (StringUtils.equalsIgnoreCase(inputUri.getScheme(), uri.getScheme())
                            && inputUri.getHost().endsWith(uri.getHost())
                            && inputUri.getPort() == uri.getPort()) {
                        String uriPath = uri.getPath().endsWith("/") ? uri.getPath() : uri.getPath() + "/";
                        if (inputUriPath.startsWith(uriPath)) {
                            return true;
                        }
                    }
                } catch (URISyntaxException e) {
                    log.error("Parse the redirect uri string[{}] to object error!", redirectUri, e);
                    return false;
                }

            }
        } catch (URISyntaxException e) {
            log.error("Parse the input redirect uri [{}] to object error!", input, e);
            return false;
        }
        return false;
    }

    /**
     * 校验sScopes是否是pScopes的子集
     *
     * @param pScopes
     * @param sScopes
     * @return
     */
    default boolean isSubScopes(String pScopes, String sScopes) {
        if (StringUtils.isBlank(pScopes) || StringUtils.isBlank(sScopes)) {
            return false;
        }
        List<String> pScopeList = Arrays.asList(StringUtils.trim(pScopes).split("\\s+"));
        List<String> sScopeList = Arrays.asList(StringUtils.trim(sScopes).split("\\s+"));
        return CollectionUtils.isSubCollection(sScopeList, pScopeList);
    }

}
