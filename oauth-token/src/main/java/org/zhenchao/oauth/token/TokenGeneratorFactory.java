package org.zhenchao.oauth.token;

import org.zhenchao.oauth.token.enums.TokenType;
import org.zhenchao.oauth.token.generator.AbstractTokenGenerator;
import org.zhenchao.oauth.token.generator.BearerAccessTokenGenerator;
import org.zhenchao.oauth.token.generator.MacAccessTokenGenerator;
import org.zhenchao.oauth.token.pojo.TokenElement;

/**
 * static token generator factory
 *
 * @author zhenchao.wang 2017-02-11 15:27
 * @version 1.0.0
 */
public class TokenGeneratorFactory {
    
    public static AbstractTokenGenerator getGenerator(TokenElement element) {
        if (TokenType.BEARER.equals(element.getTokenType())) {
            return new BearerAccessTokenGenerator(element);
        }
        // 默认下发 mac 类型 token
        return new MacAccessTokenGenerator(element);
    }

}
