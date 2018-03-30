package com.joe.customlibrary.crash_log;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.joe.customlibrary.utils.TimeUtils;

import java.io.File;
import java.io.FileOutputStream;

public class LogFileStorage {

	private static final String TAG = LogFileStorage.class.getName();

	public static final String APP_FILE = "iBaby";

	public static final String LOG_SUFFIX = "崩溃日志.txt";
	
	private static final String CHARSET = "UTF-8";

	private static LogFileStorage sInstance;

	private Context mContext;

	private LogFileStorage(Context ctx) {
		mContext = ctx.getApplicationContext();
	}

	public static synchronized LogFileStorage getInstance(Context ctx) {
		if (ctx == null) {
			return null;
		}
		if (sInstance == null) {
			sInstance = new LogFileStorage(ctx);
		}
		return sInstance;
	}
	
//	public File getUploadLogFile(){
//		File dir = mContext.getFilesDir();
//		File logFile = new File(dir, LogCollectorUtility.getMid(mContext)
//				+ LOG_SUFFIX);
//		if(logFile.exists()){
//			return logFile;
//		}else{
//			return null;
//		}
//	}
//	
//	public boolean deleteUploadLogFile(){
//		File dir = mContext.getFilesDir();
//		File logFile = new File(dir, LogCollectorUtility.getMid(mContext)
//				+ LOG_SUFFIX);
//		return logFile.delete();
//	}

	public boolean saveLogFile2Internal(String logString) {
		try {
			File dir = mContext.getFilesDir();
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File logFile = new File(dir, LogCollectorUtility.getMid(mContext)
					+ LOG_SUFFIX);
			FileOutputStream fos = new FileOutputStream(logFile , true);
			fos.write(logString.getBytes(CHARSET));
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean saveLogFile2SDcard(String logString, boolean isAppend) {
		if (!LogCollectorUtility.isSDcardExsit()) {
			return false;
		}
		try {
			File logDir = getExternalLogDir();
			if (!logDir.exists()) {
				logDir.mkdirs();
			}
			
			File logFile = new File(logDir,LOG_SUFFIX);
			/*if (!isAppend) {
				if (logFile.exists() && !logFile.isFile())
					logFile.delete();
			}*/
			FileOutputStream fos = new FileOutputStream(logFile , isAppend);
			fos.write(logString.getBytes(CHARSET));
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "saveLogFile2SDcard failed!");
			return false;
		}
		return true;
	}

	private File getExternalLogDir() {
		File logDir = LogCollectorUtility.getExternalDir(mContext);
		return logDir;
	}
	
	public boolean saveGZipLogFile2SDcard(String logString) {
		if (!LogCollectorUtility.isSDcardExsit()) {
			return false;
		}
		logString = logString + "\r\n"+"\r\n"+"\r\n";
		try {
			File logDir = getExternalLogDir();
			if (!logDir.exists()) {
				logDir.mkdirs();
			}
			File logFile = new File(logDir,"json.txt");
			FileOutputStream fos = new FileOutputStream(logFile,true);
			fos.write(logString.getBytes(CHARSET));
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "saveLogFile2SDcard failed!");
			return false;
		}
		return true;
	}

	public boolean saveCatchLogFile2SDcard(String msg, String whichClass) {
		if (!LogCollectorUtility.isSDcardExsit()) {
			return false;
		}
		String lineSeparator = "\r\n";
		String appVerName = "appVerName:" + LogCollectorUtility.getVerName(mContext);
		String appVerCode = "appVerCode:" + LogCollectorUtility.getVerCode(mContext);
		String OsVer = "OsVer:" + Build.VERSION.RELEASE;
		String vendor = "vendor:" + Build.MANUFACTURER;
		String model = "model:" + Build.MODEL;
		String logTime = "logTime:" + LogCollectorUtility.getCurrentTime();
		StringBuilder sb = new StringBuilder();
		sb.append(logTime).append(lineSeparator).
				append(appVerName).append(lineSeparator).
				append(appVerCode).append(lineSeparator).
				append(OsVer).append(lineSeparator).
				append(vendor).append(lineSeparator).
				append(model).append(lineSeparator).
				append(whichClass).append(lineSeparator).
				append(msg).append(lineSeparator).append(lineSeparator);


		try {
			File logDir = getExternalLogDir();
			if (!logDir.exists()) {
				logDir.mkdirs();
			}
			File logFile = new File(logDir,LOG_SUFFIX);
			/*if (!isAppend) {
				if (logFile.exists() && !logFile.isFile())
					logFile.delete();
			}*/
			FileOutputStream fos = new FileOutputStream(logFile , true);
			fos.write(sb.toString().getBytes(CHARSET));
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "saveLogFile2SDcard failed!");
			return false;
		}
		return true;
	}


	/**
	 *
	 * @param currentD 计算后当前用户使用下载的流量
	 * @param currentU 计算后当前用户使用上传的流量
	 * @param rx 从开机起下载的流量
	 * @param tx 从开机起上传的流量
     * @return
     */
	public boolean saveFlowMon2SDcard(long currentD,long currentU,long rx,long tx,String action) {
		if (!LogCollectorUtility.isSDcardExsit()) {
			return false;
		}

		String logString = TimeUtils.getCurrentTime() + "\r\n";
		logString = logString + "Action:" + action + "\r\n";
		logString = logString + "该用户下载流量:" + currentD +"KB" + "\r\n";
		logString = logString + "该用户上传流量:" + currentU + "KB" + "\r\n";
		logString = logString + "从开机起下载流量:" + rx + "KB" + "\r\n";
		logString = logString + "从开机起上传流量:" + tx + "KB" + "\r\n";
		logString = logString + "\r\n"+"\r\n";

		try {
			File logDir = getExternalLogDir();
			if (!logDir.exists()) {
				logDir.mkdirs();
			}
			File logFile = new File(logDir,"flow_monitor.txt");
			FileOutputStream fos = new FileOutputStream(logFile,true);
			fos.write(logString.getBytes(CHARSET));
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "saveLogFile2SDcard failed!");
			return false;
		}
		return true;
	}

	public boolean saveData2SDcard(String action) {
		if (!LogCollectorUtility.isSDcardExsit()) {
			return false;
		}

		String logString = "";
		logString = logString + "action:" + action + "\r\n";
		logString = logString + "时间:" + TimeUtils.getCurrentTime()+ "\r\n";
		logString = logString + "\r\n"+"\r\n";

		try {
			File logDir = getExternalLogDir();
			if (!logDir.exists()) {
				logDir.mkdirs();
			}
			File logFile = new File(logDir,"onlinedate.txt");
			FileOutputStream fos = new FileOutputStream(logFile,true);
			fos.write(logString.getBytes(CHARSET));
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "saveLogFile2SDcard failed!");
			return false;
		}
		return true;
	}

}
