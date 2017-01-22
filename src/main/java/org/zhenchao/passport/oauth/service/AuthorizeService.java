package org.zhenchao.passport.oauth.service;

import org.zhenchao.passport.oauth.exceptions.OAuthServiceException;
import org.zhenchao.passport.oauth.model.AuthorizationCode;
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
     * 生成授权码
     *
     * @param uaa
     * @return
     * @throws OAuthServiceException
     */
    Optional<AuthorizationCode> generateAuthorizationCode(UserAppAuthorization uaa) throws OAuthServiceException;

}
