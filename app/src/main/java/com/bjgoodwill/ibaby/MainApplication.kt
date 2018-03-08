package com.bjgoodwill.ibaby

import cn.bmob.v3.Bmob
import com.bjgoodwill.customlibrary.app.BaseApplication

/**
 * @author qiaojianfeng on 18/3/2.
 */

class MainApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        BaseApplication.setContext(applicationContext)

        Bmob.initialize(this, "b8cc25a7a37d76437da7c365b539d352", "bmob")

    }


}
