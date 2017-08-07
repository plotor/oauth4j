package org.zhenchao.oauth.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zhenchao.oauth.dao.AuthorizeRelationMapper;
import org.zhenchao.oauth.entity.AuthorizeRelation;
import org.zhenchao.oauth.entity.AuthorizeRelationExample;
import org.zhenchao.oauth.service.AuthorizeRelationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;

/**
 * {@link AuthorizeRelationService} 实现类
 *
 * @author zhenchao.wang 2017-01-20 23:19
 * @version 1.0.0
 */
@Service
public class AuthorizeRelationServiceImpl implements AuthorizeRelationService {

    private static final Logger log = LoggerFactory.getLogger(AuthorizeRelationServiceImpl.class);

    @Resource
    private AuthorizeRelationMapper authorizeRelationMapper;

    @Override
    public List<AuthorizeRelation> getUserAndAppRelationList(long userId, long appId) {
        if (userId < 0 || appId < 0) {
            log.error("Get user and app authorization error, userId[{}] or appId[{}] less than 0!", userId, appId);
            return new ArrayList<>();
        }
        AuthorizeRelationExample example = new AuthorizeRelationExample();
        AuthorizeRelationExample.Criteria criteria = example.createCriteria();
        criteria.andAppIdEqualTo(appId).andUserIdEqualTo(userId);
        return authorizeRelationMapper.selectByExample(example);
    }

    @Override
    public Optional<AuthorizeRelation> getUserAndAppRelationList(long userId, long appId, String scopeSign) {
        if (userId < 0 || appId < 0 || StringUtils.isBlank(scopeSign)) {
            log.error("Get user and app authorization params error, userId[{}], appId[{}], scope sign [{}]!", userId, appId, scopeSign);
            return Optional.empty();
        }
        AuthorizeRelationExample example = new AuthorizeRelationExample();
        AuthorizeRelationExample.Criteria criteria = example.createCriteria();
        criteria.andAppIdEqualTo(appId).andUserIdEqualTo(userId).andScopeSignEqualTo(scopeSign);
        List<AuthorizeRelation> authorizations = authorizeRelationMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(authorizations)) {
            return Optional.empty();
        }
        return Optional.of(authorizations.get(0));
    }

    @Override
    public boolean replaceAuthorizeRelation(AuthorizeRelation relation) {
        if (null == relation || StringUtils.isBlank(relation.getScopeSign())) {
            return false;
        }
        AuthorizeRelationExample example = new AuthorizeRelationExample();
        AuthorizeRelationExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(
                relation.getUserId()).andAppIdEqualTo(relation.getAppId()).andScopeSignEqualTo(relation.getScopeSign());
        List<AuthorizeRelation> authorizations = authorizeRelationMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(authorizations)) {
            // 用户与APP之间不存在特定授权关系
            return authorizeRelationMapper.insertSelective(relation) == 1;
        } else {
            // 用户与APP之间存在特定授权关系
            AuthorizeRelation ar = authorizations.get(0);
            ar.setRefreshTokenExpirationTime(relation.getRefreshTokenExpirationTime());
            ar.setRefreshTokenKey(relation.getRefreshTokenKey());
            ar.setTokenKey(relation.getTokenKey());
            ar.setUpdateTime(relation.getUpdateTime());
            example = new AuthorizeRelationExample();
            criteria = example.createCriteria();
            criteria.andUserIdEqualTo(ar.getUserId()).andAppIdEqualTo(ar.getAppId()).andScopeSignEqualTo(relation.getScopeSign());
            return authorizeRelationMapper.updateByExampleSelective(ar, example) == 1;
        }
    }

}
