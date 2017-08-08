package org.zhenchao.oauth.common.cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.zhenchao.oauth.common.ErrorCode;
import org.zhenchao.oauth.common.exception.CodecException;

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
public class AESEncipher extends SymmetricalEncipher {

    private static final String AES = "AES";

    private static final String CIPHER = "AES/ECB/PKCS7Padding";

    private static final String BOUNCY_CASTLE = "BC";

    private static final int KEY_LENGTH = 128;

    /**
     * generate aes key
     *
     * @return
     */
    @Override
    public byte[] generateKey() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(AES);
            kg.init(KEY_LENGTH, new SecureRandom());
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
     * @throws CodecException
     */
    @Override
    public byte[] encrypt(byte[] key, byte[] data) throws CodecException {
        try {
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(CIPHER, BOUNCY_CASTLE);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, AES));
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException |
                BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            throw new CodecException(e, ErrorCode.AES_ENCRYPT_ERROR);
        }
    }

    /**
     * aes decrypt
     *
     * @param key
     * @param data
     * @return
     * @throws CodecException
     */
    @Override
    public byte[] decrypt(byte[] key, byte[] data) throws CodecException {
        try {
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(CIPHER, BOUNCY_CASTLE);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, AES));
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException |
                InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new CodecException(e, ErrorCode.AES_DECRYPT_ERROR);
        }
    }
}
