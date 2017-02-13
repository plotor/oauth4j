package org.zhenchao.passport.oauth.crypt;

import org.junit.Assert;
import org.junit.Test;

import java.util.Base64;

/**
 * @author zhenchao.wang 2017-02-13 18:13
 * @version 1.0.0
 */
public class AESEncipherTest {

    @Test
    public void test() throws Exception {
        String data = "Hello World!";
        // 128位
        System.out.println("128>>");
        byte[] key = AESEncipher.generateKey(AESEncipher.KeyLength.KL_128);
        System.out.println("key:" + Base64.getEncoder().encodeToString(key));
        byte[] encryptData = AESEncipher.encrypt(key, data.getBytes());
        System.out.println("encrypt:" + Base64.getEncoder().encodeToString(encryptData));
        byte[] decryptData = AESEncipher.decrypt(key, encryptData);
        Assert.assertEquals(data, new String(decryptData));

        // 192位
        System.out.println("192>>");
        key = AESEncipher.generateKey(AESEncipher.KeyLength.KL_192);
        System.out.println("key:" + Base64.getEncoder().encodeToString(key));
        encryptData = AESEncipher.encrypt(key, data.getBytes());
        System.out.println("encrypt:" + Base64.getEncoder().encodeToString(encryptData));
        decryptData = AESEncipher.decrypt(key, encryptData);
        Assert.assertEquals(data, new String(decryptData));

        // 256位
        System.out.println("256>>");
        key = AESEncipher.generateKey(AESEncipher.KeyLength.KL_256);
        System.out.println("key:" + Base64.getEncoder().encodeToString(key));
        encryptData = AESEncipher.encrypt(key, data.getBytes());
        System.out.println("encrypt:" + Base64.getEncoder().encodeToString(encryptData));
        decryptData = AESEncipher.decrypt(key, encryptData);
        Assert.assertEquals(data, new String(decryptData));
    }

}