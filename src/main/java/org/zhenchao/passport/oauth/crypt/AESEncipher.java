package org.zhenchao.passport.oauth.crypt;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.zhenchao.passport.oauth.commons.ErrorCode;
import org.zhenchao.passport.oauth.exceptions.CryptException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES symmetrical encryption
 *
 * @author zhenchao.wang 2017-02-13 17:20
 * @version 1.0.0
 */
public class AESEncipher {

    public enum KeyLength {
        KL_128(128),

        KL_192(192),

        KL_256(256);

        private int length;

        KeyLength(int length) {
            this.length = length;
        }

        public int getLength() {
            return length;
        }
    }

    private static final String AES = "AES";

    private static final String CIPHER = "AES/ECB/PKCS7Padding";

    private static final String BOUNCY_CASTLE = "BC";

    /**
     * generate aes key
     *
     * @param length
     * @return
     */
    public static byte[] generateKey(KeyLength length) {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(AES);
            kg.init(length.getLength(), new SecureRandom());
            return kg.generateKey().getEncoded();
        } catch (NoSuchAlgorithmException e) {
            // never happen
        }
        return new byte[0];
    }

    /**
     * aes encrypt
     *
     * @param key
     * @param data
     * @return
     * @throws CryptException
     */
    public static byte[] encrypt(byte[] key, byte[] data) throws CryptException {
        try {
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(CIPHER, BOUNCY_CASTLE);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, AES));
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException |
                BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            throw new CryptException(e, ErrorCode.AES_ENCRYPT_ERROR);
        }
    }

    /**
     * aes decrypt
     *
     * @param key
     * @param data
     * @return
     * @throws CryptException
     */
    public static byte[] decrypt(byte[] key, byte[] data) throws CryptException {
        try {
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(CIPHER, BOUNCY_CASTLE);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, AES));
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException |
                InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new CryptException(e, ErrorCode.AES_DECRYPT_ERROR);
        }
    }
}
