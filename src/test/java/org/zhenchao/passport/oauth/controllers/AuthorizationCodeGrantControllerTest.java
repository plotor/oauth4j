package org.zhenchao.passport.oauth.controllers;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.PATH_OAUTH_AUTHORIZE_CODE;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.PATH_ROOT_OAUTH;
import org.zhenchao.passport.oauth.utils.LoginUtils;

/**
 * authorization code grant ut
 *
 * @author zhenchao.wang 2017-02-21 22:55
 * @version 1.0.0
 */
public class AuthorizationCodeGrantControllerTest {

    private static final long CLIENT_ID = 2882303761517520186L;

    private static final String REDIRECT_URI = "http://www.zhenchao.com";

    private static final String SCOPE = "1 4";

    @BeforeClass
    public void setUp() throws Exception {
        LoginUtils.login(); // FIXME 2017-2-23 23:48:53
        RestAssured.baseURI = "http://localhost:8080" + PATH_ROOT_OAUTH;
    }

    @Test
    public void authorizeTest() throws Exception {
        // 不指定response_type
        Response response = RestAssured.get(PATH_OAUTH_AUTHORIZE_CODE);
        // Assert.assertEquals(400, response.getStatusCode());
        System.out.println(response.asString());
    }

}