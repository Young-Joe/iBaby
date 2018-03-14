package com.joe.ibaby.helper

import cn.bmob.v3.exception.BmobException
import com.joe.ibaby.R

/**
 * @author qiaojianfeng on 18/3/9.
 */
object BmobUtil {

    abstract class BaseBmobListener {

        abstract fun onNetNotAvailable()

        abstract fun onError()

    }

    abstract class OnHandleBmobListener: BaseBmobListener() {

        override fun onNetNotAvailable() {}

        override fun onError() {}

        abstract fun onSuccess()

    }

    fun onHandleBmob(e: BmobException?, listener: OnHandleBmobListener?) {
        if (isSuccess(e)) {
            listener?.onSuccess()
        }else {
            when {
                isNetNotAvailable(e) -> {listener?.onError() }
                isUniqueConflict(e) -> { }
                else -> listener?.onError()
            }
        }
    }

    /**
     * Bmob 请求服务返回结果
     *  true -> 成功
     */
    fun isSuccess(e: BmobException?): Boolean {
        return e == null
    }

    private fun isNetNotAvailable(e: BmobException?): Boolean {
        if (e != null) {
            if (e.errorCode == 9016) {
                TastyToastUtil.showError(ResourceUtil.getString(R.string.app_net_not_available))
                return true
            }
        }
        return false
    }

    private fun isUniqueConflict(e: BmobException?): Boolean {
        if (e != null) {
            if (e.errorCode == 401) {
                TastyToastUtil.showError(ResourceUtil.getString(R.string.txt_unique_error))
                return true
            }
        }
        return false
    }



}