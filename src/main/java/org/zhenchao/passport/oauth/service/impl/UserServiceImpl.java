package org.zhenchao.passport.oauth.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zhenchao.passport.oauth.commons.ErrorCode;
import org.zhenchao.passport.oauth.commons.GlobalConstant;
import org.zhenchao.passport.oauth.dao.UserMapper;
import org.zhenchao.passport.oauth.exceptions.EncryptException;
import org.zhenchao.passport.oauth.model.User;
import org.zhenchao.passport.oauth.model.UserExample;
import org.zhenchao.passport.oauth.service.UserService;
import org.zhenchao.passport.oauth.utils.EncryptUtils;

import java.security.spec.InvalidKeySpecException;
import java.util.List;
import javax.annotation.Resource;

/**
 * {@link UserService} 实现类
 *
 * @author zhenchao.wang 2017-01-02 13:25
 * @version 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserMapper userMapper;

    @Override
    public User validatePassword(String username, String password) throws EncryptException {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            log.error("Params error, username or password is null or empty!");
            return null;
        }

        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<User> users = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(users)) {
            log.error("Can't find any user by name[{}]!", username);
            return null;
        }

        String encryptPassword;
        try {
            encryptPassword = EncryptUtils.pbkdf2(password, GlobalConstant.SALT);
        } catch (InvalidKeySpecException e) {
            throw new EncryptException("PBKDF2 error, invalid ke spec", e, ErrorCode.ENCRYPT_ERROR);
        }

        User user = users.get(0);
        log.debug("Password validate[current={}, expected={}]", encryptPassword, user.getPassword());
        return StringUtils.equals(encryptPassword, user.getPassword()) ? user : null;
    }

}
