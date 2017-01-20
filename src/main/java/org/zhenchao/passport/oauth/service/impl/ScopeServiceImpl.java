package org.zhenchao.passport.oauth.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zhenchao.passport.oauth.model.Scope;
import org.zhenchao.passport.oauth.service.ScopeService;

import java.util.List;

/**
 * {@link ScopeService} 实现类
 *
 * @author zhenchao.wang 2017-01-21 01:06
 * @version 1.0.0
 */
@Service
public class ScopeServiceImpl implements ScopeService {

    private static final Logger log = LoggerFactory.getLogger(ScopeServiceImpl.class);

    @Override
    public List<Scope> getScopes(String ids) {
        // TODO 2017-1-21 01:07:57
        return null;
    }

    @Override
    public List<Scope> getScopes(List<Integer> ids) {
        // TODO 2017-1-21 01:08:05
        return null;
    }
}
