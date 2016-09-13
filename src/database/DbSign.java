package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/** 签到与查看，数据库处理 */
public class DbSign {
	private Connection con = null;

	public DbSign() {
		con = DbMain.getInatance().getConnect();
	}

	/**
	 * 学生签到
	 */
	public String stuSign(String stu_id, String stu, String tea, String time_str) {
		int time = 0;// 时间转换
		try {
			time = Integer.parseInt(time_str);
		} catch (Exception e) {
			return "-2";
		}
		Statement stmt = null;
		ResultSet res = null;
		String sql = "";
		try {// 是否已经签到
			stmt = con.createStatement();
			sql = "select * from state_s where stu_id='" + stu_id
					+ "' and time=" + time + "";
			res = stmt.executeQuery(sql);
			if (res.next()) {
				stmt.close();
				res.close();
				return "2";
			}
		} catch (Exception ex) {// 都报错了，就不必close了
			return "-2";
		}
		try {// 签到
				// ////////////////需要根据time判断签到时间是否合适（此处在APP端限制）
			sql = "insert into state_s values('" + stu_id + "','" + stu + "','"
					+ tea + "'," + time + ")";
			int num = stmt.executeUpdate(sql);
			stmt.close();
			res.close();
			if (num != 0) // 修改成功
				return "1";
			return "-2";
		} catch (Exception e) {
			System.out.println("签到异常");
			return "-2";
		}
	}

	/**
	 * 教师查看签到
	 */
	public String teaSeeSign(String tea, String time_str) {
		int time = 0;// 时间转换
		try {
			time = Integer.parseInt(time_str);
		} catch (Exception e) {
			return "-2";
		}
		Statement stmt = null;
		ResultSet res = null;
		String sql = "";
		int sign_cur = 0, sign_all = 0;
		try { // /////////////////////////后期可考虑返回签到的学生的姓名
			stmt = con.createStatement();
			sql = "select * from state_s where teacher='" + tea + "' and time="
					+ time + "";
			res = stmt.executeQuery(sql);
			while (res.next()) {
				// // /////////////姓名获取已准备好，封装json数组返回即可
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
		} catch (Exception ex) {// 都报错了，就不必close了
			return "-2";
		}
	}

}
