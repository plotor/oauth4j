package org.zhenchao.oauth.service;

import org.zhenchao.oauth.common.exception.ServiceException;
import org.zhenchao.oauth.entity.AuthorizeRelation;
import org.zhenchao.oauth.pojo.AuthorizationCode;
import org.zhenchao.oauth.pojo.AuthorizeRequestParams;

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
     * @param relation
     * @param codeParams
     * @return
     * @throws ServiceException
     */
    Optional<AuthorizationCode> buildAndCacheAuthCode(AuthorizeRelation relation, AuthorizeRequestParams codeParams)
            throws ServiceException;

    /**
     * 从缓存中获取对应的授权码
     *
     * @param code
     * @return
     */
    Optional<AuthorizationCode> getAuthCodeFromCache(String code);

    /**
     * 从缓存中删除对应的授权码
     *
     * @param code
     */
    boolean deleteAuthCodeFromCache(String code);

}
