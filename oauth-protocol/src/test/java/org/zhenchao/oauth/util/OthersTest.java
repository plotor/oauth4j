package org.zhenchao.oauth.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * @author zhenchao.wang 2017-01-21 17:19
 * @version 1.0.0
 */
public class OthersTest {

    @Test
    public void test() throws Exception {
        /*String key = "zhenchao";
        String value = "Hello World! Hello World!";
        String result = Base64.getEncoder().encodeToString(HmacUtils.hmacSha1(key, value));
        System.out.println(result);*/

        String randomStr = RandomStringUtils.randomAlphanumeric(64);
        System.out.println(randomStr);
    }

}
