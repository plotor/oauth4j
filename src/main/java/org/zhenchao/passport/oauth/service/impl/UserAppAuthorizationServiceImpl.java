package org.zhenchao.passport.oauth.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zhenchao.passport.oauth.dao.UserAppAuthorizationMapper;
import org.zhenchao.oauth.model.UserAppAuthorization;
import org.zhenchao.oauth.model.UserAppAuthorizationExample;
import org.zhenchao.passport.oauth.service.UserAppAuthorizationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;

/**
 * {@link UserAppAuthorizationService} 实现类
 *
 * @author zhenchao.wang 2017-01-20 23:19
 * @version 1.0.0
 */
@Service
public class UserAppAuthorizationServiceImpl implements UserAppAuthorizationService {

    private static final Logger log = LoggerFactory.getLogger(UserAppAuthorizationServiceImpl.class);

    @Resource
    private UserAppAuthorizationMapper userAppAuthorizationMapper;

    @Override
    public List<UserAppAuthorization> getUserAndAppAuthorizationInfo(long userId, long appId) {
        if (userId < 0 || appId < 0) {
            log.error("Get user and app authorization error, userId[{}] or appId[{}] less than 0!", userId, appId);
            return new ArrayList<>();
        }
        UserAppAuthorizationExample example = new UserAppAuthorizationExample();
        UserAppAuthorizationExample.Criteria criteria = example.createCriteria();
        criteria.andAppIdEqualTo(appId).andUserIdEqualTo(userId);
        return userAppAuthorizationMapper.selectByExample(example);
    }

    @Override
    public Optional<UserAppAuthorization> getUserAndAppAuthorizationInfo(long userId, long appId, String scopeSign) {
        if (userId < 0 || appId < 0 || StringUtils.isBlank(scopeSign)) {
            log.error("Get user and app authorization params error, userId[{}], appId[{}], scope sign [{}]!", userId, appId, scopeSign);
            return Optional.empty();
        }
        UserAppAuthorizationExample example = new UserAppAuthorizationExample();
        UserAppAuthorizationExample.Criteria criteria = example.createCriteria();
        criteria.andAppIdEqualTo(appId).andUserIdEqualTo(userId).andScopeSignEqualTo(scopeSign);
        List<UserAppAuthorization> authorizations = userAppAuthorizationMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(authorizations)) {
            return Optional.empty();
        }
        return Optional.of(authorizations.get(0));
    }

    @Override
    public boolean replaceUserAndAppAuthorizationInfo(UserAppAuthorization authorization) {
        if (null == authorization || StringUtils.isBlank(authorization.getScopeSign())) {
            return false;
        }
        UserAppAuthorizationExample example = new UserAppAuthorizationExample();
        UserAppAuthorizationExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(
                authorization.getUserId()).andAppIdEqualTo(authorization.getAppId()).andScopeSignEqualTo(authorization.getScopeSign());
        List<UserAppAuthorization> authorizations = userAppAuthorizationMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(authorizations)) {
            // 用户与APP之间不存在特定授权关系
            return userAppAuthorizationMapper.insertSelective(authorization) == 1;
        } else {
            // 用户与APP之间存在特定授权关系
            UserAppAuthorization uaa = authorizations.get(0);
            uaa.setRefreshTokenExpirationTime(authorization.getRefreshTokenExpirationTime());
            uaa.setRefreshTokenKey(authorization.getRefreshTokenKey());
            uaa.setTokenKey(authorization.getTokenKey());
            uaa.setUpdateTime(authorization.getUpdateTime());
            example = new UserAppAuthorizationExample();
            criteria = example.createCriteria();
            criteria.andUserIdEqualTo(uaa.getUserId()).andAppIdEqualTo(uaa.getAppId()).andScopeSignEqualTo(authorization.getScopeSign());
            return userAppAuthorizationMapper.updateByExampleSelective(uaa, example) == 1;
        }
    }

}
