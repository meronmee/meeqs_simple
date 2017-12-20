package com.meronmee.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * 加解密工具类
 * <pre>
 * 支持如下加密算法：
 * 单向加密: MD5、SHA1
 * 双向加解密: Base64, XOR, DES、DESede(3DES)、AES
 * </pre>
 */
public class EncryptUtil {        
    /** 3DES加解密密钥生成算法 */
    private static final String DESEDE_KEY_ALGORITHM = "DESede";
    /** 3DES加密/解密算法/工作模式/填充方式 */
    private static final String DESEDE_CIPHER_ALGORITHM = "DESede/ECB/PKCS5Padding";
    
    /** DES加解密密钥生成算法 */
    private static final String DES_KEY_ALGORITHM = "DES";
    /** DES加密/解密算法/工作模式/填充方式 */
    private static final String DES_CIPHER_ALGORITHM = "DES";

    /** AES加解密密钥长度 */
    private static final int AES_KEY_LENGTH = 128;
    /** AES加解密密钥生成算法 */
    private static final String AES_KEY_ALGORITHM = "AES";
    /** AES加密/解密算法/工作模式/填充方式 */
    private static final String AES_CIPHER_ALGORITHM = "AES";
    

    //----------------工具方法-----------------
    /**
     * 字节数组转16进制字符串
     * @param data 目标字节数组
     * @return 小写十六进制字符串
     */
    public static String hex(final byte[] data) {
        return Hex.encodeHexString(data);
    }
    /**
     * 16进制字符串转字节数组
     * @param data 目标16进制字符串
     * @return 对应的字节数组
     */
    public static byte[] hex(final String data) throws Exception {
        return Hex.decodeHex(data);
    }
    
    /**
     * 字符串转UTF-8编码的字节数组
     * @param str 目标字符串
     */
    public static byte[] getBytesUtf8(final String str) {
        return org.apache.commons.codec.binary.StringUtils.getBytesUtf8(str);
    }

    /**
     * UTF-8编码的字节数组转字符串
     * @param bytes 目标字节数组
     */
    public static String newStringUtf8(final byte[] bytes) {
        return org.apache.commons.codec.binary.StringUtils.newStringUtf8(bytes);
    }
    
    /**
     * UUID方式生成一个32位的秘钥
     */
    public static String generateKey() {   
		return java.util.UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
    }  
    
    //----------------MD5-----------------
    
    /**
     * 求一个文件的MD5值
     * @param file 目标文件
     * @return MD5值（32位的小写十六进制字符串）, 如果为file非合法文件或出现IO异常，则返回null
     */
    public static String md5(final File file) {
    	try {
        	if(file == null || !file.exists() || !file.isFile()){
        		return null;
        	}
			return DigestUtils.md5Hex(new FileInputStream(file)).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
    		return null;
		}
    }
    
    /**
     * 求一个字符串的MD5值
     * @param str 目标字符串
     * @return MD5值（32位的小写十六进制字符串）
     */
    public static String md5(final String str) {
    	return md5(str, "");
    }
    
    /**
     * 求一个字符串的MD5值
     * @param str 目标字符串
     * @param salt 盐
     * @return MD5值（32位的小写十六进制字符串）
     */
    public static String md5(final String str, final String salt) {
    	StringBuilder sb = new StringBuilder("");
    	if(StringUtils.isNotBlank(str)){
    		sb.append(str);
    	}
    	if(StringUtils.isNotBlank(salt)){
    		sb.append(salt);
    	}
    	return DigestUtils.md5Hex(sb.toString()).toLowerCase();
    }
    

    //----------------SHA-1-----------------
    /**
     * 求一个文件的SHA-1值
     * @param file 目标文件
     * @return SHA-1值（小写十六进制字符串）, 如果为file非合法文件或出现IO异常，则返回null
     */
    public static String sha1(final File file) {
    	try {
        	if(file == null || !file.exists() || !file.isFile()){
        		return null;
        	}
			return DigestUtils.sha1Hex(new FileInputStream(file)).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
    		return null;
		}
    }
    
    /**
     * 求一个字符串的SHA-1值
     * @param str 目标字符串
     * @return SHA-1值（小写十六进制字符串）
     */
    public static String sha1(final String str) {
    	return sha1(str, "");
    }
    
    /**
     * 求一个字符串的SHA-1值
     * @param str 目标字符串
     * @param salt 盐
     * @return SHA-1值（小写十六进制字符串）
     */
    public static String sha1(final String str, final String salt) {
    	StringBuilder sb = new StringBuilder("");
    	if(StringUtils.isNotBlank(str)){
    		sb.append(str);
    	}
    	if(StringUtils.isNotBlank(salt)){
    		sb.append(salt);
    	}
    	return DigestUtils.sha1Hex(sb.toString()).toLowerCase();
    }
    
    
    //----------------Base64-----------------
    
