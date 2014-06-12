/**
 * @author dingdj
 * Date:2014-6-12上午11:32:07
 *
 */
package com.ddj.commonkit.android.system;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;

import com.google.android.mms.ContentType;

/**
 * @author dingdj Date:2014-6-12上午11:32:07
 * 
 */
public class SDCardUtil {

	public static String SDCART_PATH = "/mnt/sdcard/";

	/**
	 * 判断SD卡是否存在
	 * 
	 * @return true:存在
	 */
	public static boolean isSdExsit() {
		boolean b1 = android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
		File file = new File(SDCART_PATH);
		boolean b2 = file.exists();
		return b1 && b2;
	}

	/**
	 * 获取手机外部可用空间大小
	 * 
	 * @return
	 */
	public static long getAvailableExternalMemorySize() {
		if (isSdExsit()) {
			StatFs extralStatFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
			long blockSize = extralStatFs.getBlockSize();
			long availableBlocks = extralStatFs.getAvailableBlocks();
			return availableBlocks * blockSize;
		} else {
			return -1;
		}
	}

	/**
	 * 获取手机外部空间大小
	 * 
	 * @return
	 */
	public static long getTotalExternalMemorySize() {
		if (isSdExsit()) {
			StatFs extralStatFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
			long blockSize = extralStatFs.getBlockSize();
			long totalBlocks = extralStatFs.getBlockCount();
			return totalBlocks * blockSize;
		} else {
			return -1;
		}
	}

	/**
	 * 扫描SDCARD的指定目录 如动态生成图片到SDCARD，但是相册中看不见，是因为尚未被扫描到系统多媒体库中。
	 * 
	 * @param dir
	 *            目录
	 */
	public static void scannerSdcardDir(final String dir, final Context context) {
		try {
			File dFile = new File(dir);
			if (dFile.exists() && dFile.isDirectory()) {
				File[] fileList = dFile.listFiles();
				for (File file : fileList) {
					scannerSdcardFile(file.getAbsolutePath(), context);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 扫描SDCARD的指定文件，使其生效 如动态生成图片到SDCARD，但是相册中看不见，是因为尚未被扫描到系统多媒体库中。
	 * 
	 * @param filePath
	 */
	public static void scannerSdcardFile(String filePath, Context context) {
		int sdkLevel = VersionUtil.getApiLevel();
		if (sdkLevel > 7)
			MediaScannerConnection.scanFile(context, new String[] { filePath }, new String[] { ContentType.IMAGE_JPG, ContentType.IMAGE_JPEG }, null);
		else {
			Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri uri = Uri.fromFile(new File(filePath));
			intent.setData(uri);
			context.sendBroadcast(intent);
		}
	}

}
