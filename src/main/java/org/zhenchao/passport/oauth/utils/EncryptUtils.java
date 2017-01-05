package org.zhenchao.passport.oauth.utils;

import org.apache.commons.lang3.StringUtils;
import org.zhenchao.passport.oauth.exceptions.EncryptException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * 加密算法工具类
 *
 * @author zhenchao.wang 2017-01-02 13:49
 * @version 1.0.0
 */
public class EncryptUtils {

    /**
     * PBKDF2加密
     *
     * @param text 加密字符串
     * @param salt 盐
     * @param iterationCount 迭代次数
     * @param length 密钥长度
     * @return
     * @throws EncryptException
     */
    public static String pbkdf2(String text, String salt, int iterationCount, int length) throws InvalidKeySpecException {
        KeySpec keySpec = new PBEKeySpec(text.toCharArray(), salt.getBytes(), iterationCount, length);
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return Base64.getEncoder().encodeToString(secretKeyFactory.generateSecret(keySpec).getEncoded());
        } catch (NoSuchAlgorithmException e) {
            // never happen
        }
        return StringUtils.EMPTY;
    }

    /**
     * PBKDF2加密
     * 默认迭代9次，密钥长度32个字符
     *
     * @param text 加密字符串
     * @param salt 盐
     * @return
     * @throws EncryptException
     */
    public static String pbkdf2(String text, String salt) throws InvalidKeySpecException {
        return pbkdf2(text, salt, 8, 128);
    }

}
