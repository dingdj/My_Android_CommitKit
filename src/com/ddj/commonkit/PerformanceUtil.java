/**
 * @author dingdj
 * Date:2014-6-12上午10:57:43
 *
 */
package com.ddj.commonkit;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

/**
 * @author dingdj
 * Date:2014-6-12上午10:57:43
 *
 */
public class PerformanceUtil {
	
	
	public static String toMB(long size){
		return String.format("%.2f", size/(1024*1024f));
	}
	
	/**
	 * 打印出当前进程的内存使用量
	 * @author dingdj
	 * Date:2014-6-12上午10:58:14
	 */
	public static void dumpMemoryUsed(){
		long total = Runtime.getRuntime().totalMemory();
		long free = Runtime.getRuntime().freeMemory();
		long max = Runtime.getRuntime().maxMemory();
		Log.e("PerformanceUtil", "total:"+toMB(total) + "|" +
				"free:"+toMB(free) + "|" + "max:"+
				toMB(max)+"|"+"used:"+toMB(total-free));
	}
	
	
	/**
	 * 获取当前进程名
	 * @param context
	 * @return String
	 */
	public static String getCurProcessName(Context context) {
		try {
			int pid = android.os.Process.myPid();
			ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
				if (appProcess.pid == pid) {
					return appProcess.processName;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

}
