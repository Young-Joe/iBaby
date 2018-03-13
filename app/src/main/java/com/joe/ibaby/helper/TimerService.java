package com.joe.ibaby.helper;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.joe.customlibrary.utils.TimeUtils;
import com.joe.ibaby.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 病人数据同步服务。网络变化时、登录成功时启动
 * 
 */
public class TimerService extends Service {

	// 15分钟同步一次
	private static long RESTART_FREQUENCY = 1000 * 5;

	/**
	 * 移动网络同步时间为 60分
	 * 无线网络同步时间为 2分
	 */
	private void initRestartTime(){
//		int netState = CommonUtils.getNetworkState(getApplicationContext());
//		if(netState == ConnectivityManager.TYPE_MOBILE){
//			RESTART_FREQUENCY = 1000 * 60 * 60;
//		}else if(netState == ConnectivityManager.TYPE_WIFI){
//			RESTART_FREQUENCY = 1000 * 60 * 1;
//		}else {
//			stopService();
//		}
	}

	@Override
	public void onCreate() {
		Log.e("Joe--->", "TimerService : " + TimeUtils.getCurrentTime());

		initRestartTime();
		//TODO intentService
		stopServiceAndRestart();

		sendNotification();
	}

	/**
	 *
	 */
	private void sendNotification() {
		//获取NotificationManager实例
		NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		//实例化NotificationCompat.Builde并设置相关属性
//		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//				//设置小图标
//				.setSmallIcon(R.mipmap.ic_launcher)
//				//设置通知标题
//				.setContentTitle("最简单的Notification")
//				//设置通知内容
//				.setContentText("只有小图标、标题、内容")
//				.setDefaults(Notification.DEFAULT_ALL)
//				.setOngoing(true)
//				.setAutoCancel(true);
//		//设置通知时间，默认为系统发出通知的时间，通常不用设置
//		//.setWhen(System.currentTimeMillis());
//		//通过builder.build()方法生成Notification对象,并发送通知,id=1
//		notifyManager.notify(1, builder.build());

		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
		bigPictureStyle.setBigContentTitle("Title");
		bigPictureStyle.setSummaryText("SummaryText");
		Bitmap bigPicture = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
		bigPictureStyle.bigPicture(bigPicture);
		bigPictureStyle.bigLargeIcon(bigPicture);
		builder.setSmallIcon(R.mipmap.ic_launcher)
				.setTicker("PicNotification")
				.setContentTitle("最简单的Notification")
				.setContentText("只有小图标、标题、内容")
				.setStyle(bigPictureStyle)
				.setAutoCancel(true)
				.setDefaults(Notification.DEFAULT_ALL);
		notifyManager.notify(1, builder.build());
	}


	private void stopServiceAndRestart() {
		setRestartServiceTime();
		stopService();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void stopService() {
		stopSelf();
	}

	private final IBinder mBinder = new Binder() {
		@Override
		protected boolean onTransact(int code, Parcel data, Parcel reply,
				int flags) throws RemoteException {
			return super.onTransact(code, data, reply, flags);
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private void setRestartServiceTime() {
		Intent myIntent = new Intent(this, TimerService.class);
		PendingIntent mAlarmSender = PendingIntent.getService(this, 0,
				myIntent, 0);
		long current = SystemClock.elapsedRealtime();
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(mAlarmSender);
		alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, current
				+ RESTART_FREQUENCY, mAlarmSender);
	}

	private boolean shouldSynchDaily() {
		// 如果正在同步字典，就不再同步
		SimpleDateFormat currDateSdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat syncDateSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long currTimeMillis = System.currentTimeMillis();
		String syncTime = "00:00";
		String syncDate = currDateSdf.format(currTimeMillis) + " " + syncTime;
		Calendar syncCalendar = Calendar.getInstance();
		try {
			syncCalendar.setTime(syncDateSdf.parse(syncDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (currTimeMillis > syncCalendar.getTimeInMillis() - RESTART_FREQUENCY
				&& currTimeMillis < syncCalendar.getTimeInMillis()
						+ RESTART_FREQUENCY) {
			return true;
		}
		return false;
	}

}
