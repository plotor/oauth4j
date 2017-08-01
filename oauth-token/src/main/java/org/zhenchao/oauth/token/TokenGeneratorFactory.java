package org.zhenchao.oauth.token;

import org.zhenchao.oauth.token.enums.TokenType;
import org.zhenchao.oauth.token.generator.AbstractTokenGenerator;
import org.zhenchao.oauth.token.generator.BearerAccessTokenGenerator;
import org.zhenchao.oauth.token.generator.MacAccessTokenGenerator;
import org.zhenchao.oauth.token.pojo.TokenElement;

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
     * @param element
     * @return
     */
    public static Optional<AbstractTokenGenerator> getGenerator(TokenElement element) {
        if (TokenType.MAC.equals(element.getTokenType())) {
            return Optional.of(new MacAccessTokenGenerator(element));
        } else if (TokenType.BEARER.equals(element.getTokenType())) {
            return Optional.of(new BearerAccessTokenGenerator(element));
        }
        return Optional.empty();
    }

}
