package org.zhenchao.oauth.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zhenchao.wang 2017-08-08 23:26
 * @version 1.0.0
 */
public abstract class ScopeUtils {

    private static final String SEP = "\\s+";

    /**
     * 校验输入的两个scope之间是否存在交集
     *
     * @param criterion
     * @param input
     * @return
     */
    public static boolean hasIntersection(String criterion, String input) {
        return CollectionUtils.containsAny(
                Arrays.asList(StringUtils.split(criterion, SEP)),
                Arrays.asList(StringUtils.split(input, SEP)));
    }

    /**
     * 校验input是不是criterion的子集
     *
     * @param criterion
     * @param input
     * @return
     */
    public static boolean isSubScopes(String criterion, String input) {
        return CollectionUtils.isSubCollection(
                Arrays.asList(StringUtils.split(input, SEP)),
                Arrays.asList(StringUtils.split(criterion, SEP)));
    }

    /**
     * 对scope信息按照字典顺序排序重组
     *
     * @param scope
     * @return
     */
    public static String format(final String scope) {
        if (StringUtils.isBlank(scope)) {
            return StringUtils.EMPTY;
        }
        List<String> scopes = new ArrayList<>();
        String[] items = StringUtils.trim(scope).split(SEP);
        Arrays.stream(items).forEach(item -> {
            if (NumberUtils.isDigits(item)) {
                scopes.add(item);
            }
        });
        Collections.sort(scopes); // 按照字典顺序
        return StringUtils.join(scopes, SEP);
    }

    /**
     * 对scope集合按照字典顺序组织
     *
     * @param scopeList
     * @return
     */
    public static String format(List<Integer> scopeList) {
        if (CollectionUtils.isEmpty(scopeList)) {
            return StringUtils.EMPTY;
        }
        List<String> scopes = new ArrayList<>();
        scopeList.forEach(scope -> {
            if (scope > 0) {
                scopes.add(String.valueOf(scope));
            }
        });
        Set<String> set = new HashSet<>(scopes);
        scopes.clear();
        scopes.addAll(set);
        Collections.sort(scopes); // 按照字典顺序
        return StringUtils.join(scopes, SEP);
    }

    /**
     * scope字符串转换成list集合
     * 按照自然顺序组织、去重，忽略不是数字的字符
     *
     * @param scope
     * @return
     */
    public static List<Integer> toList(String scope) {
        if (StringUtils.isBlank(scope)) {
            return new ArrayList<>();
        }
        String[] items = StringUtils.trim(scope).split("\\s+");
        Set<Integer> scopeIdSet = new HashSet<>();
        Arrays.stream(items).forEach(item -> {
            if (NumberUtils.isDigits(item)) {
                scopeIdSet.add(NumberUtils.toInt(item));
            }
        });
        List<Integer> scopeList = new ArrayList<>(scopeIdSet);
        Collections.sort(scopeList);
        return scopeList;
    }

    /**
     * 验证scope是否合法
     *
     * @param scope
     * @param scopeSet
     * @return
     */
    public static boolean validateScope(String scope, Set<Integer> scopeSet) {
        if (CollectionUtils.isEmpty(scopeSet) || StringUtils.isBlank(scope)) {
            return false;
        }
        List<Integer> scopeList = toList(scope);
        for (Integer val : scopeList) {
            if (!scopeSet.contains(val)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 验证scope是否合法
     *
     * @param scope
     * @param scopeList
     * @return
     */
    public static boolean validateScope(String scope, List<String> scopeList) {
        if (CollectionUtils.isEmpty(scopeList) || StringUtils.isBlank(scope)) {
            return false;
        }
        // 转换List为Set
        Set<Integer> set = new HashSet<>();
        for (final String s : scopeList) {
            if (NumberUtils.isDigits(s)) {
                set.add(Integer.valueOf(s));
            }
        }
        return validateScope(scope, set);
    }

    /**
     * 生成scope的指纹信息
     *
     * @param scope
     * @return
     */
    public static String getScopeSign(String scope) {
        if (StringUtils.isBlank(scope)) {
            return StringUtils.EMPTY;
        }
        List<String> scopes = Arrays.asList(StringUtils.trim(scope).split(SEP));
        Collections.sort(scopes);
        return StringUtils.mid(DigestUtils.md5Hex(StringUtils.join(scopes, SEP)), 8, 24);
    }

    /**
     * 判断scope1和scope2是否在值上相等，如果其中一个为空字符串也返回false
     *
     * @param scope1
     * @param scope2
     * @return
     */
    public static boolean isSame(String scope1, String scope2) {
        if (StringUtils.isBlank(scope1) || StringUtils.isBlank(scope2)) {
            return false;
        }
        return CollectionUtils.isEqualCollection(
                Arrays.asList(scope1.split(SEP)), Arrays.asList(scope2.split(SEP)));
    }

}
