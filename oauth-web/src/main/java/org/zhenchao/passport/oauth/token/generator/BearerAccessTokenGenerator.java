package org.zhenchao.passport.oauth.token.generator;

import org.zhenchao.passport.oauth.pojo.TokenRequestParams;
import org.zhenchao.passport.oauth.token.AbstractAccessToken;

import java.util.Optional;

/**
 * bearer type access token generator
 *
 * @author zhenchao.wang 2017-02-11 13:52
 * @version 1.0.0
 */
public class BearerAccessTokenGenerator extends AbstractAccessTokenGenerator {

    public BearerAccessTokenGenerator(TokenRequestParams params) {
        super(params);
    }

    @Override
    public Optional<AbstractAccessToken> create() {
        return null;
    }
}
