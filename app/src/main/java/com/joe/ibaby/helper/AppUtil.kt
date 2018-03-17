package com.joe.ibaby.helper

import com.joe.customlibrary.common.CommonUtils
import com.joe.customlibrary.utils.TimeUtils
import com.joe.ibaby.dao.beans.Baby
import com.joe.ibaby.dao.beans.User

/**
 * @author qiaojianfeng on 18/3/13.
 */
object AppUtil {

    val REQUEST_CODE_LOGIN = 10
    //跳转图片选择界面请求码
    val REQUEST_CODE_CHOOSE = 11
    val REQUEST_CODE_ADD_BABY = 12

    val CURRENT_USER = "currentUser"

    fun getUserHeaderPath(user: User): String{
        return  AppConstant.userHeaderPath + user.bmobObjId + ".png"
    }

    fun getBabyPicPath(baby: Baby): String{
        return AppConstant.babyPicPath + baby.userId + ".png"
    }

    fun getBabyCall(user: User): String{
        if (user.gender == 2) {
            return "老妈"
        }else {
            return "老爹"
        }
    }

    fun getBabyAge(birth: String?): Int{
        return TimeUtils.getDateDif(birth).toInt()
    }

    fun getBabyAgeInfo(birth: String?): String{
        var info = ""
        if (CommonUtils.isTextEmpty(birth)){
            return info
        }
        val days = getBabyAge(birth)
        if (days >= 365) {
            val age: String
            val years = days / 365
            val month = (days % 365) / 30
            if (month.toInt() == 0) {
                age = years.toString() + "岁"
            }else {
                age = years.toString() + "岁" + month + "个月"
            }
            info = "已经" +age + "啦~"
        }else if (days > 0){
            info = "已经" + days.toString() + "天啦~"
        }else if (days.toInt() == 0) {
            info = "今天出生啦~"
        }else if (days > -30) {
            info = "还有" + Math.abs(days) + "天就要来到这个世界啦~"
        }else {
            info = "宝宝来到这个世界还有一段时间哟!"
        }
        return  info
    }


}