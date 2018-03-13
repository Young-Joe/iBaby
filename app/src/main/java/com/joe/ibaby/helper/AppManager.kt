package com.joe.ibaby.helper

import android.app.Activity
import android.content.Context
import com.joe.ibaby.dao.offline.DbManager
import java.util.*

/**
 * 应用程序Activity管理类
 *
 * @author zhangyubao
 */
class AppManager private constructor() {

    private var activityStack: Stack<Activity>? = null

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack!!.add(activity)
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity {
        return activityStack!!.lastElement()
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        var activity: Activity? = activityStack!!.lastElement()
        if (activity != null) {
            activity.finish()
            activity = null
        }
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        var activity = activity
        if (activity != null) {
            activityStack!!.remove(activity)
            activity.finish()
            activity = null
        }
    }

    /**
     * 移除指定的Activity
     */
    fun removeActivity(activity: Activity?) {
        var activity = activity
        if (activity != null) {
            activityStack!!.remove(activity)
            activity = null
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        for (activity in activityStack!!) {
            if (activity.javaClass == cls) {
                finishActivity(activity)
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        var i = 0
        val size = activityStack!!.size
        while (i < size) {
            if (null != activityStack!![i]) {
                activityStack!![i].finish()
            }
            i++
        }
        activityStack!!.clear()
        activityStack = null
    }

    /**
     * 退出应用程序
     */
    fun exitApp(context: Context) {
        try {
            finishAllActivity()
            DbManager.closeDb()
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {
        private var instance: AppManager? = null

        /**
         * 单一实例
         */
        val getInstance: AppManager
            @Synchronized get() {
                if (instance == null) {
                    instance = AppManager()
                }
                return instance as AppManager
            }
    }

}