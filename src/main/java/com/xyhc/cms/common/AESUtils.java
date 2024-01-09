package com.xyhc.cms.common;


import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AESUtils {


    private static final String CHARSET = "utf-8";

    private static AESUtils aesUtils = null;

    private AESUtils() {
    }

    /**
     * 获取工具类对象实例
     *
     * @return
     */
    public static AESUtils getInstance() {
        if (null == aesUtils) {
            aesUtils = new AESUtils();
        }
        return aesUtils;
    }

    /**
     * 加密
     *
     * @param seed
     * @param content
     * @return
     */
    public static String encryptBase64(String seed, String content) {
        try {
            byte[] rawKey = getRawKey(seed.getBytes(CHARSET));
            byte[] result = encrypt(rawKey, content.getBytes(CHARSET));
            return Base64.encodeBase64String(result);
        } catch (Exception e) {
            log.error("AES加密base64编码出错", e);
            return null;
        }
    }

    /**
     * 解密
     *
     * @param seed
     * @param encrypted
     * @return
     */
    public static String decryptBase64(String seed, String encrypted) {
        try {
            byte[] rawKey = getRawKey(seed.getBytes(CHARSET));
            byte[] enc = Base64.decodeBase64(encrypted);
            byte[] result = decrypt(rawKey, enc);
            return new String(result, CHARSET);
        } catch (Exception e) {
            log.error("AES解密base64出错", e);
            return null;
        }
    }

    /**
     * content先Base64 加密
     *
     * @param seed
     * @param content 需要做Base64处理
     * @return
     */
    public static String encryptContentBase64(String seed, String content) {
        log.info("content:{}", content);
        try {
            byte[] rawKey = getRawKey(seed.getBytes(CHARSET));
            byte[] result = encrypt(rawKey, Base64.encodeBase64(content.getBytes(CHARSET)));
            return Base64.encodeBase64String(result);
        } catch (Exception e) {
            log.error("AES加密base64编码出错", e);
            return null;
        }
    }

    /**
     * 返回先Base64 解密
     *
     * @param seed
     * @param encrypted
     * @return 需要做Base64处理
     */
    public static String decryptContentBase64(String seed, String encrypted) {
        try {
            byte[] rawKey = getRawKey(seed.getBytes(CHARSET));
            byte[] enc = Base64.decodeBase64(encrypted);
            byte[] result = decrypt(rawKey, enc);
            return new String(Base64.decodeBase64(new String(result, CHARSET)), CHARSET);
        } catch (Exception e) {
            log.error("AES解密base64出错", e);
            return null;
        }
    }

    private static byte[] getRawKey(byte[] seed) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES", "SunJCE");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);
        // 192 and 256 bits may not be available
        kgen.init(128, sr);
        SecretKey skey = kgen.generateKey();
        return skey.getEncoded();
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        // AES/CBC/PKCS5Padding
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return cipher.doFinal(clear);
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        // AES/CBC/PKCS5Padding
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        return cipher.doFinal(encrypted);
    }

    /**
     * 功能: [CBC加密]<br/>
     * 详细说明：应?于获取约定的AES加密参数<br/>
     */
    public static String des3EncodeCBC(String aesKey, String dtKey) {
        byte[] keyiv = {1, 2, 3, 4, 5, 6, 7, 8};
        try {
            byte[] key = Base64.decodeBase64(dtKey);
            byte[] data = aesKey.getBytes("UTF-8");
            byte[] str5 = des3EncodeCBC(key, keyiv, data);
            return new String(Base64.encodeBase64(str5));
        } catch (Exception e) {
            log.error("加密AES参数报错", e);
            throw new RuntimeException(e);
        }
    }

    public static UUID getUUID() {
        return UUID.randomUUID();
    }

    private static byte[] des3EncodeCBC(byte[] key, byte[] keyiv, byte[] data) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(1, deskey, ips);
        return cipher.doFinal(data);
    }

    public static String encryptCBC(String sKey, String sSrc) throws Exception {
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            log.info("Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
        IvParameterSpec iv = new IvParameterSpec("1234567890123456".getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());
        return Base64.encodeBase64String(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }
}