    /**
     * base64编码一个二进制数组
     * @param data
     * @return Base64 编码字符串
     */
    public static String base64Encode(byte[] data) {
    	return Base64.encodeBase64String(data);
    }
    /**
     * base64编码一个二进制数组<p>
     * 替换URL中不安全的 + 和 / 分别替换为- 和 _
     * @param data
     * @return  Base64 编码字符串
     */
    public static String base64URLSafeEncode(byte[] data) {
    	return Base64.encodeBase64URLSafeString(data);
    }
    
    /**
     * 解码一个base64字符串
     * @param base64String base64String 编码后的字符串
     * @return
     */
    public static byte[] base64Decode(String base64String ) {
    	return Base64.decodeBase64(base64String);
    }
       
    //----------------
    
    /**
     * base64编码一个普通字符串
     * @param str 要编码的字符串
     * @return Base64 编码字符串，如果str为空则返回空字符串
     */
    public static String base64EncodeString(String str) {
    	if(StringUtils.isBlank(str)){
  		  	return "";
  	  	}
  	  	
  		return base64Encode(getBytesUtf8(str));
    }
    
    /**
     * base64编码一个普通字符串
     * 替换URL中不安全的 + 和 / 分别替换为- 和 _
     * @param str 要编码的字符串
     * @return Base64 编码字符串，如果str为空则返回空字符串
     */
    public static String base64URLSafeEncodeString(String str) {
    	if(StringUtils.isBlank(str)){
  		  	return "";
  	  	}
  	  	
  		return base64URLSafeEncode(getBytesUtf8(str));
    }
    
    /**
     * 解码一个base64字符串
     * @param base64String base64String 编码后的字符串
     * @return
     */
    public static String base64DecodeString(String base64String ) {
    	byte[] data = Base64.decodeBase64(base64String);
    	return newStringUtf8(data);
    }    
    
    //----------------XOR-----------------
    /**
     * 异或 加密字符串
     * @param raw 要加密的字符串明文
     * @param key 密钥
     * @return
     */
    public static String xorEncode(String raw, String key) {
        byte[] bs = getBytesUtf8(raw);
        for (int i = 0; i < bs.length; i++) {
            bs[i] = (byte) ((bs[i]) ^ key.hashCode());
        }
        return hex(bs);
    }
 
    /**
     * 异或 解密字符串
     * @param crypto 要解密的字符串密文
     * @param key 密钥
     * @return
     */
    public static String xorDecode(String crypto, String key) throws Exception {
        byte[] bs = hex(crypto);
        for (int i = 0; i < bs.length; i++) {
            bs[i] = (byte) ((bs[i]) ^ key.hashCode());
        }
        return newStringUtf8(bs);
    }
    
    
    //----------------DESede(3DES)-----------------
    
