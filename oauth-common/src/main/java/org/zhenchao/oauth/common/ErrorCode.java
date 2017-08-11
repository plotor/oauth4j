package org.zhenchao.oauth.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 错误码
 *
 * @author zhenchao.wang 2016-09-01 11:17
 * @version 1.0.0
 */
public enum ErrorCode {

    UNKNOWN_ERROR(-1, "unknown error"),
    NO_ERROR(0, "no error"),

    /**
     * code约定：
     * 1~3位为错误类别，范围：100~999
     * 4~6位为同一错误类别下的错误细分，范围：000~999
     */

    /**
     * 编号：100
     * 描述：系统错误
     */

    SYSTEM_ERROR(100001, "system error"),
    INNER_ERROR(100002, "inner error"),
    UNSUPPORTED_REQUEST(100003, "unsupported request"),

    /**
     * 编号：101
     * 描述：参数错误
     */
    PARAMETER_ERROR(101000, "parameter error"),
    PARAMETER_NULL_OR_EMPTY(101001, "parameter is null or empty"),
    PARAMETER_SID_ILLEGAL(101002, "illegal sid"),
    PARAMETER_RECOVERABLE_WHITE_ILLEGAL(101003, "no recoverable authority"),
    PARAMETER_CALLBACK_ILLEGAL(101004, "callback url illegal"),
    PARAMETER_NONCE_ILLEGAL(101005, "illegal nonce"),
    PARAMETER_SIGNATURE_ILLEGAL(101006, "illegal signature"),
    NECESSARY_PARAMETER_MISSING(101007, "necessary params missing"),
    PARAMETER_FORMAT_ERROR(101008, "params format error"),
    OPEN_ID_ERROR(101009, "illegal openId"),
    DATA_VERIFY_INCONSISTENT(101010, "data inconsistent"),
    URI_SYNTAX_ERROR(101011, "illegal uri"),
    PARAMETER_VALIDATE_EXCEPTION(101012, "validate params error"),
    UNSUPPORTED_IMAGE_TYPE(101013, "unsupported image type"),
    IMAGE_OVERSIZE(101014, "image oversize"),
    IMAGE_BIT_OVERSIZE(101015, "image bit oversize"),
    CLIENT_SIGN_ILLEGAL(101016, "illegal client sign"),
    FID_NONCE_ILLEGAL(101017, "illegal fid nonce or sign"),

    /**
     * 编号：102
     * 描述：数据库错误
     */
    DATABASE_ERROR(102000, "database error"),
    TRANSACTIONAL_CONSISTENCY_ERROR(102001, "data inconsistent"),
    DATABASE_INSERT_ERROR(102100, "database insert error"),
    DATABASE_DELETE_ERROR(102200, "database delete error"),
    DATABASE_UPDATE_ERROR(102300, "database update error"),
    DATABASE_SELECT_ERROR(102400, "database select error"),
    /*缓存相关错误*/
    CACHE_ERROR(102500, "cache error"),
    LOCAL_CACHE_ERROR(102600, "local cache error"),
    LOCAL_CACHE_PUT_ERROR(102640, "local cache put error"),
    LOCAL_CACHE_GET_ERROR(102680, "local cache get error"),
    LOCAL_CACHE_UPDATE_ERROR(102720, "local cache update error"),
    LOCAL_CACHE_DELETE_ERROR(102760, "local cache delete error"),
    GLOBAL_CACHE_ERROR(102800, "global cache error"),
    GLOBAL_CACHE_PUT_ERROR(102840, "global cache put error"),
    GLOBAL_CACHE_GET_ERROR(102880, "global cache get error"),
    GLOBAL_CACHE_UPDATE_ERROR(102920, "global cache update error"),
    GLOBAL_CACHE_DELETE_ERROR(102960, "global cache delete error"),

    /**
     * 编号：103
     * 描述：服务错误
     */
    SERVICE_ERROR(103000, "service error"),
    LOCAL_SERVICE_ERROR(103100, "local service error"),
    REMOTE_SERVICE_ERROR(103200, "remote service error"),
    REMOTE_SERVICE_TIMEOUT(103201, "remote service timeout"),
    REMOTE_SERVICE_BUSY(103202, "remote service busy"),
    FID_VALIDATE_SERVICE_ERROR(103203, "fid validate remote service error"),
    WECHAT_SERVICE_ERROR(103204, "wechat service error or request params illegal"),
    OPEN_PLATFORM_SERVICE_ERROR(103205, "open platform service error"),
    OAUTH_MT_SERVICE_ERROR(103206, "oauth mt service error"),

