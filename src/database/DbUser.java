package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbUser {
	private Connection con = null;

	public DbUser() {
		con = DbMain.getConnect();
	}

	public String stuLogin(String name, String pass, boolean need_class) {
		Statement stmt = null;
		ResultSet res = null;
		String sql = "";
		String result = "";
		try {
			stmt = con.createStatement();
			sql = "select * from user_t where name='" + name + "' and pass='"
					+ pass + "'";
			res = stmt.executeQuery(sql);
			if (res.next()) { // ��¼�ɹ�
				if (need_class) {
					// ////////////////�����Ƿ񷵻ؿγ̱�///////////////ע��stmt�Ƿ��ͻ
					result = "1";
				} else {
					result = "1";
				}
			} else {
				/////////////////////δע�ᴦ��//////////////////////
				result = "-1";
			}
			stmt.close();
			res.close();
		} catch (Exception ex) {
			System.out.println("��¼�쳣");
			result = "-2";
		}
		return result;
	}

	public String teaLogin(String name, String pass, boolean need_class) {
		Statement stmt = null;
		ResultSet res = null;
		String sql = "";
		String result = "";
		try {
			stmt = con.createStatement();
			sql = "select * from user_t where name='" + name + "' and pass='"
					+ pass + "'";
			res = stmt.executeQuery(sql);
			if (res.next()) { // ��¼�ɹ�
				if (need_class) {
					// ////////////////�����Ƿ񷵻ؿγ̱�///////////////ע��stmt�Ƿ��ͻ
					result = "1";
				} else {
					result = "1";
				}
			} else {
				result = "-1";
			}
			stmt.close();
			res.close();
		} catch (Exception ex) {
			System.out.println("��¼�쳣");
			result = "-2";
		}
		return result;
	}

	public String teaRegister(String name, String pass, String real) {
		Statement stmt = null;
		ResultSet res = null;
		String sql = "";
		try {// �Ƿ��˺��ظ�
			stmt = con.createStatement();
			sql = "select * from user_t where name='" + name + "' or real='"
					+ real + "'";
			res = stmt.executeQuery(sql);
			if (res.next()) {
				stmt.close();
				res.close();
				return "-1";
			}
		} catch (Exception ex) {// �������ˣ��Ͳ���close��
			return "-2";
		}
		try {// ע��
			sql = "insert into user_t values('" + name + "','" + pass + "','"
					+ real + "')";
			int num = stmt.executeUpdate(sql);
			stmt.close();
			res.close();
			if (num != 0) // �޸ĳɹ�
				return "1";
			return "-2";
		} catch (Exception e) {
			System.out.println("ע���쳣");
			return "-2";
		}
	}

}
