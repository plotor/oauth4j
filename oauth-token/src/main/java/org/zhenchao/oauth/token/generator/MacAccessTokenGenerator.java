package org.zhenchao.oauth.token.generator;

import org.zhenchao.oauth.token.AbstractAccessToken;
import org.zhenchao.oauth.token.MacAccessToken;
import org.zhenchao.oauth.token.enums.TokenVersion;
import org.zhenchao.oauth.token.pojo.TokenElement;

import java.util.Optional;

/**
 * mac type access token generator
 *
 * @author zhenchao.wang 2017-02-11 13:52
 * @version 1.0.0
 */
public class MacAccessTokenGenerator extends AbstractAccessTokenGenerator {

    public MacAccessTokenGenerator(TokenElement element) {
        super(element);
    }

    @Override
    public Optional<AbstractAccessToken> create() {
        if (null == element) {
            return Optional.empty();
        }

        MacAccessToken accessToken = new MacAccessToken();
        long currentTime = System.currentTimeMillis();
        accessToken.setVersion(TokenVersion.V_1_0_0)
                .setUserId(element.getUserId()).setClientId(element.getClientId()).setScope(element.getScope())
                .setIssueTime(currentTime).setExpirationTime((currentTime + element.getValidity() * 1000L) / 1000L)  // 时间单位保持一致（过期时间以秒为单位）
                .setType(MacAccessToken.TYPE);
        if (element.isIssueRefreshToken()) {
            // 生成刷新令牌
            // TODO 生成刷新令牌
            accessToken.setRefreshToken("refresh token");
        }

        return Optional.of(accessToken);
    }
}
