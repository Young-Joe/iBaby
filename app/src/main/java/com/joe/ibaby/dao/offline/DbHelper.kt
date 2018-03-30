package com.joe.ibaby.dao.offline

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.joe.ibaby.dao.greendao.DaoMaster


/**
 *
 * DaoMaster.OpenHelper继承类
 * Created by QiaoJF on 2017/7/27.
 */
class DbHelper : DaoMaster.DevOpenHelper {

    constructor(context: Context, name: String) : super(context, name) {}

    constructor(context: Context, name: String, factory: SQLiteDatabase.CursorFactory) : super(context, name, factory) {}

    //DevOpenHelper 会删除全部的表数据,如果不想改为OpenHelper
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        super.onUpgrade(db, oldVersion, newVersion)
    }


}
