package org.zhenchao.passport.oauth.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.zhenchao.passport.oauth.commons.ErrorCode;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.SEPARATOR_REDIRECT_SCOPE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 常规工具类
 *
 * @author zhenchao.wang 2017-01-20 23:41
 * @version 1.0.0
 */
public class CommonUtils {

    /**
     * 生成scope的指纹信息
     *
     * @param scope
     * @return
     */
    public static String genScopeSign(String scope) {
        if (StringUtils.isBlank(scope)) {
            return StringUtils.EMPTY;
        }
        List<String> scopes = Arrays.asList(StringUtils.trim(scope).split(SEPARATOR_REDIRECT_SCOPE));
        Collections.sort(scopes);
        return StringUtils.mid(DigestUtils.md5Hex(StringUtils.join(scopes, SEPARATOR_REDIRECT_SCOPE)), 8, 24);
    }

    /**
     * 判断scope1和scope2是否在值上相等，如果其中一个为空字符串也返回false
     *
     * @param scope1
     * @param scope2
     * @return
     */
    public static boolean checkScope(String scope1, String scope2) {
        if (StringUtils.isBlank(scope1) || StringUtils.isBlank(scope2)) {
            return false;
        }
        return CollectionUtils.isEqualCollection(
                Arrays.asList(scope1.split(SEPARATOR_REDIRECT_SCOPE)), Arrays.asList(scope2.split(SEPARATOR_REDIRECT_SCOPE)));
    }

    /**
     * build error response
     *
     * @param mav
     * @param redirectUri
     * @param errorCode
     * @param state
     * @return
     */
    public static ModelAndView buildErrorResponse(ModelAndView mav, String redirectUri, ErrorCode errorCode, String state) {
        List<String> params = new ArrayList<>();
        params.add(String.format("error=%s", errorCode.getCode()));
        params.add(String.format("error_description=%s", errorCode.getDesc()));
        if (StringUtils.isNotBlank(state)) {
            params.add(String.format("state=%s", state));
        }
        mav.setViewName("redirect:" + String.format("%s#%s", redirectUri, StringUtils.join(params, "&")));
        return mav;
    }

}
