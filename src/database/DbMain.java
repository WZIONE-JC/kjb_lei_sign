package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbMain {
	private String driver = "com.mysql.jdbc.Driver"; // ����
	private String url = "jdbc:mysql://localhost:3306/library";
	private String name = "root";
	private String password = "root";// 123456
	private Connection con = null;//�ݲ����Ǿ�̬
	private Statement stmt = null;
	private ResultSet res = null;

	/** �������������������� */
	public DbMain() {
		try {// �����������ݿ������
			Class.forName(driver); // ������������
			con = DriverManager.getConnection(url, name, password); // �������ݿ�
			stmt = con.createStatement();
		} catch (Exception e) {
			System.out.println("--���ݿ����Ӵ���--");
		}
	}

	
}
