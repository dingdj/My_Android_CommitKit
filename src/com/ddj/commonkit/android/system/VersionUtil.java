/**
 * @author dingdj
 * Date:2014-6-12上午11:14:40
 *
 */
package com.ddj.commonkit.android.system;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * @author dingdj
 * Date:2014-6-12上午11:14:40
 *
 */
public class VersionUtil {
	
	/**
	 * 获取字符串型的固件版本，如1.5、1.6、2.1
	 * @return String
	 */
	@SuppressWarnings("deprecation")
	public static String getFirmWareVersion() {
		final String version_3 = "1.5";
		final String version_4 = "1.6";
		final String version_5 = "2.0";
		final String version_6 = "2.0.1";
		final String version_7 = "2.1";
		final String version_8 = "2.2";
		final String version_9 = "2.3";
		final String version_10 = "2.3.3";
		final String version_11 = "3.0";
		final String version_12 = "3.1";
		final String version_13 = "3.2";
		final String version_14 = "4.0";
		final String version_15 = "4.0.3";
		final String version_16 = "4.1.1";
		final String version_17 = "4.2";
		final String version_18 = "4.3";
		final String version_19 = "4.4";
		String versionName = "";
		try {
			// android.os.Build.VERSION.SDK_INT Since: API Level 4
			// int version = android.os.Build.VERSION.SDK_INT;
			int version = Integer.parseInt(android.os.Build.VERSION.SDK);
			switch (version) {
			case 3:
				versionName = version_3;
				break;
			case 4:
				versionName = version_4;
				break;
			case 5:
				versionName = version_5;
				break;
			case 6:
				versionName = version_6;
				break;
			case 7:
				versionName = version_7;
				break;
			case 8:
				versionName = version_8;
				break;
			case 9:
				versionName = version_9;
				break;
			case 10:
				;
				versionName = version_10;
				break;
			case 11:
				versionName = version_11;
				break;
			case 12:
				versionName = version_12;
				break;
			case 13:
				versionName = version_13;
				break;
			case 14:
				versionName = version_14;
				break;
			case 15:
				versionName = version_15;
				break;
			case 16:
				versionName = version_16;
				break;
			case 17:
				versionName = version_17;
				break;
			case 18:
				versionName = version_18;
				break;
			case 19:
				versionName = version_19;
				break;
			default:
				versionName = version_7;
			}
		} catch (Exception e) {
			versionName = version_7;
		}
		return versionName;
	}

	/**
	 * 获取软件版本名称
	 */
	public static String getVersionName(Context ctx) {
		return getVersionName(ctx, ctx.getPackageName());
	}

	/**
	 * 获取versionName
	 * @param context
	 * @param packageName
	 * @return String
	 */
	public static String getVersionName(Context context, String packageName) {
		String versionName = "";
		try {
			PackageInfo packageinfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
			versionName = packageinfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}

	/**
	 * 获取软件版本号 code
	 * @param ctx
	 * @return int
	 */
	public static int getVersionCode(Context ctx) {
		return getVersionCode(ctx, ctx.getPackageName());
	}

	/**
	 * 获取软件版本号 code
	 * @param ctx
	 * @param packageName
	 * @return int
	 */
	public static int getVersionCode(Context ctx, String packageName) {
		int versionCode = 0;
		try {
			PackageInfo packageinfo = ctx.getPackageManager().getPackageInfo(packageName, PackageManager.GET_INSTRUMENTATION);
			versionCode = packageinfo.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 比较versionName,是否存在新版本
	 * @param newVersionName 新版本号
	 * @param oldeVersionName 旧版本号
	 * @return 新版本号> 旧版本号 return true
	 */
	public static boolean isExistNewVersion(String newVersionName, String oldeVersionName) {
		if (oldeVersionName.toLowerCase().startsWith("v")) {
			oldeVersionName = oldeVersionName.substring(1, oldeVersionName.length());
		}
		if (newVersionName.toLowerCase().startsWith("v")) {
			newVersionName = newVersionName.substring(1, oldeVersionName.length());
		}

		if (oldeVersionName == null || newVersionName == null) {
			return false;
		}
		if (oldeVersionName.trim().length() == 0 || newVersionName.trim().length() == 0) {
			return false;
		}
		try {
			List<String> codes = parser(oldeVersionName.trim(), '.');
			List<String> versionCodes = parser(newVersionName.trim(), '.');
			for (int i = 0; i < codes.size(); i++) {
				if (i > (versionCodes.size() - 1)) {
					return false;
				}
				int a = Integer.parseInt(codes.get(i).trim());
				int b = Integer.parseInt(versionCodes.get(i).trim());
				if (a < b) {
					return true;
				} else if (a > b) {
					return false;
				}
			}
			if (codes.size() < versionCodes.size()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 获取数字型API_LEVEL 如：4、6、7
	 * 
	 * @return int
	 */
	@SuppressWarnings("deprecation")
	public static int getApiLevel() {
		int apiLevel = 7;
		try {
			apiLevel = Integer.parseInt(android.os.Build.VERSION.SDK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return apiLevel;
	}

	
	/**
	 * 2.60.3=>[2,60,3]
	 * 
	 * @param value
	 * @param c
	 * @return List<String>
	 */
	private static List<String> parser(String value, char c) {
		List<String> ss = new ArrayList<String>();
		char[] cs = value.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < cs.length; i++) {
			if (c == cs[i]) {
				ss.add(sb.toString());
				sb = new StringBuffer();
				continue;
			}
			sb.append(cs[i]);
		}
		if (sb.length() > 0) {
			ss.add(sb.toString());
		}
		return ss;
	}

}
