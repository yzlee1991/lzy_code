package com.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

public class M {

	public static void main(String[] args) throws HttpException, IOException {
		List<String> list=new ArrayList<String>();
		list.add("1111");
		list.add("2222");
		String[] s= list.toArray(new String[]{});
		for(String ss:s){
			System.out.println(ss);
		}
		
		list.toArray();
		
		/*String[] s=new String[]{};
		Object[] o=(Object[])s;
		
		String[] ss=(String[]) o;*/
	}
	
}
