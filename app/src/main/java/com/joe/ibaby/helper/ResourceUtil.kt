package com.joe.ibaby.helper

import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import com.joe.customlibrary.app.BaseApplication


/**
 * Created by QiaoJF on 17/12/22.
 */

object ResourceUtil {

    fun getDrawable(@DrawableRes resourceId: Int): Drawable {
        return BaseApplication.getContext().resources.getDrawable(resourceId)
    }

    fun getString(@StringRes stringRes: Int): String {
        return BaseApplication.getContext().resources.getString(stringRes)
    }

    fun getDimension(@DimenRes dimenRes: Int): Int {
        return BaseApplication.getContext().resources.getDimension(dimenRes).toInt()
    }

    fun getColor(@ColorRes colorRes: Int): Int {
        return BaseApplication.getContext().resources.getColor(colorRes)
    }

}
