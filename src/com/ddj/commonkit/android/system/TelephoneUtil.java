package com.ddj.commonkit.android.system;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 获取手机相关参数的工具类
 */
public class TelephoneUtil {
	private static final String TAG = "TelephoneUtil";

	/**
	 * 取得IMEI号
	 * 
	 * @param ctx
	 * @return String
	 */
	public static String getIMEI(Context ctx) {
		TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
		if (tm == null)
			return "";
		String result = null;
		try {
			result = tm.getDeviceId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result == null)
			return "";

		return result;
	}

	/**
	 * 取得IMSI号
	 * 
	 * @param ctx
	 * @return String
	 */
	public static String getIMSI(Context ctx) {
		TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
		if (tm == null)
			return "";

		String result = tm.getSubscriberId();
		if (result == null)
			return "";

		return result;
	}

	/**
	 * 取得MAC
	 * 
	 * @param ctx
	 * @return String
	 */
	public static String getMAC(Context ctx) {
		String mac = "";
		WifiManager wifi = (WifiManager) (ctx.getSystemService(Context.WIFI_SERVICE));
		if (wifi == null) {
			return mac;
		}
		WifiInfo info = wifi.getConnectionInfo();
		if (info == null) {
			return mac;
		}
		mac = info.getMacAddress();
		return mac;
	}

	/**
	 * sim卡是否存在
	 * 
	 * @param ctx
	 * @return boolean
	 */
	public static boolean isSimExist(Context ctx) {
		TelephonyManager manager = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
		if (manager.getSimState() == TelephonyManager.SIM_STATE_ABSENT)
			return false;
		else
			return true;
	}

	

	/**
	 * 查询系统广播
	 * 
	 * @param ctx
	 * @param actionName
	 * @return boolean
	 */
	public static boolean queryBroadcastReceiver(Context ctx, String actionName) {
		PackageManager pm = ctx.getPackageManager();
		try {
			Intent intent = new Intent(actionName);
			List<ResolveInfo> apps = pm.queryBroadcastReceivers(intent, 0);
			if (apps.isEmpty())
				return false;
			else
				return true;
		} catch (Exception e) {
			Log.d(TAG, "queryBroadcastReceivers: " + e.toString());
			return false;
		}
	}

	/**
	 * 获取CPU_ABI
	 * 
	 * @return String
	 */
	public static String getCPUABI() {
		String abi = Build.CPU_ABI;
		abi = (abi == null || abi.trim().length() == 0) ? "" : abi;
		// 检视是否有第二类型，1.6没有这个字段
		try {
			String cpuAbi2 = Build.class.getField("CPU_ABI2").get(null).toString();
			cpuAbi2 = (cpuAbi2 == null || cpuAbi2.trim().length() == 0) ? null : cpuAbi2;
			if (cpuAbi2 != null) {
				abi = abi + "," + cpuAbi2;
			}
		} catch (Exception e) {
		}
		return abi;
	}


	/**
	 * 是否是小米2手机
	 * 
	 * @return boolean
	 */
	public static boolean isMI2Moble() {
		try {
			return SystemEnvUtil.getMachineName().contains("MI 2");
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 是否是小米手机
	 * 
	 * @return boolean
	 */
	public static boolean isMIMoble() {
		try {
			return SystemEnvUtil.getMachineName().contains("MI-ONE") || SystemEnvUtil.getMachineName().contains("MI 2");
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 是否是HTC G13手机
	 * 
	 * @return boolean
	 */
	public static boolean isHTC_G13_Mobile() {
		try {
			return SystemEnvUtil.getMachineName().contains("HTC A510e");
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 是否是Note2
	 * 
	 * @return boolean
	 */
	public static boolean isNote2() {
		try {
			return (SystemEnvUtil.getMachineName().contains("GT-N71") || SystemEnvUtil.getMachineName().contains("SCH-N719"));
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 是否是HTC One
	 * 
	 * @return boolean
	 */
	public static boolean isHTC_ONE() {
		try {
			return SystemEnvUtil.getMachineName().contains("HTC 802");
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 是否是oppo 4.2 的机子
	 * 
	 * @return boolean
	 */
	public static boolean isOppo_42() {
		try {
			String name = SystemEnvUtil.getManufacturer();
			String version = VersionUtil.getFirmWareVersion();
			if ("OPPO".equalsIgnoreCase(name) && "4.2".equals(version)) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 是否是HTC4.0或以上的手机
	 * 
	 * @return boolean
	 **/
	public static boolean isHTC_4OrAbove() {
		try {
			return (SystemEnvUtil.getManufacturer().equalsIgnoreCase("HTC")) && (VersionUtil.getApiLevel() >= 14);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 是否是夏新N79+, 系统2.3.5的手机
	 * 
	 * @return boolean
	 **/
	public static boolean isAmoiN79_235() {
		try {
			return (SystemEnvUtil.getMachineName().contains("AMOI_N79+")) && (android.os.Build.VERSION.RELEASE.equals("2.3.5"));
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 是否是Oppo的一些旧机型 R805, T703等。
	 * 
	 * @return boolean
	 **/
	public static boolean isOppoOldPhone() {
		try {
			return (SystemEnvUtil.getMachineName().contains("R805") || SystemEnvUtil.getMachineName().contains("T703") || SystemEnvUtil
					.getManufacturer().equalsIgnoreCase("alps"));
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 是否是步步高手机
	 * 
	 * @return boolean
	 **/
	public static boolean isVivoPhone() {
		try {
			return (SystemEnvUtil.getMachineName().contains("vivo") || SystemEnvUtil.getManufacturer().equalsIgnoreCase("BBK"));
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 是否天宇手机 W700型号（manufacturer为NVIDIA有误，所以不放入判断）
	 * 
	 * @return boolean
	 **/
	public static boolean isKtouchPhone() {
		try {
			return SystemEnvUtil.getMachineName().contains("W700");
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 是否酷派手机
	 * 
	 * @return boolean
	 **/
	public static boolean isCoolpadPhone() {
		try {
			return (SystemEnvUtil.getMachineName().contains("Coolpad") || SystemEnvUtil.getManufacturer().equalsIgnoreCase("YuLong"));
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 是否联想手机
	 * 
	 * @return boolean
	 **/
	public static boolean isLenovoPhone() {
		try {
			return (SystemEnvUtil.getMachineName().toLowerCase().contains("lenovo") || SystemEnvUtil.getManufacturer().toLowerCase()
					.contains("lenovo"));
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 是否联想手机K900
	 * 
	 * @return boolean
	 **/
	public static boolean isLenovoPhoneK900() {
		try {
			return isLenovoPhone() && SystemEnvUtil.getMachineName().contains("K900");
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 是否摩托罗拉手机
	 * 
	 * @return boolean
	 **/
	public static boolean isMotorolaPhone() {
		try {
			return (SystemEnvUtil.getManufacturer().toLowerCase().contains("motorola"));
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 是否是Sony的一些机型
	 * 
	 * @return boolean
	 **/
	public static boolean isSonyPhone() {
		try {
			String manufacturer = SystemEnvUtil.getManufacturer();
			return (manufacturer.equalsIgnoreCase("SONY") || manufacturer.equalsIgnoreCase("Sony Ericsson"));
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 是否为魅族固件为4.2以上手机
	 * 
	 * @return
	 */
	public static boolean isMeizuPhone42() {
		try {
			return SystemEnvUtil.getManufacturer().toLowerCase().contains("meizu") && Build.VERSION.SDK_INT >= 17;
		} catch (Exception e) {
			return false;
		}
	}

}
