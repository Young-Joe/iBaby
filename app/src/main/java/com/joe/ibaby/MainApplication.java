package com.joe.ibaby;


import com.joe.customlibrary.app.BaseApplication;
import com.joe.ibaby.helper.PreferenceUtil;

import cn.bmob.v3.Bmob;

/**
 * @author qiaojianfeng on 18/3/2.
 */

public class MainApplication extends BaseApplication {

    private static String sCurrentUserId;

    @Override
    public void onCreate() {
        super.onCreate();
        BaseApplication.setContext(getApplicationContext());

        Bmob.initialize(this, "b8cc25a7a37d76437da7c365b539d352", "bmob");


    }

    public static String getCurrentUserId() {
        return PreferenceUtil.INSTANCE.getField(PreferenceUtil.INSTANCE.getCURRENT_USER());
    }

}
