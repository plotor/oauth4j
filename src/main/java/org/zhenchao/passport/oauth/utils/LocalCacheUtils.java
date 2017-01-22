package org.zhenchao.passport.oauth.utils;

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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * local cache utils base on ehcache
 *
 * @author zhenchao.wang 2017-01-22 14:49
 * @version 1.0.0
 */
public class LocalCacheUtils {

    private static final Logger log = LoggerFactory.getLogger(LocalCacheUtils.class);

    private static final CacheManager CACHE_MANAGER;

    static {
        CACHE_MANAGER = CacheManagerBuilder.newCacheManagerBuilder().build();
        CACHE_MANAGER.init();
    }

    /** 授权有效时间（10分钟） */
    private static final int AUTHORIZATION_CODE_EXPIRATION = 10;

    private static Map<String, Cache<String, Object>> namespaces = new ConcurrentHashMap<>();

    /**
     * 获取指定namespace的cacheHandler
     *
     * @param namespace
     * @param expireMinutes 仅对首次调用有效
     * @return
     */
    public static Cache<String, Object> getCacheHandler(String namespace, int expireMinutes) {
        if (StringUtils.isBlank(namespace)) {
            throw new IllegalArgumentException("cache namespace can't be null or empty!");
        }
        Cache<String, Object> cache = namespaces.get(namespace);
        if (null == cache) {
            int duration = expireMinutes < 0 ? AUTHORIZATION_CODE_EXPIRATION : expireMinutes;
            cache = CACHE_MANAGER.createCache(
                    namespace,
                    CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Object.class,
                            ResourcePoolsBuilder.newResourcePoolsBuilder())
                            .withExpiry(Expirations.timeToLiveExpiration(Duration.of(duration, TimeUnit.MINUTES))));
            namespaces.put(namespace, cache);
        }
        return cache;
    }

}
