package org.zhenchao.passport.oauth.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.SEPARATOR_REDIRECT_SCOPE;
import org.zhenchao.passport.oauth.dao.ScopeMapper;
import org.zhenchao.passport.oauth.model.Scope;
import org.zhenchao.passport.oauth.model.ScopeExample;
import org.zhenchao.passport.oauth.service.ScopeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;

/**
 * {@link ScopeService} 实现类
 *
 * @author zhenchao.wang 2017-01-21 01:06
 * @version 1.0.0
 */
@Service
public class ScopeServiceImpl implements ScopeService {

    private static final Logger log = LoggerFactory.getLogger(ScopeServiceImpl.class);

    @Resource
    private ScopeMapper scopeMapper;

    @Override
    public List<Scope> getScopes(String ids) {
        if (StringUtils.isBlank(ids)) {
            return new ArrayList<>();
        }
        ScopeExample example = new ScopeExample();
        ScopeExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(
                Arrays.stream(StringUtils.trim(ids).split(SEPARATOR_REDIRECT_SCOPE)).map(Integer::valueOf).collect(Collectors.toList()));
        return scopeMapper.selectByExample(example);
    }

    @Override
    public List<Scope> getScopes(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        ScopeExample example = new ScopeExample();
        ScopeExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(ids);
        return scopeMapper.selectByExample(example);
    }
}
