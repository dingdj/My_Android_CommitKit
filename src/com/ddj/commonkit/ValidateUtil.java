/**
 * @author dingdj
 * Date:2014-6-12上午10:39:43
 *
 */
package com.ddj.commonkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证格式
 * @author dingdj
 * Date:2014-6-12上午10:39:43
 *
 */
public class ValidateUtil {
	
	/**
	 * 验证注册名称是否正确
	 * @param str
	 * @return boolean
	 */
	public static boolean checkOnlyContainCharaterAndNumbers(String str) {
		Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 验证邮件地址格式是否正确
	 * @param str
	 * @return boolean
	 */
	public static boolean checkValidMailAddress(String str) {
		Pattern p1 = Pattern.compile("\\w+@(\\w+\\.)+[a-z]{2,3}");
		Matcher m = p1.matcher(str);
		return m.matches();
	}
	
	/**
	 * 验证是否正常
	 * @param str
	 * @return boolean
	 */
	public static boolean checkValidMobilePhoneNumber(String str) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(str);
		return m.matches(); 
	}

}
