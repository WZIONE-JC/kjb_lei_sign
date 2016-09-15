package action;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DbUser;

/**
 * 登录与注册监听类<br/>
 * Login仅用于验证与跳转，不引入Session/Token机制
 */
@WebServlet(urlPatterns = { "/UserAction" })
public class UserAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DbUser dbUser = null;

	public UserAction() {
		super();
		dbUser = new DbUser();
	}

	// http://localhost:8080/kjb_sign_lei/UserAction
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write("UserAction");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("REQUEST_TYPE");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String state = "-2";
		if ("LOGIN".equals(type)) {
			state = login(request);
		} else if ("REGISTER".equals(type)) {
			state = register(request);
		}
		response.getWriter().write(URLEncoder.encode(state, "UTF-8"));
	}

	/**
	 * 教师注册
	 */
	private String register(HttpServletRequest request) {
		System.out.println("user-register");
		String name = request.getParameter("name");
		String pass = request.getParameter("pass");
		String real = request.getParameter("real");
		try {
			int year = Integer.parseInt(name.substring(0, 4));
			if (year < 1000 || year > 2016) {// 取巧的核验算法
				if (year < 9000)// 开放9开头数据，便于测试
					return "-3";
			}
		} catch (Exception e) {
			return "-3";
		}
		// 判断是否已经注册，并进行注册
		return dbUser.teaRegister(name, pass, real);
	}

	/**
	 * 教师与学生登录
	 */
	private String login(HttpServletRequest request) {
		System.out.println("user-login");
		String name = request.getParameter("name");
		String pass = request.getParameter("pass");
		String type = request.getParameter("type");
		String auto = request.getParameter("auto");// auto不需要课表
		if ("1".equals(type)) {// 教师
			return dbUser.teaLogin(name, pass, "0".equals(auto));
		} else if ("0".equals(type)) {// 学生
			return dbUser.stuLogin(name, pass, "0".equals(auto));
		}
		return "-1";
	}

}
