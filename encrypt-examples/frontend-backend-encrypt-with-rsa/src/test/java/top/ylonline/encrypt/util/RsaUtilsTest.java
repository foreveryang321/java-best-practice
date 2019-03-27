package top.ylonline.encrypt.util;

import org.junit.Assert;
import org.junit.Test;
import top.ylonline.encrypt.common.Const;

/**
 * @author YL
 */
public class RsaUtilsTest {

    @Test
    public void test() {
        // 初始化 rsa，构建密钥，获取到模、私钥指数、公钥指数
        // RsaUtils.generateKeyPair();

        String str = Const.STR;
        
        // 模
        String modulus = Const.MODULUS;
        // 私钥指数
        String privateExponent = Const.PRIVATE_EXPONENT;
        // 公钥指数
        String publicExponent = Const.PUBLIC_EXPONENT;

        // 私钥加密，公钥解密
        String encrypt = RsaUtils.encryptPrivate(modulus, privateExponent, str);
        System.out.println("encrypt: " + encrypt);
        String decrypt = RsaUtils.decryptPrivate(modulus, publicExponent, encrypt);
        System.out.println("decrypt: " + decrypt);
        Assert.assertEquals(str, decrypt);

        // 公钥加密，私钥解密
        encrypt = RsaUtils.encryptPrivate(modulus, publicExponent, str);
        System.out.println("encrypt: " + encrypt);
        decrypt = RsaUtils.decryptPrivate(modulus, privateExponent, encrypt);
        System.out.println("decrypt: " + decrypt);
        Assert.assertEquals(str, decrypt);
    }
}