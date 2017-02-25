package org.zhenchao.passport.oauth.controllers;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.zhenchao.passport.oauth.commons.ErrorCode;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.PATH_OAUTH_AUTHORIZE_CODE;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.PATH_ROOT_OAUTH;
import org.zhenchao.passport.oauth.commons.ResponseType;
import org.zhenchao.passport.oauth.utils.LoginUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * authorization code grant ut
 *
 * @author zhenchao.wang 2017-02-21 22:55
 * @version 1.0.0
 */
public class AuthorizationCodeGrantControllerTest {

    private static final long CLIENT_ID = 2882303761517520186L;

    private static final String REDIRECT_URI = "http://www.zhenchao.com";

    private static final String SCOPE = "1 2 4";

    private Response resp4Login;

    @Before
    public void setUp() throws Exception {
        RestAssured.baseURI = "http://localhost:8080" + PATH_ROOT_OAUTH;
        resp4Login = LoginUtils.login();
    }

    @Test
    public void authorizeWithErrorRequestParamsTest() throws Exception {
        // 必要参数缺失
        Map<String, Object> params = new HashMap<>();
        Response response = RestAssured.with().params(params).cookies(resp4Login.getCookies()).get(PATH_OAUTH_AUTHORIZE_CODE);
        Assert.assertEquals(400, response.getStatusCode());
        // System.out.println(response.asString());

        params.put("response_type", ResponseType.AUTHORIZATION_CODE.getType());
        response = RestAssured.with().params(params).cookies(resp4Login.getCookies()).get(PATH_OAUTH_AUTHORIZE_CODE);
        Assert.assertEquals(400, response.getStatusCode());
        // System.out.println(response.asString());

        params.put("client_id", CLIENT_ID);
        response = RestAssured.with().params(params).cookies(resp4Login.getCookies()).get(PATH_OAUTH_AUTHORIZE_CODE);
        Assert.assertEquals(400, response.getStatusCode());
        // System.out.println(response.asString());

        params.put("redirect_uri", REDIRECT_URI);
        response = RestAssured.with().params(params).cookies(resp4Login.getCookies()).post(PATH_OAUTH_AUTHORIZE_CODE);
        Assert.assertEquals(200, response.getStatusCode());
        Assert.assertTrue(response.asString().contains("用户授权"));

        String errorResponseType = RandomStringUtils.randomAlphanumeric(8);
        params.put("response_type", errorResponseType);
        response = RestAssured.with().params(params).cookies(resp4Login.getCookies()).post(PATH_OAUTH_AUTHORIZE_CODE);
        Assert.assertEquals(302, response.getStatusCode());
        Map<String, String> errors = this.getLocationUrlParamsValue(response);
        Assert.assertEquals(ErrorCode.UNSUPPORTED_RESPONSE_TYPE.getCode(), NumberUtils.toInt(errors.get("error")));

        long errorClientId = RandomUtils.nextLong();
        params.put("response_type", ResponseType.AUTHORIZATION_CODE.getType());
        params.put("client_id", errorClientId);
        response = RestAssured.with().params(params).cookies(resp4Login.getCookies()).post(PATH_OAUTH_AUTHORIZE_CODE);
        Assert.assertEquals(302, response.getStatusCode());
        errors = this.getLocationUrlParamsValue(response);
        Assert.assertEquals(ErrorCode.CLIENT_NOT_EXIST.getCode(), NumberUtils.toInt(errors.get("error")));

        String errorRedirectUrl = "https://www.google.com";
        params.put("client_id", CLIENT_ID);
        params.put("redirect_uri", errorRedirectUrl);
        response = RestAssured.with().params(params).cookies(resp4Login.getCookies()).post(PATH_OAUTH_AUTHORIZE_CODE);
        Assert.assertEquals(200, response.getStatusCode());
        System.out.println(response.asString());
        Assert.assertEquals(ErrorCode.INVALID_REDIRECT_URI.getCode(), response.jsonPath().getInt("code"));
    }

    @Test
    public void test() throws Exception {
        Assert.assertTrue(UrlValidator.getInstance().isValid("https://www.zhenchao.org"));
    }

    private Map<String, String> getLocationUrlParamsValue(Response response) {
        String location = response.getHeader("Location");
        if (StringUtils.isBlank(location)) {
            return new HashMap<>();
        }
        return this.getUrlParamsValue(location);
    }

    private Map<String, String> getUrlParamsValue(String url) {
        Map<String, String> result = new HashMap<>();
        try {
            String query = new URI(url).getQuery();
            if (StringUtils.isNotBlank(query)) {
                String[] pairs = query.split("&");
                for (final String pair : pairs) {
                    int index = pair.indexOf('=');
                    result.put(pair.substring(0, index), pair.substring(index + 1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}