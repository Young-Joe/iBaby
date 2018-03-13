package com.joe.ibaby

import android.os.Bundle
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.joe.customlibrary.utils.ToastUtil
import com.joe.ibaby.base.BaseActivity
import com.joe.ibaby.dao.beans.User

class MainActivity : BaseActivity() {
    override fun setContentLayout(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
    }

    override fun initData() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val user = User()
        user.userId = "34234dwf"
        user.password = "qnodnqo"
        user.save(object : SaveListener<String>() {
            override fun done(objectId: String?, e: BmobException?) {
                if (e == null) {
                    ToastUtil.showToast("成功")
                }else {
                    ToastUtil.showToast("失败")
                }
            }

        })
//        startService(Intent(this, TimerService::class.java))

    }


}
