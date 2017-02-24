package org.zhenchao.passport.oauth.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;

/**
 * @author zhenchao.wang 2017-02-23 23:03
 * @version 1.0.0
 */
public class LoginUtils {

    public static Response login() {
        Response response =
                RestAssured.with().param("username", "zhenchao").param("password", "123456").post("http://localhost:8080/login");
        Assert.assertEquals(200, response.jsonPath().getInt("code"));
        return response;
    }
}
