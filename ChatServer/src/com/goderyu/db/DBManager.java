package com.goderyu.db;
/**
* @author 于好贤
* @version 创建时间：2017年11月8日 下午10:41:29
* @description 用于数据库连接配置
*/

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBManager {

	public static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
	public static final String USERNAME = "root";
	public static final String PASSWORD = "123456";
	public static final String URL = "jdbc:mysql://localhost:3306/DB_Chat?characterEncoding=utf8&useSSL=false";
	public static DataSource dataSource = null;

	// 准备加载数据源 C3P0
	static {
		try {
			ComboPooledDataSource pool = new ComboPooledDataSource();
			pool.setDriverClass(DRIVER_NAME);
			pool.setUser(USERNAME);
			pool.setPassword(PASSWORD);
			pool.setJdbcUrl(URL);
			pool.setInitialPoolSize(6);
			pool.setMaxPoolSize(30);
			pool.setMinPoolSize(5);
			dataSource = pool;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("数据库连接池加载失败!");
		}
	}

	// 通过此方法获得Connection连接对象
	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

}
