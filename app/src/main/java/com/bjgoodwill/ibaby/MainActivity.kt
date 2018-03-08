package com.bjgoodwill.ibaby

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.bjgoodwill.customlibrary.utils.ToastUtil
import com.bjgoodwill.ibaby.dao.beans.User

class MainActivity : AppCompatActivity() {

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

    }


}
