/**
 * @author dingdj
 * Date:2014-6-12下午1:59:47
 *
 */
package com.ddj.commonkit.android.image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import com.ddj.commonkit.FileUtil;
import com.ddj.commonkit.StringUtils;


/**
 * @author dingdj
 * Date:2014-6-12下午1:59:47
 *
 */
public class ImageUtil extends FileUtil{
	
	public static final String TAG = "ImageUtil";
	
	/**
	 * 先通过BitmapFactory.decodeStream方法，创建出一个bitmap，再将其设为ImageView的 source，
	 * decodeStream最大的秘密在于其直接调用JNI>>nativeDecodeAsset()来完成decode，
	 * 无需再使用java层的createBitmap，从而节省了java层的空间。
	 * 如果在读取时加上图片的Config参数，可以跟有效减少加载的内存，从而跟有效阻止抛out of Memory异常
	 * 另外，decodeStream直接拿的图片来读取字节码了， 不会根据机器的各种分辨率来自动适应
	 * 使用了decodeStream之后，需要在hdpi和mdpi，ldpi中配置相应的图片资源，
	 * 否则在不同分辨率机器上都是同样大小（像素点数量），显示出来的大小就不对了
	 * @param context
	 * @param resId 资源Id
	 * @param insampleSize 缩小比例
	 * @return Bitmap
	 */
	public static Bitmap createBitmapFormResId(Context context, int resId, int insampleSize) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = false;
		opts.inSampleSize = insampleSize;
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opts);
	}
	
	/**
	 * 以最省内存的方式读取图片资源
	 * @param context
	 * @param resId
	 * @return 16位的图
	 */
	public static Bitmap createThumbnailBitmapFromResId(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取图片资源
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}
	
	/**
	 * 详见decodeStreamABitmap(Context context, int resId, int insampleSize)
	 * @param context
	 * @param resId
	 * @param targetWidth
	 * @param targetHeight
	 * @return Bitmap
	 */
	public static Bitmap createExpectBitmapFromResId(Context context, int resId, int targetWidth, int targetHeight) {
		InputStream is = context.getResources().openRawResource(resId);
		int[] imageWH = getImageWH(is);
		if (imageWH == null)
			return null;

		int scale = 1;
		if (imageWH[0] > imageWH[1])
			scale = Math.round((float) imageWH[0] / targetWidth);
		else
			scale = Math.round((float) imageWH[1] / targetHeight);
		scale = scale == 0 ? 1 : scale;

		return createBitmapFormResId(context, resId, scale);
	}
	
	/**
	 * 图片圆角处理
	 * @param bitmap
	 * @param roundPx 值越大 圆角越大
	 * @return Bitmap
	 */
	public static Bitmap createRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 销毁Drawable，回收系统资源
	 * @param drawable
	 */
	public static void safeDestoryDrawable(Drawable drawable) {
		if (null == drawable)
			return;
		drawable.setCallback(null);
		drawable = null;
	}
	
	/**
	 * 将一个新图片合成到旧图片里
	 * @param src 旧图片
	 * @param dst 新图片
	 * @return Bitmap
	 */
	public static Bitmap craeteComposeBitmap(Bitmap src, Bitmap dst) {
		Bitmap newb = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Config.ARGB_8888);
		Canvas cv = new Canvas(newb);
		cv.drawBitmap(src, 0, 0, null);
		cv.drawBitmap(dst, 0, 0, null);
		return newb;
	}
	
	/**
	 * 生成指定大小的Bitmap
	 * @param bitmapOrg
	 * @param newWidth
	 * @param newHeight
	 * @return Bitmap
	 */
	public static Bitmap createExpectSizeBitmap(Bitmap bitmapOrg, int newWidth, int newHeight) {

		if (null == bitmapOrg) {
			return null;
		}

		// 获取这个图片的宽和高
		int w = bitmapOrg.getWidth();
		int h = bitmapOrg.getHeight();

		int x, y = 0;

		int wTemp = newWidth * h / newHeight;
		if (wTemp > w) {
			// 以宽度
			h = newHeight * w / newWidth;
			x = 0;
			y = (bitmapOrg.getHeight() - h) / 2;
		} else {
			w = wTemp;
			y = 0;
			x = (bitmapOrg.getWidth() - wTemp) / 2;
		}

		float scaleWidth, scaleHeight = 0;

		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		Bitmap resizedBitmap;

		// 将整个头像按比例缩放绘制到屏幕中
		// 计算缩放率，新尺寸除原始尺寸
		scaleWidth = ((float) newWidth) / w;
		scaleHeight = ((float) newHeight) / h;

		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);

		// 创建新的图片
		resizedBitmap = Bitmap.createBitmap(bitmapOrg, x, y, w, h, matrix, true);
		
		return resizedBitmap;
	}
	
	/**
	 * 获取图片文件，指定大小来获取，如果指定的大小超过原图尺寸，则按原图尺寸返回
	 * @param contentUri 图片资源
	 * @param ctx
	 * @param targetWidth
	 * @param targetHeight
	 * @return Bitmap
	 * @throws Exception
	 */
	public static Bitmap getImageFile(Uri contentUri, Context ctx, int targetWidth, int targetHeight) throws Exception {
		Bitmap tmpBmp = null;
		try {
			if (contentUri == null)// 文件不存在
				return null;
			ContentResolver cr = ctx.getContentResolver();
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// 先测量图片的尺寸
			if (contentUri.toString().indexOf("content") != -1)
				BitmapFactory.decodeStream(cr.openInputStream(contentUri), null, opts);
			else
				BitmapFactory.decodeFile(contentUri.toString(), opts);

			int imWidth = opts.outWidth; // 图片宽
			int imHeight = opts.outHeight; // 图片高

			int scale = 1;
			if (imWidth > imHeight)
				scale = Math.round((float) imWidth / targetWidth);
			else
				scale = Math.round((float) imHeight / targetHeight);
			scale = scale == 0 ? 1 : scale;

			opts.inJustDecodeBounds = false;
			opts.inSampleSize = scale;
			if (contentUri.toString().indexOf("content") != -1)
				tmpBmp = BitmapFactory.decodeStream(cr.openInputStream(contentUri), null, opts);
			else {
				FileInputStream fis = new FileInputStream(new File(contentUri.toString()));
				tmpBmp = BitmapFactory.decodeStream(fis, null, opts);
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return tmpBmp;
	}

	/**
	 * 复制图片 
	 * @param bitmapOrg
	 * @return Bitmap
	 */
	public static Bitmap copyBitmap(Bitmap bitmapOrg) {
		if (null == bitmapOrg)
			return null;
		Bitmap resultBitmap = Bitmap.createScaledBitmap(bitmapOrg, bitmapOrg.getWidth(), bitmapOrg.getHeight(), true);
		Canvas canvas = new Canvas();
		canvas.setBitmap(resultBitmap);
		Paint paint = new Paint();
		paint.setFilterBitmap(true);
		paint.setAntiAlias(true);
		canvas.drawBitmap(bitmapOrg, 0, 0, paint);
		return resultBitmap;
	}
	
	/**
	 * 根据指定大小，缩放Bitmap
	 * @param bmp 被缩放的bitmap
	 * @param newWidth bitmap缩放后的宽度
	 * @param newHeiht bitmap缩放后的高度
	 * @return Bitmap
	 */
	public static Bitmap resizeImage(Bitmap bmp, int newWidth, int newHeiht) {
		if (bmp == null) {
			return null;
		}

		int originWidth = bmp.getWidth();
		int originHeight = bmp.getHeight();
		if (originWidth == newWidth && originHeight == newHeiht)
			return bmp;

		float scaleWidth = ((float) newWidth) / originWidth;
		float scaleHeight = ((float) newHeiht) / originHeight;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizeBitmap = Bitmap.createBitmap(bmp, 0, 0, originWidth, originHeight, matrix, true);
		return resizeBitmap;
	}
	
	/**
	 * 根据指定缩放比例，缩放Bitmap
	 * @param bmp 被缩放的bitmap
	 * @param scale 缩放比例
	 * @return Bitmap
	 */
	public static Bitmap resizeImage(Bitmap bmp, float scale) {
		if (bmp == null) {
			return null;
		}

		int originWidth = bmp.getWidth();
		int originHeight = bmp.getHeight();

		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		return Bitmap.createBitmap(bmp, 0, 0, originWidth, originHeight, matrix, true);
	}
	
	
	
	
	/**
	 * 获取图像的宽高
	 * @param path 图像路径
	 * @return 失败返回{ -1, -1 }
	 */
	public static int[] getImageWH(String path) {
		int[] wh = { -1, -1 };
		if (path == null) {
			return wh;
		}
		File file = new File(path);
		if (file.exists() && !file.isDirectory()) {
			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				InputStream is = new FileInputStream(path);
				BitmapFactory.decodeStream(is, null, options);
				wh[0] = options.outWidth;
				wh[1] = options.outHeight;
			} catch (Throwable e) {
				Log.w(TAG, "getImageWH Throwable.", e);
			}
		}
		return wh;
	}

	/**
	 * 获取图像的宽高
	 * @param is
	 * @return 失败返回{ -1, -1 }
	 */
	public static int[] getImageWH(InputStream is) {
		int[] wh = { -1, -1 };
		if (is == null) {
			return wh;
		}
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;

			BitmapFactory.decodeStream(is, null, options);
			wh[0] = options.outWidth;
			wh[1] = options.outHeight;
		} catch (Throwable e) {
			Log.w(TAG, "getImageWH Throwable.", e);
		}
		return wh;
	}

	
	/**
	 * 销毁Bitmap，回收资源
	 * @param bmp
	 */
	public static void destoryBitmap(Bitmap bmp) {
		if (null != bmp && !bmp.isRecycled()) {
			bmp.recycle();
			bmp = null;
		}
	}
	
	/**
	 * 将drawable转换为bitmap
	 * @param drawable
	 * @return Bitmap
	 */
	public static Bitmap drawable2Bitmap(Drawable drawable) {
		if (null == drawable || drawable.getIntrinsicWidth() < 0 || drawable.getIntrinsicHeight() < 0)
			return null;
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 将bitmap转换为byte[]
	 * @param bm
	 * @return byte[]
	 */
	public static byte[] bitmap2Bytes(Bitmap bm) {
		if(null == bm) return null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
			byte[] result = baos.toByteArray();
			return result;
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 将drawable转换为byte[]
	 * @param drawable
	 * @return byte[]
	 */
	public static byte[] bitmapDrawable2Bytes(BitmapDrawable drawable) {
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			((BitmapDrawable) drawable).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos);
			byte[] result = baos.toByteArray();
			return result;
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 将byte[]转换为bitmap
	 * @param b
	 * @return 失败返回null
	 */
	public static Bitmap bytes2Bitmap(byte[] b) {
		if (b != null && b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}
	
	/**
	 * 根据名称得到Drawable
	 * @param ctx
	 * @param resName
	 * @return Drawable
	 */
	public static Drawable getDrawableByResourceName(Context ctx, String resName) {
		if (!StringUtils.isNotEmpty(resName))
			return null;

		Resources res = ctx.getResources();
		int resId = res.getIdentifier(resName, "drawable", ctx.getPackageName());
		if (resId == 0)
			return null;

		return res.getDrawable(resId);
	}

	/**
	 * 根据名称得到Bitmap
	 * @param ctx
	 * @param resName
	 * @return Bitmap
	 */
	public static Bitmap getBitmapByResourceName(Context ctx, String resName) {
		if (!StringUtils.isNotEmpty(resName))
			return null;

		Resources res = ctx.getResources();
		int resId = res.getIdentifier(resName, "drawable", ctx.getPackageName());
		if (resId == 0)
			return null;

		return ((BitmapDrawable) res.getDrawable(resId)).getBitmap();
	}
	
	/**
	 * <br>Description:获取资源ID
	 * <br>Date:2014-3-31上午11:43:10
	 * @param ctx
	 * @param key
	 * @param type
	 * @return
	 */
	public static int getResourceId(Context ctx, String key, String type) {
        return ctx.getResources().getIdentifier(key, type, ctx.getPackageName());
    }

	

}
