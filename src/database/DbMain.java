package database;

import java.sql.Connection;
import java.sql.DriverManager;

/////////////////////记得要更新sql数据库备份文件

public class DbMain {
	private static final String driver = "com.mysql.jdbc.Driver"; // 驱动
	private static final String url = "jdbc:mysql://localhost:3306/kjb_sign";
	private static final String name = "root";
	private static final String password = "root";// 123456
	private static Connection con = null;

	/** 获取数据库连接 */
	public static Connection getConnect() {
		if (con == null)
			try {// 建立连接数据库的连接
				Class.forName(driver); // 加载驱动程序
				con = DriverManager.getConnection(url, name, password); // 连接数据库
			} catch (Exception e) {
				System.out.println("--数据库连接错误--");
			}
		return con;
	}

}
