package org.zhenchao.passport.oauth.controller;

import com.alibaba.fastjson.JSONObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.zhenchao.oauth.common.ErrorCode;
import org.zhenchao.passport.oauth.enums.ResponseType;
import org.zhenchao.passport.oauth.common.RequestPath;
import org.zhenchao.passport.oauth.util.MockUserOperationUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * implicit grant ut
 *
 * @author zhenchao.wang 2017-02-25 17:27
 * @version 1.0.0
 */
public class ImplicitGrantControllerTest implements RequestPath{

    private static final long CLIENT_ID = 2882303761517520186L;

    private static final String REDIRECT_URI = "http://www.zhenchao.com";

    private static final String SCOPE = "1 2 4";

    private static final String ALL_SCOPE = "1 2 4 5";

    private Response resp4Login;

    @Before
    public void setUp() throws Exception {
        RestAssured.baseURI = "http://localhost:8080" + PATH_ROOT_OAUTH;
        resp4Login = MockUserOperationUtils.login();
    }

    @Test
    public void requestAccessTokenWithErrorParamsTest() throws Exception {
        Map<String, Object> params = new HashMap<>();

        // 缺失必要的参数
        Response response = RestAssured.with().params(params).cookies(resp4Login.cookies()).get(PATH_OAUTH_IMPLICIT_TOKEN);
        Assert.assertEquals(400, response.getStatusCode());

        params.put("response_type", ResponseType.IMPLICIT.getType());
        response = RestAssured.with().params(params).cookies(resp4Login.cookies()).get(PATH_OAUTH_IMPLICIT_TOKEN);
        Assert.assertEquals(400, response.getStatusCode());

        // 用户未登录
        response = RestAssured.with().params(params).get(PATH_OAUTH_IMPLICIT_TOKEN);
        Assert.assertEquals(200, response.getStatusCode());
        Assert.assertTrue(response.asString().contains("用户登录"));

        params.put("client_id", CLIENT_ID);
        response = RestAssured.with().params(params).cookies(resp4Login.cookies()).get(PATH_OAUTH_IMPLICIT_TOKEN);
        Assert.assertEquals(400, response.getStatusCode());

        params.put("redirect_uri", REDIRECT_URI);
        response = RestAssured.with().params(params).cookies(resp4Login.cookies()).get(PATH_OAUTH_IMPLICIT_TOKEN);
        Assert.assertTrue(response.asString().contains("用户授权"));

        params = new HashMap<>();
        params.put("response_type", ResponseType.IMPLICIT.getType());
        params.put("client_id", CLIENT_ID);
        params.put("redirect_uri", REDIRECT_URI);

        // 错误的response type
        params.put("response_type", ResponseType.AUTHORIZATION_CODE.getType());
        response = RestAssured.with().params(params).cookies(resp4Login.cookies()).get(PATH_OAUTH_IMPLICIT_TOKEN);
        Assert.assertEquals(400, response.getStatusCode());

        // 错误的client id
        params.put("response_type", ResponseType.IMPLICIT.getType());
        long errorClientId = RandomUtils.nextLong();
        params.put("client_id", errorClientId);
        response = RestAssured.with().params(params).cookies(resp4Login.cookies()).get(PATH_OAUTH_IMPLICIT_TOKEN);
        System.out.println(response.asString());
        JSONObject json = JSONObject.parseObject(response.asString());
        Assert.assertEquals(ErrorCode.CLIENT_NOT_EXIST.getCode(), json.getIntValue("code"));

        // 错误的redirect uri
        String errorRedirectUri = "https://www.google.com";
        params.put("client_id", CLIENT_ID);
        params.put("redirect_uri", errorRedirectUri);
        response = RestAssured.with().params(params).cookies(resp4Login.cookies()).get(PATH_OAUTH_IMPLICIT_TOKEN);
        System.out.println(response.asString());
        json = JSONObject.parseObject(response.asString());
        Assert.assertEquals(ErrorCode.INVALID_REDIRECT_URI.getCode(), json.getIntValue("code"));
    }

    @Test
    public void requestAccessTokenTest() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("response_type", ResponseType.IMPLICIT.getType());
        params.put("client_id", CLIENT_ID);
        params.put("redirect_uri", REDIRECT_URI);
        Response response = RestAssured.with().params(params).cookies(resp4Login.cookies()).get(PATH_OAUTH_IMPLICIT_TOKEN);
        response = MockUserOperationUtils.implicitGrantUserAuthorize(response, resp4Login.getCookies(), ALL_SCOPE, StringUtils.EMPTY);
        String location = response.getHeader("Location");
        response = RestAssured.with().cookies(resp4Login.cookies()).post(location);
        String result = response.getHeader("Location");
        System.out.println(result);
        /*result = result.replace("#", "?");
        System.out.println(result);
        Map<String, String> results = ResultUtils.getUrlParamsValue(result);
        Assert.assertNotNull(results.get("access_token"));
        Assert.assertNotNull(results.get("mac_key"));
        Assert.assertEquals(MacAccessToken.ALGORITHM.HMAC_SHA_1.getValue(), results.get("mac_algorithm"));*/
    }

}