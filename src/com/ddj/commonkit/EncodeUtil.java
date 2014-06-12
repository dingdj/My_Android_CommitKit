/**
 * @author dingdj
 * Date:2014-6-12上午10:48:49
 *
 */
package com.ddj.commonkit;

/**
 * @author dingdj
 * Date:2014-6-12上午10:48:49
 *
 */
public class EncodeUtil {
	
	/**
     * 去除UTF-8格式BOM头
     * 
     * @return
     */
    public static String removeBomHeader(String s) {
    	if (s != null && s.startsWith("\ufeff")) {
			return s.substring(1);
		}
    	return s;
    }

}
