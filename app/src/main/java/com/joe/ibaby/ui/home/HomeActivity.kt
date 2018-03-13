package com.joe.ibaby.ui.home

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import android.widget.TextSwitcher
import android.widget.TextView
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.UpdateListener
import cn.bmob.v3.listener.UploadFileListener
import com.jakewharton.rxbinding2.view.RxView
import com.joe.customlibrary.common.SystemBarHelper
import com.joe.customlibrary.utils.FileUtils
import com.joe.customlibrary.utils.ToastUtil
import com.joe.customlibrary.view.CircleImageView
import com.joe.ibaby.MainApplication
import com.joe.ibaby.R
import com.joe.ibaby.base.BaseActivity
import com.joe.ibaby.dao.beans.BaseBean
import com.joe.ibaby.dao.beans.User
import com.joe.ibaby.helper.*
import com.joe.ibaby.ui.add.AddBabyActivity
import com.joe.ibaby.ui.login.LoginActivity
import com.soundcloud.android.crop.Crop
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import java.io.File

class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    val REQUEST_CODE_LOGIN = 10
    val REQUEST_CODE_CHOOSE = AppUtil.REQUEST_CODE_CHOOSE

    private var mHeaderLayout: View? = null
    private var mIvUserHeader: CircleImageView? = null
    private var mTvHintLogin: TextView? = null
    private var mTvNickname: TextView? = null
    private var mTsBaby: TextSwitcher? = null

    private var mUser: User? = null

    override fun setContentLayout(): Int {
        return R.layout.activity_home
    }

    override fun initView() {
        setSupportActionBar(toolbar)
        SystemBarHelper.tintStatusBarForDrawer(this, drawer_layout, ResourceUtil.getColor(R.color.colorPrimary), 0F)
        SystemBarHelper.setPadding(this, nav_view.getHeaderView(0))

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        mHeaderLayout = nav_view.getHeaderView(0)

        mIvUserHeader = mHeaderLayout?.findViewById(R.id.iv_head)
        mTvHintLogin = mHeaderLayout?.findViewById(R.id.tv_hint_login)
        mTvNickname = mHeaderLayout?.findViewById(R.id.tv_nickname)
        mTsBaby = mHeaderLayout?.findViewById(R.id.ts_baby)

    }

    override fun initData() {
        initUserInfo()

    }

    private fun initUserInfo() {
        mUser = MainApplication.getUser()
        isShowUserInfo(mUser != null)
        if (mUser != null) {
            mTvNickname?.text = mUser!!.nickName
            loadUserHead()
            RxView.clicks(mIvUserHeader!!)
                    .subscribe { setUserHead() }

        }else {
            mIvUserHeader?.setImageResource(R.mipmap.ic_default_baby)
            RxView.clicks(mIvUserHeader!!)
                    .subscribe { startActivityForResult(Intent(this, LoginActivity::class.java), REQUEST_CODE_LOGIN) }
        }
    }

    private fun loadUserHead() {
        if (FileUtils.isFileExist(AppUtil.getUserHeaderPath(mUser!!))) {
            mIvUserHeader?.setImageBitmap(BitmapFactory.decodeFile(AppUtil.getUserHeaderPath(mUser!!)))
        }
    }

    private fun isShowUserInfo(isShow: Boolean) {
        mTvHintLogin?.visibility = if (isShow) { View.GONE } else { View.VISIBLE }

        mTvNickname?.visibility = if (isShow) { View.VISIBLE } else { View.GONE }
        mTsBaby?.visibility = if (isShow) { View.VISIBLE } else { View.GONE }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.home, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.action_settings -> return true
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_camera -> {
                if (isUserLogin()) { setUserHead() } else {
                    TastyToastUtil.showInfo("哼!你还没登录呢")
                }
            }
            R.id.nav_gallery -> {
                if (isUserLogin()) { startActivity(Intent(this, AddBabyActivity::class.java)) } else {
                    TastyToastUtil.showInfo("哼!你还没登录呢")
                }
            }
            R.id.nav_logout -> {
                PreferenceUtil.saveField(PreferenceUtil.CURRENT_USER, BaseBean.TEXT_EMPTY)
                MainApplication.setUser(null)
                initUserInfo()
            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    var userHeaderPath: String? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK) {
            initUserInfo()
        } else if (requestCode == REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            val result = Matisse.obtainResult(data)
            userHeaderPath = AppUtil.getUserHeaderPath(mUser!!)
            val outputUri = Uri.fromFile(File(userHeaderPath) )
            Crop.of(result[0], outputUri).asSquare().start(this)
        }else if (requestCode == Crop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            TastyToastUtil.showOK("头像设置成功")
            val bmobFile = BmobFile(File(userHeaderPath))

            mIvUserHeader?.setImageBitmap(BitmapFactory.decodeFile(userHeaderPath))

            bmobFile.uploadblock(object : UploadFileListener() {
                override fun done(p0: BmobException?) {
                    if(p0==null){
                        mUser!!.userHead = bmobFile
                        mUser!!.update(mUser!!.bmobObjId, object : UpdateListener() {
                            override fun done(p0: BmobException?) {
                                if(p0==null){
                                    ToastUtil.showToast("上传文件成功")
                                }else{
                                    ToastUtil.showToast("上传文件失败")
                                }
                            }
                        })
                    }else{
                        ToastUtil.showToast("上传文件失败")
                    }
                }
            })
        }
    }

    private fun setUserHead() {
        MatisseUtil.turn2album(this)
    }

    private fun isUserLogin(): Boolean {
        return mUser != null
    }

}
