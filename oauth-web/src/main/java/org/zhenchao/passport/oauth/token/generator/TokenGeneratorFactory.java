package org.zhenchao.passport.oauth.token.generator;

import org.zhenchao.oauth.protocol.enums.ResponseType;
import org.zhenchao.oauth.token.enums.TokenType;
import org.zhenchao.passport.oauth.pojo.TokenRequestParams;

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
        Optional<ResponseType> opt = ResponseType.resolve(params.getResponseType());
        if (opt.isPresent()) {
            if (TokenType.MAC.getValue().equalsIgnoreCase(params.getTokenType())) {
                return Optional.of(new MacAccessTokenGenerator(params));
            } else if (TokenType.BEARER.getValue().equalsIgnoreCase(params.getTokenType())) {
                return Optional.of(new BearerAccessTokenGenerator(params));
            }
        } else {
            // TODO other grant type
        }
        return Optional.empty();
    }

}
