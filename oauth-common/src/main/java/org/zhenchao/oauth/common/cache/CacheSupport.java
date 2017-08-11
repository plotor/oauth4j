package org.zhenchao.oauth.common.cache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author zhenchao.wang 2017-08-09 09:57
 * @version 1.0.0
 */
@SuppressWarnings("unchecked")
public abstract class CacheSupport<T> implements Namespace {

    protected static final Logger log = LoggerFactory.getLogger(CacheSupport.class);

    protected static CacheManager manager;

    protected void init(String namespace, Class valueType, long expire) {
        // TIPS: 实际应用中对于授权码缓存，只应该在缓存时间上进行控制
        manager = CacheManagerBuilder.newCacheManagerBuilder().withCache(
                namespace,
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, valueType, ResourcePoolsBuilder.heap(1024))
                        .withExpiry(Expirations.timeToLiveExpiration(Duration.of(expire, TimeUnit.MINUTES)))).build();
        manager.init();
    }

    public T get(String namespace, Class<T> clazz, String key) {
        Cache<String, T> cache = this.getCacheInstance(namespace, clazz);
        Object value = cache.get(key);
        return null == value ? null : (T) value;
    }

    public void put(String namespace, Class<T> clazz, String key, T value) {
        Cache<String, T> cache = this.getCacheInstance(namespace, clazz);
        cache.put(key, value);
    }

    public void remove(String namespace, Class<T> clazz, String key) {
        Cache<String, T> cache = this.getCacheInstance(namespace, clazz);
        cache.remove(key);
    }

    public void remove(String namespace, Class<T> clazz, String key, T value) {
        Cache<String, T> cache = this.getCacheInstance(namespace, clazz);
        cache.remove(key, value);
    }

    private Cache<String, T> getCacheInstance(String namespace, Class<T> clazz) {
        return manager.getCache(namespace, String.class, clazz);
    }

}
