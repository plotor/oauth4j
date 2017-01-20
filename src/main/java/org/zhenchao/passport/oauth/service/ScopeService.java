package org.zhenchao.passport.oauth.service;

import org.zhenchao.passport.oauth.model.Scope;

import java.util.List;

/**
 * scope相关业务逻辑
 *
 * @author zhenchao.wang 2017-01-21 01:03
 * @version 1.0.0
 */
public interface ScopeService {

    /**
     * 获取scope信息列表
     *
     * @param ids
     * @return
     */
    List<Scope> getScopes(String ids);

    /**
     * 获取scope信息列表
     *
     * @param ids
     * @return
     */
    List<Scope> getScopes(List<Integer> ids);

}
