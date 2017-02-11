package org.zhenchao.passport.oauth.domain;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.SALT;
import org.zhenchao.passport.oauth.model.OAuthAppInfo;
import org.zhenchao.passport.oauth.utils.CommonUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * 授权码
 *
 * @author zhenchao.wang 2017-01-22 14:26
 * @version 1.0.0
 */
public class AuthorizationCode implements Serializable {

    private static final long serialVersionUID = 2991560593633764910L;

    private OAuthAppInfo appInfo;

    private long userId;

    private String scopes;

    /** 请求参数中的回调地址 */
    private String redirectUri;

    public AuthorizationCode() {
    }

    public AuthorizationCode(OAuthAppInfo appInfo, long userId, String scopes) {
        this.appInfo = appInfo;
        this.userId = userId;
        this.scopes = scopes;
    }

    /**
     * 将对象转换成字符串授权码
     * 出错则返回空值
     * FIXME code生成应该加入时间戳
     *
     * @return
     */
    public String toStringCode() {
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            dos.writeLong(this.appInfo.getAppId());
            dos.writeLong(this.userId);
            dos.writeUTF(this.scopes);
            dos.writeUTF(CommonUtils.genScopeSign(this.scopes));
            dos.writeUTF(StringUtils.trimToEmpty(this.redirectUri));
            dos.writeUTF(SALT);
            dos.flush();
            return DigestUtils.md5Hex(baos.toByteArray()).toUpperCase();
        } catch (IOException e) {
            // ignore
        } finally {
            if (null != baos) {
                try {
                    baos.close();
                } catch (IOException e) {
                    // ignore
                }
            }
            if (null != dos) {
                try {
                    dos.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
        return StringUtils.EMPTY;
    }

    public OAuthAppInfo getAppInfo() {
        return appInfo;
    }

    public AuthorizationCode setAppInfo(OAuthAppInfo appInfo) {
        this.appInfo = appInfo;
        return this;
    }

    public long getUserId() {
        return userId;
    }

    public AuthorizationCode setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public String getScopes() {
        return scopes;
    }

    public AuthorizationCode setScopes(String scopes) {
        this.scopes = scopes;
        return this;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public AuthorizationCode setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }
}
