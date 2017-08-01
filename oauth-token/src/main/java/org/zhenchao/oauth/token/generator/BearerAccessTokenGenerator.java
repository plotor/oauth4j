package org.zhenchao.oauth.token.generator;

import org.zhenchao.oauth.token.AbstractAccessToken;
import org.zhenchao.oauth.token.pojo.TokenElement;

import java.util.Optional;

/**
 * bearer type access token generator
 *
 * @author zhenchao.wang 2017-02-11 13:52
 * @version 1.0.0
 */
public class BearerAccessTokenGenerator extends AbstractAccessTokenGenerator {

    public BearerAccessTokenGenerator(TokenElement element) {
        super(element);
    }

    @Override
    public Optional<AbstractAccessToken> create() {
        return null;
    }
}
