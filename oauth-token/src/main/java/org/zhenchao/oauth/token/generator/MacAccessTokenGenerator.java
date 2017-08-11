package org.zhenchao.oauth.token.generator;

import org.apache.commons.lang3.RandomStringUtils;
import org.zhenchao.oauth.token.AbstractAccessToken;
import org.zhenchao.oauth.token.MacAccessToken;
import org.zhenchao.oauth.token.constant.TokenConstant;
import org.zhenchao.oauth.token.enums.TokenVersion;
import org.zhenchao.oauth.token.pojo.TokenElement;

import java.util.Optional;

/**
 * mac type access token generator
 *
 * @author zhenchao.wang 2017-02-11 13:52
 * @version 1.0.0
 */
public class MacAccessTokenGenerator extends AccessTokenGenerator {

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
                .setUserId(element.getUserId())
                .setClientId(element.getAppInfo().getAppId())
                .setScope(element.getScope())
                .setIssueTime(currentTime)
                .setExpirationTime((currentTime + TokenConstant.DEFAULT_ACCESS_TOKEN_VALIDITY * 1000L) / 1000L) // 时间单位保持一致（过期时间以秒为单位）
                .setType(MacAccessToken.TYPE)
                .setKey(element.getTokenKey());
        if (null != element.getGrantType() && element.isIssueRefreshToken()) {
            // TODO 生成刷新令牌
            accessToken.setRefreshToken(RandomStringUtils.randomAlphanumeric(32));
        }
        return Optional.of(accessToken);
    }
}
