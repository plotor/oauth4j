package org.zhenchao.passport.oauth.service;

import org.zhenchao.passport.oauth.exception.OAuthServiceException;
import org.zhenchao.passport.oauth.domain.AuthorizationCode;
import org.zhenchao.passport.oauth.domain.AuthorizeRequestParams;
import org.zhenchao.passport.oauth.model.UserAppAuthorization;

import java.util.Optional;

/**
 * 授权相关业务逻辑
 *
 * @author zhenchao.wang 2017-01-22 14:30
 * @version 1.0.0
 */
public interface AuthorizeService {

    /**
     * 生成并缓存授权码
     *
     * @param uaa
     * @param codeParams
     * @return
     * @throws OAuthServiceException
     */
    Optional<AuthorizationCode> generateAndCacheAuthorizationCode(UserAppAuthorization uaa, AuthorizeRequestParams codeParams)
            throws OAuthServiceException;

    /**
     * 从缓存中获取对应的授权码
     *
     * @param code
     * @return
     */
    Optional<AuthorizationCode> getAuthorizationCodeFromCache(String code);

    /**
     * 从缓存中删除对应的授权码
     *
     * @param code
     */
    boolean deleteAuthorizationCodeFromCache(String code);

}
