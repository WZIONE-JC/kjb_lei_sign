package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import utils.ClassTool;
import utils.PassTool;
import model.AnClass;

/** �û���ز��������ݿ⴦�� */
public class DbUser {
	private Connection con = null;

	public DbUser() {
		con = DbMain.getInatance().getConnect();
	}

	/** ѧ����¼��str/1-succ,-1�û������벻��,-2��¼ʧ�ܣ����������� */
	public String stuLogin(String name, String pass, boolean need_class) {
		String raw_pass = pass;
		pass = PassTool.getMD5(pass);
		Statement stmt = null;
		ResultSet res = null;
		String sql = "";
		String result = "";
		try {
			stmt = con.createStatement();
			sql = "select pass from user_s where name='" + name + "'";
			res = stmt.executeQuery(sql);
			/* ����������򻯣�Ϊ������⣬������Դ�������ͷ� */
			if (!res.next())// �û������ڣ�ע���û�������
				return stuRegister(stmt, name, raw_pass);// �������ʹ��ԭ������
			if (!pass.equals(res.getString("pass"))) // ���벻��
				return "-1";
			if (!need_class) // ����Ҫ���ؿγ̱�
				return "1";
			// �����α�����
			sql = "select real_name from user_s where name='" + name + "'";
			res = stmt.executeQuery(sql);
			if (res.next()) {// �鵽�α��򷵻ؿγ̱�
				String real = res.getString("real_name");
				sql = "select class,time,place,teacher from info_c where student='"
						+ name + "'";
				res = stmt.executeQuery(sql);
				ArrayList<AnClass> list = new ArrayList<AnClass>();
				while (res.next()) {
					String cls_name = res.getString("class");
					int time = res.getInt("time");
					String place = res.getString("place");
					String teacher = res.getString("teacher");
					if (teacher == null)
						teacher = "";// ���п���Ϊ��
					AnClass cls = new AnClass("", cls_name, place, time,
							teacher);
					list.add(cls);
				}
				result = "{\"name\":\"" + real + "\",\"table\":"
						+ ClassTool.getClassJson(list) + "}";
				System.out.println(result);// ///////////////////////////////////////////////
			}
			stmt.close();
			res.close();
		} catch (Exception ex) {
			System.out.println("��¼�쳣");
			result = "-2";
		}
		return result;
	}

	/** ��ʦ��¼��str/1-succ,-1�û������벻��,-2��¼ʧ�ܣ����������� */
	public String teaLogin(String name, String pass, boolean need_class) {
		pass = PassTool.getMD5(pass);
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
				if (need_class) {// ���ؿγ̱�
					String real = res.getString("real_name");
					sql = "select class,time,place from info_c where teacher='"
							+ real + "'";
					res = stmt.executeQuery(sql);
					ArrayList<AnClass> list = new ArrayList<AnClass>();
					while (res.next()) {
						String cls_name = res.getString("class");
						int time = res.getInt("time");
						String place = res.getString("place");
						AnClass cls = new AnClass("", cls_name, place, time, "");
						list.add(cls);
					}
					result = "{\"name\":\"" + real + "\",\"table\":"
							+ ClassTool.getClassJson(list) + "}";
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

	/** ��ʦע�ᣨstr/1-succ,-1�Ѿ�ע��,-2ע��ʧ�ܣ�����������-3ƥ��ʧ�ܣ� */
	public String teaRegister(String name, String pass, String real) {
		pass = PassTool.getMD5(pass);
		Statement stmt = null;
		ResultSet res = null;
		String sql = "";
		try {// �Ƿ��˺��ظ�
			stmt = con.createStatement();
			sql = "select * from user_t where name='" + name
					+ "' or real_name='" + real + "'";
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
			System.out.println(name + "--" + pass);
			String class_raw = ClassTool.getRawClassTable(name, pass);
			if (class_raw == null || "".equals(class_raw))// ��¼ʧ��
				return "-1";
			ArrayList<AnClass> list = ClassTool.getClassTable(class_raw);
			String real = ClassTool.getRealName(name, class_raw);
			if (list == null || list.size() == 0)
				return "-1";
			// д��ע����Ϣ�����ݿ�
			String sql = "insert into user_s values('" + name + "','"
					+ PassTool.getMD5(pass) + "','" + real + "')";
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
			// �˴����Թر���������
			stmt.close();
			return "1";
		} catch (Exception e) {
			return "-2";
		}
	}

}
