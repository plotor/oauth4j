package org.zhenchao.passport.oauth.service;

import org.zhenchao.oauth.model.OAuthAppInfo;

import java.util.Optional;

/**
 * APP相关业务逻辑
 *
 * @author zhenchao.wang 2017-01-20 17:45
 * @version 1.0.0
 */
public interface OAuthAppInfoService {

    /**
     * 获取APP信息
     *
     * @param clientId
     * @return
     */
    Optional<OAuthAppInfo> getAppInfo(long clientId);

}
