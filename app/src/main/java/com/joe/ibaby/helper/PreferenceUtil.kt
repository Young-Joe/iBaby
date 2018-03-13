package com.joe.ibaby.helper

import android.content.Context
import com.joe.ibaby.MainApplication


object PreferenceUtil {

    private val GLOBAL_RECORD = "iBaby"

    public val CURRENT_USER = "current_user"

    fun saveField(key: String, value: String?) {
        val preferences = MainApplication.getContext()
                .getSharedPreferences(GLOBAL_RECORD, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getField(key: String): String {
        val preferences = MainApplication.getContext()
                .getSharedPreferences(GLOBAL_RECORD, Context.MODE_PRIVATE)
        return preferences.getString(key, "")
    }

    fun saveIntStatus(key: String, value: Int) {
        val preferences = MainApplication.getContext()
                .getSharedPreferences(GLOBAL_RECORD, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    /**
     * 默认 -1
     * @param key
     * @return
     */
    fun getIntStatus(key: String): Int {
        val preferences = MainApplication.getContext()
                .getSharedPreferences(GLOBAL_RECORD, Context.MODE_PRIVATE)
        return preferences.getInt(key, -1)
    }

}
