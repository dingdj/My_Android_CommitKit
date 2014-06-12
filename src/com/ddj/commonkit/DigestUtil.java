/**
 * @author dingdj
 * Date:2014-6-12上午11:05:06
 *
 */
package com.ddj.commonkit;

import java.security.MessageDigest;

/**
 * 加密
 * @author dingdj
 * Date:2014-6-12上午11:05:06
 *
 */
public class DigestUtil {
	
	/**
	 * MD5加密，32位
	 * @param str 待加密的字符串
	 * @return String
	 */
	public static String MD5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return str ;
		}

		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

}
