package com.joe.ibaby.helper

import cn.bmob.v3.exception.BmobException
import com.joe.ibaby.R

/**
 * @author qiaojianfeng on 18/3/9.
 */
object BmobUtil {


    interface OnHandleBmobListener{
        fun onSuccess()
    }

    fun onHandleBmob(e: BmobException?, listener: OnHandleBmobListener?) {
        if (isSuccess(e)) {
            listener?.onSuccess()
        }else {
            isNetNotAvailable(e)
            isUniqueConflict(e)
        }
    }

    /**
     * Bmob 请求服务返回结果
     *  true -> 成功
     */
    fun isSuccess(e: BmobException?): Boolean {
        return e == null
    }

    private fun isNetNotAvailable(e: BmobException?) {
        e?.run {
            if (e.errorCode == 9016) {
                TastyToastUtil.showError(ResourceUtil.getString(R.string.app_net_not_available))
            }
        }
    }

    private fun isUniqueConflict(e: BmobException?) {
        e?.run {
            if (e.errorCode == 401) {
                TastyToastUtil.showError(ResourceUtil.getString(R.string.txt_unique_error))
            }
        }
    }



}