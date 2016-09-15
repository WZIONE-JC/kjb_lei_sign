package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;

import model.AnClass;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

/** 课程表获取与解析 */
public class ClassTool {
	private static final String file_name = "/root/temp/class_table.tmp";
	private static String real_name = "";

	/**
	 * 封装为JSON数据
	 */
	public static String getClassJson(ArrayList<AnClass> list) {
		String result = "[";
		for (int i = 0; i < list.size(); i++) {
			if (!result.equals("["))
				result += ",";
			result += new Gson().toJson(list.get(i));
		}
		result += "]";
		return result;
	}

	/**
	 * 获取用户真实姓名
	 */
	public static String getRealName() {
		return real_name;
	}

	/**
	 * 获取用户真实姓名
	 */
	private static String getRealName(String user_name, String raw_text) {
		int pos = raw_text.indexOf(user_name);
		try {
			// 解析原文： <span class="td1">学院 专业 姓名(学号)</span>
			String result = raw_text.substring(pos - 20, pos - 1);// 最多允许20个字的名字
			result = result.substring(result.lastIndexOf(" ") + 1);
			return result;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 解析课表信息，封装为列表
	 */
	public static ArrayList<AnClass> getClassTable(String user_name,
			String user_pass) {
		ArrayList<AnClass> list = new ArrayList<AnClass>();
		if (!getRawClassTable(user_name, user_pass))// 获取原生课程信息
			return list;
		Document doc = null;
		try {
			doc = Jsoup.parse(new File(file_name), "GB2312");
		} catch (Exception e) {
			return list;
		}
		real_name = getRealName(user_name, doc.body().html());
		Elements e = doc.body().select("table table tbody");
		boolean first_tag = true;
		for (Element ee : e.get(3).getElementsByTag("tr")) {
			if (first_tag) {
				first_tag = false;
				continue;
			}
			int jump = 0;
			String id = "", name = "", place = "", time = "";
			for (Element eee : ee.getElementsByTag("td")) {
				jump++;
				switch (jump) {
				case 1:
					name = eee.text().substring(0, eee.text().length() - 1);
					break;
				case 3:
				case 4:
					String id_temp = eee.text().substring(0,
							eee.text().length() - 1);
					if ("".equals(id)) {
						id = id_temp;
					} else {
						id += "_" + id_temp;
					}
					break;
				case 7:
					place = eee.text().substring(0, eee.text().length() - 1);
					// place = place.replace("软件园", "");
					place = place.replace("d", "");
					place = place.replace("区", "-");
					break;
				case 8:
					time = eee.text().substring(0, eee.text().length() - 1);
					break;
				}
			}
			list.add(new AnClass(id, name, place, time));
		}
		// 排序
		Collections.sort(list);// 涉及一个合并问题，暂不处理
		// // 测试显示
		// for (AnClass a : list) {
		// System.out.println(a);
		// }
		return list;
	}

	/**
	 * 通过爬网的形式获取课程表信息，登录失败返回null
	 */
	private static boolean getRawClassTable(String name, String pass) {
		// 用户登录
		String url = "http://jwxt.sdu.edu.cn:7890/pls/wwwbks/bks_login2.login?jym2005=12006.557322974271";
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(url);
		NameValuePair username = new NameValuePair("stuid", name);
		NameValuePair password = new NameValuePair("pwd", pass);
		post.setRequestBody(new NameValuePair[] { username, password });// 添加表单
		try {
			client.executeMethod(post);
		} catch (Exception e) {
			return false;
		}
		// 获取其课表
		String newurl = "http://jwxt.sdu.edu.cn:7890/pls/wwwbks/xk.CourseView";
		Header header = post.getResponseHeader("Set-Cookie");
		if (header == null)// 登录失败
			return false;
		String cookie = header.getValue();// 获取cookie
		GetMethod get = new GetMethod(newurl);// 用get请求新的网址
		if (get.getRequestHeader("Set-Cookie") == null) {// 若需要cookie，则添加
			get.setRequestHeader("Set-Cookie", cookie);
		}
		try {
			client.executeMethod(get);
			// 读取URL的响应
			InputStream input = get.getResponseBodyAsStream();
			StringBuffer sb = new StringBuffer();
			OutputStream output = new FileOutputStream(file_name);
			byte[] buffer = new byte[1024 * 8];
			int count = 0;
			while ((count = input.read(buffer)) > 0) {
				output.write(buffer, 0, count);
			}
			output.close();
			post.releaseConnection();
			get.releaseConnection();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void main(String args[]) throws Exception {
		// System.out.println("A");
		// // 提交时请注意保护个人信息
		// System.out.println("" + getRawClassTable("", ""));
		// System.out.println("B");
	}

}
