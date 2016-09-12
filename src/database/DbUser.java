package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import utils.ClassTool;
import model.AnClass;

public class DbUser {
	private Connection con = null;

	public DbUser() {
		con = DbMain.getConnect();
	}

	/** ѧ����¼ */
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
			} else {// δע�ᴦ��
				result = stuRegister(stmt, name, pass);
			}
			stmt.close();
			res.close();
		} catch (Exception ex) {
			System.out.println("��¼�쳣");
			result = "-2";
		}
		return result;
	}

	/** ��ʦ��¼ */
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

	/** ��ʦע�� */
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

	/**
	 * ѧ��ע�ᣬ1�ɹ���-1ʧ�ܡ�
	 */
	private String stuRegister(Statement stmt, String name, String pass) {
		try {
			// ģ���½����ȡ�α�
			String class_raw = ClassTool.getRawClassTable(name, pass);
			if (class_raw == null || "".equals(class_raw))
				return "-1";
			ArrayList<AnClass> list = ClassTool.getClassTable(class_raw);
			String real = ClassTool.getRealName(name, class_raw);
			if (list == null || list.size() == 0)
				return "-1";
			// д��ע����Ϣ�����ݿ�
			String sql = "insert into user_s values('" + name + "','" + pass
					+ "','" + real + "','" + ClassTool.getClassJson(list)
					+ "')";
			stmt.executeUpdate(sql);
			// д��γ���Ϣ�����ݿ�
			for (int i = 0; i < list.size(); i++) {
				AnClass cls = list.get(i);
				sql = "insert into info_c values('" + name + "','','"
						+ cls.getName() + "','" + cls.getTime() + "','"
						+ cls.getPlace() + "','" + cls.getId() + "')";
				stmt.executeUpdate(sql);
			}
			// �����ʦ��Ϣ���γ���Ϣ��/*������̫������*/
			sql = "update info_c set teacher="
					+ "(select teacher from final_class "
					+ "where class_id=id)where student=" + name;
			stmt.executeUpdate(sql);
			return "1";
		} catch (Exception e) {
			return "-1";
		}
	}

}
