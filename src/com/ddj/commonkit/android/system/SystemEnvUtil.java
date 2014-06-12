/**
 * @author dingdj
 * Date:2014-6-12上午11:17:00
 *
 */
package com.ddj.commonkit.android.system;

import java.io.File;
import java.util.List;
import java.util.Locale;

import com.ddj.commonkit.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.speech.RecognizerIntent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;

/**
 * @author dingdj
 * Date:2014-6-12上午11:17:00
 *
 */
public class SystemEnvUtil {
	
	/**
	 * 获取手机型号                                      
	 * @return String
	 */
	public static String getMachineName() {
		return android.os.Build.MODEL;
	}
	
	/**
	 * 网络是否可用
	 * @param context
	 * @return boolean
	 */
	public synchronized static boolean isNetworkAvailable(Context context) {
		boolean result = false;
		if (context == null) {
			return result;
		}
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = null;
		if (null != connectivityManager) {
			networkInfo = connectivityManager.getActiveNetworkInfo();
			if (null != networkInfo && networkInfo.isAvailable() && networkInfo.isConnected()) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * wifi是否启动
	 * @param ctx
	 * @return boolean
	 */
	public static boolean isWifiEnable(Context ctx) {
		if(ctx == null){
			return false;
		}
		ConnectivityManager tele = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (tele == null ||tele.getActiveNetworkInfo() == null || !tele.getActiveNetworkInfo().isAvailable()) {
			return false;
		}
		int type = tele.getActiveNetworkInfo().getType();
		return type == ConnectivityManager.TYPE_WIFI;
	}

	/**
	 * 返回网络连接方式
	 * @param ctx
	 * @return int
	 */
	public static int getNetworkState(Context ctx) {
		if (isWifiEnable(ctx)) {
			return 0;
		} else {
			return 1;
		}
	}
	
	/**
	 * sd卡是否存在
	 * @return boolean
	 */
	public static boolean isSdcardExist() {
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * 是否中文环境
	 * @param ctx
	 * @return boolean
	 */
	public static boolean isZh(Context ctx) {
		Locale lo = ctx.getResources().getConfiguration().locale;
		if (lo.getLanguage().equals("zh"))
			return true;
		return false;
	}
	
	/**
	 * 获取当前语言
	 * @return String
	 */
	public static String getLanguage() {
		return Locale.getDefault().getLanguage();
	}
	
	/**
	 * 获取IP地址
	 * 
	 * @param ctx
	 * @return 失败返回""
	 */
	public static String getWifiAddress(Context ctx) {

		try {
			// 获取wifi服务
			WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
			// 判断wifi是否开启
			if (wifiManager.isWifiEnabled()) {
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				int ipAddress = wifiInfo.getIpAddress();
				String ip = StringUtils.intToIp(ipAddress);
				return ip;
			}
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 获取手机上网类型(cmwap/cmnet/wifi/uniwap/uninet)
	 * 
	 * @param ctx
	 * @return String
	 */
	public static String getNetworkTypeName(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (null == info || null == info.getTypeName()) {
			return "unknown";
		}
		return info.getTypeName();
	}

	/**
	 * 获取制造商
	 * 
	 * @return String
	 */
	public static String getManufacturer() {
		return android.os.Build.MANUFACTURER;
	}
	
	/**
	 * 是否拥有root权限
	 */
	public static boolean hasRootPermission() {
		boolean rooted = true;
		try {
			File su = new File("/system/bin/su");
			if (su.exists() == false) {
				su = new File("/system/xbin/su");
				if (su.exists() == false) {
					rooted = false;
				}
			}
		} catch (Exception e) {
			rooted = false;
		}
		return rooted;
	}

	/**
	 * 获取网络类型值
	 * 
	 * @param ctx
	 * @return String
	 */
	public static String getNT(Context ctx) {
		/**
		 * 0 未知
		 * 
		 * 10 WIFI网络
		 * 
		 * 20 USB网络
		 * 
		 * 31 联通
		 * 
		 * 32 电信
		 * 
		 * 53 移动
		 * 
		 * IMSI是国际移动用户识别码的简称(International Mobile Subscriber Identity)
		 * 
		 * IMSI共有15位，其结构如下： MCC+MNC+MIN MNC:Mobile NetworkCode，移动网络码，共2位
		 * 在中国，移动的代码为电00和02，联通的代码为01，电信的代码为03
		 */
		String imsi = TelephoneUtil.getIMSI(ctx);
		String nt = "0";
		if (SystemEnvUtil.isWifiEnable(ctx)) {
			nt = "10";
		} else if (imsi == null) {
			nt = "0";
		} else if (imsi.length() > 5) {
			String mnc = imsi.substring(3, 5);
			if (mnc.equals("00") || mnc.equals("02")) {
				nt = "53";
			} else if (mnc.equals("01")) {
				nt = "31";
			} else if (mnc.equals("03")) {
				nt = "32";
			}
		}
		return nt;
	}
	
	
	/**
	 * 获取手机内部可用空间大小
	 * 
	 * @return
	 */
	public static long getAvailableInternalMemorySize() {
		StatFs innerStatFs = new StatFs(Environment.getDataDirectory().getPath());
		long blockSize = innerStatFs.getBlockSize();
		long availableBlocks = innerStatFs.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * 获取手机内部空间大小
	 * 
	 * @return
	 */
	public static long getTotalInternalMemorySize() {
		StatFs innerStatFs = new StatFs(Environment.getDataDirectory().getPath());
		long blockSize = innerStatFs.getBlockSize();
		long totalBlocks = innerStatFs.getBlockCount();
		return totalBlocks * blockSize;
	}
	
	/**
	 * 显示软键盘
	 * @param view
	 */
	public static void showKeyboard(View view) {
		if (null == view)
			return;
		InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, 0);
	}

	/**
	 * 隐藏软键盘
	 * @param view
	 */
	public static void hideKeyboard(View view) {
		if (null == view)
			return;
		InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/**
	 * 隐藏软键盘
	 * @param ctx
	 */
	public static void createHideInputMethod(Activity ctx) {
		final InputMethodManager manager = (InputMethodManager) ctx.getSystemService(Activity.INPUT_METHOD_SERVICE);
		ctx.getWindow().getDecorView().setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (manager.isActive()) {
					manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
				return true;
			}
		});
	}
	
	/**
	 * 判断机型(或固件版本)是否支持google语音识别功能
	 * @return 支持返回true, 否则返回false
	 */
	public static boolean isVoiceRecognitionEnable(Context context) {
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() != 0)
			return true;
		else
			return false;
	}



}
