package com.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lzy.H;

@RequestMapping("/index")
@Controller
public class C {
	
	@Autowired
	H h;
	
	@RequestMapping()
	public ModelAndView index() throws IOException{
		return new ModelAndView("/index");
	}
	
	@RequestMapping(params="method=t1")
	public ModelAndView t1(String cookie){
		System.out.println(h);
		
		System.out.println("--->>>>"+cookie);
		return new ModelAndView("/MyJsp");
	}

}