    /**
     * 3DES加密数据
     * 
     * @param data 待加密数据
     * @param key 密钥
     * @return byte[] 加密后的数据
     * */
    public static byte[] desedeEncrypt(byte[] data, byte[] key) throws Exception {
    	Key secretKey = SecretKeyFactory.getInstance(DESEDE_KEY_ALGORITHM).generateSecret(new DESedeKeySpec(key)); 
        Cipher cipher = Cipher.getInstance(DESEDE_CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
       
        return cipher.doFinal(data);
    }

    /**
     * 3DES解密数据
     * 
     * @param data 待解密数据
     * @param key 密钥
     * @return byte[] 解密后的数据
     * */
    public static byte[] desedeDecrypt(byte[] data, byte[] key) throws Exception {
    	Key secretKey = SecretKeyFactory.getInstance(DESEDE_KEY_ALGORITHM).generateSecret(new DESedeKeySpec(key)); 
        Cipher cipher = Cipher.getInstance(DESEDE_CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        
        return cipher.doFinal(data);
    }
    
    /**
     * 3DES加密数据
     * 
     * @param data 待加密数据
     * @param key 密钥
     * @return 加密后的数据
     * */
    public static String desedeEncrypt(String data, String key) throws Exception {
        byte[] cryptoData = desedeEncrypt(getBytesUtf8(data), getBytesUtf8(key));
        return base64Encode(cryptoData);
    }

    /**
     * 3DES解密数据
     * 
     * @param data 待解密数据
     * @param key 密钥
     * @return 解密后的数据
     * */
    public static String desedeDecrypt(String data, String key) throws Exception {
    	byte[] rawData = desedeDecrypt(base64Decode(data), getBytesUtf8(key));
    	return newStringUtf8(rawData);
    }

    //----------------DES-----------------

    /**
     * DES加密数据
     * 
     * @param data 待加密数据
     * @param key 密钥
     * @return byte[] 加密后的数据
     * */
    public static byte[] desEncrypt(byte[] data, byte[] key) throws Exception {       
        SecureRandom random = new SecureRandom();
        SecretKey securekey = SecretKeyFactory.getInstance(DES_KEY_ALGORITHM).generateSecret(new DESKeySpec(key));
        Cipher cipher = Cipher.getInstance(DES_CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, securekey, random); 
        
        return cipher.doFinal(data);
    }

    /**
     * DES解密数据
     * 
     * @param data 待解密数据
     * @param key 密钥
     * @return byte[] 解密后的数据
     * */
    public static byte[] desDecrypt(byte[] data, byte[] key) throws Exception {
    	SecureRandom random = new SecureRandom();
        SecretKey securekey = SecretKeyFactory.getInstance(DES_KEY_ALGORITHM).generateSecret(new DESKeySpec(key));
        Cipher cipher = Cipher.getInstance(DES_CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, securekey, random); 
        
        return cipher.doFinal(data);
    }
    
    /**
     * DES加密数据
     * 
     * @param data 待加密数据
     * @param key 密钥
     * @return 加密后的数据
     * */
    public static String desEncrypt(String data, String key) throws Exception {
        byte[] cryptoData = desEncrypt(getBytesUtf8(data), getBytesUtf8(key));
        return base64Encode(cryptoData);
    }

    /**
     * DES解密数据
     * 
     * @param data 待解密数据
     * @param key 密钥
     * @return 解密后的数据
     * */
    public static String desDecrypt(String data, String key) throws Exception {
    	byte[] rawData = desDecrypt(base64Decode(data), getBytesUtf8(key));
    	return newStringUtf8(rawData);
    }
    
    //----------------AES-----------------
    /**
     * AES加密数据
     * 
     * @param data 待加密数据
     * @param key 密钥
     * @return byte[] 加密后的数据
     * */
    public static byte[] aesEncrypt(byte[] data, byte[] key) throws Exception {
        //生成密钥
        KeyGenerator keygen = KeyGenerator.getInstance(AES_KEY_ALGORITHM);
        keygen.init(AES_KEY_LENGTH, new SecureRandom(key));
        SecretKey securekey = new SecretKeySpec(keygen.generateKey().getEncoded(), AES_KEY_ALGORITHM);
         
        //创建密码器
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
         
        //初始化密码器
        cipher.init(Cipher.ENCRYPT_MODE, securekey);
        
        //执行加密操作
        return cipher.doFinal(data);
    }

    /**
     * AES解密数据
     * 
     * @param data 待解密数据
     * @param key 密钥
     * @return byte[] 解密后的数据
     * */
    public static byte[] aesDecrypt(byte[] data, byte[] key) throws Exception {
        //生成密钥
        KeyGenerator keygen = KeyGenerator.getInstance(AES_KEY_ALGORITHM);
        keygen.init(AES_KEY_LENGTH, new SecureRandom(key));
        SecretKey securekey = new SecretKeySpec(keygen.generateKey().getEncoded(), AES_KEY_ALGORITHM);
         
        //创建密码器
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
         
        //初始化密码器
        cipher.init(Cipher.DECRYPT_MODE, securekey);
        
        //执行解密操作
        return cipher.doFinal(data);
    }
    
    /**
     * AES加密数据
     * 
     * @param data 待加密数据
     * @param key 密钥
     * @return 加密后的数据
     * */
    public static String aesEncrypt(String data, String key) throws Exception {
        byte[] cryptoData = aesEncrypt(getBytesUtf8(data), getBytesUtf8(key));
        return base64Encode(cryptoData);
    }

    /**
     * AES解密数据
     * 
     * @param data 待解密数据
     * @param key 密钥
     * @return 解密后的数据
     * */
    public static String aesDecrypt(String data, String key) throws Exception {
    	byte[] rawData = aesDecrypt(base64Decode(data), getBytesUtf8(key));
    	return newStringUtf8(rawData);
    }   
    
    
    
    public static void main(String[] args) throws Exception{
    	String str = "Hello, 树先生！";
    	String key = "GfgLwwdK270ADSfsw4xho8lyTho97";
    	
    	String crypto = aesEncrypt(str, key);
    	log(crypto);
    	log(aesDecrypt(crypto, key));
    }
    private static void log(Object... objs){
		if(objs == null){
			return;
		}
		StringBuilder sb = new StringBuilder();
		for(Object obj : objs){
			sb.append(String.valueOf(obj));
		}
		System.out.println(sb.toString());
	}
}