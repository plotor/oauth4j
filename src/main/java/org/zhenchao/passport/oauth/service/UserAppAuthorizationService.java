package org.zhenchao.passport.oauth.service;

import org.zhenchao.oauth.model.UserAppAuthorization;

import java.util.List;
import java.util.Optional;

/**
 * 用户与APP之间授权关系业务逻辑
 *
 * @author zhenchao.wang 2017-01-20 23:16
 * @version 1.0.0
 */
public interface UserAppAuthorizationService {

    /**
     * 获取用户与APP之间全部的授权关系
     *
     * @param userId
     * @param appId
     * @return
     */
    List<UserAppAuthorization> getUserAndAppAuthorizationInfo(long userId, long appId);

    /**
     * 获取用户与APP之间特定权限的授权关系
     *
     * @param userId
     * @param appId
     * @param scopeSign
     * @return
     */
    Optional<UserAppAuthorization> getUserAndAppAuthorizationInfo(long userId, long appId, String scopeSign);

    /**
     * 更新用户与APP之间的授权关系
     *
     * @param authorization
     * @return
     */
    boolean replaceUserAndAppAuthorizationInfo(UserAppAuthorization authorization);

}
