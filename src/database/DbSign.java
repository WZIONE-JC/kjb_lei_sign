package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/** ǩ����鿴�����ݿ⴦�� */
public class DbSign {
	private Connection con = null;

	public DbSign() {
		con = DbMain.getInatance().getConnect();
	}

	/**
	 * ѧ��ǩ��
	 */
	public String stuSign(String stu_id, String stu, String tea, String time_str) {
		int time = 0;// ʱ��ת��
		try {
			time = Integer.parseInt(time_str);
		} catch (Exception e) {
			return "-2";
		}
		Statement stmt = null;
		ResultSet res = null;
		String sql = "";
		try {// �Ƿ��Ѿ�ǩ��
			stmt = con.createStatement();
			sql = "select * from state_s where stu_id='" + stu_id
					+ "' and time=" + time + "";
			res = stmt.executeQuery(sql);
			if (res.next()) {
				stmt.close();
				res.close();
				return "2";
			}
		} catch (Exception ex) {// �������ˣ��Ͳ���close��
			return "-2";
		}
		try {// ǩ��
				// ////////////////��Ҫ����time�ж�ǩ��ʱ���Ƿ���ʣ��˴���APP�����ƣ�
			sql = "insert into state_s values('" + stu_id + "','" + stu + "','"
					+ tea + "'," + time + ")";
			int num = stmt.executeUpdate(sql);
			stmt.close();
			res.close();
			if (num != 0) // �޸ĳɹ�
				return "1";
			return "-2";
		} catch (Exception e) {
			System.out.println("ǩ���쳣");
			return "-2";
		}
	}

	/**
	 * ��ʦ�鿴ǩ��
	 */
	public String teaSeeSign(String tea, String time_str) {
		int time = 0;// ʱ��ת��
		try {
			time = Integer.parseInt(time_str);
		} catch (Exception e) {
			return "-2";
		}
		Statement stmt = null;
		ResultSet res = null;
		String sql = "";
		int sign_cur = 0, sign_all = 0;
		try { // /////////////////////////���ڿɿ��Ƿ���ǩ����ѧ��������
			stmt = con.createStatement();
			sql = "select * from state_s where teacher='" + tea + "' and time="
					+ time + "";
			res = stmt.executeQuery(sql);
			while (res.next()) {
				// // /////////////������ȡ��׼���ã���װjson���鷵�ؼ���
				// String sign_name = res.getString("student");
				sign_cur++;
			}
			sql = "select * from info_c where teacher='" + tea + "' and time="
					+ time + "";
			res = stmt.executeQuery(sql);
			while (res.next()) {
				sign_all++;
			}
			return "{'all':" + sign_all + ",'curr':" + sign_cur + "}";
		} catch (Exception ex) {// �������ˣ��Ͳ���close��
			return "-2";
		}
	}

}
