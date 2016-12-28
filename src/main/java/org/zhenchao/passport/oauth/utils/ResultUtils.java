package org.zhenchao.passport.oauth.utils;

import net.sf.json.JSONObject;
import org.zhenchao.passport.oauth.commons.ErrorCode;

import java.util.Map;

/**
 * 响应返回工具类
 *
 * @author zhenchao.wang 2016-12-28 17:49
 * @version 1.0.0
 */
public class ResultUtils {

    /**
     * 生成错误的JSON返回数据
     *
     * @param errorCode
     * @return
     */
    public static JSONObject genFailedJsonResult(ErrorCode errorCode) {
        JSONObject result = new JSONObject();
        result.put("result", "ERROR");
        result.put("code", errorCode.getCode());
        result.put("description", errorCode.getDesc());
        return result;
    }

    /**
     * 生成正确的JSON返回数据
     *
     * @param data
     * @return
     */
    public static JSONObject genSuccessJsonResult(Map<String, Object> data) {
        JSONObject result = JSONObject.fromObject(data);
        result.put("result", "OK");
        result.put("code", "0");
        return result;
    }

}
