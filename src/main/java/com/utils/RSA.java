package com.utils;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;

public class RSA {
	
	/** 
     * RSA最大加密明文大小  
     */    
    private static final int MAX_ENCRYPT_BLOCK = 117;
	
    
    public static final int PADDING=0;
    
    public static final int NOPADDING=1;

	 //模  
	public BigInteger modulus;
    //公钥指数  
	public BigInteger public_exponent;
    //私钥指数  
	public BigInteger private_exponent;
    //公钥
	public RSAPublicKey publicKey;
    //私钥
	public RSAPrivateKey privateKey;
    
    
    @SuppressWarnings("unused")
	@Autowired
	public RSA(int mode) throws NoSuchAlgorithmException{
    	KeyPairGenerator keyPairGen = null;
    	if(mode==PADDING){//获取以padding模式生成的rsa密钥对，java默认模式
    		keyPairGen = KeyPairGenerator.getInstance("RSA");
    	}else if(mode==NOPADDING){//获取以nopadding模式生成的rsa密钥对，非java默认模式，需第三方包支持
    		keyPairGen = KeyPairGenerator.getInstance("RSA",new BouncyCastleProvider());
    	}
    	//初始化密钥长度,单位 bit
    	keyPairGen.initialize(1024);
    	//获取公私钥
		KeyPair keyPair = keyPairGen.generateKeyPair();
		publicKey = (RSAPublicKey) keyPair.getPublic();  
        privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //模  
        modulus = publicKey.getModulus();
        //公钥指数  
        public_exponent = publicKey.getPublicExponent();
        //私钥指数  
        private_exponent = privateKey.getPrivateExponent();
        
        if(keyPairGen==null)
			throw new RuntimeException("the mode isn't exist,please check your mode!");
    }
    
    
    //nopadding模式的私钥解密
    public static String decryptByNoPadding(String data, RSAPrivateKey privateKey)  
            throws Exception {  
        Cipher cipher = Cipher.getInstance("RSA",new BouncyCastleProvider());  
        cipher.init(Cipher.DECRYPT_MODE, privateKey);  
        StringBuilder sb = new StringBuilder(new String(cipher.doFinal(Hex
                .decode(data))));
        return sb.reverse().toString();
    }
    
    
    /** 
     * 用公钥加密 <br> 
     * 每次加密的字节数，不能超过密钥的长度值减去11 
     * 
     * @param data 
     *            需加密数据的byte数据 
     * @param publicKey 公钥 
     * @return 加密后的byte型数据 
     */  
    public static byte[] encryptData(byte[] data, PublicKey publicKey)  
    {  
          
        try  
        {  
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");  
            // 编码前设定编码方式及密钥  
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
            // 传入编码数据并返回编码结果  
            int inputLen = data.length;    
            ByteArrayOutputStream out = new ByteArrayOutputStream();    
            int offSet = 0;    
            byte[] cache;    
            int i = 0;    
            // 对数据分段加密    
            while (inputLen - offSet > 0) {    
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {    
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);    
                } else {    
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);    
                }    
                out.write(cache, 0, cache.length);    
                i++;    
                offSet = i * MAX_ENCRYPT_BLOCK;    
            }    
            byte[] encryptedData = out.toByteArray();    
            out.close();    
            return encryptedData;    
        } catch (Exception e)  
        {  
            e.printStackTrace();  
            return null;  
        }  
    }  
    
    /** 
     * 用私钥解密 
     * 
     * @param encryptedData 
     *            经过encryptedData()加密返回的byte数据 
     * @param privateKey 
     *            私钥 
     * @return 
     */  
    public static byte[] decryptData(byte[] encryptedData, PrivateKey privateKey)  
    {  
        try  
        {  
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");  
            cipher.init(Cipher.DECRYPT_MODE, privateKey);  
            return cipher.doFinal(encryptedData);  
        } catch (Exception e)  
        {     
            e.printStackTrace();  
            return null;  
        }  
    }  
    
    
    public static void main(String[] args) throws Exception{
    	RSA rsa=new RSA(RSA.PADDING);
    	String s="12sss3";
    	String ss=new String(rsa.encryptData(s.getBytes(), rsa.publicKey));
    	ss=new String(rsa.decryptData(ss.getBytes(), rsa.privateKey));
    	System.out.println(s);
    	
    	
	}
}
