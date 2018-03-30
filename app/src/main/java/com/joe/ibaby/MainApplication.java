package com.joe.ibaby;


import com.joe.customlibrary.app.BaseApplication;
import com.joe.ibaby.helper.Logger;
import com.joe.ibaby.helper.PreferenceUtil;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;

/**
 * @author qiaojianfeng on 18/3/2.
 */

public class MainApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        BaseApplication.setContext(getApplicationContext());

        Bmob.initialize(this, "b8cc25a7a37d76437da7c365b539d352", "bmob");

        // 使用推送服务时的初始化操作
        BmobInstallationManager.getInstance().initialize(new InstallationListener<BmobInstallation>() {
            @Override
            public void done(BmobInstallation bmobInstallation, BmobException e) {
                if (e == null) {
                    Logger.INSTANCE.e(bmobInstallation.getObjectId() + "-" + bmobInstallation.getInstallationId());
                } else {
                    Logger.INSTANCE.e(e.getMessage().toString());
                }
            }
        });
        // 启动推送服务
        BmobPush.startWork(this);

    }

    public static String getCurrentUserId() {
        return PreferenceUtil.INSTANCE.getField(PreferenceUtil.INSTANCE.getCURRENT_USER());
    }

}
