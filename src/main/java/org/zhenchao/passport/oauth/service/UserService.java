package org.zhenchao.passport.oauth.service;

/**
 * 用户相关业务逻辑
 *
 * @author zhenchao.wang 2016-12-28 18:21
 * @version 1.0.0
 */
public interface UserService {

    /**
     * 验证用户名和密码
     *
     * @param username
     * @param password
     * @return
     */
    boolean validatePassword(String username, String password);
}
