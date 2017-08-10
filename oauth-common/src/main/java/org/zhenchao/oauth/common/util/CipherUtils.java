package org.zhenchao.oauth.common.util;

import org.apache.commons.codec.binary.Base64;
import org.zhenchao.oauth.common.ErrorCode;
import org.zhenchao.oauth.common.exception.CodecException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * 加密算法工具类
 *
 * @author zhenchao.wang 2017-01-02 13:49
 * @version 1.0.0
 */
public abstract class CipherUtils {

    /**
     * PBKDF2加密
     *
     * @param text 加密字符串
     * @param salt 盐
     * @param iterationCount 迭代次数
     * @param length 密钥长度
     * @return
     * @throws CodecException
     */
    public static String pbkdf2(String text, String salt, int iterationCount, int length) throws CodecException {
        KeySpec keySpec = new PBEKeySpec(text.toCharArray(), salt.getBytes(), iterationCount, length);
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return Base64.encodeBase64String(secretKeyFactory.generateSecret(keySpec).getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new CodecException("PDKDF2 Error", e, ErrorCode.ENCRYPT_ERROR);
        }
    }

    /**
     * PBKDF2加密
     * 默认迭代9次，密钥长度32个字符
     *
     * @param text 加密字符串
     * @param salt 盐
     * @return
     * @throws CodecException
     */
    public static String pbkdf2(String text, String salt) throws CodecException {
        return pbkdf2(text, salt, 8, 128);
    }

}
