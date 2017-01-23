package org.zhenchao.passport.oauth.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zhenchao.passport.oauth.commons.ErrorCode;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.CACHE_NAMESPACE_AUTHORIZATION_CODE;
import org.zhenchao.passport.oauth.exceptions.OAuthServiceException;
import org.zhenchao.passport.oauth.model.AuthorizationCode;
import org.zhenchao.passport.oauth.model.OAuthAppInfo;
import org.zhenchao.passport.oauth.model.UserAppAuthorization;
import org.zhenchao.passport.oauth.service.AuthorizeService;
import org.zhenchao.passport.oauth.service.OAuthAppInfoService;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
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

    /** 授权有效时间（10分钟） */
    private static final int AUTHORIZATION_CODE_EXPIRATION = 10;

    private static final CacheManager CACHE_MANAGER;

    static {
        // TIPS: 实际应用中对于授权码缓存，只应该在缓存时间上进行控制
        CACHE_MANAGER = CacheManagerBuilder.newCacheManagerBuilder().withCache(
                CACHE_NAMESPACE_AUTHORIZATION_CODE,
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, AuthorizationCode.class, ResourcePoolsBuilder.heap(1024))
                        .withExpiry(Expirations.timeToLiveExpiration(Duration.of(AUTHORIZATION_CODE_EXPIRATION, TimeUnit.MINUTES)))).build();
        CACHE_MANAGER.init();
    }

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
        Cache<String, AuthorizationCode> cache = CACHE_MANAGER.getCache(CACHE_NAMESPACE_AUTHORIZATION_CODE, String.class, AuthorizationCode.class);
        cache.put(strCode, code);
        return Optional.of(code);
    }

}