    /**
     * 编号：104
     * 描述：编码错误
     */
    CODEC_ERROR(104000, "codec error"),
    ENCRYPT_ERROR(104001, "encrypt error"),
    DECRYPT_ERROR(104002, "decrypt error"),
    MD5_CONFLICT(104003, "md5 conflict"),
    AES_ENCRYPT_ERROR(104004, "aes encrypt error"),
    AES_DECRYPT_ERROR(104005, "aes decrypt error"),
    INVALID_KEY(104006, "invalid key"),
    INVALID_PUBLIC_KEY(104007, "invalid public key"),
    INVALID_PRIVATE_KEY(104008, "invalid private key"),
    RSA_ENCRYPT_ERROR(104009, "rsa encrypt error"),
    RSA_DECRYPT_ERROR(104010, "rsa decrypt error"),
    INVALID_PADDING(104011, "invalid padding"),
    ILLEGAL_BLOCK_SIZE(104012, "illegal block size"),

    /**
     * 编号：105
     * 描述：业务相关
     */
    UNKNOWN_VISITOR_TYPE(105000, "illegal visitor type"),
    NOT_DEFAULT(105001, "need default visitor type"),
    NOT_FID(105002, "need fid visitor type"),
    NOT_RECOVERABLE(105003, "need recoverable visitor type"),
    VISITOR_ID_ILLEGAL(105004, "illegal visitorId"),
    NOTIFICATION_URL_GENERATE_ERROR(105005, "generate notification url error"),
    LOGIN_SERVICE_ERROR(105006, "login service error"),
    CALLBACK_URL_GENERATE_ERROR(105007, "generate callback url error"),
    INVALID_USER(105008, "illegal user"),
    INVALID_FID(105009, "illegal fid"),
    ILLEGAL_REQUEST(105010, "illegal request"),
    FORWARD_REVOKE_VISITOR_UNSUPPORTED(105011, "forward revoke visitor unsupported"),
    REVERSE_REVOKE_VISITOR_UNSUPPORTED(105012, "reverse revoke visitor unsupported"),
    NO_BIND_RELATION(105013, "no bind relation found"),
    VISITOR_ID_VALIDATE_ERROR(105014, "validate visitor id error"),
    EXT_ID_VALIDATE_ERROR(105015, "validate ext id error"),
    RESOLVE_WECHAT_USER_INFO_ERROR(105016, "decrypt wechat user info error"),
    DATA_VALIDATE_INCONSISTENT(105017, "data validate inconsistent"),
    NOT_WECHAT(105018, "need wechat visitor type"),
    EXT_ID_GENERATE_ERROR(105019, "generate ext id error"),
    CREATE_OR_RECOVER_VISITOR_ERROR(105020, "create or recover visitor info error"),
    ONLY_RECOVER_SCHEMA_UNSUPPORTED(105021, "only recover schema unsupported"),
    VISITOR_NOT_EXIST(105022, "visitor not exist"),
    INVALID_SID(105023, "invalid sid"),
    GENERATE_OPEN_ID_ERROR(105024, "generate open id error"),
    INVALID_AUTHORIZATION(105025, "invalid authorization"),
    RESPONSE_RESULT_BUILD_ERROR(105026, "build response error"),
    AUTHORIZATION_CODE_GENERATE_ERROR(105027, "generate authorization code error"),
    BUILD_AUTHORIZE_RELATION_ERROR(105028, "build authorize relation error"),
    NO_APP_ORDER_CONFIG(105029, "app order configuration missing"),
    FILE_UPLOAD_ERROR(105030, "file upload error"),
    GENERATE_UNION_ID_ERROR(105031, "generate union id error"),
    SNS_INVALID_TOKEN(105032, "illegal sns token or not latest"),
    LOGIN_VERIFICATION_FAILED(105033, "login verification error"),

