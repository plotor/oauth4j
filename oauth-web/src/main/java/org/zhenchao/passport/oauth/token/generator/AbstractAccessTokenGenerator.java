package org.zhenchao.passport.oauth.token.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhenchao.passport.oauth.pojo.TokenRequestParams;
import org.zhenchao.passport.oauth.token.AbstractAccessToken;

import java.util.Optional;

/**
 * abstract access token generator
 *
 * @author zhenchao.wang 2017-02-11 13:51
 * @version 1.0.0
 */
public abstract class AbstractAccessTokenGenerator implements AbstractTokenGenerator {

    protected static final Logger log = LoggerFactory.getLogger(AbstractAccessTokenGenerator.class);

    protected TokenRequestParams params;

    public AbstractAccessTokenGenerator(TokenRequestParams params) {
        this.params = params;
    }

    public abstract Optional<AbstractAccessToken> create();

}
