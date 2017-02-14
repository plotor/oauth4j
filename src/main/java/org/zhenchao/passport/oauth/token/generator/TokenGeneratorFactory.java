package org.zhenchao.passport.oauth.token.generator;

import org.zhenchao.passport.oauth.commons.GlobalConstant;
import org.zhenchao.passport.oauth.domain.TokenRequestParams;
import org.zhenchao.passport.oauth.token.AbstractAccessToken;

import java.util.Optional;

/**
 * static token generator factory
 *
 * @author zhenchao.wang 2017-02-11 15:27
 * @version 1.0.0
 */
public class TokenGeneratorFactory {

    /**
     * get generator object
     *
     * @param params
     * @return
     */
    public static Optional<AbstractTokenGenerator> getGenerator(TokenRequestParams params) {
        if (GlobalConstant.GRANT_TYPE_CODE.equalsIgnoreCase(params.getGrantType())) {
            // 请求下发accessToken
            if (AbstractAccessToken.TokenType.MAC.getValue().equalsIgnoreCase(params.getTokenType())) {
                return Optional.of(new MacAccessTokenGenerator(params));
            } else if (AbstractAccessToken.TokenType.BEARER.getValue().equalsIgnoreCase(params.getTokenType())) {
                return Optional.of(new BearerAccessTokenGenerator(params));
            }
        }
        return Optional.empty();
    }

}
