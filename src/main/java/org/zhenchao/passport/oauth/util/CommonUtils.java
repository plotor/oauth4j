package org.zhenchao.passport.oauth.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import static org.zhenchao.passport.oauth.common.GlobalConstant.SEPARATOR_REDIRECT_SCOPE;

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
    public static boolean checkScopeIsSame(String scope1, String scope2) {
        if (StringUtils.isBlank(scope1) || StringUtils.isBlank(scope2)) {
            return false;
        }
        return CollectionUtils.isEqualCollection(
                Arrays.asList(scope1.split(SEPARATOR_REDIRECT_SCOPE)), Arrays.asList(scope2.split(SEPARATOR_REDIRECT_SCOPE)));
    }

}
