package com.joe.ibaby.helper

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.joe.ibaby.dao.beans.Baby


/**
 * @author qiaojianfeng on 18/3/23.
 */
object ShareUtil {

    fun shareTxt(context: Context) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "贴心的提醒服务")
        intent.putExtra(Intent.EXTRA_TEXT, "再也不必担心忘记給宝宝接种疫苗啦~")
        context.startActivity(Intent.createChooser(intent, "分享iBaby"))
    }

    /**
     * 分享图片和文字内容
     *
     * @param dlgTitle
     *      分享对话框标题
     * @param subject
     *      主题
     * @param content
     *      分享内容（文字）
     * @param uri
     *      图片资源URI
     */
    fun shareImg(context: Context, baby: Baby) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(AppUtil.getBabyPicPath(baby)))
            intent.putExtra(Intent.EXTRA_SUBJECT, "贴心的提醒服务")
            intent.putExtra(Intent.EXTRA_TEXT, "再也不必担心忘记給宝宝接种疫苗啦~")
            context.startActivity(Intent.createChooser(intent, "分享iBaby"))
    }

}