package org.zhenchao.oauth.service.impl;

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
import org.zhenchao.oauth.common.ErrorCode;
import static org.zhenchao.oauth.common.GlobalConstant.CACHE_NAMESPACE_AUTHORIZATION_CODE;
import org.zhenchao.oauth.common.exception.OAuthServiceException;
import org.zhenchao.oauth.common.exception.ServiceException;
import org.zhenchao.oauth.entity.AppInfo;
import org.zhenchao.oauth.entity.AuthorizeRelation;
import org.zhenchao.oauth.pojo.AuthorizationCode;
import org.zhenchao.oauth.service.AppInfoService;
import org.zhenchao.oauth.service.AuthorizeService;
import org.zhenchao.oauth.pojo.AuthorizeRequestParams;

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
    private AppInfoService appInfoService;

    @Override
    public Optional<AuthorizationCode> buildAndCacheAuthCode(AuthorizeRelation relation, AuthorizeRequestParams codeParams)
            throws ServiceException {
        if (null == relation || null == codeParams) {
            log.error("Generate authorization code error, the input params is null!");
            return Optional.empty();
        }

        AppInfo appInfo = appInfoService.getAppInfo(relation.getAppId()).orElseThrow(() -> new OAuthServiceException(ErrorCode.CLIENT_NOT_EXIST));
        AuthorizationCode code = new AuthorizationCode();
        code.setAppInfo(appInfo).setUserId(relation.getUserId()).setScopes(relation.getScope()).setRedirectUri(codeParams.getRedirectUri());
        String key = code.getValue();
        if (StringUtils.isBlank(key)) {
            log.error("Generate authorization code error!");
            throw new OAuthServiceException("generate authorization code error", ErrorCode.GENERATE_CODE_ERROR);
        }
        Cache<String, AuthorizationCode> cache = CACHE_MANAGER.getCache(CACHE_NAMESPACE_AUTHORIZATION_CODE, String.class, AuthorizationCode.class);
        cache.put(key, code);
        return Optional.of(code);
    }

    @Override
    public Optional<AuthorizationCode> getAuthCodeFromCache(String code) {
        if (StringUtils.isBlank(code)) {
            return Optional.empty();
        }

        Cache<String, AuthorizationCode> cache = CACHE_MANAGER.getCache(CACHE_NAMESPACE_AUTHORIZATION_CODE, String.class, AuthorizationCode.class);
        AuthorizationCode authorizationCode = cache.get(StringUtils.trim(code));
        if (null == authorizationCode) {
            log.error("No cached authorization code [{}] founded!", code);
            return Optional.empty();
        }
        return Optional.of(authorizationCode);
    }

    @Override
    public boolean deleteAuthCodeFromCache(String code) {
        if (StringUtils.isBlank(code)) {
            return false;
        }
        Cache<String, AuthorizationCode> cache = CACHE_MANAGER.getCache(CACHE_NAMESPACE_AUTHORIZATION_CODE, String.class, AuthorizationCode.class);
        cache.remove(StringUtils.trim(code));
        log.info("Delete authorization code [{}] from cache!", code);
        return true;
    }
}
