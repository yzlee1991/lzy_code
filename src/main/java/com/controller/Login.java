package com.controller;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.utils.RSA;

/**
 * author :lzy
 * date   :2017年9月15日下午2:24:20
 */

@Controller
@RequestMapping("/login")
public class Login {
	
	static Gson gson=new Gson();
	
	@RequestMapping()
	public String login(){
		return "login";
	}
	
	@RequestMapping("/auth")
	public void auth(String userName,String passWord,HttpSession session,HttpServletResponse response) throws Exception{
		String modulus=(String) session.getAttribute("modulus");
		String private_exponent=(String) session.getAttribute("private_exponent");
		RSAPrivateKey privateKey=getPrivateKey(modulus, private_exponent);
		
//		RSAPrivateKey privateKey=(RSAPrivateKey) session.getAttribute("privateKey");
//		System.out.println(passWord);
		passWord=RSA.decryptByNoPadding(passWord, privateKey);
		System.out.println(passWord);
		
		String json=gson.toJson("YY");
        PrintWriter pw=response.getWriter();
        pw.write(json);
        pw.flush();
        pw.close();
		
	}
	
	/*
	 * 因为js加密时使用的是nopadding模式（每次加密结果一样），而java中默认是padding15，每次加密结果不一样，所以要依赖第三方包来解密
	 */
	@RequestMapping("/passkey")
	public void passKey(HttpSession session,HttpServletResponse response) throws Exception{
		
		RSA rsa=new RSA(RSA.NOPADDING);
		//session设置本次会话的私钥
        session.setAttribute("modulus", rsa.modulus.toString());
        session.setAttribute("private_exponent", rsa.private_exponent.toString());
		//返回客户端公钥
        Map<String,String> map=new HashMap<String, String>();
        map.put("modulus", rsa.modulus.toString(16)); //js需要转成16进制的字符串
        map.put("public_exponent", rsa.public_exponent.toString(16));//js需要转成16进制的字符串
        String json=gson.toJson(map);
        PrintWriter pw=response.getWriter();
        pw.write(json);
        pw.flush();
        pw.close();
        
		/*KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA",new org.bouncycastle.jce.provider.BouncyCastleProvider());
		//初始化密钥长度,单位 bit
		keyPairGen.initialize(1024);
		
		//获取公私钥
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //模  
        String modulus = publicKey.getModulus().toString();  //js需要转成16进制的字符串
        //公钥指数  
        String public_exponent = publicKey.getPublicExponent().toString(16);	//js需要转成16进制的字符串
        //私钥指数  
        String private_exponent = privateKey.getPrivateExponent().toString();
        
        //session设置本次会话的私钥
        HttpSession session=request.getSession();
        session.setAttribute("modulus", modulus);
        session.setAttribute("private_exponent", private_exponent);
        session.setAttribute("privateKey", privateKey);
        
        //返回客户端公钥
        Map<String,String> map=new HashMap<String, String>();
        map.put("modulus", publicKey.getModulus().toString(16));
//        map.put("modulus", Base64.encode(publicKey.getEncoded()));
        map.put("public_exponent", public_exponent);
        
        String json=gson.toJson(map);
        PrintWriter pw=response.getWriter();
        pw.write(json);
        pw.flush();
        pw.close();
        
        String s=encryptByPublicKey("12345", publicKey);
        System.out.println("s1 "+s);
        s=encryptByPublicKey("12345", publicKey);
        System.out.println("s1 "+s);
        System.out.println(decryptByPrivateKey(s,privateKey));
        
        
        return;*/
        
	}
	
	/** 
     * 公钥加密 
     *  
     * @param data 
     * @param publicKey 
     * @return 
     * @throws Exception 
     */  
    public static String encryptByPublicKey(String data, RSAPublicKey publicKey)  
            throws Exception {  
        Cipher cipher = Cipher.getInstance("RSA",new org.bouncycastle.jce.provider.BouncyCastleProvider());  
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
        // 模长  
        int key_len = publicKey.getModulus().bitLength() / 8;  
        // 加密数据长度 <= 模长-11  
        String[] datas = splitString(data, key_len - 11);  
        String mi = "";  
        //如果明文长度大于模长-11则要分组加密  
        for (String s : datas) {  
            mi += bcd2Str(cipher.doFinal(s.getBytes()));  
        }  
        return mi;  
    }  
    
