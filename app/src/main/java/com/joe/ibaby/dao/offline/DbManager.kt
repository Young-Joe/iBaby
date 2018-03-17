package com.joe.ibaby.dao.offline

import android.database.sqlite.SQLiteDatabase

import com.joe.ibaby.MainApplication
import com.joe.ibaby.dao.greendao.DaoMaster
import com.joe.ibaby.dao.greendao.DaoSession
import com.joe.ibaby.helper.AppConstant


/**
 * db管理类
 * Created by QiaoJF on 2017/7/27.
 */

class DbManager private constructor() {

    init {
        // 初始化数据库信息
        mDevOpenHelper = DbHelper(MainApplication.getContext(), AppConstant.dbPath)
        daoMaster
        daoSession
    }

    companion object {

        // 是否加密
        val ENCRYPTED = true

        @Volatile
        private var mDbManager: DbManager? = null
        private var mDevOpenHelper: DaoMaster.DevOpenHelper? = null
        private var mDaoMaster: DaoMaster? = null
        private var mDaoSession: DaoSession? = null

        val instance: DbManager?
            get() {
                if (null == mDbManager) {
                    synchronized(DbManager::class.java) {
                        if (null == mDbManager) {
                            mDbManager = DbManager()
                        }
                    }
                }
                return mDbManager
            }

        /**
         *
         * @desc 获取可读数据库
         */
        val readableDatabase: SQLiteDatabase
            get() {
                if (null == mDevOpenHelper) {
                    instance
                }
                return mDevOpenHelper!!.readableDatabase
            }

        /**
         *
         * @desc 获取可写数据库
         */
        val writableDatabase: SQLiteDatabase
            get() {
                if (null == mDevOpenHelper) {
                    instance
                }
                return mDevOpenHelper!!.writableDatabase
            }

        /**
         *
         * @desc 获取DaoMaster
         */
        val daoMaster: DaoMaster
            get() {
                if (null == mDaoMaster) {
                    synchronized(DbManager::class.java) {
                        if (null == mDaoMaster) {
                            mDaoMaster = DaoMaster(writableDatabase)
                        }
                    }
                }
                return mDaoMaster!!
            }

        /**
         *
         * @desc 获取DaoSession
         */
        val daoSession: DaoSession
            get() {
                if (null == mDaoSession) {
                    synchronized(DbManager::class.java) {
                        mDaoSession = daoMaster.newSession()
                    }
                }
                return mDaoSession!!
            }

        fun closeDb() {
            mDevOpenHelper!!.writableDatabase.close()
            mDevOpenHelper!!.readableDatabase.close()
            mDevOpenHelper = null
            mDaoMaster = null
            mDaoSession = null
            mDbManager = null
            Runtime.getRuntime().gc()
        }
    }

}

