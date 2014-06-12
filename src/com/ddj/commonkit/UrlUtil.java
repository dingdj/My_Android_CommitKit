/**
 * @author dingdj
 * Date:2014-6-12上午10:48:11
 *
 */
package com.ddj.commonkit;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author dingdj
 * Date:2014-6-12上午10:48:11
 *
 */
public class UrlUtil {
	
	public static final String CHARSET_UTF_8 = org.apache.http.protocol.HTTP.UTF_8;
	
	/**
	 * 为URL参数做UTF-8编码
	 * @param value
	 * @return String
	 */
	public static String encodeUrlParams(String value){
		String returnValue = "";
		try {
			value = URLEncoder.encode(value, "UTF-8");
			returnValue = value.replaceAll("\\+", "%20");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
		return returnValue;
	}
	
	/**
	 * URL进行utf8编码
	 * @param url
	 * @return String
	 */
	public static String utf8URLencode(String url) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < url.length(); i++) {
			char c = url.charAt(i);
			if ((c >= 0) && (c <= 255)) {
				result.append(c);
			} else {
				byte[] b = new byte[0];
				try {
					b = Character.toString(c).getBytes(CHARSET_UTF_8);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					result.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return result.toString();
	}

}
