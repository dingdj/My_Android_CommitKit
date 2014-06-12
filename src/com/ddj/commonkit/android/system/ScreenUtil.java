/**
 * @author dingdj
 * Date:2014-6-12上午11:31:16
 *
 */
package com.ddj.commonkit.android.system;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @author dingdj
 * Date:2014-6-12上午11:31:16
 *
 */
public class ScreenUtil {
	
	private static float currentDensity = 0;
	
	private static float scaledDensity = 0;
	
	/**
	 * 返回屏幕分辨率,字符串型。如 320x480
	 * 
	 * @param ctx
	 * @return String
	 */
	public static String getScreenResolution(Context ctx) {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) ctx.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		String resolution = width + "x" + height;
		return resolution;
	}

	/**
	 * 返回屏幕分辨率,数组型
	 * 
	 * @param ctx
	 * @return int[]
	 */
	public static int[] getScreenResolutionXY(Context ctx) {
		int[] resolutionXY = new int[2];
		if (resolutionXY[0] != 0) {
			return resolutionXY;
		}
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) ctx.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metrics);
		resolutionXY[0] = metrics.widthPixels;
		resolutionXY[1] = metrics.heightPixels;
		return resolutionXY;
	}

	/**
	 * 返回屏幕密度
	 * 
	 * @param ctx
	 * @return float
	 */
	public static float getScreenDensity(Context ctx) {
		return ctx.getResources().getDisplayMetrics().density;
	}
	
	/**
	 * 返回屏幕尺寸(宽)
	 * @param context
	 * @return int
	 */
	public static int getCurrentScreenWidth(Context context) {
		DisplayMetrics metrics = getDisplayMetrics(context);
		boolean isLand = isOrientationLandscape(context);
		if (isLand) {
			return metrics.heightPixels;
		}
		return metrics.widthPixels;
	}

	/**
	 * 返回屏幕尺寸(高)
	 * @param context
	 * @return int
	 */
	public static int getCurrentScreenHeight(Context context) {
		DisplayMetrics metrics = getDisplayMetrics(context);
		boolean isLand = isOrientationLandscape(context);
		if (isLand) {
			return metrics.widthPixels;
		}
		return metrics.heightPixels;
	}
	
	/**
	 * 返回屏幕尺寸
	 * @param context
	 * @return DisplayMetrics
	 */
	public static DisplayMetrics getDisplayMetrics(Context context) {
		return context.getResources().getDisplayMetrics();
	}

	/**
	 * 判断是否横屏
	 * @param context
	 * @return boolean
	 */
	public static boolean isOrientationLandscape(Context context) {
		if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			return true;
		}
		return false;
	}
	
	/**
	 * dp转px
	 * @param context
	 * @param dipValue
	 * @return int
	 */
	public static int dip2px(Context context, float dipValue) {
		if (currentDensity > 0)
			return (int) (dipValue * currentDensity + 0.5f);

		currentDensity = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * currentDensity + 0.5f);
	}
	
	/**
	 * sp转px
	 * @param context
	 * @param spValue
	 * @return int
	 */
	public static int sp2px(Context context, float spValue) {
		if (scaledDensity > 0)
			return (int) (spValue * scaledDensity + 0.5f);

		scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * scaledDensity + 0.5f);
	}

}
