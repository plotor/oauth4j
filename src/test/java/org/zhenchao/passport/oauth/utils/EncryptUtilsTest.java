package org.zhenchao.passport.oauth.utils;

import org.junit.Assert;
import org.junit.Test;
import org.zhenchao.passport.oauth.commons.GlobalConstant;

import java.util.Base64;

/**
 * @author zhenchao.wang 2017-01-05 17:54
 * @version 1.0.0
 */
public class EncryptUtilsTest {

    @Test
    public void pbkdf2() throws Exception {
        System.out.println(EncryptAndDecryptUtils.pbkdf2("123456", GlobalConstant.SALT));
    }

    @Test
    public void aesTest() throws Exception {
        String username = "zhenchao";
        String decryptDate = new String(EncryptAndDecryptUtils.aesDecrypt(EncryptAndDecryptUtils.aesEncrypt(username)));
        Assert.assertEquals(username, decryptDate);
    }

    @Test
    public void generateAesKeyTest() throws Exception {
        byte[] key = EncryptAndDecryptUtils.genAesKey();
        System.out.println(new String(Base64.getEncoder().encode(key)));
    }

}