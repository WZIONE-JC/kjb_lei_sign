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
			if (res.next()) { // 登录成功
				if (need_class) {
					// ////////////////考虑是否返回课程表///////////////注意stmt是否冲突
					result = "1";
				} else {
					result = "1";
				}
			} else {
				/////////////////////未注册处理//////////////////////
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

}
