package com.joe.customlibrary.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

	public static boolean isTopActivity(Activity activity) {
		String packageName = activity.getPackageName();
		ActivityManager activityManager = (ActivityManager) activity
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
		if (tasksInfo.size() > 0) {
			if (packageName.equals(tasksInfo.get(0).topActivity
					.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	public static boolean isTextEmpty(String input) {
		if (null == input || input.trim().length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 *判断是否为空,为空返回 " "
	 */
	public static String isTextNull(String input){
		if (null == input || input.trim().length() == 0) {
			return " ";
		}else{
			return input;
		}
	}

	public static <T> boolean isListEmpty(List<T> inputs) {
		if (null == inputs || inputs.isEmpty()) {
			return true;
		}
		return false;
	}


	/**
	 * 将对象序列化成byte数组
	 *
	 * @param obj
	 * @return
	 */
	public static byte[] toByteArray(Object obj) {
		byte[] bytes = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			bytes = bos.toByteArray();
			oos.close();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes;
	}

	/**
	 *
	 *
	 * 将byte数组反序列化
	 *
	 * @param bytes
	 * @return
	 */
	public static Object toObject(byte[] bytes) {
		Object obj = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
			obj = ois.readObject();

			ois.close();
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * 使用java正则表达式去掉多余的.与0
	 *
	 * @param s
	 * @return
	 */
	public static String subZeroAndDot(String s) {
		if (s.indexOf(".") > 0) {
			s = s.replaceAll("0+?$", "");// 去掉多余的0
			s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
		}
		return s;
	}

	/**
	 * 判断是否是正整数和正实数
	 *
	 * @param str
	 * @return
	 */
	public static boolean isIntegerOrPositiveDecial(String str) {
		if (CommonUtils.isTextEmpty(str)) {
			return false;
		}
		Pattern pattern1 = Pattern.compile("^\\+{0,1}[1-9]\\d*");
		Pattern pattern2 = Pattern
				.compile("\\+{0,1}[0]\\.[1-9]*|\\+{0,1}[1-9]\\d*\\.\\d*");
		Matcher matcher1 = pattern1.matcher(str.trim());
		Matcher matcher2 = pattern2.matcher(str.trim());
		if (matcher1.matches()) {
			return true;
		} else if (matcher2.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNetworkAvailabe(Context context) {
		ConnectivityManager conMng = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobNetInfo = conMng.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiInfo = conMng.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if((mobNetInfo != null && mobNetInfo.isConnected()) ||
				(wifiInfo != null && wifiInfo.isConnected())){
			return true;
		}
		return false;
	}

	/**
	 * 获取网络状态
	 *   移动网络 0,无线 1, 其他 -1
	 * @param context
	 * @return
     */
	public static int getNetworkState(Context context){
		ConnectivityManager conMng = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobNetInfo = conMng.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiInfo = conMng.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(mobNetInfo != null && mobNetInfo.isConnected()){
			return ConnectivityManager.TYPE_MOBILE;
		}else if(wifiInfo != null && wifiInfo.isConnected()){
			return ConnectivityManager.TYPE_WIFI;
		}else{
			return -1;
		}
	}


	/**
	 * 数组拼接成字符串
	 * @param array
	 * @param separator
	 * @return
	 */
    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        int arraySize = array.length;
        int bufSize = (arraySize == 0 ? 0 : ((array[0] == null ? 16 : array[0].toString().length()) + 1) * arraySize);
        StringBuffer buf = new StringBuffer(bufSize);
        for (int i = 0; i < arraySize; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }


    /**
     * 设置Pop可通过点击空白区域|返回键|menu键触发消失
     *
     */
    public static void SetPopCancle(final PopupWindow popupWindow){
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.getContentView().setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == 0
						|| event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					popupWindow.dismiss();
				}
				return false;
			}
		});
		/**
		 * 1.解决再次点击MENU键无反应问题 2.sub_view是PopupWindow的子View
		 */
    	popupWindow.getContentView().setFocusableInTouchMode(true);
    	popupWindow.getContentView().setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_BACK&&popupWindow.isShowing()){
					popupWindow.dismiss();
					return true;
				}
				if(keyCode == KeyEvent.KEYCODE_MENU&&popupWindow.isShowing()){
					popupWindow.dismiss();
					return true;
				}
				return false;
			}
		});
    }

	/**
	 * 判断字符串是否为数字
	 * @param str
	 * @return
     */
	public static boolean isNumeric(String str){
		if(isTextEmpty(str)){
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if( !isNum.matches() ){
			return false;
		}
		return true;
	}

	/**
	 *
	 * @param dialog
	 * @param context ->Activity
	 */
	public static void dismiss(Dialog dialog , Context context){
		if (dialog != null && CommonUtils.isValidContext(context)) {
			dialog.dismiss();
		}
	}

	/**
	 * 判断Activity是否已销毁
	 * a.isDestroyed()
	 * @param c
	 * @return
     */
	public static boolean isValidContext (Context c){
		Activity a = (Activity)c;
		if (a.isFinishing()){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * 字符序列转换为16进制字符串
	 * @param src
	 * @return
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();

		if (src == null || src.length <= 0) {
			return null;
		}
		char[] buffer = new char[2];
		for (int i = 0; i < src.length; i++) {
			buffer[0] = Character.toUpperCase(Character.forDigit(
					(src[i] >>> 4) & 0x0F, 16));
			buffer[1] = Character.toUpperCase(Character.forDigit(src[i] & 0x0F,
					16));
			System.out.println(buffer);
			stringBuilder.append(buffer);
		}
		return stringBuilder.toString();
	}

	public static <T extends AppCompatActivity> void turn2Activity(Context context, Class<T> t) {
		Intent intent = new Intent(context, t);
		context.startActivity(intent);
	}

}
