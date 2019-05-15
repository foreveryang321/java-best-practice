package top.ylonline.encrypt.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * RSA 加解密算法工具
 * <pre>
 *     依赖第三方jar包：bcprov-jdk16-1.46.jar 或者 bcprov-jdk15on-1.46.jar
 *     记录：
 *     1、Java Sun 的 security provider 默认：RSA/None/PKCS1Padding
 *     2、Android 的 security provider 是 Bouncycastle Security provider，默认：RSA/None/NoPadding
 *     3、Cipher.getInstance("RSA/ECB/NoPadding")，有一个缺点就是解密后的明文比加密之前多了很多空格
 *     推荐：
 *     Cipher.getInstance("RSA"，"BC")
 * </pre>
 * <pre>
 *     一般用法：
 *     1、私钥加密，公钥解密（服务端加密，客户端解密）
 *     2、公钥加密，私钥解密（客户端加密，服务端解密）
 *     注意：私钥不要泄漏出去，客户端一般使用公钥
 * </pre>
 *
 * @author YL
 */
public class RsaUtils {
    // /**
    //  * RSA 最大加密明文大小
    //  */
    // private static final int MAX_ENCRYPT_BLOCK = 117;
    //
    // /**
    //  * RSA 最大解密密文大小
    //  */
    // private static final int MAX_DECRYPT_BLOCK = 128;
    /**
     * 编码
     */
    private static final String CHARSET = "UTF-8";

    /**
     * 加密算法 RSA
     */
    private static final String ALGORITHM = "RSA";

    /**
     * 这个值关系到块加密的大小，可以更改，但是不要太大，否则效率会低
     * <pre>
     *     目前被破解的最长RSA密钥是768个二进制位。也就是说，长度超过768位的密钥，还无法破解（至少没人公开宣布）。
     *     因此可以认为，1024位的RSA密钥基本安全，2048位的密钥极其安全。
     * </pre>
     */
    private static final int KEY_SIZE = 1024;

