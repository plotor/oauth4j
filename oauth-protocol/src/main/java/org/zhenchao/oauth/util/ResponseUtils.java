package org.zhenchao.oauth.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;
import org.zhenchao.oauth.common.ErrorCode;
import org.zhenchao.oauth.common.GlobalConstant;
import org.zhenchao.oauth.common.RequestPath;
import org.zhenchao.oauth.entity.AppInfo;
import org.zhenchao.oauth.entity.Scope;
import org.zhenchao.oauth.entity.UserInfo;
import org.zhenchao.oauth.pojo.TokenRelevantRequestParams;
import org.zhenchao.oauth.token.AbstractAccessToken;
import org.zhenchao.oauth.token.MacAccessToken;
import org.zhenchao.oauth.token.enums.TokenType;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * @author zhenchao.wang 2017-08-09 09:44
 * @version 1.0.0
 */
public abstract class ResponseUtils {

    private static final Logger log = LoggerFactory.getLogger(ResponseUtils.class);

    public static ModelAndView buildErrorResponse(String redirectUri, ErrorCode errorCode, String state) {
        return buildErrorResponse(new ModelAndView(), redirectUri, errorCode, state);
    }

    public static ModelAndView buildErrorResponse(ModelAndView mav, String redirectUri, ErrorCode errorCode, String state) {
        List<String> params = new ArrayList<>();
        params.add(String.format("error=%s", errorCode.getCode()));
        if (StringUtils.isNotBlank(errorCode.getDescription())) {
            try {
                params.add(String.format("error_description=%s", URLEncoder.encode(errorCode.getDescription(), "UTF-8")));
            } catch (UnsupportedEncodingException e) {
                // never happen
            }
        }
        if (StringUtils.isNotBlank(state)) {
            params.add(String.format("state=%s", state));
        }
        String errorUrl = String.format("%s?%s", redirectUri, StringUtils.join(params, "&"));
        log.debug("Redirect to error page, url[{}]", errorUrl);
        mav.setViewName("redirect:" + errorUrl);
        return mav;
    }

    public static ModelAndView buildImplicitErrorResponse(String redirectUri, ErrorCode errorCode, String state) {
        return buildImplicitErrorResponse(new ModelAndView(), redirectUri, errorCode, state);
    }

    public static ModelAndView buildImplicitErrorResponse(ModelAndView mav, String redirectUri, ErrorCode errorCode, String state) {
        List<String> params = new ArrayList<>();
        params.add(String.format("error=%s", errorCode.getCode()));
        if (StringUtils.isNotBlank(errorCode.getDescription())) {
            try {
                params.add(String.format("error_description=%s", URLEncoder.encode(errorCode.getDescription(), "UTF-8")));
            } catch (UnsupportedEncodingException e) {
                // never happen
            }
        }
        if (StringUtils.isNotBlank(state)) {
            params.add(String.format("state=%s", state));
        }
        mav.setViewName("redirect:" + String.format("%s#%s", redirectUri, StringUtils.join(params, "&")));
        return mav;
    }

    public static ModelAndView buildLoginResponse(HttpServletRequest request, AppInfo appInfo) {
        ModelAndView mav = new ModelAndView();
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
            builder.path(RequestPath.PATH_ROOT_LOGIN)
                    .queryParam(GlobalConstant.CALLBACK, RequestUtils.getEncodeRequestUrl(request))
                    .queryParam("app_name", URLEncoder.encode(appInfo.getAppName(), "UTF-8"));
            String loginUrl = builder.toUriString();
            log.debug("Redirect to login page, appId[{}], url[{}]", appInfo.getAppId(), loginUrl);
            mav.setViewName("redirect:" + loginUrl);
        } catch (UnsupportedEncodingException e) {
            // never happen
        }
        return mav;
    }

    public static ModelAndView buildAuthorizeResponse(
            HttpServletRequest request, List<Scope> scopes, UserInfo userInfo, AppInfo appInfo, String state) {
        return buildAuthorizeResponse(new ModelAndView(), request, scopes, userInfo, appInfo, state);
    }

    public static ModelAndView buildAuthorizeResponse(
            ModelAndView mav, HttpServletRequest request, List<Scope> scopes, UserInfo userInfo, AppInfo appInfo, String state) {
        // TODO 页面跳转需要添加参数签名，防止请求被更改或伪造
        mav.setViewName("user-authorize");
        mav.addObject(GlobalConstant.CALLBACK, RequestUtils.getEncodeRequestUrl(request))
                .addObject("scopes", scopes)
                .addObject("user", userInfo)
                .addObject("app", appInfo);
        if (StringUtils.isNotBlank(state)) {
            mav.addObject("state", StringUtils.trimToEmpty(state));
        }
        log.debug("Redirect to user authorize page, appId[{}], userId[{}]", appInfo.getAppId(), userInfo.getId());
        return mav;
    }

    public static ModelAndView buildImplicitTokenResponse(AbstractAccessToken accessToken, TokenRelevantRequestParams tokenParams) throws IOException {
        ModelAndView mav = new ModelAndView();
        List<String> params = new ArrayList<>();
        params.add(String.format("access_token=%s", accessToken.getValue()));
        params.add(String.format("token_type=%s", accessToken.getType().getValue()));
        params.add(String.format("expires_in=%d", accessToken.getExpirationTime()));
        params.add(String.format("scope=%s", URLEncoder.encode(accessToken.getScope(), "UTF-8")));
        if (TokenType.MAC.equals(accessToken.getType())) {
            // mac类型token
            params.add(String.format("mac_key=%s", accessToken.getKey()));
            params.add(String.format("mac_algorithm=%s", ((MacAccessToken) accessToken).getAlgorithm().getValue()));
        }
        if (StringUtils.isNotBlank(tokenParams.getState())) {
            params.add(String.format("state=%s", tokenParams.getState()));
        }
        mav.setViewName("redirect:" + String.format("%s#%s", tokenParams.getRedirectUri(), StringUtils.join(params, "&")));
        return mav;
    }

    public static JSONObject format(AbstractAccessToken accessToken) throws IOException {
        JSONObject result = new JSONObject();
        result.put("access_token", accessToken.getValue());
        result.put("expires_in", accessToken.getExpirationTime());
        if (StringUtils.isNotBlank(accessToken.getRefreshToken())) {
            result.put("refresh_token", accessToken.getRefreshToken());
        }
        result.put("scope", accessToken.getScope());
        if (TokenType.MAC.equals(accessToken.getType())) {
            // MAC类型token需要指定key和algorithm
            result.put("mac_key", accessToken.getKey());
            result.put("mac_algorithm", ((MacAccessToken) accessToken).getAlgorithm().getValue());
        }
        return result;
    }
}
