package org.zhenchao.passport.oauth.model;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import static org.zhenchao.passport.oauth.commons.GlobalConstant.SALT;
import org.zhenchao.passport.oauth.utils.CommonUtils;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 授权码
 *
 * @author zhenchao.wang 2017-01-22 14:26
 * @version 1.0.0
 */
public class AuthorizationCode {

    private OAuthAppInfo appInfo;

    private long userId;

    private String scopes;

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
     *
     * @return
     */
    public String toStringCode() {
        ByteOutputStream bos = null;
        DataOutputStream dos = null;
        try {
            bos = new ByteOutputStream();
            dos = new DataOutputStream(bos);
            dos.writeLong(appInfo.getAppId());
            dos.writeLong(this.userId);
            dos.writeUTF(this.scopes);
            dos.writeUTF(CommonUtils.genScopeSign(this.scopes));
            dos.writeUTF(SALT);
            dos.flush();
            return DigestUtils.md5Hex(bos.getBytes()).toUpperCase();
        } catch (IOException e) {
            // ignore
        } finally {
            if (null != bos) {
                bos.close();
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
}
