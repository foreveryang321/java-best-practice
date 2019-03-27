package top.ylonline.encrypt.util;

import org.junit.Assert;
import org.junit.Test;
import top.ylonline.encrypt.common.Const;

/**
 * @author YL
 */
public class AesUtilsTest {

    @Test
    public void backend() {
        String str = Const.STR;

        String key = AesUtils.randomKey();
        System.out.println("key: " + key);
        String encrypt = AesUtils.encrypt(key, "UTF-8", str);
        System.out.println("encrypt: " + encrypt);
        String decrypt = AesUtils.decrypt(key, "UTF-8", encrypt);
        System.out.println("decrypt: " + decrypt);
        Assert.assertEquals(str, decrypt);
    }

    /**
     * 前端解密，后端解密
     */
    @Test
    public void frontend() {
        String key = "NHXGgRvVrvLv4GaB";
        String encrypt = "Eq8kNQVlgaz+JbpR3R/Z0Q==";
        String decrypt = AesUtils.decrypt(key, "UTF-8", encrypt);
        System.out.println("decrypt: " + decrypt);
        Assert.assertEquals(Const.STR, decrypt);
    }
}