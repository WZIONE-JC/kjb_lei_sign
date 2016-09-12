package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import model.AnClass;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

/** �γ̱��ȡ����� */
public class ClassTool {

	/**
	 * ��װΪJSON����
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
	 * ��ȡ�û���ʵ����
	 */
	public static String getRealName(String user_name, String raw_text) {
		int pos = raw_text.indexOf(user_name);
		try {
			String result = raw_text.substring(pos - 20, pos - 1);// �������20���ֵ�����
			result = result.substring(result.lastIndexOf(" ") + 1);
			return result;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * �����α���Ϣ����װΪ�б�
	 */
	public static ArrayList<AnClass> getClassTable(String raw_text) {
		Document doc = Jsoup.parse(raw_text);
		Elements e = doc.body().select("table table tbody");
		boolean first_tag = true;
		ArrayList<AnClass> list = new ArrayList<AnClass>();
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
					// place = place.replace("���԰", "");
					place = place.replace("d", "");
					place = place.replace("��", "-");
					break;
				case 8:
					time = eee.text().substring(0, eee.text().length() - 1);
					break;
				}
			}
			list.add(new AnClass(id, name, place, time));
		}
		// ����
		Collections.sort(list);// �漰һ���ϲ����⣬�ݲ�����
		// // ������ʾ
		// for (AnClass a : list) {
		// System.out.println(a);
		// }
		return list;
	}

	/**
	 * ͨ����������ʽ��ȡ�γ̱���Ϣ
	 */
	public static String getRawClassTable(String name, String pass)
			throws Exception {
		// �û���¼
		String url = "http://jwxt.sdu.edu.cn:7890/pls/wwwbks/bks_login2.login?jym2005=12006.557322974271";
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(url);
		NameValuePair username = new NameValuePair("stuid", name);
		NameValuePair password = new NameValuePair("pwd", pass);
		post.setRequestBody(new NameValuePair[] { username, password });// ��ӱ�
		client.executeMethod(post);
		// ��ȡ��α�
		String newurl = "http://jwxt.sdu.edu.cn:7890/pls/wwwbks/xk.CourseView";
		String cookie = post.getResponseHeader("Set-Cookie").getValue();// ��ȡcookie
		GetMethod get = new GetMethod(newurl);// ��get�����µ���ַ
		if (get.getRequestHeader("Set-Cookie") == null) {// ����Ҫcookie�������
			get.setRequestHeader("Set-Cookie", cookie);
		}
		client.executeMethod(get);
		// ��ȡURL����Ӧ
		BufferedReader in = new BufferedReader(new InputStreamReader(
				get.getResponseBodyAsStream()));
		StringBuffer sb = new StringBuffer("");
		String line = "";
		while ((line = in.readLine()) != null) {
			sb.append(line + "\n");
		}
		in.close();
		String result = sb.toString();
		post.releaseConnection();
		get.releaseConnection();
		// System.out.println("��ȡ�α�ɹ�");
		return result;
	}

	public static void main(String args[]) {
		System.out.println("A");
		try {// /////////////ȷ��һ��������󷵻�ʲô��������û���쳣�����󷵻�null
				// �ύʱ��ע�Ᵽ��������Ϣ
			System.out.println(getRawClassTable("201400301104", "000000"));
		} catch (Exception e) {
			System.out.println("ERROR");
		}
		System.out.println("B");
	}

}
