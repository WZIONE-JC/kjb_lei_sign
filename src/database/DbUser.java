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

	/** 学生登录 */
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
			if (res.next()) { // 登录成功
				if (need_class) {
					// ////////////////考虑是否返回课程表///////////////注意stmt是否冲突
					result = "1";
				} else {
					result = "1";
				}
			} else {// 未注册处理
				result = stuRegister(stmt, name, pass);
			}
			stmt.close();
			res.close();
		} catch (Exception ex) {
			System.out.println("登录异常");
			result = "-2";
		}
		return result;
	}

	/** 教师登录 */
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
			if (res.next()) { // 登录成功
				if (need_class) {
					// ////////////////考虑是否返回课程表///////////////注意stmt是否冲突
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
			System.out.println("登录异常");
			result = "-2";
		}
		return result;
	}

	/** 教师注册 */
	public String teaRegister(String name, String pass, String real) {
		Statement stmt = null;
		ResultSet res = null;
		String sql = "";
		try {// 是否账号重复
			stmt = con.createStatement();
			sql = "select * from user_t where name='" + name + "' or real='"
					+ real + "'";
			res = stmt.executeQuery(sql);
			if (res.next()) {
				stmt.close();
				res.close();
				return "-1";
			}
		} catch (Exception ex) {// 都报错了，就不必close了
			return "-2";
		}
		try {// 注册
			sql = "insert into user_t values('" + name + "','" + pass + "','"
					+ real + "')";
			int num = stmt.executeUpdate(sql);
			stmt.close();
			res.close();
			if (num != 0) // 修改成功
				return "1";
			return "-2";
		} catch (Exception e) {
			System.out.println("注册异常");
			return "-2";
		}
	}

	/**
	 * 学生注册，1成功，-1失败。
	 */
	private String stuRegister(Statement stmt, String name, String pass) {
		try {
			// 模拟登陆，获取课表
			String class_raw = ClassTool.getRawClassTable(name, pass);
			if (class_raw == null || "".equals(class_raw))
				return "-1";
			ArrayList<AnClass> list = ClassTool.getClassTable(class_raw);
			String real = ClassTool.getRealName(name, class_raw);
			if (list == null || list.size() == 0)
				return "-1";
			// 写入注册信息到数据库
			String sql = "insert into user_s values('" + name + "','" + pass
					+ "','" + real + "','" + ClassTool.getClassJson(list)
					+ "')";
			stmt.executeUpdate(sql);
			// 写入课程信息到数据库
			for (int i = 0; i < list.size(); i++) {
				AnClass cls = list.get(i);
				sql = "insert into info_c values('" + name + "','','"
						+ cls.getName() + "','" + cls.getTime() + "','"
						+ cls.getPlace() + "','" + cls.getId() + "')";
				stmt.executeUpdate(sql);
			}
			// 补充教师信息到课程信息中/*我真是太机智了*/
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
