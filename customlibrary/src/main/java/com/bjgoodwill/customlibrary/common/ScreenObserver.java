package com.bjgoodwill.customlibrary.common;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * @author qiaojianfeng
 *		屏幕状态监听 必须动态注册监听才行
 *      通过监听屏幕暗至屏幕解屏(没有解屏的手机默认屏幕亮即为)的时间差判断是否要重新登录
 *
 */
public class ScreenObserver {
	private static String TAG = "ScreenObserver";
	private Context mContext;
	private ScreenBroadcastReceiver mScreenReceiver;
	private ScreenStateListener mScreenStateListener;
	private static Method mReflectScreenState;
	//设置重新登录时间 毫秒级
	public static int ONE_MINUTE_TIME = 60*1000;
	public static int SCREEN_RESTART_TIME = 20*ONE_MINUTE_TIME;

	public ScreenObserver(Context context) {
		mContext = context;
		mScreenReceiver = new ScreenBroadcastReceiver();
		try {
			mReflectScreenState = PowerManager.class.getMethod("isScreenOn",new Class[] {});
		} catch (Exception nsme) {
			Log.d(TAG, "API < 7," + nsme);
		}
	}

	/**
	 * screen状态广播接收者
	 */
	private class ScreenBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
				mScreenStateListener.onScreenOn();
			} else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
				mScreenStateListener.onScreenOff();
			} else if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
				mScreenStateListener.onUserPresent();
			} else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
				if (null != mOnHomeClickListener) {
					mOnHomeClickListener.onHomeClick();
				}
			}
		}
	}

	/**
	 * 请求screen状态更新
	 */
	public void requestScreenStateUpdate(ScreenStateListener listener) {
		mScreenStateListener = listener;
		startScreenBroadcastReceiver();
		firstGetScreenState();
	}

	/**
	 * 第一次请求screen状态
	 */
	private void firstGetScreenState() {
		PowerManager manager = (PowerManager) mContext
				.getSystemService(Activity.POWER_SERVICE);
		if (isScreenOn(manager)) {
			if (mScreenStateListener != null) {
				mScreenStateListener.onScreenOn();
			}
		} else {
			if (mScreenStateListener != null) {
				mScreenStateListener.onScreenOff();
			}
		}
	}

	/**
	 * 停止screen状态更新
	 */
	public void stopScreenStateUpdate() {
		mContext.unregisterReceiver(mScreenReceiver);
	}

	/**
	 * 启动screen状态广播接收器
	 */
	private void startScreenBroadcastReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_USER_PRESENT);
		filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);//home
		mContext.registerReceiver(mScreenReceiver, filter);
	}

	/**
	 * screen是否打开状态
	 */
	private static boolean isScreenOn(PowerManager pm) {
		boolean screenState;
		try {
			screenState = (Boolean) mReflectScreenState.invoke(pm);
		} catch (Exception e) {
			screenState = false;
		}
		return screenState;
	}

	// 外部调用接口
	public interface ScreenStateListener {
		void onScreenOn();

		void onScreenOff();

		void onUserPresent();
	}

	//home键监听
	private OnHomeClickListener mOnHomeClickListener;
	public interface OnHomeClickListener{
		void onHomeClick();
	}

	public void setOnHomeClickListener(OnHomeClickListener onHomeClickListener){
		this.mOnHomeClickListener = onHomeClickListener;
	}

	//判断屏幕是否被锁定
	//因为屏幕在灭了以后是否锁定可以由用户设置，所以我们不能通过跟踪解锁事件来判断其状态，需要一个可以时时获取状态的函数
	public final static boolean isScreenLocked(Context c) {
		KeyguardManager mKeyguardManager = (KeyguardManager) c
				.getSystemService(Context.KEYGUARD_SERVICE);
		return mKeyguardManager.inKeyguardRestrictedInputMode();
	}

}
