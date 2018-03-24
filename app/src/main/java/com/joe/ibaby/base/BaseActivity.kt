package com.joe.ibaby.base

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import com.joe.customlibrary.common.PermissionHelper
import com.joe.customlibrary.common.SystemBarHelper
import com.joe.customlibrary.utils.ToastUtil
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
    var mHelper: PermissionHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mContext = this
        super.onCreate(savedInstanceState)
        if (this@BaseActivity !is HomeActivity &&
                this@BaseActivity !is AddBabyActivity ) {
            SystemBarHelper.tintStatusBar(this, ResourceUtil.getColor(R.color.colorPrimary), 0f)
        }
        AppManager.getInstance.addActivity(this)
        setContentView(setContentLayout())

        checkPermission()

        initView()
        initData()
    }

    @LayoutRes
    abstract fun setContentLayout(): Int

    abstract fun initView()

    abstract fun initData()

    private fun checkPermission() {
        mHelper = PermissionHelper(this)
        mHelper?.requestPermissions("请授予iBaby相关权限！",
                object : PermissionHelper.PermissionListener {
                    override fun doAfterGrand(vararg permission: String) {}

                    override fun doAfterDenied(vararg permission: String) {
                        ToastUtil.showToast("权限缺少可能会影响使用!")
                    }
                },
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WAKE_LOCK
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        AppManager.getInstance.removeActivity(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mHelper?.handleRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}
