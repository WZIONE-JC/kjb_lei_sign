package action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DbUser;

/**
 * ��¼��ע�������<br/>
 * Login��������֤����ת��������Session/Token����
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
		response.getWriter().write("USER_ACTION");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("REQUEST_TYPE");
		response.addHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=utf-8");//ָ������
		String state = "-2";
		if ("LOGIN".equals(type)) {
			state = login(request);
		} else if ("REGISTER".equals(type)) {
			state = register(request);
		}
		response.getWriter().write(state);
	}

	/**
	 * ��ʦע��
	 */
	private String register(HttpServletRequest request) {
		System.out.println("user-register");
		String name = request.getParameter("name");
		String pass = request.getParameter("pass");
		String real = request.getParameter("real");
		try {
			int year = Integer.parseInt(name.substring(0, 4));
			if (year < 1000 || year > 2016) {// ȡ�ɵĺ����㷨
				if (year < 9000)// ����9��ͷ���ݣ����ڲ���
					return "-3";
			}
		} catch (Exception e) {
			return "-3";
		}
		// �ж��Ƿ��Ѿ�ע�ᣬ������ע��
		return dbUser.teaRegister(name, pass, real);
	}

	/**
	 * ��ʦ��ѧ����¼
	 */
	private String login(HttpServletRequest request) {
		System.out.println("user-login");
		String name = request.getParameter("name");
		String pass = request.getParameter("pass");
		String type = request.getParameter("type");
		String auto = request.getParameter("auto");// auto����Ҫ�α�
		if ("1".equals(type)) {// ��ʦ
			return dbUser.teaLogin(name, pass, "0".equals(auto));
		} else if ("0".equals(type)) {// ѧ��
			return dbUser.stuLogin(name, pass, "0".equals(auto));
		}
		return "-1";
	}

}
