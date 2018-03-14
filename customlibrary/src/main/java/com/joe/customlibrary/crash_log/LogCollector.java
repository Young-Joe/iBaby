package com.joe.customlibrary.crash_log;

import android.content.Context;

public class LogCollector {

	private static final String TAG = LogCollector.class.getName();

	private static Context mContext;

	private static boolean isInit = false;

	public static void init(Context c) {

		if (c == null) {
			return;
		}

		if (isInit) {
			return;
		}

		CrashHandler crashHandler = CrashHandler.getInstance(c);
		crashHandler.init();

		isInit = true;

	}

}