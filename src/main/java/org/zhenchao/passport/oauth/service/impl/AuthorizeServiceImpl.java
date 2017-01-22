package org.zhenchao.passport.oauth.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.ehcache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zhenchao.passport.oauth.commons.ErrorCode;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.CACHE_NAMESPACE_AUTHORIZATIONCODE;
import org.zhenchao.passport.oauth.exceptions.OAuthServiceException;
import org.zhenchao.passport.oauth.model.AuthorizationCode;
import org.zhenchao.passport.oauth.model.OAuthAppInfo;
import org.zhenchao.passport.oauth.model.UserAppAuthorization;
import org.zhenchao.passport.oauth.service.AuthorizeService;
import org.zhenchao.passport.oauth.service.OAuthAppInfoService;
import org.zhenchao.passport.oauth.utils.LocalCacheUtils;

import java.util.Optional;
import javax.annotation.Resource;

/**
 * {@link AuthorizeService} 实现类
 *
 * @author zhenchao.wang 2017-01-22 14:33
 * @version 1.0.0
 */
@Service
public class AuthorizeServiceImpl implements AuthorizeService {

    private static final Logger log = LoggerFactory.getLogger(AuthorizeServiceImpl.class);

    @Resource
    private OAuthAppInfoService appInfoService;

    @Override
    public Optional<AuthorizationCode> generateAuthorizationCode(UserAppAuthorization uaa) throws OAuthServiceException {
        if (null == uaa) {
            log.error("Generate authorization code error, the input params is null!");
            return Optional.empty();
        }

        OAuthAppInfo appInfo = appInfoService.getAppInfo(uaa.getAppId()).orElseThrow(() -> new OAuthServiceException(ErrorCode.CLIENT_NOT_EXIST));
        AuthorizationCode code = new AuthorizationCode();
        code.setAppInfo(appInfo).setUserId(uaa.getUserId()).setScopes(uaa.getScope());
        String strCode = code.toStringCode();
        if (StringUtils.isBlank(strCode)) {
            log.error("Generate authorization code error!");
            throw new OAuthServiceException("generate authorization code error", ErrorCode.GENERATE_CODE_ERROR);
        }
        Cache<String, Object> cache = LocalCacheUtils.getCacheHandler(CACHE_NAMESPACE_AUTHORIZATIONCODE, 10);
        cache.put(strCode, code);
        return Optional.of(code);
    }

}
