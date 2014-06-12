/**
 * @author dingdj
 * Date:2014-6-12下午1:35:51
 *
 */
package com.ddj.commonkit.android.apk;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.ddj.commonkit.StringUtils;

/**
 * @author dingdj
 * Date:2014-6-12下午1:35:51
 *
 */
public class ApkUtil {

	/**
	 * 判断apk文件是否能安装
	 * @param context
	 * @param apkFile APK文件的路径
	 * @return boolean
	 */
	public static boolean checkApkIfValidity(Context context, String apkFile) {
		if (StringUtils.isNotEmpty(apkFile)){
			final PackageManager pm = context.getPackageManager();
			final android.content.pm.PackageInfo pInfo = pm.getPackageArchiveInfo(apkFile, 0);
			if (pInfo == null) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取未安装APK的版本信息
	 * @param context
	 * @param archiveFilePath APK文件的路径。如：/sdcard/PandaHome2/Downloads/XX.apk
	 * @return 成功返回版本号，失败返回-1
	 */
	public static int getUninatllApkInfo(Context context, String archiveFilePath) {
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
		if (info != null) {
			return info.versionCode;
		}
		return -1;
	}
	
	/**
	 * 判断安装的应用与apk包是否是同一个应用
	 * @param context
	 * @param packageName 安装的应用的包名
	 * @param apkFilePath apk包的路径
	 * @return boolean
	 */
	public static boolean isApkInstalled(Context context, String packageName, String apkFilePath) {
		try {
			PackageManager pm = context.getPackageManager();
			//根据包名获取系统中已安装的packageInfo信息
			PackageInfo installedPkgInfo = pm.getPackageInfo(packageName, 0);
			// APK包的应用信息
			final PackageInfo dlkPkgInfo = getPackageByArchiveFile(context, apkFilePath);
			if (installedPkgInfo.packageName.equals(dlkPkgInfo.packageName) && installedPkgInfo.versionCode == dlkPkgInfo.versionCode)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	
	/**
	 * 是否已安装apk
	 * @param ctx
	 * @param apkFile apk包文件夹路径
	 * @return 已安装返回true，未安装或异常时返回false
	 */
	public static boolean isApkInstalled(Context ctx, String apkFile) {
		if (!StringUtils.isNotEmpty(apkFile))
			return false;
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo apkPackageInfo = pm.getPackageArchiveInfo(apkFile, 0);
			//根据包名获取系统中已安装的packageInfo信息
			PackageInfo packageInfo = pm.getPackageInfo(apkPackageInfo.packageName, 0);
			if (apkPackageInfo.versionCode == packageInfo.versionCode)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 应用是否已经安装
	 * 
	 * @param pkgName
	 * @return
	 */
	public boolean isAppInstalled(Context context, String pkgName) {
		try {
			PackageInfo pkgInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
			if (pkgInfo != null)
				return true;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 获取apk包信息
	 * @param ctx
	 * @param apkFile apk包文件夹路径
	 * @return 没有获取到或异常时返回null
	 */
	public static android.content.pm.PackageInfo getPackageByArchiveFile(Context ctx, String apkFile) {
		if (StringUtils.isNotEmpty(apkFile)) {
			final PackageManager pm = ctx.getPackageManager();
			final android.content.pm.PackageInfo pInfo = pm.getPackageArchiveInfo(apkFile, 0);
			if (pInfo != null)
				return pInfo;
		}
		return null;
	}
	
	/**
	 * 获取安装包的唯一结识
	 * @param context
	 * @param apkFilePath
	 * @return 包名+版本号
	 */
	public static String getApkFileKey(Context context, String apkFilePath) {
		String key = null;
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo info = pm.getPackageArchiveInfo(apkFilePath, 0);
			key = info.packageName + info.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (TextUtils.isEmpty(key))
			key = apkFilePath;

		return key;
	}

	/**
	 * 获取apk包的PackageInfo
	 * @param context
	 * @param apkFilePath
	 * @return 没有获取到或异常时返回null
	 */
	public static PackageInfo getApkFilePackageInfo(Context context, String apkFilePath) {
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo info = pm.getPackageArchiveInfo(apkFilePath, 0);
			return info;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 利用反射机制构建一个未安装apk的Resource对象，来访问apk内的资源
	 * @param apkPath
	 * @param ctx
	 * @return 返回apk的Resource对象，异常时返回null
	 */
	public static Resources getUninstallAPKResources(String apkPath, Context ctx) {
		String PATH_AssetManager = "android.content.res.AssetManager";
		try {
			Class<?>[] typeArgs = new Class[1];
			typeArgs[0] = String.class;
			Object[] valueArgs = new Object[1];
			valueArgs[0] = apkPath;
			DisplayMetrics metrics = new DisplayMetrics();
			metrics.setToDefaults();
			typeArgs = new Class[4];
			typeArgs[0] = File.class;
			typeArgs[1] = String.class;
			typeArgs[2] = DisplayMetrics.class;
			typeArgs[3] = Integer.TYPE;
			Class<?> assetMagCls = Class.forName(PATH_AssetManager);
			Constructor<?> assetMagCt = assetMagCls.getConstructor((Class[]) null);
			Object assetMag = assetMagCt.newInstance((Object[]) null);
			typeArgs = new Class[1];
			typeArgs[0] = String.class;
			Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod("addAssetPath", typeArgs);
			valueArgs = new Object[1];
			valueArgs[0] = apkPath;
			assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);
			Resources res = ctx.getResources();
			typeArgs = new Class[3];
			typeArgs[0] = assetMag.getClass();
			typeArgs[1] = res.getDisplayMetrics().getClass();
			typeArgs[2] = res.getConfiguration().getClass();
			Constructor<?> resCt = Resources.class.getConstructor(typeArgs);
			valueArgs = new Object[3];
			valueArgs[0] = assetMag;
			valueArgs[1] = res.getDisplayMetrics();
			valueArgs[2] = res.getConfiguration();
			res = (Resources) resCt.newInstance(valueArgs);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 安装应用程序,普通安装方式
	 * @param ctx
	 * @param mainFile
	 * @return boolean
	 */
	public static boolean installApplicationNormal(Context ctx, File mainFile) {
		try {
			Uri data = Uri.fromFile(mainFile);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(data, "application/vnd.android.package-archive");
			ctx.startActivity(intent);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 是不是系统应用
	 * @param appInfo
	 * @return boolean
	 */
	public static boolean isSystemApplication(ApplicationInfo appInfo) {
		if (appInfo == null)
			return false;
		
		return isSystemApplication(appInfo.flags);
	}
	
	/**
	 * 是不是系统应用
	 * @author dingdj
	 * Date:2014-6-12下午5:31:40
	 *  @param flags
	 *  @return
	 */
	public static boolean isSystemApplication(int flags) {
		if ((flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0)
			return true;
		else if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0)
			return true;

		return false;
	}
}
