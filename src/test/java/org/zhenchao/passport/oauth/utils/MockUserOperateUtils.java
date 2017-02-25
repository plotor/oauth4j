package org.zhenchao.passport.oauth.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhenchao.wang 2017-02-23 23:03
 * @version 1.0.0
 */
public class MockUserOperateUtils {

    public static Response login() {
        Response response =
                RestAssured.with().param("username", "zhenchao").param("password", "123456").post("http://localhost:8080/login");
        Assert.assertEquals(200, response.jsonPath().getInt("code"));
        return response;
    }

    public static Response userAuthorize(Response response, Map<String, String> cookies, String scope, String state) {
        Map<String, String> params = new HashMap<>();
        params.put("callback", "http%3A%2F%2Flocalhost%3A8080%2Foauth%2Fauthorize%2Fcode%3Fresponse_type%3Dcode%26redirect_uri%3Dhttp%253A%252F%252Fwww.zhenchao.com%26client_id%3D2882303761517520186");
        params.put("user_id", "100000");
        params.put("client_id", "2882303761517520186");
        params.put("scope", scope);
        params.put("state", state);
        return RestAssured.with().params(params).cookies(response.getCookies()).cookies(cookies).post("http://localhost:8080/oauth/user/authorize");
    }
}
