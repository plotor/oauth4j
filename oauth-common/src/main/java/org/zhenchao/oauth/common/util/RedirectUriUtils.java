package org.zhenchao.oauth.common.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zhenchao.wang 2017-08-08 22:56
 * @version 1.0.0
 */
public abstract class RedirectUriUtils {

    private static final Logger log = LoggerFactory.getLogger(RedirectUriUtils.class);

    private static final String SEP = ";";

    private static UrlValidator urlValidator = UrlValidator.getInstance();

    public static String format(List<String> redirectUris) {
        if (CollectionUtils.isEmpty(redirectUris)) return StringUtils.EMPTY;
        Set<String> set = new HashSet<>();
        redirectUris.forEach(uri -> {
            if (urlValidator.isValid(uri)) {
                set.add(uri);
            }
        });
        return StringUtils.join(set, SEP);
    }

    public static boolean isValid(String input, String redirectUris) {
        if (StringUtils.isBlank(input) || StringUtils.isBlank(redirectUris)) return false;
        return isValid(input, Arrays.asList(redirectUris.trim().split("\\s*[;\\s]+\\s*")));
    }

    public static boolean isValid(String input, List<String> redirectUris) {
        if (StringUtils.isBlank(input) || !urlValidator.isValid(input) || CollectionUtils.isEmpty(redirectUris)) {
            return false;
        }
        try {
            URI inputUri = new URI(input);
            String inputUriPath = inputUri.getPath().endsWith("/") ? inputUri.getPath() : inputUri.getPath() + "/";
            for (final String redirectUri : redirectUris) {
                if (StringUtils.equalsIgnoreCase(input, redirectUri)) {
                    return true;
                }
                URI uri = new URI(redirectUri);
                if (StringUtils.equalsIgnoreCase(inputUri.getScheme(), uri.getScheme())
                        && inputUri.getHost().endsWith(uri.getHost())
                        && inputUri.getPort() == uri.getPort()) {
                    String uriPath = uri.getPath().endsWith("/") ? uri.getPath() : uri.getPath() + "/";
                    if (inputUriPath.startsWith(uriPath)) {
                        return true;
                    }
                }
            }
        } catch (URISyntaxException e) {
            log.error("Parse redirect uri error, input[{}], config[{}]", input, redirectUris, e);
            return false;
        }
        return false;
    }

}
