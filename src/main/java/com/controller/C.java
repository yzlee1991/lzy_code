package com.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lzy.H;

@RequestMapping("/c")
@Controller
public class C {
	
	@Autowired
	H h;
	
	@RequestMapping()
	public void t(HttpServletResponse response) throws IOException{
		System.out.println("------------------>>>accpet");
		PrintWriter pw=response.getWriter();
		pw.write("success change !!!!!!!!!!!!!!");
		pw.close();
	}
	
	@RequestMapping(params="method=t1")
	public ModelAndView t1(String cookie){
		System.out.println(h);
		
		System.out.println("--->>>>"+cookie);
		return new ModelAndView("/MyJsp");
	}

}
