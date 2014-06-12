/**
 * @author dingdj
 * Date:2014-6-12上午10:46:59
 *
 */
package com.ddj.commonkit.android.system;

import android.graphics.Color;

/**
 * @author dingdj Date:2014-6-12上午10:46:59
 * 
 */
public class ColorUtil {

	/**
	 * 转换ARGB色值，格式如#FFFFFF
	 * 
	 * @param color
	 * @return String
	 */
	public static String Color2String(int color) {
		String A = "";
		String R = "";
		String G = "";
		String B = "";
		try {
			A = Integer.toHexString(Color.alpha(color));
			A = A.length() < 2 ? ('0' + A) : A;
			R = Integer.toHexString(Color.red(color));
			R = R.length() < 2 ? ('0' + R) : R;
			G = Integer.toHexString(Color.green(color));
			G = G.length() < 2 ? ('0' + G) : G;
			B = Integer.toHexString(Color.blue(color));
			B = B.length() < 2 ? ('0' + B) : B;
		} catch (Exception e) {
			return "#FFFFFF";
		}
		return '#' + A + R + G + B;
	}

	/**
	 * 颜色设置
	 * 
	 * @param color
	 * @param alpha
	 * @return int
	 */
	public static int argbColorAlpha(int color, int alpha) {
		int r = Color.red(color);
		int g = Color.green(color);
		int b = Color.blue(color);
		return Color.argb(alpha, r, g, b);
	}

	/**
	 * 取相反颜色
	 * 
	 * @param alpha
	 * @param color
	 * @return int
	 */
	public static int antiColorAlpha(int alpha, int color) {
		if (-1 == alpha) {
			alpha = Color.alpha(color);
			if (255 == alpha) {
				alpha = 200;
			}
		}
		int r = Color.red(color);
		int g = Color.green(color);
		int b = Color.blue(color);
		return Color.argb(alpha, 255 - r, 255 - g, 255 - b);
	}

	/**
	 * 解析颜色值
	 * 
	 * @param colorStr
	 * @return int
	 */
	public static int parseColor(String colorStr) {
		int color = 0xff000000;
		try {
			color = Color.parseColor(colorStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return color;
	}

	/**
	 * 解析颜色值
	 * 
	 * @param colorStr
	 * @return int
	 */
	public static float[] parseColor2Array(String colorStr) {
		float[] colorArray = new float[3];
		int color = parseColor(colorStr);
		colorArray[0] = (1.0f * Color.red(color)) / 255.0f;
		colorArray[1] = (1.0f * Color.green(color)) / 255.0f;
		colorArray[2] = (1.0f * Color.blue(color)) / 255.0f;
		return colorArray;
	}

}
