package org.zhenchao.oauth.token.enums;

/**
 * access (refresh) token version
 *
 * @author zhenchao.wang 2017-07-31 22:59
 * @version 1.0.0
 */
public enum TokenVersion {

    V_1_0_0("1.0.0");

    private String value;

    TokenVersion(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
