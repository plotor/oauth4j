package org.zhenchao.oauth.pojo;

import org.zhenchao.oauth.common.ErrorCode;
import org.zhenchao.oauth.common.exception.VerificationException;
import org.zhenchao.oauth.token.pojo.TokenElement;

/**
 * 请求参数接口
 *
 * @author zhenchao.wang 2017-01-20 17:34
 * @version 1.0.0
 */
public interface RequestParams {

    ErrorCode validate() throws VerificationException;

    TokenElement toTokenElement();

}
