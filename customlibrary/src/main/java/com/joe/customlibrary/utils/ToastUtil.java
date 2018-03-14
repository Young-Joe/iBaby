package com.joe.customlibrary.utils;

import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.joe.customlibrary.app.BaseApplication;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * Created by QiaoJF on 2016/9/22.
 * 宿主项目私有的Util
 *
 */
public class ToastUtil {

    public static void showToast(String msgContent) {
        if (!TextUtils.isEmpty(msgContent)) {
            showToast(Gravity.CENTER, Toast.LENGTH_SHORT, msgContent);
        }
    }

    /**
     * 只能显示时间在Toast.LENGTH_LONG(3.5s)之内
     * @param msgContent
     * @param period
     */
    public static void showToast(String msgContent, long period) {
        if (!TextUtils.isEmpty(msgContent) && period <= 3500) {
            final Toast toast = showToast(Gravity.CENTER, Toast.LENGTH_LONG, msgContent);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (toast != null){
                        toast.cancel();
                    }
                }
            },period);
        }
    }

    /**
     *
     * @param pos
     *            location of toast.
     * @param duration
     *            How long to display the message.
     * @param text
     */
    private static Toast showToast(int pos, int duration, CharSequence text) {
		Toast mToast = Toast.makeText(BaseApplication.getContext(), text,
				duration);
		mToast.setGravity(pos, 0, 0);
		mToast.show();
        return mToast;
    }

}

