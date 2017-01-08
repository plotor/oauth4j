package org.zhenchao.passport.oauth.utils;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.zhenchao.passport.oauth.exceptions.EncryptException;

import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密算法工具类
 *
 * @author zhenchao.wang 2017-01-02 13:49
 * @version 1.0.0
 */
public class EncryptUtils {

    private static final String AES = "AES";

    /**
     * 加解密算法/模式/填充方式
     * ECB模式只用密钥即可对数据进行加密解密，CBC模式需要添加一个参数iv
     */
    private static final String AES_CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";

    /** AES 密钥 */
    private static byte[] aesKey;

    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            aesKey = keyGenerator.generateKey().getEncoded();
        } catch (NoSuchAlgorithmException e) {
            // never happen
        }
    }

    /**
     * AES 加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] aesEncrypt(byte[] data) throws Exception {
        Key key = new SecretKeySpec(aesKey, AES);
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        //设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key, generateIV());
        return cipher.doFinal(data);
    }

    /**
     * AES 加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] aesEncrypt(String data) throws Exception {
        Key key = new SecretKeySpec(aesKey, AES);
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        //设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key, generateIV());
        return cipher.doFinal(data.getBytes("UTF-8"));
    }

    /**
     * AES 解密
     *
     * @param encryptedData
     * @return
     * @throws Exception
     */
    public static byte[] aesDecrypt(byte[] encryptedData) throws Exception {
        Key key = new SecretKeySpec(aesKey, AES);
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        //设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, key, generateIV());
        return cipher.doFinal(encryptedData);
    }

    /**
     * AES 解密
     *
     * @param encryptedData
     * @return
     * @throws Exception
     */
    public static byte[] aesDecrypt(String encryptedData) throws Exception {
        Key key = new SecretKeySpec(aesKey, AES);
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        //设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, key, generateIV());
        return cipher.doFinal(encryptedData.getBytes("UTF-8"));
    }

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

    /**
     * 生成IV
     * IV为一个16字节的数组，这里数据全为0
     *
     * @return
     * @throws Exception
     */
    private static AlgorithmParameters generateIV() throws Exception {
        byte[] iv = new byte[16];
        Arrays.fill(iv, (byte) 0x00);
        AlgorithmParameters params = AlgorithmParameters.getInstance(AES);
        params.init(new IvParameterSpec(iv));
        return params;
    }

}
