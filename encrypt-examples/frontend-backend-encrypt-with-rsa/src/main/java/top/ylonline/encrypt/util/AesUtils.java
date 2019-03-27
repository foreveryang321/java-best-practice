package top.ylonline.encrypt.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * AES 加密、解密
 *
 * @author YL
 */
public class AesUtils {
    private static final String ALGORITHM = "AES";
    private static final String S_KEY = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int S_KEY_LENGTH = S_KEY.length();

    /**
     * 随机生成 16 个字符的 key
     */
    public static String randomKey() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int floor = (int) Math.floor(Math.random() * S_KEY_LENGTH);
            sb.append(S_KEY.charAt(floor));
        }
        return sb.toString();
    }

    /**
     * 加密
     *
     * @param key     加密密码
     * @param charset 编码格式
     * @param data    需要加密的内容
     *
     * @return 加密后的数据
     */
    private static byte[] enc(String key, String charset, String data) throws EncryptException {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(charset), ALGORITHM);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            return cipher.doFinal(data.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            throw new EncryptException("AES 不支持的编码格式", e);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException("AES 不支持的加密算法", e);
        } catch (NoSuchPaddingException e) {
            throw new EncryptException("AES 不支持的填充机制", e);
        } catch (InvalidKeyException e) {
            throw new EncryptException("AES 无效的Key、错误的长度、未初始化等", e);
        } catch (IllegalBlockSizeException e) {
            throw new EncryptException("AES 密码的块大小不匹配", e);
        } catch (BadPaddingException e) {
            throw new EncryptException("AES 输入的数据错误，导致填充机制未能正常填充", e);
        }
    }

    /**
     * 解密
     *
     * @param key     解密密码
     * @param charset 编码格式
     * @param data    需要解密的内容
     *
     * @return 解密后的数据
     */
    private static byte[] dec(String key, String charset, byte[] data) throws EncryptException {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(charset), ALGORITHM);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            return cipher.doFinal(data);
        } catch (UnsupportedEncodingException e) {
            throw new EncryptException("AES 不支持的编码格式", e);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException("AES 不支持的加密算法", e);
        } catch (NoSuchPaddingException e) {
            throw new EncryptException("AES 不支持的填充机制", e);
        } catch (InvalidKeyException e) {
            throw new EncryptException("AES 无效的Key、错误的长度、未初始化等", e);
        } catch (IllegalBlockSizeException e) {
            throw new EncryptException("AES 密码的块大小不匹配", e);
        } catch (BadPaddingException e) {
            throw new EncryptException("AES 输入的数据错误，导致填充机制未能正常填充", e);
        }
    }

    /**
     * 加密
     *
     * @param key     加密 key
     * @param charset 编码格式
     * @param data    要加密的明文
     *
     * @return 加密后的字符串密文
     */
    public static String encrypt(String key, String charset, String data) throws EncryptException {
        byte[] enc = enc(key, charset, data);
        byte[] b = Base64.getEncoder().encode(enc);
        // byte[] b = Base64.encodeBase64(enc);
        try {
            return new String(b, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 解密
     *
     * @param key     解密 key
     * @param charset 编码格式
     * @param data    要解密的密文
     *
     * @return 解密后的字符串明文
     */
    public static String decrypt(String key, String charset, String data) throws EncryptException {
        if (data == null || "".equals(data)) {
            return "";
        }
        try {
            byte[] b = Base64.getDecoder().decode(data.getBytes(charset));
            // byte[] b = Base64.decodeBase64(data.getBytes(charset));
            byte[] dec = dec(key, charset, b);
            return new String(dec, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
