package org.zhenchao.passport.oauth.token.generator;

import org.zhenchao.passport.oauth.domain.AuthorizationCode;
import org.zhenchao.passport.oauth.domain.AuthorizationTokenParams;
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

    public MacAccessTokenGenerator(AuthorizationTokenParams params) {
        super(params);
    }

    @Override
    public Optional<AbstractAccessToken> create() {
        if (null == params) {
            return Optional.empty();
        }

        AuthorizationCode code = params.getAuthorizationCode();
        if (null == code) {
            log.error("Generate mac access token error, lack of necessary parameter : authorization code!");
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
                .setUserId(code.getUserId()).setClientId(params.getClientId()).setScope(code.getScopes())
                .setIssueTime(currentTime).setExpirationTime(currentTime + code.getAppInfo().getTokenValidity() * 1000L)  // 时间单位保持一致
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
