package org.zhenchao.oauth.handler;

import org.zhenchao.oauth.common.cache.CacheSupport;
import org.zhenchao.oauth.pojo.AuthorizationCode;

/**
 * @author zhenchao.wang 2017-08-09 10:27
 * @version 1.0.0
 */
public class AuthCodeCacheHandler extends CacheSupport<AuthorizationCode> {

    private static class Inner {
        private static final AuthCodeCacheHandler INSTANCE = new AuthCodeCacheHandler();
    }

    private AuthCodeCacheHandler() {
        super.init(NAMESPACE_AUTH_CODE, AuthorizationCode.class, 10);
        log.info("Init auth code cache handler success, namespace[{}]", NAMESPACE_AUTH_CODE);
    }

    public static AuthCodeCacheHandler getInstance() {
        return Inner.INSTANCE;
    }

    public AuthorizationCode get(String key) {
        log.info("Get auth code from cache, key[{}]", key);
        return super.get(NAMESPACE_AUTH_CODE, AuthorizationCode.class, key);
    }

    public void put(String key, AuthorizationCode value) {
        log.info("Put auth code to cache, key[{}]", key);
        super.put(NAMESPACE_AUTH_CODE, AuthorizationCode.class, key, value);
    }

    public void remove(String key) {
        log.info("Remove auth code from cache, key[{}]", key);
        super.remove(NAMESPACE_AUTH_CODE, AuthorizationCode.class, key);
    }

    public void remove(String key, AuthorizationCode value) {
        log.info("Remove auth code from cache, key[{}]", key);
        super.remove(NAMESPACE_AUTH_CODE, AuthorizationCode.class, key, value);
    }
}
