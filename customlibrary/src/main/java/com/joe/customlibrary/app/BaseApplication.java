package com.joe.customlibrary.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by qiaojianfeng on 17/8/18.
 */

public class BaseApplication extends Application{

    private static Context context;

    /**
     * The static APP context,used for global static variable.
     *
     */
    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        BaseApplication.context = context;
    }

}
