package database;

import java.sql.Connection;
import java.sql.DriverManager;

/** ���ݿ������࣬������ȷ��Ψһ���� */
public class DbMain {
	private final String driver = "com.mysql.jdbc.Driver"; // ����
	private final String url = "jdbc:mysql://localhost:3306/kjb_sign";
	private final String name = "root";
	private final String password = "root";// 123456
	private Connection con = null;
	private static DbMain db = null;

	private DbMain() {
		try {// �����������ݿ������
			Class.forName(driver); // ������������
			con = DriverManager.getConnection(url, name, password); // �������ݿ�
		} catch (Exception e) {
			System.out.println("--���ݿ����Ӵ���--");
		}
	}

	/** ��ȡ����ʵ�� */
	public static DbMain getInatance() {
		if (db == null)
			db = new DbMain();
		return db;
	}

	/** ��ȡ���ݿ����� */
	public Connection getConnect() {
		return con;
	}

}
