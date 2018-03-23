package com.joe.ibaby.ui.home

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.UpdateListener
import cn.bmob.v3.listener.UploadFileListener
import cn.bmob.v3.update.BmobUpdateAgent
import com.jakewharton.rxbinding2.view.RxView
import com.joe.customlibrary.common.CommonUtils
import com.joe.customlibrary.common.SystemBarHelper
import com.joe.customlibrary.rxjava.RxObservable
import com.joe.customlibrary.utils.FileUtils
import com.joe.customlibrary.utils.ToastUtil
import com.joe.customlibrary.view.CircleImageView
import com.joe.ibaby.MainApplication
import com.joe.ibaby.R
import com.joe.ibaby.base.BaseActivity
import com.joe.ibaby.base.BaseLazyFragment
import com.joe.ibaby.dao.beans.Baby
import com.joe.ibaby.dao.beans.BaseBean
import com.joe.ibaby.dao.beans.PackageBean
import com.joe.ibaby.dao.beans.User
import com.joe.ibaby.dao.offline.OffLineDataLogic
import com.joe.ibaby.helper.*
import com.joe.ibaby.helper.AppUtil.REQUEST_CODE_CHOOSE
import com.joe.ibaby.helper.AppUtil.REQUEST_CODE_LOGIN
import com.joe.ibaby.ui.add.AddBabyActivity
import com.joe.ibaby.ui.login.LoginActivity
import com.joe.ibaby.ui.more.MoreActivity
import com.shundaojia.live.Live
import com.soundcloud.android.crop.Crop
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.content_home.*
import java.io.File

class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var mHeaderLayout: View? = null
    private var mIvUserHeader: CircleImageView? = null
    private var mTvHintLogin: TextView? = null
    private var mTvNickname: TextView? = null
    private var mTvBaby: TextView? = null

    private val mFragments: ArrayList<BaseLazyFragment> = ArrayList()

    private var mUser: User? = null

    override fun setContentLayout(): Int {
        return R.layout.activity_home
    }

    override fun initView() {
        setSupportActionBar(toolbar)
        SystemBarHelper.tintStatusBarForDrawer(this, drawer_layout, ResourceUtil.getColor(R.color.colorPrimary), 0F)
        SystemBarHelper.setPadding(this, nav_view.getHeaderView(0))

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        mHeaderLayout = nav_view.getHeaderView(0)

        mIvUserHeader = mHeaderLayout?.findViewById(R.id.iv_head)
        mTvHintLogin = mHeaderLayout?.findViewById(R.id.tv_hint_login)
        mTvNickname = mHeaderLayout?.findViewById(R.id.tv_nickname)
        mTvBaby = mHeaderLayout?.findViewById(R.id.tv_baby)

    }

    override fun initData() {
        initUserInfoView()
        initUserInfo()

        mFragments.add(Vaccine1Fragment())
        mFragments.add(Vaccine2Fragment())

        val pagerAdapter = PagerAdapter(supportFragmentManager)
        viewpager.adapter = pagerAdapter
        tabs.tabMode = TabLayout.MODE_FIXED
        tabs.setupWithViewPager(viewpager)

        BmobUpdateAgent.update(this)
        startService(Intent(mContext, TimerService::class.java))
    }

    fun initUserInfo() {
        if (!CommonUtils.isTextEmpty(MainApplication.getCurrentUserId())) {
            RxObservable<PackageBean>().getObservableIo {
                return@getObservableIo OffLineDataLogic.instance!!.getUserByIdInPkg(MainApplication.getCurrentUserId())
            }.subscribe {
                        if (it.isDataRight) {
                            mUser = it.obj as User?
                        }
                        initUserInfoView()
                    }
        }else {
            mUser = null
            initUserInfoView()
        }
    }

    private fun initUserInfoView() {
        isShowUserInfoView(mUser != null)
        if (mUser != null) {
            mTvNickname?.text = mUser!!.nickName
            loadUserHead()
            RxView.clicks(mIvUserHeader!!)
                    .subscribe { setUserHead() }
            RxObservable<PackageBean>().getObservableIo {
                return@getObservableIo OffLineDataLogic.instance?.getBabyByIdInPkg(mUser!!.userId)
            }
                    .compose(Live.bindLifecycle(this))
                    .subscribe {
                        if (it.isDataRight) {
                            mUser!!.baby = it.obj as Baby?
                            showBabyInfoView(it.obj as Baby?)
                        }
                    }
        } else {
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

    private fun isShowUserInfoView(isShow: Boolean) {
        mTvHintLogin?.visibility = if (isShow) { View.GONE } else { View.VISIBLE }

        mTvNickname?.visibility = if (isShow) { View.VISIBLE } else { View.GONE }
        mTvBaby?.visibility = if (isShow) { View.VISIBLE } else { View.GONE }
        mTvBaby?.text = "Baby : 您还没有添加宝宝哟~"
    }


    private fun showBabyInfoView(baby: Baby?) {
        mTvBaby?.text = "Baby : " + baby?.babyName + " " + AppUtil.getBabyAgeInfo(baby?.babyBirth)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_camera -> {
                if (isUserLogin()) { setUserHead() } else {
                    TastyToastUtil.showInfo("哼!你还没登录呢")
                }
            }
            R.id.nav_gallery -> {
                if (isUserLogin()) {
                    val intent = Intent(this, AddBabyActivity::class.java)
                    intent.putExtra(AppUtil.CURRENT_USER, mUser)
                    startActivityForResult(intent, AppUtil.REQUEST_CODE_ADD_BABY)
                } else {
                    TastyToastUtil.showInfo("哼!你还没登录呢")
                }
            }
            R.id.nav_alert -> {
                if (isUserLogin()) {
                    if (mUser?.baby != null) {
                        DialogUtil.showAlertDialog(mContext!!, mUser!!.baby)
                    }else {
                        TastyToastUtil.showInfo("哼!你还添加宝宝呢")
                    }
                } else {
                    TastyToastUtil.showInfo("哼!你还没登录呢")
                }
            }
            R.id.nav_logout -> {
                PreferenceUtil.saveField(PreferenceUtil.CURRENT_USER, BaseBean.TEXT_EMPTY)
                initUserInfo()
            }
            R.id.nav_share -> {
                if (isUserLogin() && mUser!!.baby != null &&
                        FileUtils.isFileExist(AppUtil.getBabyPicPath(mUser!!.baby))) {
                    ShareUtil.shareImg(mContext!!, mUser!!.baby)
                }else {
                    ShareUtil.shareTxt(mContext!!)
                }
            }
            R.id.nav_send -> {
                startActivity(Intent(mContext, MoreActivity::class.java))
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
        }else if (requestCode == AppUtil.REQUEST_CODE_ADD_BABY && resultCode == Activity.RESULT_OK) {
            initUserInfo()
        }
    }

    private fun setUserHead() {
        MatisseUtil.turn2album(this)
    }

    private fun isUserLogin(): Boolean {
        return mUser != null
    }


    internal inner class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun setPrimaryItem(container: ViewGroup?, position: Int, `object`: Any) {
            super.setPrimaryItem(container, position, `object`)
        }

        override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
            super.destroyItem(container, position, `object`)
        }

        override fun getItem(position: Int): Fragment {
            return mFragments[position]
        }

        override fun getCount(): Int {
            return if (CommonUtils.isListEmpty(mFragments)) 0 else mFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            if (position == 0) {
                return "一类疫苗"
            }else {
                return "二类疫苗"
            }
        }

    }

}
