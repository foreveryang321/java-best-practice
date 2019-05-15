package top.ylonline.encrypt.util;

import org.junit.Test;
import top.ylonline.encrypt.common.Const;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static org.junit.Assert.assertEquals;

/**
 * @author YL
 */
public class RsaUtilsTest {

    @Test
    public void publicKey() {
        RSAPublicKey publicKey = RsaUtils.generatePublicKey(Const.MODULUS, Const.PUBLIC_EXPONENT);
        assertEquals(Const.PUBLIC_EXPONENT, RsaUtils.getPublicExponent(publicKey));
        assertEquals(Const.MODULUS, RsaUtils.getModulus(publicKey));

    }

    @Test
    public void privateKey() {
        RSAPrivateKey privateKey = RsaUtils.generatePrivateKey(Const.MODULUS, Const.PRIVATE_EXPONENT);
        assertEquals(Const.PRIVATE_EXPONENT, RsaUtils.getPrivateExponent(privateKey));
        assertEquals(Const.MODULUS, RsaUtils.getModulus(privateKey));
    }

    @Test
    public void backend() {
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
        assertEquals(str, decrypt);

        // 公钥加密，私钥解密
        encrypt = RsaUtils.encryptPrivate(modulus, publicExponent, str);
        System.out.println("encrypt: " + encrypt);
        decrypt = RsaUtils.decryptPrivate(modulus, privateExponent, encrypt);
        System.out.println("decrypt: " + decrypt);
        assertEquals(str, decrypt);
    }

    /**
     * 前端公钥加密，后端私钥解密
     */
    @Test
    public void frontend() {
        String encrypt =
                "60ffccfdc632d0f42a73db72e29ebfd55263701791f4eea1c6de0594cbf07fd39443360e51bd2cd6f0d10a9438eb34203eb425394fe62aa4b2d62a7fe2c92b33eb9b79840a2ae2c18d575970d225ebb92e037c5f81a7d58673bbe7ad283e0f15e82bc526cc43575442bd7ebcf478dd1b7be7535adf3aa3c4c2f1b43d262bc910";
        String decrypt = RsaUtils.decryptPrivate(Const.MODULUS, Const.PRIVATE_EXPONENT, encrypt);
        System.out.println("decrypt: " + decrypt);
        assertEquals(Const.STR, decrypt);
    }
}