package com.joe.ibaby.helper


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.joe.customlibrary.common.ScreenObserver


/**
 * 开机广播
 * Created by qiaojianfeng on 17/9/19.
 */

class BootReceiver : BroadcastReceiver() {

    var mScreenObserver: ScreenObserver? = null

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {

            Log.e("Joe--->", "BootReceiver-onReceive")

//            mScreenObserver = ScreenObserver(context.applicationContext)
//            mScreenObserver!!.requestScreenStateUpdate(object : ScreenObserver.ScreenStateListener {
//                override fun onScreenOn() {
//                    Log.e("Joe--->", "onScreenOn")
//                }
//
//                override fun onScreenOff() {
//                    Log.e("Joe--->", "onScreenOff")
//                }
//
//                override fun onUserPresent() {
//                    Log.e("Joe--->", "onUserPresent")
//                }
//
//            })

            context.startService(Intent(context, TimerService::class.java))

        }
    }

}
