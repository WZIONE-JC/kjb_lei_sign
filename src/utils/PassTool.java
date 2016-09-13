package utils;

import java.security.MessageDigest;

public class PassTool {

	/**
	 * 获取文本数据的MD5编码
	 * 
	 * @param text
	 *            要编码的文本数据
	 * @return 数据的32位MD5字符串值
	 */
	public static String getMD5(String text) {// 返回32位MD5数组
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
	 * 将byte数组转换为Hex字符串，这其实是HttpClient里面的codec.jar中Hex类中的encodeHex方法
	 * （这里没有必要导入整个包，所以只拿出来这个方法）
	 * 
	 * @param md
	 *            要转换的byte数组
	 * @return 转换后的字符串
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
