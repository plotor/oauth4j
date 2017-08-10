package org.zhenchao.oauth.handler;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.zhenchao.oauth.common.cache.CacheSupport;
import org.zhenchao.oauth.pojo.AuthorizationCode;

import java.util.concurrent.TimeUnit;

/**
 * @author zhenchao.wang 2017-08-09 10:27
 * @version 1.0.0
 */
public class AuthCodeCacheHandler extends CacheSupport<AuthorizationCode> {

    private static class Inner {
        private static final AuthCodeCacheHandler INSTANCE = new AuthCodeCacheHandler();
    }

    private AuthCodeCacheHandler() {
        this.init();
    }

    public static AuthCodeCacheHandler getInstance() {
        return Inner.INSTANCE;
    }

    @Override
    protected void init() {
        // TIPS: 实际应用中对于授权码缓存，只应该在缓存时间上进行控制
        manager = CacheManagerBuilder.newCacheManagerBuilder().withCache(
                NAMESPACE_AUTH_CODE,
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, AuthorizationCode.class, ResourcePoolsBuilder.heap(1024))
                        .withExpiry(Expirations.timeToLiveExpiration(Duration.of(10, TimeUnit.MINUTES)))).build();
        manager.init();
        log.info("Init auth code cache handler success, namespace[{}]", NAMESPACE_AUTH_CODE);
    }

    public AuthorizationCode get(String key) {
        log.info("Get auth code from cache, key[{}]", key);
        return super.get(NAMESPACE_AUTH_CODE, key);
    }

    public void put(String key, AuthorizationCode value) {
        log.info("Put auth code to cache, key[{}]", key);
        super.put(NAMESPACE_AUTH_CODE, key, value);
    }

    public void remove(String key) {
        log.info("Remove auth code from cache, key[{}]", key);
        super.remove(NAMESPACE_AUTH_CODE, key);
    }

    public void remove(String key, AuthorizationCode value) {
        log.info("Remove auth code from cache, key[{}]", key);
        super.remove(NAMESPACE_AUTH_CODE, key, value);
    }
}
