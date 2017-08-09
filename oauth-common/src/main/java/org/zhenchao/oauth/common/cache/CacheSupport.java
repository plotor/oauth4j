package org.zhenchao.oauth.common.cache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhenchao.wang 2017-08-09 09:57
 * @version 1.0.0
 */
@SuppressWarnings("unchecked")
public abstract class CacheSupport<T> implements Namespace {

    protected static final Logger log = LoggerFactory.getLogger(CacheSupport.class);

    protected static CacheManager manager;

    protected abstract void init();

    public T get(String namespace, String key) {
        Cache<String, Object> cache = manager.getCache(namespace, String.class, Object.class);
        Object value = cache.get(key);
        return null == value ? null : (T) value;
    }

    public void put(String namespace, String key, T value) {
        Cache<String, Object> cache = manager.getCache(namespace, String.class, Object.class);
        cache.put(key, value);
    }

    public void remove(String namespace, String key) {
        Cache<String, Object> cache = manager.getCache(namespace, String.class, Object.class);
        cache.remove(key);
    }

    public void remove(String namespace, String key, T value) {
        Cache<String, Object> cache = manager.getCache(namespace, String.class, Object.class);
        cache.remove(key, value);
    }

}
