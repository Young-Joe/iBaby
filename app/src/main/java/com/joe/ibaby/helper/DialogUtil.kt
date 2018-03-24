package com.joe.ibaby.helper

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AlertDialog
import com.joe.customlibrary.common.PermissionHelper
import com.joe.customlibrary.dialog.AwesomeProgressDialog
import com.joe.customlibrary.utils.ToastUtil
import com.joe.ibaby.R

/**
 * @author qiaojianfeng on 18/3/11.
 */
object DialogUtil {

    fun showProgress(context: Context, title: String): Dialog {
        return AwesomeProgressDialog(context)
                .setTitle(title)
                .setMessage(null)
                .setColoredCircle(R.color.colorPrimary)
                .setCancelable(false)
                .show()
    }

    fun showAlertDialog(context: Context, helper: PermissionHelper) {
        helper.requestPermissions("请授予iBaby日历读写权限！",
                object : PermissionHelper.PermissionListener {
                    override fun doAfterGrand(vararg permission: String) {
                        showAlertDialog(context)
                    }

                    override fun doAfterDenied(vararg permission: String) {
                        ToastUtil.showToast("权限缺少可能会影响使用!")
                        showAlertDialog(context)
                    }
                },
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR
        )
    }

    fun showNoticeDialog(context: Context): AlertDialog {
        return AlertDialog.Builder(context)
                .setTitle("iBaby公告")
                .setMessage("\t\t\t\t所有数据均来源于网络,仅供参考.还请各位宝爸宝妈在对宝宝接种疫苗前向医生进行确认!" +
                "\n\t\t\t\t用户在进行设置过宝宝出生日期,并开启提醒后,会在疫苗接种前三天进行通知提醒.但......由于国内厂商均有" +
                        "应用后台杀死机制,iBaby考虑用户体验以及耗电性本身没有进行线程保活,采取了向系统日历写入行程的方式来进行提醒通知." +
                        "\n\t\t\t\t如有建议欢迎提issue,祝各位宝宝健康成长~")
                .setPositiveButton("确定", null)
                .show()
    }


    private fun showAlertDialog(context: Context) {
        AlertDialog.Builder(context)
                .setTitle("开启疫苗提醒")
                .setMessage("疫苗提醒服务将以通知的方式来提醒您该給爱宝进行那种疫苗接种")
                .setPositiveButton("开启", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        context.startService(Intent(context, TimerService::class.java))
                    }

                })
                .setNegativeButton("关闭", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        context.stopService(Intent(context, TimerService::class.java))
                    }

                })
                .show()
    }


}