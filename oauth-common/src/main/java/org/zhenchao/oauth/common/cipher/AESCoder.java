package org.zhenchao.oauth.common.cipher;

import org.apache.commons.codec.binary.Base64;
import org.zhenchao.oauth.common.ErrorCode;
import org.zhenchao.oauth.common.exception.CodecException;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES对称加密(128位密钥)
 *
 * @author zhenchao.wang 2017-06-07 15:51
 * @version 1.0.0
 */
public class AESCoder {

    public static final String AES_ALGORITHM = "AES";

    /** 加密/解密算法/工作模式/填充方式 */
    public static final String PADDING_MODE = "AES/ECB/PKCS5Padding";

    public AESCoder() {
    }

    /**
     * 生成随机密钥
     *
     * @return byte[] 二进制密钥
     */
    public byte[] initKey() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(AES_ALGORITHM);
            kg.init(128);
            SecretKey secretKey = kg.generateKey();
            return secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            // never happen
        }
        return null;
    }

    /**
     * 解密
     *
     * @param encryptData 待解密数据
     * @param key 密钥
     * @return byte[] 解密数据
     * @throws CodecException
     */
    public byte[] decrypt(byte[] encryptData, byte[] key) throws CodecException {
        try {
            Key k = new SecretKeySpec(key, AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(PADDING_MODE);
            cipher.init(Cipher.DECRYPT_MODE, k);
            return cipher.doFinal(encryptData);
        } catch (NoSuchAlgorithmException e) {
            throw new CodecException("no such method", e, ErrorCode.AES_DECRYPT_ERROR);
        } catch (NoSuchPaddingException e) {
            throw new CodecException("no such padding", e, ErrorCode.INVALID_PADDING);
        } catch (BadPaddingException e) {
            throw new CodecException("bad padding", e, ErrorCode.INVALID_PADDING);
        } catch (IllegalBlockSizeException e) {
            throw new CodecException("illegal block size", e, ErrorCode.ILLEGAL_BLOCK_SIZE);
        } catch (InvalidKeyException e) {
            throw new CodecException("invalid sym key", e, ErrorCode.INVALID_KEY);
        }
    }

    /**
     * 解密
     *
     * @param encryptData 待解密数据
     * @param key base64后的密钥
     * @return byte[] 解密数据
     * @throws CodecException
     */
    public byte[] decrypt(byte[] encryptData, String key) throws CodecException {
        return this.decrypt(encryptData, Base64.decodeBase64(key));
    }

    /**
     * 加密
     *
     * @param data 待加密数据
     * @param key 密钥
     * @return byte[] 加密数据
     * @throws CodecException
     */
    public byte[] encrypt(byte[] data, byte[] key) throws CodecException {
        try {
            Key k = new SecretKeySpec(key, AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(PADDING_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, k);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            throw new CodecException("no such method", e, ErrorCode.AES_ENCRYPT_ERROR);
        } catch (NoSuchPaddingException e) {
            throw new CodecException("no such padding", e, ErrorCode.INVALID_PADDING);
        } catch (BadPaddingException e) {
            throw new CodecException("bad padding", e, ErrorCode.INVALID_PADDING);
        } catch (IllegalBlockSizeException e) {
            throw new CodecException("illegal block size", e, ErrorCode.ILLEGAL_BLOCK_SIZE);
        } catch (InvalidKeyException e) {
            throw new CodecException("invalid sym key", e, ErrorCode.INVALID_KEY);
        }
    }

    /**
     * 加密
     *
     * @param data 待加密数据
     * @param key base64后的密钥
     * @return byte[] 加密数据
     * @throws CodecException
     */
    public byte[] encrypt(byte[] data, String key) throws CodecException {
        return this.encrypt(data, Base64.decodeBase64(key));
    }

}
