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
	public String stuSign(String stu_id, String stu, String tea, String tea2,
			String time_str) {
		int time = 0;// 时间转换
		try {
			time = Integer.parseInt(time_str);
		} catch (Exception e) {
			return "-2";
		}
		Statement stmt = null;
		ResultSet res = null;
		String sql = "";
		// 根据time判断签到时间是否合适（此处在APP端限制）
		// 使用矩形判断处理位置信息，作差，手动调研各个区位xy（此处在APP端处理，虽然不安全，但服务器压力小）
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
			sql = "insert into state_s values('" + stu_id + "','" + stu + "','"
					+ tea + "'," + time + ")";
			int num = stmt.executeUpdate(sql);
			if (tea2 != null && !tea2.equals("")) {// 两门课程
				sql = "insert into state_s values('" + stu_id + "','" + stu
						+ "','" + tea + "'," + time + ")";
				num += stmt.executeUpdate(sql);
			}
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
		String cls_name = "";
		try { // 后期可考虑返回签到的学生的姓名////
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
			if (res.next()) {
				sign_all++;
				cls_name = res.getString("class");
			}
			while (res.next()) {
				sign_all++;
			}
			return "{'name':'" + cls_name + "','all':" + sign_all + ",'curr':"
					+ sign_cur + "}";
		} catch (Exception ex) {// 都报错了，就不必close了
			return "-2";
		}
	}

	/** 获取位置信息（str-succ,-1失败，服务器错误） */
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
			System.out.println("登录异常");
			result = "-2";
		}
		return result;
	}

}
