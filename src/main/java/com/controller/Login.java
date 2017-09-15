package com.controller;

import java.io.PrintWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;

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
	
	@RequestMapping("/passkey")
	public void passKey(HttpServletRequest request,HttpServletResponse response) throws Exception{
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		//初始化密钥长度,单位 bit
		keyPairGen.initialize(1024);
		
		//获取公私钥
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //模  
        String modulus = publicKey.getModulus().toString();  
        //公钥指数  
        String public_exponent = publicKey.getPublicExponent().toString();
        //私钥指数  
        String private_exponent = privateKey.getPrivateExponent().toString();
        
        //session设置本次会话的私钥
        HttpSession session=request.getSession();
        session.setAttribute("modulus", modulus);
        session.setAttribute("private_exponent", private_exponent);
        
        //返回客户端公钥
        Map<String,String> map=new HashMap<String, String>();
        map.put("modulus", modulus);
        map.put("public_exponent", public_exponent);
        
        String json=gson.toJson(map);
        PrintWriter pw=response.getWriter();
        pw.write(json);
        pw.flush();
        pw.close();
        return;
        
        /*//使用模和指数生成公钥和私钥  
        RSAPublicKey pubKey = getPublicKey(modulus, public_exponent);  
        RSAPrivateKey priKey = getPrivateKey(modulus, private_exponent); */
		
	}
	
}