    /*手机号验证项目错误码（预留：105100~105150）*/
    PERMISSION_DENIED(105100, "permission denied"),
    ACTIVATION_INFO_NOT_FOUND(105101, "activation info not found"),
    ACTIVATION_INFO_EXPIRED(105102, "activation info expired"),
    PHONE_NUMBER_INCONSISTENT(105103, "phone number inconsistent"),
    PHONE_NUMBER_CHANGED(105104, "phone number changed"),
    PHONE_NUMBER_INVALID(105105, "phone number invalid"),
    ACTIVATION_INFO_NOT_FOUND_OR_EXPIRED(105016, "activation info not found or expired"),
    CLIENT_ALREADY_EXIST(105017, "client already exist"),
    CLIENT_ALREADY_ON_SERVICE(105018, "client already on service"),
    CLIENT_NOT_ON_SERVICE(105019, "client not on service"),

    /**
     * 编号：106
     * 描述：TOKEN、密钥相关
     */
    TOKEN_ERROR(106000, "illegal token"),
    TOKEN_EXPIRED(106001, "token expiration"),
    TOKEN_REJECTED(106002, "reject token"),
    TOKEN_GENERATE_ERROR(106003, "generate token error"),
    TOKEN_ENCRYPT_ERROR(106004, "encrypt token error"),
    TOKEN_DECRYPT_ERROR(106005, "decrypt token error"),
    KEY_STORE_LOAD_ERROR(106006, "load key store error"),
    KEY_STORE_GET_ERROR(106007, "get key store error"),
    ACCESS_TOKEN_GENERATE_ERROR(106008, "generate access token error"),
    REFRESH_TOKEN_GENERATE_ERROR(106009, "generate refresh token error"),
    INVALID_TOKEN_SECRET(106010, "invalid token secret"),
    SIGN_GENERATE_ERROR(106011, "generate sign error"),
    SIGN_VALIDATE_ERROR(106012, "validate sign error"),
    TOKEN_ENCODE_ERROR(106013, "encode token error"),
    TOKEN_DECODE_ERROR(106014, "decode token error"),
    TOKEN_FORMAT_ERROR(106015, "invalid token format"),
    TOKEN_VERSION_UNEXPECTED(106016, "unexpected token version"),
    KEY_GENERATE_ERROR(106017, "generate key error"),
    SYMMETRIC_KEY_GENERATE_ERROR(106018, "generate symmetric key error"),
    AES_KEY_GENERATE_ERROR(106019, "generate aes key error"),
    ASYMMETRIC_KEY_GENERATE_ERROR(106020, "generate asymmetric key error"),
    RSA_KEY_GENERATE_ERROR(106021, "generate rsa key error"),
    APP_SECRET_NOT_EXIST(106022, "app secret not exist"),

    PASS_TOKEN_ERROR(106100, "illegal pass token"),
    PASS_TOKEN_EXPIRED(106101, "pass token expiration"),
    PASS_TOKEN_GENERATE_ERROR(106102, "generate pass token error"),
    PASS_TOKEN_ENCRYPT_ERROR(106103, "encrypt pass token error"),
    PASS_TOKEN_DECRYPT_ERROR(106104, "decrypt pass token error"),
    PASS_TOKEN_UPDATE_ERROR(106105, "update pass token error"),
    PASS_TOKEN_SECURITY_UPDATE_ERROR(106106, "update pass token security error"),
    PASS_TOKEN_VISITOR_ID_MISMATCH(106107, "visitor id unmatched with pass token"),

    AUTH_TOKEN_ERROR(106200, "illegal auth token"),
    AUTH_TOKEN_EXPIRED(106201, "auth token expiration"),
    AUTH_TOKEN_GENERATE_ERROR(106202, "generate auth token error"),
    AUTH_TOKEN_ENCRYPT_ERROR(106203, "encrypt auth token error"),
    AUTH_TOKEN_DECRYPT_ERROR(106204, "decrypt auth token error"),
    AUTH_TOKEN_UPDATE_ERROR(106205, "update auth token error"),
    AUTH_TOKEN_SECURITY_UPDATE_ERROR(106206, "update auth token security error"),

