package org.zhenchao.oauth.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zhenchao.oauth.common.GlobalConstant;
import org.zhenchao.oauth.common.exception.CryptException;
import org.zhenchao.oauth.common.util.CipherUtils;
import org.zhenchao.oauth.dao.UserInfoMapper;
import org.zhenchao.oauth.entity.UserInfo;
import org.zhenchao.oauth.entity.UserInfoExample;
import org.zhenchao.oauth.service.UserInfoService;

import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;

/**
 * {@link UserInfoService} 实现类
 *
 * @author zhenchao.wang 2017-01-02 13:25
 * @version 1.0.0
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    private static final Logger log = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public Optional<UserInfo> validatePassword(String username, String password) throws CryptException {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            log.error("Params error, username or password is null or empty!");
            return Optional.empty();
        }

        UserInfoExample example = new UserInfoExample();
        UserInfoExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<UserInfo> users = userInfoMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(users)) {
            log.error("Can't find any user by name[{}]!", username);
            return Optional.empty();
        }

        String encryptPassword = CipherUtils.pbkdf2(password, GlobalConstant.SALT);
        UserInfo user = users.get(0);
        log.debug("Validate password[current={}, expected={}]", encryptPassword, user.getPassword());
        return StringUtils.equals(encryptPassword, user.getPassword()) ? Optional.of(user) : Optional.empty();
    }

}
