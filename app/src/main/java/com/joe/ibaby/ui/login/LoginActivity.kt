package com.joe.ibaby.ui.login

import android.app.Activity
import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.design.widget.TextInputLayout
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.DownloadFileListener
import cn.bmob.v3.listener.FindListener
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.joe.customlibrary.common.CommonUtils
import com.joe.ibaby.R
import com.joe.ibaby.base.BaseActivity
import com.joe.ibaby.dao.beans.Baby
import com.joe.ibaby.dao.beans.User
import com.joe.ibaby.helper.*
import com.joe.ibaby.ui.register.RegisterActivity
import com.shundaojia.live.Live
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.activity_login.*
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * login
 */
class LoginActivity : BaseActivity(){

    companion object {
        val REGISTER_REQUEST_CODE = 10
        val REGISTER_USER_ID = "userId"
    }

    private var mViewModel: LoginViewModel? = null

    private var mObsUserId: Observable<Boolean>? = null
    private var mObsUserPswd: Observable<Boolean>? = null


    override fun setContentLayout(): Int {
        return R.layout.activity_login
    }

    override fun initView() {
        mViewModel = ViewModelProviders.of(this, ViewModelFactory()).get(LoginViewModel::class.java)

        setSupportActionBar(toolbar)
        supportActionBar?.title = ResourceUtil.getString(R.string.txt_login_sign)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        mObsUserId = RxTextView.textChanges(edt_user_id)
                .map {
                    !CommonUtils.isTextEmpty(it.toString())
                }

        mObsUserPswd = RxTextView.textChanges(edt_password)
                .map {
                    !CommonUtils.isTextEmpty(it.toString())
                }

        mObsUserId?.subscribe {
            observeTxtInputLayout(it, input_layout_userid)
        }
        mObsUserPswd?.subscribe { observeTxtInputLayout(it, input_layout_password) }

        RxView.clicks(btn_login)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe { onInputLogin() }
        RxView.clicks(btn_go_register)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe { goRegister() }

    }

    override fun initData() {}

    private fun goRegister() {
        startActivityForResult(Intent(this, RegisterActivity::class.java), REGISTER_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REGISTER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            edt_user_id.setText(data?.getStringExtra(REGISTER_USER_ID))
            edt_password.requestFocus()
        }
    }

    private fun onInputLogin() {
        Observable.combineLatest(mObsUserId, mObsUserPswd, object : BiFunction<Boolean, Boolean, Boolean>{
            override fun apply(userIdNotEmpty: Boolean, userPswdNotEmpty: Boolean): Boolean {
                return userIdNotEmpty && userPswdNotEmpty
            }
        })
                .subscribe {
                    if (it) {
                        loginTask()
                    }else {
                        if (CommonUtils.isTextEmpty(edt_user_id.text.toString())) {
                            input_layout_userid.error = "请输入用户登录账号"
                            edt_user_id.requestFocus()
                        }else if (CommonUtils.isTextEmpty(edt_password.text.toString())) {
                            input_layout_password.error = "请输入用户密码"
                            edt_password.requestFocus()
                        }
                    }
                }.dispose()
    }

    private fun loginTask() {
        val dialog = DialogUtil.showProgress(this, "登录中...")
        val userId = edt_user_id.text.toString()
        val password = edt_password.text.toString()
        mViewModel?.loginTask(userId, password, object : FindListener<User>() {
            override fun done(users: MutableList<User>?, p1: BmobException?) {
                CommonUtils.dismiss(dialog, mContext)
                BmobUtil.onHandleBmob(p1, object : BmobUtil.OnHandleBmobListener() {
                    override fun onSuccess() {
                        if (users?.size == 0) {
                            TastyToastUtil.showInfo("请检查账号.密码是否正确")
                        }else {
                            saveCurrentUser(users!![0])
                            checkUserBaby(users[0])
                        }
                    }

                    override fun onError() {
                        mViewModel!!.getLocalUserById(userId)
                                .compose(Live.bindLifecycle(this@LoginActivity))
                                .subscribe { t ->
                                    if (t.isDataRight) {
                                        if (mViewModel!!.checkUserPswd(t.obj as User, password)) {
                                            saveCurrentUser(users!![0])
                                            loginSuccess(true)
                                        }else {
                                            TastyToastUtil.showError("阿欧, 密码不对哦~")
                                        }
                                    }else {
                                        TastyToastUtil.showError("阿欧, 你好像还没注册哦~")
                                    }
                                }
                    }

                })
            }

        })
    }

    private fun <T : TextInputLayout> observeTxtInputLayout(notEmpty: Boolean, txtInputLayout: T) {
        if (notEmpty) {
            if (null != txtInputLayout.error && !CommonUtils.isTextEmpty(txtInputLayout.error!!.toString())) {
                txtInputLayout.isErrorEnabled = false
            }
        }
    }

    private fun saveCurrentUser(user: User) {
        mViewModel?.saveCurrentUser(user)
    }

    private fun checkUserBaby(user: User) {
        val dialog = DialogUtil.showProgress(this, "Baby数据加载中...")
        mViewModel!!.loadUserBaby(user.userId, object : FindListener<Baby>() {
            override fun done(p0: MutableList<Baby>?, p1: BmobException?) {
                CommonUtils.dismiss(dialog, mContext)
                BmobUtil.onHandleBmob(p1, object : BmobUtil.OnHandleBmobListener(){
                    override fun onSuccess() {
                        mViewModel?.saveBabyLoadBabyPic(p0?.get(0))
                        checkLocalUserHeader(user)
                    }

                    override fun onError() {
                        checkLocalUserHeader(user)
                    }
                })
            }
        })
    }

    private fun checkLocalUserHeader(user: User) {
        if (mViewModel!!.checkLocalUserHeaderExist(user)) {
            user.userHead.download(File(AppUtil.getUserHeaderPath(user)), object : DownloadFileListener() {
                var dialog: Dialog? = null
                override fun onStart() {
                    super.onStart()
                    dialog = DialogUtil.showProgress(mContext!!, "登录中成功,头像下载中...")
                }

                override fun onProgress(p0: Int?, p1: Long) {}

                override fun done(p0: String?, p1: BmobException?) {
                    CommonUtils.dismiss(dialog, mContext)
                    if (BmobUtil.isSuccess(p1)) {
                        TastyToastUtil.showOK("头像加载成功~开启爱宝生活吧")
                    } else {
                        TastyToastUtil.showError("哼!你的头怎么这么大~下载失败啦.下次再说吧")
                    }
                    loginSuccess(false)
                }
            })
        }else {
            loginSuccess(true)
        }

    }

    private fun loginSuccess(showToast: Boolean) {
        if (showToast) { TastyToastUtil.showOK("登录成功~开启爱宝生活吧") }
        setResult(Activity.RESULT_OK)
        onBackPressed()
    }

}
