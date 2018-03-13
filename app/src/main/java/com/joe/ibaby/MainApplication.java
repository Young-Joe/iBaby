package com.joe.ibaby;


import com.joe.customlibrary.app.BaseApplication;
import com.joe.customlibrary.common.CommonUtils;
import com.joe.ibaby.dao.beans.User;
import com.joe.ibaby.dao.offline.OffLineDataLogic;
import com.joe.ibaby.helper.PreferenceUtil;

import cn.bmob.v3.Bmob;

/**
 * @author qiaojianfeng on 18/3/2.
 */

public class MainApplication extends BaseApplication {

    private static User sUser;

    @Override
    public void onCreate() {
        super.onCreate();
        BaseApplication.setContext(getApplicationContext());

        Bmob.initialize(this, "b8cc25a7a37d76437da7c365b539d352", "bmob");
    }

    public static User getUser() {
        if (sUser == null) {
            String userId = PreferenceUtil.INSTANCE.getField(PreferenceUtil.INSTANCE.getCURRENT_USER());
            if (!CommonUtils.isTextEmpty(userId)) {
                sUser = OffLineDataLogic.Companion.getInstance().getUserById(userId);
            }
        }
        return sUser;
    }

    public static void setUser(User user) {
        sUser = user;
    }
}
