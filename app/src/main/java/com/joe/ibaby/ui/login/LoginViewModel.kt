package com.joe.ibaby.ui.login

import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.DownloadFileListener
import cn.bmob.v3.listener.FindListener
import com.joe.customlibrary.app.BaseViewModel
import com.joe.customlibrary.rxjava.RxObservable
import com.joe.customlibrary.utils.FileUtils
import com.joe.ibaby.dao.beans.Baby
import com.joe.ibaby.dao.beans.PackageBean
import com.joe.ibaby.dao.beans.User
import com.joe.ibaby.dao.offline.OffLineDataLogic
import com.joe.ibaby.helper.AppUtil
import com.joe.ibaby.helper.PreferenceUtil
import io.reactivex.Observable
import java.io.File


/**
 * @author qiaojianfeng on 18/3/9.
 */
class LoginViewModel : BaseViewModel() {

    fun loginTask(userId: String, password: String, listener: FindListener<User>) {
        val bmobQuery = BmobQuery<User>()
        bmobQuery.addWhereEqualTo("userId", userId)
        bmobQuery.addWhereEqualTo("password", password)
        bmobQuery.findObjects(listener)
    }

    fun getLocalUserById(userId: String): Observable<PackageBean> {
        return RxObservable<PackageBean>().getObservableIo {
            return@getObservableIo OffLineDataLogic.instance!!.getUserByIdInPkg(userId)
        }
    }

    fun checkUserPswd(user: User, password: String): Boolean {
        if (user != null && user.password.equals(password)) {
            return true
        }
        return false
    }

    fun saveCurrentUser(user: User) {
        PreferenceUtil.saveField(PreferenceUtil.CURRENT_USER, user.userId)
        OffLineDataLogic.instance?.saveUser(user)
    }

    fun loadUserBaby(userId: String, listener: FindListener<Baby>) {
        val bmobQuery = BmobQuery<Baby>()
        bmobQuery.addWhereEqualTo("userId", userId)
        bmobQuery.findObjects(listener)
    }

    fun saveBabyLoadBabyPic(baby: Baby?) {
        if (baby == null) return
        OffLineDataLogic.instance?.saveBaby(baby)
        if (!FileUtils.isFileExist(AppUtil.getBabyPicPath(baby)) && baby.babyPic != null) {
            baby.babyPic.download(File(AppUtil.getBabyPicPath(baby)), object : DownloadFileListener() {
                override fun onProgress(p0: Int?, p1: Long) {}

                override fun done(p0: String?, p1: BmobException?) {}

            })
        }
    }

    fun checkLocalUserHeaderExist(user: User): Boolean {
        return !FileUtils.isFileExist(AppUtil.getUserHeaderPath(user)) && user.userHead != null
    }

}