package org.zhenchao.passport.oauth.controllers;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.zhenchao.passport.oauth.commons.ErrorCode;
import org.zhenchao.passport.oauth.commons.GlobalConstant;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.PATH_OAUTH_AUTHORIZE_CODE;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.PATH_OAUTH_AUTHORIZE_TOKEN;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.PATH_ROOT_OAUTH;
import org.zhenchao.passport.oauth.commons.GrantType;
import org.zhenchao.passport.oauth.commons.ResponseType;
import org.zhenchao.passport.oauth.token.MacAccessToken;
import org.zhenchao.passport.oauth.utils.MockUserOperateUtils;

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

    private static final String ALL_SCOPE = "1 2 4 5";

    private Response resp4Login;

    @Before
    public void setUp() throws Exception {
        RestAssured.baseURI = "http://localhost:8080" + PATH_ROOT_OAUTH;
        resp4Login = MockUserOperateUtils.login();
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
    public void authorizeTest() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("response_type", ResponseType.AUTHORIZATION_CODE.getType());
        params.put("client_id", CLIENT_ID);
        params.put("redirect_uri", REDIRECT_URI);
        Response response = RestAssured.with().params(params).cookies(resp4Login.cookies()).get(PATH_OAUTH_AUTHORIZE_CODE);
        Assert.assertEquals(200, response.getStatusCode());
        // System.out.println(response.asString());
        response = MockUserOperateUtils.userAuthorize(response, resp4Login.cookies(), ALL_SCOPE, StringUtils.EMPTY);
        System.out.println(response.asString());
        Assert.assertEquals(302, response.getStatusCode());
        String redirectUrl = response.getHeader("Location");
        System.out.println(redirectUrl);
        response = RestAssured.with().cookies(resp4Login.getCookies()).cookies(response.cookies()).post(redirectUrl);
        System.out.println(response.getHeader("Location"));
        Map<String, String> results = this.getLocationUrlParamsValue(response);
        Assert.assertNotNull(results.get("code"));
    }

    @Test
    public void requestAccessTokenTest() throws Exception {
        // 获取授权码
        Map<String, Object> params = new HashMap<>();
        params.put("response_type", ResponseType.AUTHORIZATION_CODE.getType());
        params.put("client_id", CLIENT_ID);
        params.put("redirect_uri", REDIRECT_URI);
        Response response = RestAssured.with().params(params).cookies(resp4Login.cookies()).get(PATH_OAUTH_AUTHORIZE_CODE);
        Assert.assertEquals(200, response.getStatusCode());
        // System.out.println(response.asString());
        response = MockUserOperateUtils.userAuthorize(response, resp4Login.cookies(), ALL_SCOPE, StringUtils.EMPTY);
        System.out.println(response.asString());
        Assert.assertEquals(302, response.getStatusCode());
        String redirectUrl = response.getHeader("Location");
        System.out.println(redirectUrl);
        response = RestAssured.with().cookies(resp4Login.getCookies()).cookies(response.cookies()).post(redirectUrl);
        System.out.println(response.getHeader("Location"));
        Map<String, String> results = this.getLocationUrlParamsValue(response);
        Assert.assertNotNull(results.get("code"));

        // 获取token，错误的请求
        String code = results.get("code");
        params = new HashMap<>();
        response = RestAssured.with().params(params).get(PATH_OAUTH_AUTHORIZE_TOKEN);
        Assert.assertEquals(400, response.getStatusCode());

        params = new HashMap<>();
        params.put("grant_type", GrantType.AUTHORIZATION_CODE.getType());
        response = RestAssured.with().params(params).get(PATH_OAUTH_AUTHORIZE_TOKEN);
        Assert.assertEquals(400, response.getStatusCode());

        params.put("code", code);
        response = RestAssured.with().params(params).get(PATH_OAUTH_AUTHORIZE_TOKEN);
        Assert.assertEquals(400, response.getStatusCode());

        params.put("redirect_uri", REDIRECT_URI);
        response = RestAssured.with().params(params).get(PATH_OAUTH_AUTHORIZE_TOKEN);
        Assert.assertEquals(400, response.getStatusCode());

        params.put("client_id", CLIENT_ID);
        response = RestAssured.with().params(params).get(PATH_OAUTH_AUTHORIZE_TOKEN);
        Assert.assertEquals(200, response.getStatusCode());

    }

    @Test
    public void requestAccessTokenWithErrorGrantTypeTest() throws Exception {
        // 获取授权码
        Map<String, Object> params = new HashMap<>();
        params.put("response_type", ResponseType.AUTHORIZATION_CODE.getType());
        params.put("client_id", CLIENT_ID);
        params.put("redirect_uri", REDIRECT_URI);
        Response response = RestAssured.with().params(params).cookies(resp4Login.cookies()).get(PATH_OAUTH_AUTHORIZE_CODE);
        Assert.assertEquals(200, response.getStatusCode());
        // System.out.println(response.asString());
        response = MockUserOperateUtils.userAuthorize(response, resp4Login.cookies(), ALL_SCOPE, StringUtils.EMPTY);
        System.out.println(response.asString());
        Assert.assertEquals(302, response.getStatusCode());
        String redirectUrl = response.getHeader("Location");
        System.out.println(redirectUrl);
        response = RestAssured.with().cookies(resp4Login.getCookies()).cookies(response.cookies()).post(redirectUrl);
        System.out.println(response.getHeader("Location"));
        Map<String, String> results = this.getLocationUrlParamsValue(response);
        Assert.assertNotNull(results.get("code"));

        // 获取token
        params = new HashMap<>();
        params.put("grant_type", GrantType.AUTHORIZATION_CODE.getType());
        params.put("code", results.get("code"));
        params.put("redirect_uri", REDIRECT_URI);
        params.put("client_id", CLIENT_ID);

        // 错误的grant type
        String errorGrantType = RandomStringUtils.randomAlphanumeric(8);
        params.put("grant_type", errorGrantType);
        response = RestAssured.with().params(params).get(PATH_OAUTH_AUTHORIZE_TOKEN);
        JSONObject json = JSONObject.fromObject(response.asString());
        Assert.assertEquals(ErrorCode.UNSUPPORTED_GRANT_TYPE.getCode(), json.getInt("code"));
    }

    @Test
    public void requestAccessTokenWithErrorCodeTest() throws Exception {
        // 获取授权码
        Map<String, Object> params = new HashMap<>();
        params.put("response_type", ResponseType.AUTHORIZATION_CODE.getType());
        params.put("client_id", CLIENT_ID);
        params.put("redirect_uri", REDIRECT_URI);
        Response response = RestAssured.with().params(params).cookies(resp4Login.cookies()).get(PATH_OAUTH_AUTHORIZE_CODE);
        Assert.assertEquals(200, response.getStatusCode());
        // System.out.println(response.asString());
        response = MockUserOperateUtils.userAuthorize(response, resp4Login.cookies(), ALL_SCOPE, StringUtils.EMPTY);
        System.out.println(response.asString());
        Assert.assertEquals(302, response.getStatusCode());
        String redirectUrl = response.getHeader("Location");
        System.out.println(redirectUrl);
        response = RestAssured.with().cookies(resp4Login.getCookies()).cookies(response.cookies()).post(redirectUrl);
        System.out.println(response.getHeader("Location"));
        Map<String, String> results = this.getLocationUrlParamsValue(response);
        Assert.assertNotNull(results.get("code"));

        // 获取token
        params = new HashMap<>();
        params.put("grant_type", GrantType.AUTHORIZATION_CODE.getType());
        params.put("code", results.get("code"));
        params.put("redirect_uri", REDIRECT_URI);
        params.put("client_id", CLIENT_ID);

        // 错误的code
        String errorCode = RandomStringUtils.randomAlphanumeric(8);
        params.put("code", errorCode);
        response = RestAssured.with().params(params).get(PATH_OAUTH_AUTHORIZE_TOKEN);
        JSONObject json = JSONObject.fromObject(response.asString());
        Assert.assertEquals(ErrorCode.INVALID_AUTHORIZATION_CODE.getCode(), json.getInt("code"));
    }

    @Test
    public void requestAccessTokenWithErrorRedirectUriTest() throws Exception {
        // 获取授权码
        Map<String, Object> params = new HashMap<>();
        params.put("response_type", ResponseType.AUTHORIZATION_CODE.getType());
        params.put("client_id", CLIENT_ID);
        params.put("redirect_uri", REDIRECT_URI);
        Response response = RestAssured.with().params(params).cookies(resp4Login.cookies()).get(PATH_OAUTH_AUTHORIZE_CODE);
        Assert.assertEquals(200, response.getStatusCode());
        // System.out.println(response.asString());
        response = MockUserOperateUtils.userAuthorize(response, resp4Login.cookies(), ALL_SCOPE, StringUtils.EMPTY);
        System.out.println(response.asString());
        Assert.assertEquals(302, response.getStatusCode());
        String redirectUrl = response.getHeader("Location");
        System.out.println(redirectUrl);
        response = RestAssured.with().cookies(resp4Login.getCookies()).cookies(response.cookies()).post(redirectUrl);
        System.out.println(response.getHeader("Location"));
        Map<String, String> results = this.getLocationUrlParamsValue(response);
        Assert.assertNotNull(results.get("code"));

        // 获取token
        params = new HashMap<>();
        params.put("grant_type", GrantType.AUTHORIZATION_CODE.getType());
        params.put("code", results.get("code"));
        params.put("redirect_uri", REDIRECT_URI);
        params.put("client_id", CLIENT_ID);

        // 错误的redirect uri
        String errorRedirectUrl = "https://www.google.com";
        params.put("redirect_uri", errorRedirectUrl);
        response = RestAssured.with().params(params).get(PATH_OAUTH_AUTHORIZE_TOKEN);
        JSONObject json = JSONObject.fromObject(response.asString());
        Assert.assertEquals(ErrorCode.INVALID_GRANT.getCode(), json.getInt("code"));
    }

    @Test
    public void requestAccessTokenWithErrorClientIdTest() throws Exception {
        // 获取授权码
        Map<String, Object> params = new HashMap<>();
        params.put("response_type", ResponseType.AUTHORIZATION_CODE.getType());
        params.put("client_id", CLIENT_ID);
        params.put("redirect_uri", REDIRECT_URI);
        Response response = RestAssured.with().params(params).cookies(resp4Login.cookies()).get(PATH_OAUTH_AUTHORIZE_CODE);
        Assert.assertEquals(200, response.getStatusCode());
        // System.out.println(response.asString());
        response = MockUserOperateUtils.userAuthorize(response, resp4Login.cookies(), ALL_SCOPE, StringUtils.EMPTY);
        System.out.println(response.asString());
        Assert.assertEquals(302, response.getStatusCode());
        String redirectUrl = response.getHeader("Location");
        System.out.println(redirectUrl);
        response = RestAssured.with().cookies(resp4Login.getCookies()).cookies(response.cookies()).post(redirectUrl);
        System.out.println(response.getHeader("Location"));
        Map<String, String> results = this.getLocationUrlParamsValue(response);
        Assert.assertNotNull(results.get("code"));

        // 获取token
        params = new HashMap<>();
        params.put("grant_type", GrantType.AUTHORIZATION_CODE.getType());
        params.put("code", results.get("code"));
        params.put("redirect_uri", REDIRECT_URI);
        params.put("client_id", CLIENT_ID);

        // 错误的client id
        long errorClientId = org.apache.commons.lang.math.RandomUtils.nextLong();
        params.put("client_id", errorClientId);
        response = RestAssured.with().params(params).get(PATH_OAUTH_AUTHORIZE_TOKEN);
        JSONObject json = JSONObject.fromObject(response.asString());
        Assert.assertEquals(ErrorCode.UNAUTHORIZED_CLIENT.getCode(), json.getInt("code"));
    }

    @Test
    public void requestAcessTokenTest() throws Exception {
        // 获取授权码
        Map<String, Object> params = new HashMap<>();
        params.put("response_type", ResponseType.AUTHORIZATION_CODE.getType());
        params.put("client_id", CLIENT_ID);
        params.put("redirect_uri", REDIRECT_URI);
        Response response = RestAssured.with().params(params).cookies(resp4Login.cookies()).get(PATH_OAUTH_AUTHORIZE_CODE);
        Assert.assertEquals(200, response.getStatusCode());
        // System.out.println(response.asString());
        response = MockUserOperateUtils.userAuthorize(response, resp4Login.cookies(), ALL_SCOPE, StringUtils.EMPTY);
        System.out.println(response.asString());
        Assert.assertEquals(302, response.getStatusCode());
        String redirectUrl = response.getHeader("Location");
        System.out.println(redirectUrl);
        response = RestAssured.with().cookies(resp4Login.getCookies()).cookies(response.cookies()).post(redirectUrl);
        System.out.println(response.getHeader("Location"));
        Map<String, String> results = this.getLocationUrlParamsValue(response);
        Assert.assertNotNull(results.get("code"));

        // 获取token
        params = new HashMap<>();
        params.put("grant_type", GrantType.AUTHORIZATION_CODE.getType());
        params.put("code", results.get("code"));
        params.put("redirect_uri", REDIRECT_URI);
        params.put("client_id", CLIENT_ID);

        response = RestAssured.with().params(params).get(PATH_OAUTH_AUTHORIZE_TOKEN);
        String result = StringEscapeUtils.unescapeJson(response.asString());  // 反转义
        result = result.substring(1, result.length() - 1);
        System.out.println(result);
        Assert.assertTrue(result.startsWith(GlobalConstant.JSON_SAFE_PREFIX));
        result = result.replace(GlobalConstant.JSON_SAFE_PREFIX, StringUtils.EMPTY);
        JSONObject json = JSONObject.fromObject(result);
        System.out.println(json);
        Assert.assertNotNull(json.getString("access_token"));
        Assert.assertNotNull(json.getString("mac_key"));
        Assert.assertEquals(MacAccessToken.ALGORITHM.HMAC_SHA_1.getValue(), json.getString("mac_algorithm"));

        // 二次使用同一个code
        response = RestAssured.with().params(params).get(PATH_OAUTH_AUTHORIZE_TOKEN);
        json = JSONObject.fromObject(response.asString());
        Assert.assertEquals(ErrorCode.INVALID_AUTHORIZATION_CODE.getCode(), json.getInt("code"));
    }

    /**
     * 解析Location字段中的url参数列表
     *
     * @param response
     * @return
     */
    private Map<String, String> getLocationUrlParamsValue(Response response) {
        String location = response.getHeader("Location");
        if (StringUtils.isBlank(location)) {
            return new HashMap<>();
        }
        return this.getUrlParamsValue(location);
    }

    /**
     * 解析url参数列表
     *
     * @param url
     * @return
     */
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