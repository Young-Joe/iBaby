package com.joe.ibaby.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import com.joe.customlibrary.common.SystemBarHelper
import com.joe.ibaby.R
import com.joe.ibaby.helper.AppManager
import com.joe.ibaby.helper.ResourceUtil
import com.joe.ibaby.ui.add.AddBabyActivity
import com.joe.ibaby.ui.home.HomeActivity

/**
 * @author qiaojianfeng on 18/2/26.
 */

abstract class BaseActivity : AppCompatActivity() {

    var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mContext = this
        super.onCreate(savedInstanceState)
        if (this@BaseActivity !is HomeActivity &&
                this@BaseActivity !is AddBabyActivity ) {
            SystemBarHelper.tintStatusBar(this, ResourceUtil.getColor(R.color.colorPrimary), 0f)
        }
        AppManager.getInstance.addActivity(this)
        setContentView(setContentLayout())
        initView()
        initData()
    }

    @LayoutRes
    abstract fun setContentLayout(): Int

    abstract fun initView()

    abstract fun initData()

    override fun onDestroy() {
        super.onDestroy()
        AppManager.getInstance.removeActivity(this)
    }

}
