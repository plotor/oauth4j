package org.zhenchao.passport.oauth.utils;

import org.apache.commons.codec.digest.HmacUtils;
import org.junit.Test;

import java.util.Base64;

/**
 * @author zhenchao.wang 2017-01-21 17:19
 * @version 1.0.0
 */
public class OthersTest {

    @Test
    public void test() throws Exception {
        String key = "zhenchao";
        String value = "Hello World! Hello World!";
        String result = Base64.getEncoder().encodeToString(HmacUtils.hmacSha1(key, value));
        System.out.println(result);
    }

}
