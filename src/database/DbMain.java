package database;

import java.sql.Connection;
import java.sql.DriverManager;

/////////////////////�ǵ�Ҫ����sql���ݿⱸ���ļ�

public class DbMain {
	private static final String driver = "com.mysql.jdbc.Driver"; // ����
	private static final String url = "jdbc:mysql://localhost:3306/kjb_sign";
	private static final String name = "root";
	private static final String password = "root";// 123456
	private static Connection con = null;

	/** ��ȡ���ݿ����� */
	public static Connection getConnect() {
		if (con == null)
			try {// �����������ݿ������
				Class.forName(driver); // ������������
				con = DriverManager.getConnection(url, name, password); // �������ݿ�
			} catch (Exception e) {
				System.out.println("--���ݿ����Ӵ���--");
			}
		return con;
	}

}
