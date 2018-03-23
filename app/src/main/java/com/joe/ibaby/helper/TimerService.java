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

import com.joe.customlibrary.common.CommonUtils;
import com.joe.customlibrary.rxjava.RxCallable;
import com.joe.customlibrary.rxjava.RxObservable;
import com.joe.customlibrary.utils.FileUtils;
import com.joe.customlibrary.utils.TimeUtils;
import com.joe.ibaby.R;
import com.joe.ibaby.dao.beans.Baby;
import com.joe.ibaby.dao.beans.PackageBean;
import com.joe.ibaby.dao.beans.User;
import com.joe.ibaby.dao.beans.Vaccine;
import com.joe.ibaby.dao.offline.OffLineDataLogic;
import com.joe.ibaby.ui.add.AddBabyActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 病人数据同步服务。网络变化时、登录成功时启动
 * 
 */
public class TimerService extends Service {

	// 15分钟同步一次
	private static long RESTART_FREQUENCY = 1000 * 60;

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

		String currentUserId = PreferenceUtil.INSTANCE.getField(PreferenceUtil.INSTANCE.getCURRENT_USER());
		if (CommonUtils.isTextEmpty(currentUserId)) {
			stopService();
		}else {
			new RxObservable<PackageBean>()
					.getObservableIo(new RxCallable<PackageBean>() {
						@Override
						public PackageBean call() throws Exception {
							return OffLineDataLogic.Companion.getInstance().getUserByIdInPkg(currentUserId);
						}
					})
					.subscribe(new Consumer<PackageBean>() {
						@Override
						public void accept(PackageBean packageBean) throws Exception {
							if (packageBean.isDataRight() && ((User)packageBean.getObj()).getBaby() != null) {
								calculateBabyVaccine((User)packageBean.getObj());
							}else {
								stopService();
							}
						}
					});
		}


		initRestartTime();
		stopServiceAndRestart();

	}

	private void calculateBabyVaccine(User user) {
		List<Vaccine> vaccines = VaccineUtil.INSTANCE.getVaccine1(getApplicationContext());
		List<Vaccine> warnVaccines = new ArrayList<>();
		int babyAge2Day = AppUtil.INSTANCE.getBabyAge(user.getBaby().getBabyBirth());
		for (int i = 0; i < vaccines.size(); i++) {
			//提前3天左右提醒
			if (babyAge2Day <= vaccines.get(i).getAge2day() && babyAge2Day >= vaccines.get(i).getAge2day() - 3) {
				warnVaccines.add(vaccines.get(i));
				//每个年龄段最多同时两个疫苗
				if (i < vaccines.size() -1) {
					if (vaccines.get(i).getAge2day() == vaccines.get(i + 1).getAge2day()) {
						warnVaccines.add(vaccines.get(i + 1));
						break;
					}
				}

				break;
			}
		}

		for (int i = 0; i < warnVaccines.size() ; i++) {
			sendNotification(user, warnVaccines.get(i), i);
		}

	}


	/**
	 * @param user
	 * @param vaccine
	 * @param i
	 */
	private void sendNotification(User user, Vaccine vaccine, int i) {
		Baby baby = user.getBaby();
		String userGender = "";
		if (user.getGender() == 1) {
			userGender = "老爹";
		}else {
			userGender = "老妈";
		}

		//获取NotificationManager实例
		NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		//实例化NotificationCompat.Builde并设置相关属性
		String title = baby.getBabyName() + "需要接种" +vaccine.getVaccine() + "啦~";
		if (!CommonUtils.isTextEmpty(vaccine.getTimes())) {
			title = title + "(" + vaccine.getTimes() + ")";
		}
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setPriority(NotificationManager.IMPORTANCE_MAX)
				.setCategory(Notification.CATEGORY_SYSTEM)
				.setVisibility(Notification.VISIBILITY_PUBLIC)
				.setSmallIcon(R.mipmap.ic_notification_vaccine)
				.setTicker(userGender + ",该给您的" + baby.getBabyName() + "接种疫苗啦~")
				.setContentTitle(title)
				.setContentText(vaccine.getInfo())
				.setAutoCancel(true)
				.setShowWhen(false)
				.setDefaults(Notification.DEFAULT_ALL);
		if (FileUtils.isFileExist(AppUtil.INSTANCE.getBabyPicPath(baby))) {
			NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
			bigPictureStyle.setBigContentTitle(title);
			bigPictureStyle.setSummaryText(vaccine.getInfo());
			Bitmap bigPicture = BitmapFactory.decodeFile(AppUtil.INSTANCE.getBabyPicPath(baby));
			bigPictureStyle.bigPicture(bigPicture);
			builder.setStyle(bigPictureStyle);
		}
		Intent intent = new Intent(this, AddBabyActivity.class);
		intent.putExtra(AppUtil.INSTANCE.getCURRENT_USER(), user);
		PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
		builder.setContentIntent(pendingIntent);
		notifyManager.notify(baby.getAge() + i, builder.build());
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
