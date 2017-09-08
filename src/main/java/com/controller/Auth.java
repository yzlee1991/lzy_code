
package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * author :lzy
 * date   :2017年9月8日下午3:32:19
 */

@RequestMapping("/auth")
@Controller
public class Auth {

	static String appKey="2043795053";
	static String appSecret="bf79302f37511c8158d2ea2872c4b502";
	static String callBackURL="http://crazydota.51vip.biz/auth?callback";
	
	@RequestMapping()
	public String auth(){
		return "auth";
	}
	
	@RequestMapping(params="callback")
	public void getCode(String code,HttpServletResponse response,HttpServletRequest request) throws IOException{
		if(code!=null&&!code.isEmpty()){
			String url="https://api.weibo.com/oauth2/access_token?client_id=2043795053&client_secret=bf79302f37511c8158d2ea2872c4b502&grant_type=authorization_code&redirect_uri="+callBackURL+"&code="+code;
			
			StringBuilder sb=new StringBuilder();
			PrintWriter pw=response.getWriter();
			sb.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
			sb.append(System.lineSeparator());
			sb.append("<html>");
			sb.append(System.lineSeparator());
			sb.append("<head>");
			sb.append(System.lineSeparator());
			sb.append("<script type=\"text/javascript\">window.onload=function(){window.location.href='http://crazydota.51vip.biz/auth?getCode'}</script>");
			sb.append(System.lineSeparator());
			sb.append("</head>");
			sb.append(System.lineSeparator());
			sb.append("<body>");
			sb.append(System.lineSeparator());
			sb.append("</body>");
			sb.append(System.lineSeparator());
			sb.append("</html>");
			pw.write(sb.toString());
			pw.close();
	//		return new ModelAndView(viewName, model);
		}else{
			Enumeration<String> e=request.getParameterNames();
			while(e.hasMoreElements()){
				PrintWriter pw=response.getWriter();
				String name=e.nextElement();
				pw.write(name+"---"+request.getParameter(name));
				pw.close();
			}
		}
	}
	
}
