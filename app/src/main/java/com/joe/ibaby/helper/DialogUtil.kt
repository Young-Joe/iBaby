package com.joe.ibaby.helper

import android.app.Dialog
import android.content.Context
import com.joe.customlibrary.dialog.AwesomeProgressDialog
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

}