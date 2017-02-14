package org.zhenchao.passport.oauth.token.generator;

import org.zhenchao.passport.oauth.domain.TokenRequestParams;
import org.zhenchao.passport.oauth.model.OAuthAppInfo;
import org.zhenchao.passport.oauth.model.UserAppAuthorization;
import org.zhenchao.passport.oauth.token.AbstractAccessToken;
import org.zhenchao.passport.oauth.token.MacAccessToken;
import org.zhenchao.passport.oauth.token.RefreshToken;

import java.util.Optional;

/**
 * mac type access token generator
 *
 * @author zhenchao.wang 2017-02-11 13:52
 * @version 1.0.0
 */
public class MacAccessTokenGenerator extends AbstractAccessTokenGenerator {

    public MacAccessTokenGenerator(TokenRequestParams params) {
        super(params);
    }

    @Override
    public Optional<AbstractAccessToken> create() {
        if (null == params) {
            return Optional.empty();
        }

        OAuthAppInfo appInfo = params.getAppInfo();
        if (null == appInfo) {
            log.error("Generate mac access token error, lack of necessary parameter : oauth app info!");
            return Optional.empty();
        }

        UserAppAuthorization uaa = params.getUserAppAuthorization();
        if (null == uaa) {
            log.error("Generate mac access token error, lack of necessary parameter : user app authorization!");
            return Optional.empty();
        }

        MacAccessToken accessToken = new MacAccessToken();
        long currentTime = System.currentTimeMillis();
        accessToken.setVersion(AbstractAccessToken.TokenVersion.V_1_0_0)
                .setUserId(params.getUserId()).setClientId(params.getClientId()).setScope(params.getScope())
                .setIssueTime(currentTime).setExpirationTime(currentTime + appInfo.getTokenValidity() * 1000L)  // 时间单位保持一致
                .setType(MacAccessToken.TYPE).setKey(uaa.getTokenKey());
        if (params.isIrt()) {
            // 生成刷新令牌
            RefreshToken refreshToken = new RefreshToken();
            // TODO 生成刷新令牌
            accessToken.setRefreshToken("");
        }

        return Optional.of(accessToken);
    }
}
