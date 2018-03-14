package com.joe.ibaby.helper

import android.os.Environment
import com.joe.customlibrary.utils.FileUtils
import com.joe.ibaby.MainApplication
import java.io.File

object AppConstant {

    //	public static final int DB_VERSION = DaoMaster.SCHEMA_VERSION;

    val dbName = "mobile.db"
    private val APP_PATH = "iBaby"

    private val APP_USER_HEADER = "UserPic"
    private val APP_BABY_PIC = "BabyPic"


    val sysPath: String
        get() {
            var SYS_PATH: String? = null
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                SYS_PATH = (Environment.getExternalStorageDirectory()
                        .absolutePath
                        + File.separator
                        + APP_PATH
                        + File.separator)
            } else {
                //SYS_PATH = "/data/data/com.bjgoodwill.care/databases/";
                SYS_PATH = (MainApplication.getContext().filesDir.absolutePath
                        + File.separator
                        + APP_PATH
                        + File.separator)
            }
            return SYS_PATH
        }

    val dbPath: String
        get() {
            FileUtils.isFileExistElseCreate(sysPath)
            return sysPath + dbName
        }

    val userHeaderPath: String
        get() {
            val userHeaderPath = sysPath + APP_USER_HEADER + File.separator
            FileUtils.isFileExistElseCreate(userHeaderPath)
            return userHeaderPath
        }

    val babyPicPath: String
        get() {
            val babyPicPath = sysPath + APP_BABY_PIC + File.separator
            FileUtils.isFileExistElseCreate(babyPicPath)
            return babyPicPath
        }


}
