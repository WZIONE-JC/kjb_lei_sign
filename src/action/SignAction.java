package action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SignAction")
public class SignAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SignAction() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
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

		return null;
	}

	/**
	 * 教师查看
	 */
	private String teaWatch(HttpServletRequest request) {

		return null;
	}
}
