package com.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * author :lzy
 * date   :2017年12月22日下午4:36:34
 */

public class Mysql {

	static Connection conn = null;
	static Statement st = null;
	
	public static boolean execSQL(String sql){
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://192.168.92.59:3306/lzy?useSSL=false","root","123456");
			st=conn.createStatement();
			return st.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(conn!=null){
					conn.close();
				}
				if(st!=null){
					st.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
}
