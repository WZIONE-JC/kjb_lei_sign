package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbMain {
	private String driver = "com.mysql.jdbc.Driver"; // 驱动
	private String url = "jdbc:mysql://localhost:3306/library";
	private String name = "root";
	private String password = "root";// 123456
	private Connection con = null;//暂不考虑静态
	private Statement stmt = null;
	private ResultSet res = null;

	/** 构造器：建立数据连接 */
	public DbMain() {
		try {// 建立连接数据库的连接
			Class.forName(driver); // 加载驱动程序
			con = DriverManager.getConnection(url, name, password); // 连接数据库
			stmt = con.createStatement();
		} catch (Exception e) {
			System.out.println("--数据库连接错误--");
		}
	}

	
}
