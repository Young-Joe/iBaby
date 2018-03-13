package com.joe.ibaby.helper

import com.joe.ibaby.MainApplication
import com.sdsmdg.tastytoast.TastyToast

/**
 * @author qiaojianfeng on 18/3/10.
 */
object TastyToastUtil {

    fun showError(errorTxt: String) {
        showShort(errorTxt, TastyToast.ERROR)
    }

    fun showOK(okTxt: String) {
        showShort(okTxt, TastyToast.SUCCESS)
    }

    fun showWarn(warnTxt: String) {
        showShort(warnTxt, TastyToast.WARNING)
    }

    fun showInfo(infoTxt: String) {
        showShort(infoTxt, TastyToast.INFO)
    }

    fun showDefault(defaultTxt: String) {
        showShort(defaultTxt, TastyToast.DEFAULT)
    }

    fun showConfusion(confusionTxt: String) {
        showShort(confusionTxt, TastyToast.CONFUSING)
    }

    private fun showShort(content: String, type: Int) {
        show(content, TastyToast.LENGTH_SHORT, type)
    }

    private fun show(content: String, length: Int, type: Int) {
        TastyToast.makeText(MainApplication.getContext(), content, length, type)
    }

}