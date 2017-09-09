
package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * author :lzy
 * date   :2017��9��8������3:32:19
 */

@RequestMapping("/auth")
@Controller
public class Auth {

	static String appKey="2043795053";
	static String appSecret="bf79302f37511c8158d2ea2872c4b502";
	static String callBackURL="http://crazydota.51vip.biz/auth?callback";
	static String baseResourceURL="https://api.weibo.com/2/";
	
	
	final static String METHOD_GET="GET";
	final static String METHOD_POST="POST";
	
	String accessToken;
	String expiresIn;
	String uid;
	
	Gson gson=new Gson();
	
	@RequestMapping()
	public String auth(){
		return "auth";
	}
	
	@RequestMapping(params="callback")
	public void getGrant(String code,HttpServletResponse response){
		try{
			if(code!=null&&!code.isEmpty()){
				HttpClient client = new HttpClient(); 
				HttpMethod method=new PostMethod("https://api.weibo.com/oauth2/access_token");
				NameValuePair[] nvps=new NameValuePair[]{
					new NameValuePair("client_id", appKey),
					new NameValuePair("client_secret", appSecret),
					new NameValuePair("grant_type", "authorization_code"),
					new NameValuePair("redirect_uri", callBackURL),
					new NameValuePair("code", code),
				};
				method.setQueryString(nvps);
				client.executeMethod(method);
				
				//解析返回的数据获取token
				String json=method.getResponseBodyAsString();
				Map<String,String> map=gson.fromJson(json, new TypeToken<HashMap<String,String>>(){}.getType());
				
				accessToken=map.get("access_token");
				expiresIn=map.get("expires_in");
				uid=map.get("uid");
				
				//授权成功，进行相关数据操作或返回主页
				
				PrintWriter pw=response.getWriter();
				pw.write(write());
				pw.close();
				
			}else{
				//授权失败
			}
		}catch(IOException e){
			//授权失败
		}
		
	}
	
	
	public void getCode(String code,HttpServletResponse response) throws IOException{
		
		PrintWriter pw=response.getWriter();
		
		if(code!=null&&!code.isEmpty()){
			
			
			HttpClient client = new HttpClient(); 
			HttpMethod method=new PostMethod("https://api.weibo.com/oauth2/access_token");
			
			/*HttpMethodParams params=new HttpMethodParams();
			params.setParameter("client_id", appKey);
			params.setParameter("client_secret", appSecret);
			params.setParameter("grant_type", "authorization_code");
			params.setParameter("redirect_uri", callBackURL);
			params.setParameter("code", code);
			method.setParams(params);*/
			
			NameValuePair[] nvps=new NameValuePair[]{
				new NameValuePair("client_id", appKey),
				new NameValuePair("client_secret", appSecret),
				new NameValuePair("grant_type", "authorization_code"),
				new NameValuePair("redirect_uri", callBackURL),
				new NameValuePair("code", code),
			};
			method.setQueryString(nvps);
			
			
			client.executeMethod(method);
			
			String json=method.getResponseBodyAsString();
			method.releaseConnection();
			
			Map<String,String> map=gson.fromJson(json, new TypeToken<HashMap<String,String>>(){}.getType());
			
			accessToken=map.get("access_token");
			expiresIn=map.get("expires_in");
			uid=map.get("uid");
			
			
			Map<String,String> pam=new HashMap<String, String>();
			pam.put("access_token", accessToken);
			pam.put("uids", uid);
			String jn2=getAuthService("users/counts.json",pam,METHOD_GET);
			pw.write(jn2);
			
			/*StringBuilder sb=new StringBuilder();
			PrintWriter pw=response.getWriter();
			sb.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
			sb.append("<html>");
			sb.append("<head>");
//			sb.append("<script type=\"text/javascript\">window.onload=function(){window.location.href='http://crazydota.51vip.biz/auth?getCode'}</script>");
			sb.append("<script type=\"text/javascript\">window.onload=function(){document.getElementById('auth_table').submit();}</script>");
			sb.append("</head>");
			sb.append("<body>");
			
			//from
			sb.append("<form id=\"auth_table\" action=\"https://api.weibo.com/oauth2/access_token\" method=\"post\">");
			sb.append("<input type=\"text\" name=\"client_id\" value=\"2043795053\"/>");
			sb.append("<input type=\"text\" name=\"client_secret\" value=\"bf79302f37511c8158d2ea2872c4b502\"/>");
			sb.append("<input type=\"text\" name=\"grant_type\" value=\"authorization_code\"/>");
			sb.append("<input type=\"text\" name=\"redirect_uri\" value=\""+callBackURL+"\"/>");
			sb.append("<input type=\"text\" name=\"code\" value=\""+code+"\"/>");
			sb.append("</form>");
			
			sb.append("</body>");
			sb.append(System.lineSeparator());
			sb.append("</html>");
			pw.write(sb.toString());
			pw.close();*/
		}else{
			//授权失败
			/*Enumeration<String> e=request.getParameterNames();
			while(e.hasMoreElements()){
				PrintWriter pw=response.getWriter();
				String name=e.nextElement();
				pw.write(name+"---"+request.getParameter(name));
				pw.close();
			}*/
		}
		pw.close();
	}
	
	
	
	private String getAuthService(String uri,Map<String,String> parames,String methodType){
		try{
			HttpClient client = new HttpClient();
			HttpMethod method;
			
			if(METHOD_POST.equals(methodType)){
				method=new PostMethod(baseResourceURL+uri);
			}else{
				method=new GetMethod(baseResourceURL+uri);
			}
			
			List<NameValuePair> list=new ArrayList<NameValuePair>();
			for(String key:parames.keySet()){
				list.add(new NameValuePair(key, parames.get(key)));
			}
			method.setQueryString(list.toArray(new NameValuePair[]{}));
			client.executeMethod(method);
			
			int stateCode=method.getStatusCode();
			if(method.getStatusCode()!=200){
				// 获取服务失败
			}
			
			String json=method.getResponseBodyAsString();
			method.releaseConnection();
			return json;
			
		}catch(HttpException e){
			// 获取服务失败
		}catch (IOException e) {
			// 获取服务失败
		}
		
		return "";
	}
	
	private String write() throws HttpException, IOException{
		Map<String,String> paramesMap=new HashMap<String, String>();
		paramesMap.put("access_token", accessToken);
		paramesMap.put("uid", uid);
		return getAuthService("users/show.json",paramesMap,METHOD_GET);
	}
	
}
