/**
 * @author dingdj
 * Date:2014-6-12上午10:36:11
 *
 */
package com.ddj.commonkit;

import android.text.TextUtils;

/**
 * @author dingdj Date:2014-6-12上午10:36:11
 * 
 */
public class StringUtils {

	/**
	 * 判断字符串是否为空
	 * 
	 * @param s
	 * @return boolean
	 */
	public static boolean isNotEmpty(CharSequence s) {
		if ((s == null) || ("".equals(s.toString().trim()))) {
			return false;
		}
		return true;
	}

	/**
	 * 去除特殊字符：$^*()-+{}|.?[]&\
	 * 
	 * @param input
	 * @return String
	 */
	public static String regularSymbolFilter(String input) {
		if (TextUtils.isEmpty(input))
			return "";

		return input.replaceAll("\\$|\\^|\\*|\\(|\\)|\\-|\\+|\\{|\\}|\\||\\.|\\?|\\[|\\]|\\&|\\\\", "");
	}

	/**
	 * int 转化为ip
	 * 
	 * @author dingdj Date:2014-6-12上午11:21:32
	 * @param i
	 * @return
	 */
	public static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
	}

	/**
	 * revert a string
	 * 
	 * @param from
	 *            the string
	 * @return reverted string
	 */
	public static String revert(String from) {
		if (from == null || from.length() <= 1)
			return from;
		int len = from.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 1; i <= len; i++) {
			sb.append(from.charAt(len - i));
		}
		return sb.toString();
	}

}
