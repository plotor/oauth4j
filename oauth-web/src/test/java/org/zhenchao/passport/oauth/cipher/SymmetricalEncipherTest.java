package org.zhenchao.passport.oauth.cipher;

import org.junit.Assert;
import org.junit.Test;
import org.zhenchao.oauth.common.cipher.AESEncipher;
import org.zhenchao.oauth.common.cipher.IDEAEncipher;
import org.zhenchao.oauth.common.cipher.SymmetricalEncipher;

import java.util.Base64;

/**
 * @author zhenchao.wang 2017-02-13 18:13
 * @version 1.0.0
 */
public class SymmetricalEncipherTest {

    private static final String DATA = "Hello World!";

    @Test
    public void aes() throws Exception {
        SymmetricalEncipher aes = new AESEncipher();
        byte[] key = aes.generateKey();
        System.out.println("key:" + Base64.getEncoder().encodeToString(key));
        byte[] encryptData = aes.encrypt(key, DATA.getBytes());
        System.out.println("encrypt:" + Base64.getEncoder().encodeToString(encryptData));
        byte[] decryptData = aes.decrypt(key, encryptData);
        Assert.assertEquals(DATA, new String(decryptData));
    }

    @Test
    public void idea() throws Exception {
        SymmetricalEncipher idea = new IDEAEncipher();
        byte[] key = idea.generateKey();
        System.out.println("key:" + Base64.getEncoder().encodeToString(key));
        byte[] encryptData = idea.encrypt(key, DATA.getBytes());
        System.out.println("encrypt:" + Base64.getEncoder().encodeToString(encryptData));
        byte[] decryptData = idea.decrypt(key, encryptData);
        Assert.assertEquals(DATA, new String(decryptData));
    }

}