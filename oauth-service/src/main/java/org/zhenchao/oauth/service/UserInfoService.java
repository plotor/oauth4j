package org.zhenchao.oauth.service;

import org.zhenchao.oauth.common.exception.CryptException;
import org.zhenchao.oauth.model.User;

import java.util.Optional;

/**
 * 用户相关业务逻辑
 *
 * @author zhenchao.wang 2016-12-28 18:21
 * @version 1.0.0
 */
public interface UserInfoService {

    /**
     * 验证用户名和密码
     *
     * @param username
     * @param password
     * @return 验证成功返回user对象，否则返回null
     */
    Optional<User> validatePassword(String username, String password) throws CryptException;
}
