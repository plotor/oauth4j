package org.zhenchao.oauth.common.cipher;

import org.apache.commons.codec.binary.Base64;
import org.zhenchao.oauth.common.ErrorCode;
import org.zhenchao.oauth.common.exception.CodecException;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * RSA非对称加密
 *
 * @author zhenchao.wang 2017-05-27 14:32
 * @version 1.0.0
 */
public class RSACoder {

    private static final String RSA_ALGORITHM = "RSA";

    private int keyLength = 1024;

    public RSACoder() {
    }

    public RSACoder(int keyLength) {
        assert keyLength % 64 == 0 && keyLength >= 512 && keyLength <= 65535 : "RSA key length must multiple of 64 and between 512 and 65535!";
        this.keyLength = keyLength;
    }

    /**
     * 生成随机密钥对
     *
     * @return
     */
    public KeyPair initKey() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            kpg.initialize(keyLength, new SecureRandom());
            return kpg.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            // never happen
        }
        return null;
    }

    /**
     * 利用私钥加密数据
     *
     * @param data
     * @param key
     * @return
     * @throws CodecException
     */
    public byte[] encryptByPrivateKey(byte[] data, byte[] key) throws CodecException {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
            KeyFactory factory = KeyFactory.getInstance(RSA_ALGORITHM);
            PrivateKey privateKey = factory.generatePrivate(keySpec);
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            throw new CodecException("no such method", e, ErrorCode.RSA_ENCRYPT_ERROR);
        } catch (InvalidKeySpecException e) {
            throw new CodecException("invalid private key", e, ErrorCode.INVALID_PRIVATE_KEY);
        } catch (InvalidKeyException e) {
            throw new CodecException("invalid private key", e, ErrorCode.INVALID_PRIVATE_KEY);
        } catch (NoSuchPaddingException e) {
            throw new CodecException("no such padding", e, ErrorCode.INVALID_PADDING);
        } catch (BadPaddingException e) {
            throw new CodecException("no such padding", e, ErrorCode.INVALID_PADDING);
        } catch (IllegalBlockSizeException e) {
            throw new CodecException("illegal block size", e, ErrorCode.ILLEGAL_BLOCK_SIZE);
        }
    }

    /**
     * 利用私钥加密数据
     *
     * @param data
     * @param key base64编码后的key
     * @return
     * @throws CodecException
     */
    public byte[] encryptByPrivateKey(byte[] data, String key) throws CodecException {
        return this.encryptByPrivateKey(data, Base64.decodeBase64(key));
    }

    /**
     * 利用私钥解密数据
     *
     * @param encryptData
     * @param key
     * @return
     * @throws CodecException
     */
    public byte[] decryptByPrivateKey(byte[] encryptData, byte[] key) throws CodecException {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
            KeyFactory factory = KeyFactory.getInstance(RSA_ALGORITHM);
            PrivateKey privateKey = factory.generatePrivate(keySpec);
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(encryptData);
        } catch (NoSuchAlgorithmException e) {
            throw new CodecException("no such method", e, ErrorCode.RSA_DECRYPT_ERROR);
        } catch (InvalidKeySpecException e) {
            throw new CodecException("invalid private key", e, ErrorCode.INVALID_PRIVATE_KEY);
        } catch (InvalidKeyException e) {
            throw new CodecException("invalid private key", e, ErrorCode.INVALID_PRIVATE_KEY);
        } catch (NoSuchPaddingException e) {
            throw new CodecException("no such padding", e, ErrorCode.INVALID_PADDING);
        } catch (BadPaddingException e) {
            throw new CodecException("no such padding", e, ErrorCode.INVALID_PADDING);
        } catch (IllegalBlockSizeException e) {
            throw new CodecException("illegal block size", e, ErrorCode.ILLEGAL_BLOCK_SIZE);
        }
    }

    /**
     * 利用私钥解密数据
     *
     * @param encryptData
     * @param key base64编码后的key
     * @return
     * @throws CodecException
     */
    public byte[] decryptByPrivateKey(byte[] encryptData, String key) throws CodecException {
        return this.decryptByPrivateKey(encryptData, Base64.decodeBase64(key));
    }

    /**
     * 利用公钥加密数据
     *
     * @param data
     * @param key
     * @return
     * @throws CodecException
     */
    public byte[] encryptByPublicKey(byte[] data, byte[] key) throws CodecException {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
            KeyFactory factory = KeyFactory.getInstance(RSA_ALGORITHM);
            PublicKey publicKey = factory.generatePublic(keySpec);
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            throw new CodecException("no such method", e, ErrorCode.RSA_ENCRYPT_ERROR);
        } catch (InvalidKeySpecException e) {
            throw new CodecException("invalid public key", e, ErrorCode.INVALID_PUBLIC_KEY);
        } catch (InvalidKeyException e) {
            throw new CodecException("invalid public key", e, ErrorCode.INVALID_PUBLIC_KEY);
        } catch (NoSuchPaddingException e) {
            throw new CodecException("no such padding", e, ErrorCode.INVALID_PADDING);
        } catch (BadPaddingException e) {
            throw new CodecException("no such padding", e, ErrorCode.INVALID_PADDING);
        } catch (IllegalBlockSizeException e) {
            throw new CodecException("illegal block size", e, ErrorCode.ILLEGAL_BLOCK_SIZE);
        }
    }

    /**
     * 利用公钥加密数据
     *
     * @param data
     * @param key base64编码后的key
     * @return
     * @throws CodecException
     */
    public byte[] encryptByPublicKey(byte[] data, String key) throws CodecException {
        return this.encryptByPublicKey(data, Base64.decodeBase64(key));
    }

    /**
     * 利用公钥解密数据
     *
     * @param encryptData
     * @param key
     * @return
     * @throws CodecException
     */
    public byte[] decryptByPublicKey(byte[] encryptData, byte[] key) throws CodecException {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
            KeyFactory factory = KeyFactory.getInstance(RSA_ALGORITHM);
            PublicKey publicKey = factory.generatePublic(keySpec);
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return cipher.doFinal(encryptData);
        } catch (NoSuchAlgorithmException e) {
            throw new CodecException("no such method", e, ErrorCode.RSA_DECRYPT_ERROR);
        } catch (InvalidKeySpecException e) {
            throw new CodecException("invalid public key", e, ErrorCode.INVALID_PUBLIC_KEY);
        } catch (InvalidKeyException e) {
            throw new CodecException("invalid public key", e, ErrorCode.INVALID_PUBLIC_KEY);
        } catch (NoSuchPaddingException e) {
            throw new CodecException("no such padding", e, ErrorCode.INVALID_PADDING);
        } catch (BadPaddingException e) {
            throw new CodecException("no such padding", e, ErrorCode.INVALID_PADDING);
        } catch (IllegalBlockSizeException e) {
            throw new CodecException("illegal block size", e, ErrorCode.ILLEGAL_BLOCK_SIZE);
        }
    }

    /**
     * 利用公钥解密数据
     *
     * @param encryptData
     * @param key base64编码后的key
     * @return
     * @throws CodecException
     */
    public byte[] decryptByPublicKey(byte[] encryptData, String key) throws CodecException {
        return this.decryptByPublicKey(encryptData, Base64.decodeBase64(key));
    }
}