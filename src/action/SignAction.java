package action;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.AnClass;
import utils.ClassTool;
import database.DbSign;

/** 签到与查看监听类 */
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
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write("SignAction");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("REQUEST_TYPE");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String state = "-2";
		if ("SIGN".equals(type)) {
			state = stuSign(request);
		} else if ("WATCH".equals(type)) {
			state = teaWatch(request) + "";
		} else if ("POSITION".equals(type)) {
			state = dbSign.getPositionInfo();
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
		String tea2 = request.getParameter("tea2");
		String time = request.getParameter("time");
		return dbSign.stuSign(stuid, stu, tea, tea2, time);
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
