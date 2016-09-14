package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import utils.ClassTool;
import utils.PassTool;
import model.AnClass;

/** 用户相关操作，数据库处理 */
public class DbUser {
	private Connection con = null;

	public DbUser() {
		con = DbMain.getInatance().getConnect();
	}

	/** 学生登录（str/1-succ,-1用户名密码不对,-2登录失败，服务器错误） */
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
			/* 这里做代码简化，为方便理解，部分资源不进行释放 */
			if (!res.next())// 用户不存在，注册用户并返回
				return stuRegister(stmt, name, raw_pass);// 这里必须使用原生密码
			if (!pass.equals(res.getString("pass"))) // 密码不对
				return "-1";
			if (!need_class) // 不需要返回课程表
				return "1";
			// 检索课表并返回
			sql = "select real_name from user_s where name='" + name + "'";
			res = stmt.executeQuery(sql);
			if (res.next()) {// 查到课表，则返回课程表
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
						teacher = "";// 此列可能为空
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
			System.out.println("登录异常");
			result = "-2";
		}
		return result;
	}

	/** 教师登录（str/1-succ,-1用户名密码不对,-2登录失败，服务器错误） */
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
			if (res.next()) { // 登录成功
				if (need_class) {// 返回课程表
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
			System.out.println("登录异常");
			result = "-2";
		}
		return result;
	}

	/** 教师注册（str/1-succ,-1已经注册,-2注册失败，服务器错误，-3匹配失败） */
	public String teaRegister(String name, String pass, String real) {
		pass = PassTool.getMD5(pass);
		Statement stmt = null;
		ResultSet res = null;
		String sql = "";
		try {// 是否账号重复
			stmt = con.createStatement();
			sql = "select * from user_t where name='" + name
					+ "' or real_name='" + real + "'";
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
			System.out.println(name + "--" + pass);
			String class_raw = ClassTool.getRawClassTable(name, pass);
			if (class_raw == null || "".equals(class_raw))// 登录失败
				return "-1";
			ArrayList<AnClass> list = ClassTool.getClassTable(class_raw);
			String real = ClassTool.getRealName(name, class_raw);
			if (list == null || list.size() == 0)
				return "-1";
			// 写入注册信息到数据库
			String sql = "insert into user_s values('" + name + "','"
					+ PassTool.getMD5(pass) + "','" + real + "')";
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
			// 此处可以关闭数据流了
			stmt.close();
			return "1";
		} catch (Exception e) {
			return "-2";
		}
	}

}
