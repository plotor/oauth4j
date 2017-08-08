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
 * idea encipher
 *
 * @author zhenchao.wang 2017-02-14 10:22
 * @version 1.0.0
 */
public class IDEAEncipher extends SymmetricalEncipher {

    private static final String IDEA = "IDEA";

    private static final String CIPHER = "IDEA/ECB/PKCS7Padding";

    private static final String BOUNCY_CASTLE = "BC";

    private static final int KEY_LENGTH = 128;

    @Override
    public byte[] generateKey() {
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyGenerator kg = KeyGenerator.getInstance(IDEA);
            kg.init(KEY_LENGTH, new SecureRandom());
            return kg.generateKey().getEncoded();
        } catch (NoSuchAlgorithmException e) {
            // never happen
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public byte[] encrypt(byte[] key, byte[] data) throws CodecException {
        Security.addProvider(new BouncyCastleProvider());
        try {
            Cipher cipher = Cipher.getInstance(CIPHER, BOUNCY_CASTLE);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, IDEA));
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException |
                BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            throw new CodecException(e, ErrorCode.IDEA_ENCRYPT_ERROR);
        }
    }

    @Override
    public byte[] decrypt(byte[] key, byte[] data) throws CodecException {
        Security.addProvider(new BouncyCastleProvider());
        try {
            Cipher cipher = Cipher.getInstance(CIPHER, BOUNCY_CASTLE);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, IDEA));
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException |
                BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            throw new CodecException(e, ErrorCode.IDEA_DECRYPT_ERROR);
        }
    }

}
