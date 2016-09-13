package action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DbSign;

@WebServlet("/SignAction")
public class SignAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DbSign dbSign = null;

	public SignAction() {
		super();
		dbSign = new DbSign();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().write("SIGN_ACTION");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("REQUEST_TYPE");
		String state = "-2";
		if ("SIGN".equals(type)) {
			state = stuSign(request);
		} else if ("WATCH".equals(type)) {
			state = teaWatch(request) + "";
		}
		response.getWriter().write(state);
	}

	/**
	 * 学生签到
	 */
	private String stuSign(HttpServletRequest request) {
		String stuid = request.getParameter("stuid");
		String stu = request.getParameter("stu");
		String tea = request.getParameter("tea");
		String time = request.getParameter("time");
		return dbSign.stuSign(stuid, stu, tea, time);
	}

	/**
	 * 教师查看
	 */
	private String teaWatch(HttpServletRequest request) {
		String tea = request.getParameter("tea");
		String time = request.getParameter("time");
		return dbSign.teaSeeSign(tea, time);
	}
}
