package action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Login��������֤����ת��������Session����
 */
@WebServlet(urlPatterns = { "/UserAction" })
public class UserAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UserAction() {
		super();
	}

	// http://localhost:8080/kjb_sign_lei/UserAction
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().write("TEST");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("REQUEST_TYPE");
		String state = "-2";
		if ("LOGIN".equals(type)) {
			state = login(request);
		} else if ("REGISTER".equals(type)) {
			state = register(request) + "";
		}
		response.getWriter().write(state);
	}

	/**
	 * ��ʦע��
	 */
	private int register(HttpServletRequest request) {
		System.out.println("user-register");
		String name = request.getParameter("name");
		String pass = request.getParameter("pass");
		String real = request.getParameter("real");
		int state = -2;
		try {
			int year = Integer.parseInt(name.substring(0, 4));
			if (year < 1000 || year > 2016) {
				return -2;
			}
		} catch (Exception e) {
			return -2;
		}
		// �ж��Ƿ��Ѿ�ע��
		// ����ע��
		return state;
	}

	/**
	 * ��ʦ��ѧ����¼
	 */
	private String login(HttpServletRequest request) {
		System.out.println("user-login");
		String name = request.getParameter("name");
		String pass = request.getParameter("pass");
		String type = request.getParameter("type");
		if ("1".equals(type)) {// ��ʦ
			return loginTea(name, pass);
		} else if ("0".equals(type)) {// ѧ��
			return loginStu(name, pass);
		}
		return "-1";
	}

	private String loginTea(String name, String pass) {
		// ֱ�ӱȶ����ݿ�
		return null;
	}

	private String loginStu(String name, String pass) {
		// �û�δע��ʱ����Ҫ���⴦��
		return null;
	}

}
