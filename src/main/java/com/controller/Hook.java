package com.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/hook")
@Controller
public class Hook {
	
	Logger log=Logger.getLogger(Hook.class);
	
	@RequestMapping()
	public void hookLzy(HttpServletResponse response) throws Exception{
		
		Process process=Runtime.getRuntime().exec(new String[]{"sh","/root/git.sh"});
		BufferedReader br=new BufferedReader(new InputStreamReader(process.getInputStream()));
		String msg="";
		String line="";
		while((line=br.readLine())!=null){
			msg+=line;
		}
		
		process.waitFor();
		int exitCode=process.exitValue();
		process.destroy();
		
		
		PrintWriter pw=response.getWriter();
		pw.write("success !!!!!!!!!!!!!!    exitCode:::"+exitCode+"  msg:"+msg);
		pw.close();
		
		log.info("success !!!!!!!!!!!!!!    exitCode:::"+exitCode);
		
		
	}
	
}
