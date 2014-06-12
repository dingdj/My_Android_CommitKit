/**
 * @author dingdj
 * Date:2014-6-12上午10:42:18
 *
 */
package com.ddj.commonkit;

/**
 * @author dingdj
 * Date:2014-6-12上午10:42:18
 *
 */
public class NumberUtil {
	
	/**
	 * 获取随机数，0 和 max之间 ，不包含max
	 * @param max
	 * @return int
	 */
	public static int getRandom(int max){
		return Integer.parseInt(String.valueOf(System.currentTimeMillis() % max)) ;
	}
	
	
	/**
	 * 解析int 
	 * @param str
	 * @param defaultValue 默认值
	 * @return int
	 */
	public static int parseInt(String str, int defaultValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 解析long
	 * @param str
	 * @param defaultValue 默认值
	 * @return long
	 */
	public static long parseLong(String str, long defaultValue) {
		try {
			return Long.parseLong(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	
	/**
	 * 判断传入的字符串是否是数字类型
	 * @param sNum
	 * @return boolean
	 */
	public static boolean isNumberic(String sNum) {
		try {
			Float.parseFloat(sNum);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
