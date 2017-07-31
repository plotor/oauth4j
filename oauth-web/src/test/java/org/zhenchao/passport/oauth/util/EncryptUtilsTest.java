package org.zhenchao.passport.oauth.util;

import org.junit.Assert;
import org.junit.Test;
import org.zhenchao.oauth.common.GlobalConstant;

import java.util.Base64;

/**
 * @author zhenchao.wang 2017-01-05 17:54
 * @version 1.0.0
 */
public class EncryptUtilsTest {

    @Test
    public void pbkdf2() throws Exception {
        System.out.println(CryptUtils.pbkdf2("123456", GlobalConstant.SALT));
    }

    @Test
    public void aesTest() throws Exception {
        String username = "zhenchao";
        String decryptDate = new String(CryptUtils.aesDecrypt(CryptUtils.aesEncrypt(username)));
        Assert.assertEquals(username, decryptDate);
    }

    @Test
    public void generateAesKeyTest() throws Exception {
        byte[] key = CryptUtils.genAesKey();
        System.out.println(new String(Base64.getEncoder().encode(key)));
    }

}