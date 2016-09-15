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
	public String stuSign(String stu_id, String stu, String tea, String tea2,
			String time_str) {
		int time = 0;// ʱ��ת��
		try {
			time = Integer.parseInt(time_str);
		} catch (Exception e) {
			return "-2";
		}
		Statement stmt = null;
		ResultSet res = null;
		String sql = "";
		// ����time�ж�ǩ��ʱ���Ƿ���ʣ��˴���APP�����ƣ�
		// ʹ�þ����жϴ���λ����Ϣ������ֶ����и�����λxy���˴���APP�˴�����Ȼ����ȫ����������ѹ��С��
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
			sql = "insert into state_s values('" + stu_id + "','" + stu + "','"
					+ tea + "'," + time + ")";
			int num = stmt.executeUpdate(sql);
			if (tea2 != null && !tea2.equals("")) {// ���ſγ�
				sql = "insert into state_s values('" + stu_id + "','" + stu
						+ "','" + tea + "'," + time + ")";
				num += stmt.executeUpdate(sql);
			}
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
		String cls_name = "";
		try { // ���ڿɿ��Ƿ���ǩ����ѧ��������////
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
			if (res.next()) {
				sign_all++;
				cls_name = res.getString("class");
			}
			while (res.next()) {
				sign_all++;
			}
			return "{'name':'" + cls_name + "','all':" + sign_all + ",'curr':"
					+ sign_cur + "}";
		} catch (Exception ex) {// �������ˣ��Ͳ���close��
			return "-2";
		}
	}

	/** ��ȡλ����Ϣ��str-succ,-1ʧ�ܣ����������� */
	public String getPositionInfo() {
		Statement stmt = null;
		ResultSet res = null;
		String sql = "";
		String result = "";
		try {
			stmt = con.createStatement();
			sql = "select * from final_place";
			res = stmt.executeQuery(sql);
			while (res.next()) {
				String place = res.getString("place");
				double x = res.getDouble("x");
				double y = res.getDouble("y");
				if (!"".equals(""))
					result += ",";
				result += "{\"place\":\"" + place + "\",\"x\":" + x + ",\"y\":"
						+ y + "}";
			}
			result = "[" + result + "]";
			stmt.close();
			res.close();
		} catch (Exception ex) {
			System.out.println("��¼�쳣");
			result = "-2";
		}
		return result;
	}

}
