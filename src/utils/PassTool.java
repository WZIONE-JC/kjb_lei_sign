package utils;

import java.security.MessageDigest;

public class PassTool {

	/**
	 * ��ȡ�ı����ݵ�MD5����
	 * 
	 * @param text
	 *            Ҫ������ı�����
	 * @return ���ݵ�32λMD5�ַ���ֵ
	 */
	public static String getMD5(String text) {// ����32λMD5����
		String result = text;
		MessageDigest message = null;
		byte[] bytes = null;
		try {
			message = MessageDigest.getInstance("MD5");
			bytes = message.digest(text.getBytes("utf8"));
			result = new String(toHexString(bytes));
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * ��byte����ת��ΪHex�ַ���������ʵ��HttpClient�����codec.jar��Hex���е�encodeHex����
	 * ������û�б�Ҫ����������������ֻ�ó������������
	 * 
	 * @param md
	 *            Ҫת����byte����
	 * @return ת������ַ���
	 */
	private static String toHexString(byte[] md) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		int j = md.length;
		char str[] = new char[j * 2];
		for (int i = 0; i < j; i++) {
			byte byte0 = md[i];
			str[2 * i] = hexDigits[byte0 >>> 4 & 0xf];
			str[i * 2 + 1] = hexDigits[byte0 & 0xf];
		}
		return new String(str);
	}

}
