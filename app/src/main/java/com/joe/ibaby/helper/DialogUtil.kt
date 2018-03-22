package com.joe.ibaby.helper

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AlertDialog
import com.joe.customlibrary.dialog.AwesomeProgressDialog
import com.joe.ibaby.R
import com.joe.ibaby.dao.beans.Baby

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

    fun showAlertDialog(context: Context, baby: Baby): Dialog {
        return AlertDialog.Builder(context)
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