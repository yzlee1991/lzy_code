package com.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.utils.Mysql;

/**
 * author :lzy
 * date   :2017年12月22日上午9:40:26
 */

@RequestMapping("/games")
@Controller
public class Games {
	
	Logger log=Logger.getLogger(Games.class);

	@RequestMapping(params="gobang")
	public String gobang(){
		return "gobang";
	}
	
	@RequestMapping(params="gobangdata")
	public String gobangData(String data,String winner){
		System.out.println(data);
		String sql="insert into gobang(jsondata,winner) values('"+data+"','"+Integer.parseInt(winner)+"')";
		Mysql.execSQL(sql);
		return "ok";
	}
	
}
