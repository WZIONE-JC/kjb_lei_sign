package database;

import java.sql.Connection;
import java.sql.DriverManager;

/** 数据库连接类，单例类确保唯一连接 */
public class DbMain {
	private final String driver = "com.mysql.jdbc.Driver"; // 驱动//注：&amp;=&
	private final String url = "jdbc:mysql://localhost:3306/kjb_sign?autoReconnect=true&useUnicode=true&characterEncoding=gbk";// 这行代码我找了5个多小时…………
	private final String name = "root";
	private final String password = "root";// 123456
	private Connection con = null;
	private static DbMain db = null;

	private DbMain() {
		try {// 建立连接数据库的连接
			Class.forName(driver); // 加载驱动程序
			con = DriverManager.getConnection(url, name, password); // 连接数据库
		} catch (Exception e) {
			System.out.println("--数据库连接错误--");
		}
	}

	/** 获取此类实例 */
	public static DbMain getInatance() {
		if (db == null)
			db = new DbMain();
		return db;
	}

	/** 获取数据库连接 */
	public Connection getConnect() {
		return con;
	}

}
