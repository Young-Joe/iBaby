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

import com.joe.customlibrary.common.CommonUtils;
import com.joe.customlibrary.rxjava.RxCallable;
import com.joe.customlibrary.rxjava.RxObservable;
import com.joe.customlibrary.utils.CalendarReminderUtil;
import com.joe.customlibrary.utils.FileUtils;
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
import java.util.Date;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 病人数据同步服务。网络变化时、登录成功时启动
 * 
 */
public class TimerService extends Service {

	// 15分钟同步一次
	private static long RESTART_FREQUENCY = 1000 * 60 * 60 * 12;

	private void setRestartTime(int hour) {
		RESTART_FREQUENCY = 1000 * 60 * 60 * hour;
	}

	@Override
	public void onCreate() {
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

	}

	private void calculateBabyVaccine(User user) {
		List<Vaccine> vaccines = VaccineUtil.INSTANCE.getVaccine1(getApplicationContext());
		//通知提醒 三天前左右
		List<Vaccine> warnVaccines = new ArrayList<>();
		//行程提醒
		List<Vaccine> calendarVaccines = new ArrayList<>();

		int babyAge2Day = AppUtil.INSTANCE.getBabyAge(user.getBaby().getBabyBirth());
		for (int i = 0; i < vaccines.size(); i++) {
			if (babyAge2Day <= vaccines.get(i).getAge2day()) {
				//提前3天左右提醒
				if (babyAge2Day >= vaccines.get(i).getAge2day() - 3) {
					warnVaccines.add(vaccines.get(i));
					//每个年龄段最多同时两个疫苗
					if (i < vaccines.size() - 1) {
						if (vaccines.get(i).getAge2day() == vaccines.get(i + 1).getAge2day()) {
							warnVaccines.add(vaccines.get(i + 1));
							break;
						}
					}
					break;
				}

				//warnVaccines 不为空时已终止循环,再外层給calendarVaccines赋值
				calendarVaccines.add(vaccines.get(i));
				//每个年龄段最多同时两个疫苗
				if (i < vaccines.size() - 1) {
					if (vaccines.get(i).getAge2day() == vaccines.get(i + 1).getAge2day()) {
						calendarVaccines.add(vaccines.get(i + 1));
						break;
					}
				}
				break;
			}
		}

		//遍历通知
		for (int i = 0; i < warnVaccines.size() ; i++) {
			sendNotification(user, warnVaccines.get(i), i);
		}

		//设置行程事件
		if (!CommonUtils.isListEmpty(warnVaccines)) {
			calendarVaccines = warnVaccines;
		}
		for (Vaccine item : calendarVaccines) {
			addCalendarEvent(user, item);
		}

		//设置重复提醒
		if (!CommonUtils.isListEmpty(warnVaccines)) {
			setRestartTime(12);
			stopServiceAndRestart();
		}else {
			stopService();
		}
	}

	/**
	 * @param user
	 * @param vaccine
	 * @param i
	 */
	private void sendNotification(User user, Vaccine vaccine, int i) {
		Baby baby = user.getBaby();
		//获取NotificationManager实例
		NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		//实例化NotificationCompat.Builde并设置相关属性
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setPriority(NotificationManager.IMPORTANCE_MAX)
				.setCategory(Notification.CATEGORY_SYSTEM)
				.setVisibility(Notification.VISIBILITY_PUBLIC)
				.setSmallIcon(R.mipmap.ic_notification_vaccine)
				.setTicker(getAlertTitle(user, vaccine))
				.setContentTitle(getAlertTitle(user, vaccine))
				.setContentText(vaccine.getInfo())
				.setAutoCancel(true)
				.setShowWhen(false)
				.setDefaults(Notification.DEFAULT_ALL);
		if (FileUtils.isFileExist(AppUtil.INSTANCE.getBabyPicPath(baby))) {
			NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
			bigPictureStyle.setBigContentTitle(getAlertTitle(user, vaccine));
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

	private void addCalendarEvent(User user, Vaccine vaccine) {
		CalendarReminderUtil.deleteCalendarEvent(getApplicationContext(), getAlertTitle(user, vaccine));
		Baby baby = user.getBaby();
		SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sdfDateFormat.parse(baby.getBabyBirth());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) + vaccine.getAge2day());
			CalendarReminderUtil.addCalendarEvent(getApplicationContext(), getAlertTitle(user, vaccine), vaccine.getInfo(), calendar.getTime().getTime(), 1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
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

	private String getAlertTitle(User user, Vaccine vaccine) {
		Baby baby = user.getBaby();
		String userGender = "";
		if (user.getGender() == 1) {
			userGender = "老爹";
		}else {
			userGender = "老妈";
		}
		String title = userGender + ",该给您的" + baby.getBabyName() + "接种" + vaccine.getVaccine() + "啦~";
		if (!CommonUtils.isTextEmpty(vaccine.getTimes())) {
			title = title + "(" + vaccine.getTimes() + ")";
		}
		return title;
	}

}