    SERVICE_TOKEN_ERROR(106300, "illegal service token"),
    SERVICE_TOKEN_EXPIRED(106301, "service token expiration"),
    SERVICE_TOKEN_GENERATE_ERROR(106302, "generate service token error"),
    SERVICE_TOKEN_ENCRYPT_ERROR(106303, "encrypt service token error"),
    SERVICE_TOKEN_DECRYPT_ERROR(106304, "decrypt service token error"),
    SERVICE_TOKEN_UPDATE_ERROR(106305, "update service token error"),
    SERVICE_TOKEN_SECURITY_UPDATE_ERROR(106306, "update service token security error"),

    SECURITY_HINT_ERROR(106400, "security hint error"),
    SECURITY_HINT_UPDATE_ERROR(106401, "update security hint error"),

    /**
     * 编号：107
     * 描述：JSON相关
     */
    JSON_FORMAT_ERROR(107000, "json format error"),
    JSON_CONVERT_ERROR(107001, "json data convert error"),

    /**
     * 编号：108
     * 描述：SNS相关
     */
    SNS_ERROR(108000, "sns service error"),
    SNS_INVALID_TYPE(108001, "invalid sns type"),
    SNS_GET_PROFILE_ERROR(108002, "get sns user profile error"),
    SNS_VERIFICATION_FAILED(108004, "sns user verification error"),
    SNS_ALREADY_BIND(108005, "sns already bind"),
    SNS_SERVICE_BUSY(108006, "sns service busy"),
    SNS_HANDLE_FAIL(108007, "sns handle error"),
    SNS_HANDLE_TIMEOUT(108008, "sns service timeout"),
    SNS_NO_BIND_TOKEN(108009, "no sns bind token"),
    SNS_HANDLE_IN_PROGRESS(108010, "sns handling"),
    SNS_INVALID_IP_OR_MAC(108011, "invalid ip or mac"),
    SNS_DISCOVERY_INVISIBLE(108012, "invisible sns"),
    SNS_BIND_CONFLICT(108013, "sns bind conflict"),

    /**
     * 编号：109
     * 描述：OAuth 相关
     */
    CLIENT_NOT_EXIST(109001, "unknown client"),
    INVALID_REQUEST(109002, "invalid request"),
    INVALID_CLIENT(109003, "invalid client"),
    INVALID_GRANT(109004, "invalid grant"),
    UNAUTHORIZED_CLIENT(109005, "unauthorized client"),
    UNSUPPORTED_GRANT_TYPE(109006, "unsupported grant type"),
    INVALID_SCOPE(109007, "invalid scope"),
    INVALID_ACCESS_TOKEN(109008, "invalid access token"),
    INVALID_REFRESH_TOKEN(109009, "invalid refresh token"),
    INVALID_REDIRECT_URI(109010, "invalid redirect uri"),
    UNSUPPORTED_RESPONSE_TYPE(109011, "unsupported response type"),
    ACCESS_DENIED(109012, "access denied"),
    INVALID_AUTHORIZATION_CODE(109013, "invalid authorization code"),
    UNSUPPORTED_TOKEN_TYPE(109014, "unsupported token type"),

    /*OAuth相关业务错误*/
    UNKNOWN_OPEN_PLATFORM(109050, "unknown open platform"),
    DATA_SIGN_MISSING(109051, "data sign missing"),
    SCOPE_DATA_FORMAT_ERROR(109052, "scope data format error");

    /** 错误码 */
    private int code;

    /** 描述信息 */
    private String description;

    private static final Map<Integer, ErrorCode> ERROR_CODE_MAP = new HashMap<Integer, ErrorCode>();

    static {
        ErrorCode[] codes = ErrorCode.values();
        for (final ErrorCode errorCode : codes) {
            ERROR_CODE_MAP.put(errorCode.getCode(), errorCode);
        }
    }

    ErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 获取错误的类型
     *
     * @return
     */
    public Integer getErrorType() {
        return this.code / 1000;
    }

    /**
     * 解析code对应的错误码
     *
     * @param code
     * @return
     */
    public static ErrorCode resolveErrorCode(int code) {
        return ERROR_CODE_MAP.getOrDefault(code, ErrorCode.UNKNOWN_ERROR);
    }

    public int getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

}
