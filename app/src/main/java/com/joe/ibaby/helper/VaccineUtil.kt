package com.joe.ibaby.helper

import android.content.Context
import com.alibaba.fastjson.JSONArray
import com.joe.ibaby.dao.beans.Vaccine

/**
 * @author qiaojianfeng on 18/3/22.
 */
object VaccineUtil {

    fun getVaccine1(context: Context) : List<Vaccine> {
        val inputS = context.assets.open("vaccine_1")
        val buffer = ByteArray(inputS.available())
        inputS.read(buffer)
        return JSONArray.parseArray(String(buffer), Vaccine::class.java)
    }

    fun getVaccine2(context: Context) : List<Vaccine> {
        val inputS = context.assets.open("vaccine_2")
        val buffer = ByteArray(inputS.available())
        inputS.read(buffer)
        return JSONArray.parseArray(String(buffer), Vaccine::class.java)
    }

}