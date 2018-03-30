package com.joe.ibaby.dao.online

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import cn.bmob.push.PushConstants
import cn.bmob.v3.Bmob.getApplicationContext
import com.alibaba.fastjson.JSONObject
import com.joe.customlibrary.common.CommonUtils
import com.joe.ibaby.R
import com.joe.ibaby.dao.beans.PushBean
import com.joe.ibaby.helper.TimerService
import com.joe.ibaby.ui.home.HomeActivity
import java.util.*

/**
 * @author qiaojianfeng on 18/3/23.
 */
class PushMessageReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(PushConstants.ACTION_MESSAGE)) {
            val pushContent = intent?.getStringExtra("msg")
            if (CommonUtils.isTextEmpty(pushContent) ||
                    pushContent!!.contains("alert") ) {
                //text 文本方式推送
            }else {
                val pushBean = JSONObject.parseObject(pushContent, PushBean::class.java)
                if(pushBean.type == 1) {
                    showNotification(context!!, pushBean)
                }
            }
            context?.startService(Intent(context, TimerService::class.java))

//            Logger.e(intent?.getStringExtra("msg")!!)
//            LogFileStorage.getInstance(context).saveData2SDcard(intent?.getStringExtra("msg")!!)
        }

    }

    private fun showNotification(context: Context, pushBean: PushBean) {
        //获取NotificationManager实例
        val notifyManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        //实例化NotificationCompat.Builde并设置相关属性
        val builder = NotificationCompat.Builder(context)
        builder.setPriority(NotificationManager.IMPORTANCE_MAX)
                .setCategory(Notification.CATEGORY_SYSTEM)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setSmallIcon(R.mipmap.ic_notification_vaccine)
                .setTicker(pushBean.title)
                .setContentTitle(pushBean.title)
                .setContentText(pushBean.content)
                .setAutoCancel(true)
                .setShowWhen(false)
                .setDefaults(Notification.DEFAULT_ALL)
        val intent = Intent(context, HomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0)
        builder.setContentIntent(pendingIntent)
        val random = Random()
        notifyManager!!.notify(random.nextInt(100), builder.build())
    }

}