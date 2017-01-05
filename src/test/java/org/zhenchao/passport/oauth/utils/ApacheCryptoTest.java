package org.zhenchao.passport.oauth.utils;

import org.apache.commons.crypto.stream.CryptoOutputStream;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author zhenchao.wang 2017-01-05 22:52
 * @version 1.0.0
 */
public class ApacheCryptoTest {

    private static byte[] getUTF8Bytes(String input) {
        return input.getBytes(StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {
        final SecretKeySpec key = new SecretKeySpec(getUTF8Bytes("123456"), "AES");
        final IvParameterSpec iv = new IvParameterSpec(getUTF8Bytes("000000"));
        final String transform = "AES/CBC/PKCS5Padding";

        String input = "hello world!";
        //Encryption with CryptoOutputStream.

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (CryptoOutputStream cos = new CryptoOutputStream(transform, new Properties(), outputStream, key, iv)) {
            cos.write(getUTF8Bytes(input));
            cos.flush();
        }

        // The encrypted data:
        System.out.println("Encrypted: " + Arrays.toString(outputStream.toByteArray()));
    }

}
