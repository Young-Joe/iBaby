package com.joe.ibaby.helper

import com.joe.ibaby.dao.beans.Baby
import com.joe.ibaby.dao.beans.User

/**
 * @author qiaojianfeng on 18/3/13.
 */
object AppUtil {

    //跳转图片选择界面请求码
    val REQUEST_CODE_CHOOSE = 11

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

}