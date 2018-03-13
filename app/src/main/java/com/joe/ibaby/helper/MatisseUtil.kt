package com.joe.ibaby.helper

import android.content.pm.ActivityInfo
import com.joe.ibaby.BuildConfig
import com.joe.ibaby.R
import com.joe.ibaby.base.BaseActivity
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy

/**
 * @author qiaojianfeng on 18/3/13.
 */
object MatisseUtil {

    fun turn2album(activity: BaseActivity) {
        Matisse.from(activity)
                .choose(MimeType.ofAll(), false) // 选择 mime 的类型
                .countable(true)
                .capture(true)
                .captureStrategy(CaptureStrategy(true, BuildConfig.APPLICATION_ID + ".HeaderProvider")) // 拍照的图片路径
                .maxSelectable(1) // 图片选择的最多数量
                .gridExpectedSize(ResourceUtil.getDimension(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f) // 缩略图的比例
                .imageEngine(GlideEngine()) // 使用的图片加载引擎
                .theme(R.style.PictureChoose)
                .forResult(AppUtil.REQUEST_CODE_CHOOSE) // 设置作为标记的请求码
    }

}