    /** 
     * 拆分字符串 
     */  
    public static String[] splitString(String string, int len) {  
        int x = string.length() / len;  
        int y = string.length() % len;  
        int z = 0;  
        if (y != 0) {  
            z = 1;  
        }  
        String[] strings = new String[x + z];  
        String str = "";  
        for (int i=0; i<x+z; i++) {  
            if (i==x+z-1 && y!=0) {  
                str = string.substring(i*len, i*len+y);  
            }else{  
                str = string.substring(i*len, i*len+len);  
            }  
            strings[i] = str;  
        }  
        return strings;  
    }  
	
    /** 
     * BCD转字符串 
     */  
    public static String bcd2Str(byte[] bytes) {  
        char temp[] = new char[bytes.length * 2], val;  
  
        for (int i = 0; i < bytes.length; i++) {  
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);  
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');  
  
            val = (char) (bytes[i] & 0x0f);  
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');  
        }  
        return new String(temp);  
    }  
    
    /** 
     * 使用模和指数生成RSA公钥 
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA 
     * /None/NoPadding】 
     *  
     * @param modulus 
     *            模 
     * @param exponent 
     *            指数 
     * @return 
     */  
    public static RSAPublicKey getPublicKey(String modulus, String exponent) {  
        try {  
            BigInteger b1 = new BigInteger(modulus);  
            BigInteger b2 = new BigInteger(exponent);  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);  
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
    
    
    /** 
     * 私钥解密 
     *  
     * @param data 
     * @param privateKey 
     * @return 
     * @throws Exception 
     */  
    /*public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey)  
            throws Exception {  
        Cipher cipher = Cipher.getInstance("RSA",new org.bouncycastle.jce.provider.BouncyCastleProvider());  
        cipher.init(Cipher.DECRYPT_MODE, privateKey);  
        //模长  
        int key_len = privateKey.getModulus().bitLength() / 8;  
        byte[] bytes = data.getBytes();  
        byte[] bcd = ASCII_To_BCD(bytes, bytes.length);  
//        System.err.println(bcd.length);  
        //如果密文长度大于模长则要分组解密  
        String ming = "";  
        byte[][] arrays = splitArray(bcd, key_len);  
        for(byte[] arr : arrays){  
            ming += new String(cipher.doFinal(arr));  
        }  
        return ming;  
    }  */
    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey)  
            throws Exception {  
        Cipher cipher = Cipher.getInstance("RSA/None/NoPadding",new org.bouncycastle.jce.provider.BouncyCastleProvider());  
        cipher.init(Cipher.DECRYPT_MODE, privateKey);  
        StringBuilder sb = new StringBuilder(new String(cipher.doFinal(Hex
                .decode(data))));
        return sb.reverse().toString();
    }  
    
    
    /** 
     * ASCII码转BCD码 
     *  
     */  
    public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {  
        byte[] bcd = new byte[asc_len / 2];  
        int j = 0;  
        for (int i = 0; i < (asc_len + 1) / 2; i++) {  
            bcd[i] = asc_to_bcd(ascii[j++]);  
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));  
        }  
        return bcd;  
    }  
    public static byte asc_to_bcd(byte asc) {  
        byte bcd;  
  
        if ((asc >= '0') && (asc <= '9'))  
            bcd = (byte) (asc - '0');  
        else if ((asc >= 'A') && (asc <= 'F'))  
            bcd = (byte) (asc - 'A' + 10);  
        else if ((asc >= 'a') && (asc <= 'f'))  
            bcd = (byte) (asc - 'a' + 10);  
        else  
            bcd = (byte) (asc - 48);  
        return bcd;  
    }  
    /** 
     *拆分数组  
     */  
    public static byte[][] splitArray(byte[] data,int len){  
        int x = data.length / len;  
        int y = data.length % len;  
        int z = 0;  
        if(y!=0){  
            z = 1;  
        }  
        byte[][] arrays = new byte[x+z][];  
        byte[] arr;  
        for(int i=0; i<x+z; i++){  
            arr = new byte[len];  
            if(i==x+z-1 && y!=0){  
                System.arraycopy(data, i*len, arr, 0, y);  
            }else{  
                System.arraycopy(data, i*len, arr, 0, len);  
            }  
            arrays[i] = arr;  
        }  
        return arrays;  
    }  
    
    /** 
     * 使用模和指数生成RSA私钥 
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA 
     * /None/NoPadding】 
     *  
     * @param modulus 
     *            模 
     * @param exponent 
     *            指数 
     * @return 
     */  
    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {  
        try {  
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);  
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
}
