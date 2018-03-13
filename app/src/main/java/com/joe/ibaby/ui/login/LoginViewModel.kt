package com.joe.ibaby.ui.login

import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.joe.customlibrary.app.BaseViewModel
import com.joe.customlibrary.rxjava.RxObservable
import com.joe.ibaby.dao.beans.User
import io.reactivex.Observable


/**
 * @author qiaojianfeng on 18/3/9.
 */
class LoginViewModel : BaseViewModel() {

    fun loginTask(user: User): Observable<User> {

        return RxObservable<User>().getObservableIo {
            var resultUser: User? = null
            val bmobQuery = BmobQuery<User>()
            bmobQuery.addWhereEqualTo("userId", user.userId)
            bmobQuery.addWhereEqualTo("password", user.password)
            bmobQuery.findObjects(object : FindListener<User>(){
                override fun done(users: MutableList<User>?, e: BmobException?) {
//                    if (AppUtil.isSuccess(e)) {
//                        resultUser = users?.get(0)
//                    }
                }
            })
            return@getObservableIo resultUser
        }
    }

}