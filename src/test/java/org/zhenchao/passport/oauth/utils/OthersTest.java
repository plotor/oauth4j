package org.zhenchao.passport.oauth.utils;

import org.junit.Test;

import java.net.URLEncoder;

/**
 * @author zhenchao.wang 2017-01-21 17:19
 * @version 1.0.0
 */
public class OthersTest {

    @Test
    public void test() throws Exception {
        String url = "http://localhost:8080/oauth/authorize/code?response_type=code&client_id=1000000000000000001&redirect_uri=http://www.zhenchao.com&scope=1-4";
        System.out.println(URLEncoder.encode(url, "utf-8"));
    }

}
