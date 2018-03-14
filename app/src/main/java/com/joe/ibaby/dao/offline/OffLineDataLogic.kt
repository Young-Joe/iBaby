package com.joe.ibaby.dao.offline

import com.joe.ibaby.dao.beans.Baby
import com.joe.ibaby.dao.beans.PackageBean
import com.joe.ibaby.dao.beans.User
import com.joe.ibaby.dao.greendao.BabyDao
import com.joe.ibaby.dao.greendao.UserDao
import com.joe.ibaby.helper.TastyToastUtil
import java.lang.Exception

/**
 * @author qiaojianfeng on 18/3/12.
 */
class OffLineDataLogic private constructor(){

    companion object {
        @Volatile
        private var mOffLine: OffLineDataLogic? = null

        val instance: OffLineDataLogic?
        get() {
            if (null == mOffLine) {
                synchronized(OffLineDataLogic::class) {
                    if (null == mOffLine) {
                        mOffLine = OffLineDataLogic()
                    }
                }
            }
            return mOffLine
        }

    }

    @Synchronized
    fun saveUser(user: User?) {
        try {
            val localUser = getUserById(user?.userId!!)
            if (localUser != null) {
                user.id = localUser.id
            }
            //bmob主键
            user.bmobObjId = user.objectId
            DbManager.daoSession.userDao.save(user)
        }catch (e: Exception) {
            TastyToastUtil.showInfo("保存异常")
        }
    }

    @Synchronized
    fun getUserById(userId: String): User?{
        var user: User? = null
        try {
            user = DbManager.daoSession.userDao.queryBuilder()
                    .where(UserDao.Properties.UserId.eq(userId))
                    .unique()
        }catch (e: Exception) {
            TastyToastUtil.showInfo("查询异常")
        }finally {
            return user
        }
    }

    @Synchronized
    fun saveBaby(baby: Baby){
        try {
            val localBaby = getBabyById(baby.userId!!)
            if (localBaby != null) {
                baby.id = localBaby.id
            }
            //bmob主键
            baby.bmobObjId = baby.objectId
            DbManager.daoSession.babyDao.save(baby)
        }catch (e: Exception) {
            TastyToastUtil.showInfo("保存异常")
        }
    }

    @Synchronized
    fun getBabyById(userId: String): Baby?{
        var baby: Baby? = null
        try {
            baby = DbManager.daoSession.babyDao.queryBuilder()
                    .where(BabyDao.Properties.UserId.eq(userId))
                    .unique()
        }catch (e: Exception) {
            TastyToastUtil.showInfo("查询异常")
        }finally {
            return baby
        }
    }

    @Synchronized
    fun getUserByIdInPkg(userId: String): PackageBean?{
        val packageBean = PackageBean()
        packageBean.setDataNull()
        try {
            val user = DbManager.daoSession.userDao.queryBuilder()
                    .where(UserDao.Properties.UserId.eq(userId))
                    .unique()
            if (user != null) {
                packageBean.obj = user
                packageBean.setDataRight()
            }
        }catch (e: Exception) {
            TastyToastUtil.showInfo("查询异常")
        }finally {
            return packageBean
        }
    }

    @Synchronized
    fun getBabyByIdInPkg(userId: String): PackageBean?{
        val packageBean = PackageBean()
        packageBean.setDataNull()
        try {
            val baby = DbManager.daoSession.babyDao.queryBuilder()
                    .where(BabyDao.Properties.UserId.eq(userId))
                    .unique()
            if (baby != null) {
                packageBean.obj = baby
                packageBean.setDataRight()
            }
        }catch (e: Exception) {
            TastyToastUtil.showInfo("查询异常")
        }finally {
            return packageBean
        }
    }

}