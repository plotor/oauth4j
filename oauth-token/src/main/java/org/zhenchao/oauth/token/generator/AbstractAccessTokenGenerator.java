package org.zhenchao.oauth.token.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhenchao.oauth.token.AbstractAccessToken;
import org.zhenchao.oauth.token.pojo.TokenElement;

import java.util.Optional;

/**
 * abstract access token generator
 *
 * @author zhenchao.wang 2017-02-11 13:51
 * @version 1.0.0
 */
public abstract class AbstractAccessTokenGenerator implements AbstractTokenGenerator {

    protected static final Logger log = LoggerFactory.getLogger(AbstractAccessTokenGenerator.class);

    protected TokenElement element;

    public AbstractAccessTokenGenerator(TokenElement element) {
        this.element = element;
    }

    public abstract Optional<AbstractAccessToken> create();

}