    /**
     * 把 byte 数组变换为16进制的字符串
     *
     * @param bytes byte 数组
     *
     * @return 16进制的字符串
     */
    private static String byteToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte by : bytes) {
            int d = by;
            if (d < 0) {
                d += 256;
            }
            if (d < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(d, 16));
        }
        return sb.toString();
    }

    /**
     * 默认的安全服务提供者
     */
    private static final Provider BC_PROVIDER = new BouncyCastleProvider();
    private static final String BC = BouncyCastleProvider.PROVIDER_NAME;

    private static void addProvider() {
        String bc = Security.getProperty(BC);
        if (bc == null || "".equals(bc.trim())) {
            Security.addProvider(BC_PROVIDER);
        }
    }

    /**
     * 初始化 RSA，生成密钥对：KeyPair
     * <pre>
     *     生成密钥对是非常耗时的，一般要 2s 左右，所以最好缓存密钥对起来，定期去做更新，不用每次都初始化
     * </pre>
     */
    public static KeyPair generateKeyPair() throws EncryptException {
        try {
            addProvider();
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM, BC);
            // keysize 这个值关系到块加密的大小，可以更改，但是不要太大，否则效率会低
            keyPairGen.initialize(KEY_SIZE, new SecureRandom());
            KeyPair keyPair = keyPairGen.generateKeyPair();
            System.out.println("init：");
            System.out.println(keyPair.getPrivate());
            System.out.println(keyPair.getPublic());
            return keyPair;
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException("RSA 初始化失败。无此算法", e);
        } catch (NoSuchProviderException e) {
            throw new EncryptException("RSA 初始化失败。无此安全服务提供者", e);
        }
    }

    /**
     * 使用模（n）、公钥指数（e），还原公钥
     *
     * @param modulus        模（n）
     * @param publicExponent 公钥指数（e）
     *
     * @return RSAPublicKey 公钥
     */
    public static RSAPublicKey generatePublicKey(String modulus, String publicExponent) throws EncryptException {
        try {
            addProvider();
            KeyFactory keyFac = KeyFactory.getInstance(ALGORITHM, BC);
            BigInteger bi1 = new BigInteger(modulus, 16);
            BigInteger bi2 = new BigInteger(publicExponent, 16);
            RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(bi1, bi2);
            return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
        } catch (InvalidKeySpecException e) {
            throw new EncryptException("RSA 无效的密钥", e);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException("RSA 无此算法", e);
        } catch (NoSuchProviderException e) {
            throw new EncryptException("RSA 无此安全服务提供者", e);
        }
    }

    /**
     * * 使用模（n）、私钥指数（d），还原私钥
     *
     * @param modulus         模（n）
     * @param privateExponent 私钥指数（d）
     *
     * @return RSAPrivateKey 私钥
     */
    public static RSAPrivateKey generatePrivateKey(String modulus, String privateExponent) throws EncryptException {
        try {
            addProvider();
            KeyFactory keyFac = KeyFactory.getInstance(ALGORITHM, BC);
            BigInteger bi1 = new BigInteger(modulus, 16);
            BigInteger bi2 = new BigInteger(privateExponent, 16);
            RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(bi1, bi2);
            return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
        } catch (InvalidKeySpecException e) {
            throw new EncryptException("RSA 无效的密钥", e);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException("RSA 无此算法", e);
        } catch (NoSuchProviderException e) {
            throw new EncryptException("RSA 无此安全服务提供者", e);
        }
    }

    /**
     * 返回模值n
     */
    public static String getModulus(RSAPublicKey publicKey) {
        return publicKey.getModulus().toString(16);
    }

    /**
     * 返回模值n
     */
    public static String getModulus(RSAPrivateKey privateKey) {
        return privateKey.getModulus().toString(16);
    }

    /**
     * 返回公钥指数e
     */
    public static String getPublicExponent(RSAPublicKey publicKey) {
        return publicKey.getPublicExponent().toString(16);
    }

    /**
     * 返回私钥指数d
     */
    public static String getPrivateExponent(RSAPrivateKey privateKey) {
        return privateKey.getPrivateExponent().toString(16);
    }

    /**
     * 加密
     *
     * @param key  密钥（公钥、私钥）
     * @param data 要被加密的明文
     *
     * @return 加密后的数据
     */
    private static byte[] encrypt(Key key, byte[] data) throws EncryptException {
        try {
            addProvider();
            Cipher cipher = Cipher.getInstance(ALGORITHM, BC);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 127
            int blockSize = cipher.getBlockSize();// 获得加密块大小，如：加密前数据为128个byte，而key_size=1024
            // 加密块大小为127
            // byte,加密后为128个byte;因此共有2个加密块，第一个127
            // byte第二个为1个byte
            int length = data.length;
            int outputSize = cipher.getOutputSize(length);// 获得加密块加密后块大小
            int leavedSize = length % blockSize;
            int blocksSize = leavedSize != 0 ? length / blockSize + 1 : length / blockSize;
            byte[] raw = new byte[outputSize * blocksSize];
            int i = 0;
            while (length - i * blockSize > 0) {
                /*
                 * 这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到
                 * ByteArrayOutputStream中，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了
                 * OutputSize所以只好用dofinal方法。
                 */
                if (length - i * blockSize > blockSize) {
                    cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
                } else {
                    cipher.doFinal(data, i * blockSize, length - i * blockSize, raw, i * outputSize);
                }
                i++;
            }
            return raw;
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException("RSA 无此算法", e);
        } catch (InvalidKeyException e) {
            throw new EncryptException("RSA 无效的密钥", e);
        } catch (ShortBufferException e) {
            throw new EncryptException("RSA 缓冲区异常", e);
        } catch (NoSuchPaddingException e) {
            throw new EncryptException("RSA 无此填充方式", e);
        } catch (BadPaddingException e) {
            throw new EncryptException("RSA 错误的填充", e);
        } catch (NoSuchProviderException e) {
            throw new EncryptException("RSA 无此安全服务提供者", e);
        } catch (IllegalBlockSizeException e) {
            throw new EncryptException("RSA 非法的块大小", e);
        }
    }

    /**
     * 解密
     *
     * @param key  密钥（公钥、私钥）
     * @param data 要被解密的密文
     *
     * @return 解密后的明文
     */
    private static byte[] decrypt(Key key, byte[] data) throws EncryptException {
        ByteArrayOutputStream bout = null;
        try {
            addProvider();
            Cipher cipher = Cipher.getInstance(ALGORITHM, BC);
            cipher.init(Cipher.DECRYPT_MODE, key);
            // 128
            int blockSize = cipher.getBlockSize();
            bout = new ByteArrayOutputStream(64);
            int i = 0;
            int length = data.length;
            while (length - i * blockSize > 0) {
                bout.write(cipher.doFinal(data, i * blockSize, blockSize));
                i++;
            }
            return bout.toByteArray();
        } catch (BadPaddingException e) {
            throw new EncryptException("RSA 错误的填充", e);
        } catch (IOException e) {
            throw new EncryptException("RSA IO操作异常", e);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptException("RSA 无此算法", e);
        } catch (InvalidKeyException e) {
            throw new EncryptException("RSA 无效的密钥", e);
        } catch (NoSuchPaddingException e) {
            throw new EncryptException("RSA 无此填充方式", e);
        } catch (NoSuchProviderException e) {
            throw new EncryptException("RSA 无此安全服务提供者", e);
        } catch (IllegalBlockSizeException e) {
            throw new EncryptException("RSA 非法的块大小", e);
        } finally {
            if (bout != null) {
                try {
                    bout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String encrypt(Key pk, String data) throws EncryptException {
        try {
            data = URLEncoder.encode(data, CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 处理空格问题：URLEncoder.encode 后，会把空格变成“+”号
        if (data.contains("+")) {
            data = data.replace("+", "%20");
        }
        byte[] b1;
        try {
            b1 = data.getBytes(CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new EncryptException(String.format("不支持的编码格式：%s", CHARSET), e);
        }
        byte[] b2 = encrypt(pk, b1);
        return byteToString(b2);
    }

    private static String decrypt(Key pk, String data) throws EncryptException {
        // byte[] b1 = hexStringToBytes(data);
        byte[] b1 = org.bouncycastle.util.encoders.Hex.decode(data);
        // 使用 new BigInteger(data, 16).toByteArray() 解密 js 加密的密文有问题，但是解密 java 端的密文确正常
        // byte[] b1 = new BigInteger(data, 16).toByteArray();
        byte[] b2 = decrypt(pk, b1);
        String str = new String(b2);
        // str = new StringBuffer(str).reverse().toString();
        if (str.contains("%")) {
            str = str.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
        }
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new EncryptException(String.format("不支持的编码格式：%s", CHARSET), e);
        }
    }

    /**
     * 用公钥加密
     *
     * @param pk  公钥
     * @param str 要加密的明文
     */
    public static String encryptPublic(RSAPublicKey pk, String str) throws EncryptException {
        return encrypt(pk, str);
    }

    /**
     * 用公钥加密
     *
     * @param modulus        模
     * @param publicExponent 公钥指数
     * @param str            要加密的明文
     */
    public static String encryptPublic(String modulus, String publicExponent, String str) throws EncryptException {
        RSAPublicKey publicKey = generatePublicKey(modulus, publicExponent);
        return encrypt(publicKey, str);
    }

    /**
     * 用公钥解密
     *
     * @param pk  公钥
     * @param str 要解密的密文
     */
    public static String decryptPublic(RSAPublicKey pk, String str) throws EncryptException {
        return decrypt(pk, str);
    }

    /**
     * 用公钥解密
     *
     * @param modulus        模
     * @param publicExponent 公钥指数
     * @param str            要解密的密文
     */
    public static String decryptPublic(String modulus, String publicExponent, String str) throws EncryptException {
        RSAPublicKey publicKey = generatePublicKey(modulus, publicExponent);
        return decrypt(publicKey, str);
    }

    /**
     * 用私钥加密
     *
     * @param pk  私钥
     * @param str 要加密的明文
     */
    public static String encryptPrivate(RSAPrivateKey pk, String str) throws EncryptException {
        return encrypt(pk, str);
    }

    /**
     * 用私钥加密
     *
     * @param modulus         模
     * @param privateExponent 私钥指数
     * @param str             要加密的明文
     */
    public static String encryptPrivate(String modulus, String privateExponent, String str) throws EncryptException {
        RSAPrivateKey privateKey = generatePrivateKey(modulus, privateExponent);
        return encrypt(privateKey, str);
    }

    /**
     * 用私钥解密
     *
     * @param pk  私钥
     * @param str 要解密的密文
     */
    public static String decryptPrivate(RSAPrivateKey pk, String str) throws EncryptException {
        return decrypt(pk, str);
    }

    /**
     * 用私钥解密
     *
     * @param modulus         模
     * @param privateExponent 私钥指数
     * @param str             要解密的密文
     */
    public static String decryptPrivate(String modulus, String privateExponent, String str) throws EncryptException {
        RSAPrivateKey privateKey = generatePrivateKey(modulus, privateExponent);
        return decrypt(privateKey, str);
    }
}
/* RSA 对应 js 加解密方法 */
/*
 (function(p){if(typeof p.RSAUtils==="undefined"){var k=p.RSAUtils={}}var o=2;var e=16;var c=e;var b=1<<16;var
 z=b>>>1;var l=b*b;var h=b-1;var A=9999999999999998;var m;var g;var t,a;var v=p.BigInt=function(B){if(typeof
 B=="boolean"&&B==true){this.digits=null}else{this.digits=g.slice(0)}this.isNeg=false};k.setMaxDigits=function(C)
 {m=C;g=new Array(m);for(var B=0;B<g.length;B++){g[B]=0}t=new v();a=new v();a.digits[0]=1};k.setMaxDigits(20);var
 q=15;k.biFromNumber=function(D){var B=new v();B.isNeg=D<0;D=Math.abs(D);var C=0;while(D>0){B.digits[C++]=D&h;D=Math
 .floor(D/b)}return B};var r=k.biFromNumber(1000000000000000);k.biFromDecimal=function(F){var E=F.charAt(0)=="-";var
 D=E?1:0;var B;while(D<F.length&&F.charAt(D)=="0"){++D}if(D==F.length){B=new v()}else{var C=F.length-D;var G=C%q;if
 (G==0){G=q}B=k.biFromNumber(Number(F.substr(D,G)));D+=G;while(D<F.length){B=k.biAdd(k.biMultiply(B,r),k.biFromNumber
 (Number(F.substr(D,q))));D+=q}B.isNeg=E}return B};k.biCopy=function(C){var B=new v(true);B.digits=C.digits.slice(0);
 B.isNeg=C.isNeg;return B};k.reverseStr=function(D){var B="";for(var C=D.length-1;C>-1;--C){B+=D.charAt(C)}return B};
 var x=["0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q",
 "r","s","t","u","v","w","x","y","z"];k.biToString=function(D,F){var C=new v();C.digits[0]=F;var E=k.biDivideModulo
 (D,C);var B=x[E[1].digits[0]];while(k.biCompare(E[0],t)==1){E=k.biDivideModulo(E[0],C);digit=E[1].digits[0];
 B+=x[E[1].digits[0]]}return(D.isNeg?"-":"")+k.reverseStr(B)};k.biToDecimal=function(D){var C=new v();C.digits[0]=10;
 var E=k.biDivideModulo(D,C);var B=String(E[1].digits[0]);while(k.biCompare(E[0],t)==1){E=k.biDivideModulo(E[0],C);
 B+=String(E[1].digits[0])}return(D.isNeg?"-":"")+k.reverseStr(B)};var w=["0","1","2","3","4","5","6","7","8","9",
 "a","b","c","d","e","f"];k.digitToHex=function(D){var C=15;var B="";for(i=0;i<4;++i){B+=w[D&C];D>>>=4}return k
 .reverseStr(B)};k.biToHex=function(C){var B="";var E=k.biHighIndex(C);for(var D=k.biHighIndex(C);D>-1;--D){B+=k
 .digitToHex(C.digits[D])}return B};k.charToHex=function(I){var D=48;var C=D+9;var E=97;var H=E+25;var G=65;var
 F=65+25;var B;if(I>=D&&I<=C){B=I-D}else{if(I>=G&&I<=F){B=10+I-G}else{if(I>=E&&I<=H){B=10+I-E}else{B=0}}}return B};k
 .hexToDigit=function(E){var C=0;var B=Math.min(E.length,4);for(var D=0;D<B;++D){C<<=4;C|=k.charToHex(E.charCodeAt(D)
 )}return C};k.biFromHex=function(F){var C=new v();var B=F.length;for(var E=B,D=0;E>0;E-=4,++D){C.digits[D]=k
 .hexToDigit(F.substr(Math.max(E-4,0),Math.min(E,4)))}return C};k.biFromString=function(J,I){var B=J.charAt(0)=="-";
 var E=B?1:0;var K=new v();var C=new v();C.digits[0]=1;for(var D=J.length-1;D>=E;D--){var F=J.charCodeAt(D);var G=k
 .charToHex(F);var H=k.biMultiplyDigit(C,G);K=k.biAdd(K,H);C=k.biMultiplyDigit(C,I)}K.isNeg=B;return K};k
 .biDump=function(B){return(B.isNeg?"-":"")+B.digits.join(" ")};k.biAdd=function(C,G){var B;if(C.isNeg!=G.isNeg){G
 .isNeg=!G.isNeg;B=k.biSubtract(C,G);G.isNeg=!G.isNeg}else{B=new v();var F=0;var E;for(var D=0;D<C.digits.length;++D)
 {E=C.digits[D]+G.digits[D]+F;B.digits[D]=E%b;F=Number(E>=b)}B.isNeg=C.isNeg}return B};k.biSubtract=function(C,G){var
  B;if(C.isNeg!=G.isNeg){G.isNeg=!G.isNeg;B=k.biAdd(C,G);G.isNeg=!G.isNeg}else{B=new v();var F,E;E=0;for(var D=0;D<C
  .digits.length;++D){F=C.digits[D]-G.digits[D]+E;B.digits[D]=F%b;if(B.digits[D]<0){B.digits[D]+=b}E=0-Number(F<0)}if
  (E==-1){E=0;for(var D=0;D<C.digits.length;++D){F=0-B.digits[D]+E;B.digits[D]=F%b;if(B.digits[D]<0){B
  .digits[D]+=b}E=0-Number(F<0)}B.isNeg=!C.isNeg}else{B.isNeg=C.isNeg}}return B};k.biHighIndex=function(C){var B=C
  .digits.length-1;while(B>0&&C.digits[B]==0){--B}return B};k.biNumBits=function(D){var F=k.biHighIndex(D);var E=D
  .digits[F];var C=(F+1)*c;var B;for(B=C;B>C-c;--B){if((E&32768)!=0){break}E<<=1}return B};k.biMultiply=function(H,G)
  {var K=new v();var F;var C=k.biHighIndex(H);var J=k.biHighIndex(G);var I,B,D;for(var E=0;E<=J;++E){F=0;D=E;for(j=0;
  j<=C;++j,++D){B=K.digits[D]+H.digits[j]*G.digits[E]+F;K.digits[D]=B&h;F=B>>>e}K.digits[E+C+1]=F}K.isNeg=H.isNeg!=G
  .isNeg;return K};k.biMultiplyDigit=function(B,G){var F,E,D;result=new v();F=k.biHighIndex(B);E=0;for(var C=0;C<=F;
  ++C){D=result.digits[C]+B.digits[C]*G+E;result.digits[C]=D&h;E=D>>>e}result.digits[1+F]=E;return result};k
  .arrayCopy=function(F,I,D,H,G){var B=Math.min(I+G,F.length);for(var E=I,C=H;E<B;++E,++C){D[C]=F[E]}};var f=[0,
  32768,49152,57344,61440,63488,64512,65024,65280,65408,65472,65504,65520,65528,65532,65534,65535];k
  .biShiftLeft=function(C,I){var E=Math.floor(I/c);var B=new v();k.arrayCopy(C.digits,0,B.digits,E,B.digits.length-E)
  ;var H=I%c;var D=c-H;for(var F=B.digits.length-1,G=F-1;F>0;--F,--G){B.digits[F]=((B.digits[F]<<H)&h)|((B
  .digits[G]&f[H])>>>(D))}B.digits[0]=((B.digits[F]<<H)&h);B.isNeg=C.isNeg;return B};var u=[0,1,3,7,15,31,63,127,255,
  511,1023,2047,4095,8191,16383,32767,65535];k.biShiftRight=function(C,I){var D=Math.floor(I/c);var B=new v();k
  .arrayCopy(C.digits,D,B.digits,0,C.digits.length-D);var G=I%c;var H=c-G;
 for(var E=0,F=E+1;E<B.digits.length-1;++E,++F){B.digits[E]=(B.digits[E]>>>G)|((B.digits[F]&u[G])<<H)}B.digits[B
 .digits.length-1]>>>=G;B.isNeg=C.isNeg;return B};k.biMultiplyByRadixPower=function(C,D){var B=new v();k.arrayCopy(C
 .digits,0,B.digits,D,B.digits.length-D);return B};k.biDivideByRadixPower=function(C,D){var B=new v();k.arrayCopy(C
 .digits,D,B.digits,0,B.digits.length-D);return B};k.biModuloByRadixPower=function(C,D){var B=new v();k.arrayCopy(C
 .digits,0,B.digits,0,D);return B};k.biCompare=function(B,D){if(B.isNeg!=D.isNeg){return 1-2*Number(B.isNeg)}for(var
 C=B.digits.length-1;C>=0;--C){if(B.digits[C]!=D.digits[C]){if(B.isNeg){return 1-2*Number(B.digits[C]>D.digits[C])
 }else{return 1-2*Number(B.digits[C]<D.digits[C])}}}return 0};k.biDivideModulo=function(G,F){var B=k.biNumBits(G);var
  E=k.biNumBits(F);var D=F.isNeg;var L,K;if(B<E){if(G.isNeg){L=k.biCopy(a);L.isNeg=!F.isNeg;G.isNeg=false;F
  .isNeg=false;K=biSubtract(F,G);G.isNeg=true;F.isNeg=D}else{L=new v();K=k.biCopy(G)}return[L,K]}L=new v();K=G;var
  I=Math.ceil(E/c)-1;var H=0;while(F.digits[I]<z){F=k.biShiftLeft(F,1);++H;++E;I=Math.ceil(E/c)-1}K=k.biShiftLeft(K,
  H);B+=H;var O=Math.ceil(B/c)-1;var T=k.biMultiplyByRadixPower(F,O-I);while(k.biCompare(K,T)!=-1){++L.digits[O-I];
  K=k.biSubtract(K,T)}for(var R=O;R>I;--R){var J=(R>=K.digits.length)?0:K.digits[R];var S=(R-1>=K.digits.length)?0:K
  .digits[R-1];var Q=(R-2>=K.digits.length)?0:K.digits[R-2];var P=(I>=F.digits.length)?0:F.digits[I];var C=(I-1>=F
  .digits.length)?0:F.digits[I-1];if(J==P){L.digits[R-I-1]=h}else{L.digits[R-I-1]=Math.floor((J*b+S)/P)}var N=L
  .digits[R-I-1]*((P*b)+C);var M=(J*l)+((S*b)+Q);while(N>M){--L.digits[R-I-1];N=L.digits[R-I-1]*((P*b)|C);M=(J*b*b)+(
  (S*b)+Q)}T=k.biMultiplyByRadixPower(F,R-I-1);K=k.biSubtract(K,k.biMultiplyDigit(T,L.digits[R-I-1]));if(K.isNeg){K=k
  .biAdd(K,T);--L.digits[R-I-1]}}K=k.biShiftRight(K,H);L.isNeg=G.isNeg!=D;if(G.isNeg){if(D){L=k.biAdd(L,a)}else{L=k
  .biSubtract(L,a)}F=k.biShiftRight(F,H);K=k.biSubtract(F,K)}if(K.digits[0]==0&&k.biHighIndex(K)==0){K
  .isNeg=false}return[L,K]};k.biDivide=function(B,C){return k.biDivideModulo(B,C)[0]};k.biModulo=function(B,C){return
   k.biDivideModulo(B,C)[1]};k.biMultiplyMod=function(C,D,B){return k.biModulo(k.biMultiply(C,D),B)};k.biPow=function
   (C,E){var B=a;var D=C;while(true){if((E&1)!=0){B=k.biMultiply(B,D)}E>>=1;if(E==0){break}D=k.biMultiply(D,D)}return
    B};k.biPowMod=function(D,G,C){var B=a;var E=D;var F=G;while(true){if((F.digits[0]&1)!=0){B=k.biMultiplyMod(B,E,C)
    }F=k.biShiftRight(F,1);if(F.digits[0]==0&&k.biHighIndex(F)==0){break}E=k.biMultiplyMod(E,E,C)}return B};p
    .BarrettMu=function(B){this.modulus=k.biCopy(B);this.k=k.biHighIndex(this.modulus)+1;var C=new v();C
    .digits[2*this.k]=1;this.mu=k.biDivide(C,this.modulus);this.bkplus1=new v();this.bkplus1.digits[this.k+1]=1;this
    .modulo=s;this.multiplyMod=n;this.powMod=d};function s(J){var C=k;var I=C.biDivideByRadixPower(J,this.k-1);var
    G=C.biMultiply(I,this.mu);var F=C.biDivideByRadixPower(G,this.k+1);var E=C.biModuloByRadixPower(J,this.k+1);var
    K=C.biMultiply(F,this.modulus);var D=C.biModuloByRadixPower(K,this.k+1);var B=C.biSubtract(E,D);if(B.isNeg){B=C
    .biAdd(B,this.bkplus1)}var H=C.biCompare(B,this.modulus)>=0;while(H){B=C.biSubtract(B,this.modulus);H=C.biCompare
    (B,this.modulus)>=0}return B}function n(B,D){var C=k.biMultiply(B,D);return this.modulo(C)}function d(C,F){var
    B=new v();B.digits[0]=1;var D=C;var E=F;while(true){if((E.digits[0]&1)!=0){B=this.multiplyMod(B,D)}E=k
    .biShiftRight(E,1);if(E.digits[0]==0&&k.biHighIndex(E)==0){break}D=this.multiplyMod(D,D)}return B}var y=function
    (C,E,B){var D=k;this.e=D.biFromHex(C);this.d=D.biFromHex(E);this.m=D.biFromHex(B);this.chunkSize=2*D.biHighIndex
    (this.m);this.radix=16;this.barrett=new p.BarrettMu(this.m)};k.getKeyPair=function(C,D,B){return new y(C,D,B)};if
    (typeof p.twoDigit==="undefined"){p.twoDigit=function(B){return(B<10?"0":"")+String(B)}}k
    .encryptedString=function(I,L){var H=[];var B=L.length;var F=0;while(F<B){H[F]=L.charCodeAt(F);F++}while(H
    .length%I.chunkSize!=0){H[F++]=0}var G=H.length;var M="";var E,D,C;for(F=0;F<G;F+=I.chunkSize){C=new v();E=0;for
    (D=F;D<F+I.chunkSize;++E){C.digits[E]=H[D++];C.digits[E]+=H[D++]<<8}var K=I.barrett.powMod(C,I.e);var J=I
    .radix==16?k.biToHex(K):k.biToString(K,I.radix);M+=J+" "}return M.substring(0,M.length-1)};k
    .decryptedString=function(F,G){var I=G.split(" ");var B="";var E,D,H;for(E=0;E<I.length;++E){var C;if(F
    .radix==16){C=k.biFromHex(I[E])}else{C=k.biFromString(I[E],F.radix)}H=F.barrett.powMod(C,F.d);for(D=0;D<=k
    .biHighIndex(H);++D){B+=String.fromCharCode(H.digits[D]&255,H.digits[D]>>8)}}if(B.charCodeAt(B.length-1)==0){B=B
    .substring(0,B.length-1)}return B};k.setMaxDigits(130)})(window);


 * RSA 加密数据：返回加密后的字符串
 * <p>e 公匙
 * <p>n 模
 * <p>s 要加密的字符串
function encryptRSA(e, n, s) {
    RSAUtils.setMaxDigits(130);
    var keyPair = RSAUtils.getKeyPair(e, '', n);
    // URI 编码
    s = window.encodeURIComponent(s);
    // 反转
    s = s.split("").reverse().join("");
    return RSAUtils.encryptedString(keyPair, s);
}

 * RSA 解密数据：返回解密后的字符串
 * <p>e 公匙
 * <p>n 模
 * <p>s 要解密的字符串
function decryptRSA(e, n, s) {
    RSAUtils.setMaxDigits(130);
    var keyPair = RSAUtils.getKeyPair('', e, n);
    s = RSAUtils.decryptedString(keyPair, s);
    // 反转
    s = s.split("").reverse().join("");
    // URI 解码
    return window.decodeURIComponent(s);
}

var n = [[${modulus}]];
// console.log('n: %o', n);
var e = [[${publicExponent}]];
// console.log('e: %o', e);
var d = [[${privateExponent}]];
// console.log('d: %o', d);
var encrypt = [[${encrypt}]];
console.log('encrypt: %o', encrypt);

console.log('------------- 公钥加密，私钥解密 -------------');
var str = '客户端 - 法拉利_123_abc_《》';
console.log('str: %o', str);
var encryptR = encryptRSA(e, n, str);
console.log('encryptR: %o', encryptR);
var decryptR = decryptRSA(d, n, encryptR);
console.log('decryptR: %o', decryptR);
console.log('------------- 私钥加密，公钥解密 -------------');
console.log('str: %o', str);
encryptR = encryptRSA(d, n, str);
console.log('encryptR: %o', encryptR);
decryptR = decryptRSA(e, n, encryptR);
console.log('decryptR: %o', decryptR);

console.log('------------- 服务端私钥加密，客户端公钥解密 -------------');
decryptR = decryptRSA(e, n, encrypt);
console.log('decryptR: %o', decryptR);
 */
