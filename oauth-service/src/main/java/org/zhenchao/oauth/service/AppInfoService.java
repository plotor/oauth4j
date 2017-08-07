package org.zhenchao.oauth.service;

import org.zhenchao.oauth.entity.AppInfo;

import java.util.Optional;

/**
 * APP相关业务逻辑
 *
 * @author zhenchao.wang 2017-01-20 17:45
 * @version 1.0.0
 */
public interface AppInfoService {

    /**
     * 获取APP信息
     *
     * @param clientId
     * @return
     */
    Optional<AppInfo> getAppInfo(long clientId);

}
