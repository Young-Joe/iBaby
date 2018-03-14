package com.joe.customlibrary.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.util.LruCache;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;

public class ImageUtils {  
	private static LruCache<String, Bitmap> mMemoryCache;
	private static ImageUtils instance;

	private ImageUtils() {
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int caseSize = maxMemory / 8;
		mMemoryCache = new LruCache<String, Bitmap>(caseSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
	}

	public void addBitmapToMemoryCache(String key, Bitmap bitMap) {
		if (getBitmapFromMemoryCache(key) == null) {
			addBitmapToMemoryCache(key, bitMap);
		}
	}

	public Bitmap getBitmapFromMemoryCache(String key) {
		return mMemoryCache.get(key);
	}

	public static ImageUtils getInstance() {
		if (instance == null) {
			instance = new ImageUtils();
		}
		return instance;
	}
	
	public static void saveBitmap2SD(String filePath, String fileName, Bitmap bitmap) {
		byte[] datas = bitmapToByte(bitmap);
		saveImage2SD(filePath, fileName, datas);
	}

	public static byte[] bitmapToByte(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		return stream.toByteArray();
	}

	public static void saveImage2SD(String fullPath, String fileName, byte[] data) {
		FileUtils.isFileExistElseCreate(fullPath, fileName);
		try {
			FileOutputStream out = new FileOutputStream(fullPath);
			out.write(data);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static byte[] getBytesFromInputStream(InputStream is, int bufsiz)
			throws IOException {
		int total = 0;
		byte[] bytes = new byte[1024 * 4];
		ByteBuffer bb = ByteBuffer.allocate(bufsiz);

		while (true) {
			int read = is.read(bytes);
			if (read == -1)
				break;
			bb.put(bytes, 0, read);
			total += read;
		}

		byte[] content = new byte[total];
		bb.flip();
		bb.get(content, 0, total);
		return content;
	}

	public static byte[] inputStreamToBytes(InputStream is) throws IOException {
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch;
		while ((ch = is.read()) != -1) {
			bytestream.write(ch);
		}
		byte imgdata[] = bytestream.toByteArray();
		bytestream.close();
		return imgdata;
	}

	/**
	 * 优化图片
	 * 
	 * @param data
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static Bitmap optimizeBitmap(byte[] data, int maxWidth, int maxHeight) {
		Bitmap result = null;
		int length = data.length;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		result = BitmapFactory.decodeByteArray(data, 0, length, options);
		// int widthRatio = (int) Math.ceil(options.outWidth / maxWidth);
		// int heightRatio = (int) Math.ceil(options.outHeight / maxHeight);
		// if (widthRatio > 1 || heightRatio > 1) {
		// if (widthRatio > heightRatio) {
		// options.inSampleSize = widthRatio;
		// } else {
		// options.inSampleSize = heightRatio;
		// }
		// }
		options.inSampleSize = calculateInSampleSize(options, maxWidth,
				maxHeight);
		options.inJustDecodeBounds = false;
		result = BitmapFactory.decodeByteArray(data, 0, length, options);
		return result;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}

	/**
	 * 处理成圆角图片
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,
			final float roundPx) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
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
	 * 处理成圆形图片 未完成
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getRoundCornerBitmap(Bitmap bm) {
		final int color = 0xff424242;
		Bitmap bitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		RectF rectf = new RectF(0F, 0F, bm.getWidth(), bm.getWidth());
		canvas.drawOval(rectf, paint);
		return bitmap;
	}

	public static byte[] getBytesFromInputStreams(InputStream is, int bufsiz)
			throws IOException {
		int total = 0;
		byte[] bytes = new byte[4096];
		ByteBuffer bb = ByteBuffer.allocate(bufsiz);

		while (true) {
			int read = is.read(bytes);
			if (read == -1)
				break;
			bb.put(bytes, 0, read);
			total += read;
		}

		byte[] mByte = new byte[total];
		bb.flip();
		bb.get(mByte, 0, total);
		return mByte;
	}

	public static Bitmap getPicFromBytes(byte[] bytes,
			BitmapFactory.Options opts) {

		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	/**
	 * 销毁图片文件 如果不释放的话，不断取图片，将会内存不够
	 */
	public static void destoryBimap(Bitmap photo) {
		if (photo != null && !photo.isRecycled()) {
			photo.recycle();
			photo = null;
		}
	}

	// 显示网络上的图片
	public static Bitmap returnBitMap(String url) {
		URL myFileUrl = null;

		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();

			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return bitmap;
	}

	public static Bitmap toConformBitmap(Bitmap background, Bitmap foreground) {
		if (background == null) {
			return null;
		}

		int bgWidth = background.getWidth();
		int bgHeight = background.getHeight();
		int fgWidth = foreground.getWidth();
		// int fgHeight = foreground.getHeight();

		// create the new blank bitmap 创建一个新的和SRC长度宽度一样的位图
		Bitmap newbmp = Bitmap.createBitmap(bgWidth + fgWidth, bgHeight,
				Config.ARGB_8888);
		Canvas cv = new Canvas(newbmp);
		// draw bg into
		cv.drawBitmap(background, 0, 0, null);// 在 0，0坐标开始画入bg
		// draw fg into
		cv.drawBitmap(foreground, bgWidth, 0, null);// 在 0，0坐标开始画入fg ，可以从任意位置画入
		// save all clip
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		// store
		cv.restore();// 存储
		return newbmp;
	}
}
