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

    public static final String RESULT = "result";

    public static final String CODE = "code";

    public static final String DESC = "description";

    public static final String CALLBACK = "callback";

    public static final String OK = "OK";

    public static final String ERROR = "ERROR";

    public static final int RIGHT_CODE = 0;

    /**
     * 生成错误的JSON返回数据
     *
     * @param errorCode
     * @return
     */
    public static JSONObject genFailedJsonResult(ErrorCode errorCode) {
        JSONObject result = new JSONObject();
        result.put(RESULT, ERROR);
        result.put(CODE, errorCode.getCode());
        result.put(DESC, errorCode.getDesc());
        return result;
    }

    /**
     * 生成错误的JSON返回数据，带callback
     *
     * @param errorCode
     * @param callback
     * @return
     */
    public static JSONObject genFailedJsonResult(ErrorCode errorCode, String callback) {
        JSONObject result = new JSONObject();
        result.put(RESULT, ERROR);
        result.put(CODE, errorCode.getCode());
        result.put(DESC, errorCode.getDesc());
        result.put(CALLBACK, callback);
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
        result.put(RESULT, OK);
        result.put(CODE, RIGHT_CODE);
        return result;
    }

}
