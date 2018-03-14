package com.joe.ibaby.ui.register

import android.app.Activity
import android.content.Intent
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxRadioGroup
import com.joe.customlibrary.common.CommonUtils
import com.joe.ibaby.R
import com.joe.ibaby.base.BaseActivity
import com.joe.ibaby.dao.beans.BaseBean.DEFAULT_GENDER
import com.joe.ibaby.dao.beans.User
import com.joe.ibaby.helper.BmobUtil
import com.joe.ibaby.helper.DialogUtil
import com.joe.ibaby.helper.ResourceUtil
import com.joe.ibaby.helper.TastyToastUtil
import com.joe.ibaby.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_register.*
import java.util.concurrent.TimeUnit

class RegisterActivity : BaseActivity() {

    var userGender: Int = 1

    override fun setContentLayout(): Int {
        return R.layout.activity_register
    }

    override fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = ResourceUtil.getString(R.string.txt_login_register)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        RxView.clicks(btn_register)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe { beginRegister() }

        RxRadioGroup.checkedChanges(rg_gender)
                .subscribe {
                    if (it == R.id.rdbtn_women) {
                        userGender = 2
                    } else {
                        userGender = DEFAULT_GENDER
                    }

                }

    }

    override fun initData() {
    }

    private fun beginRegister() {
        val txtUserId = edit_register_userid.text.toString()
        val txtUserNickname = edit_register_nickname.text.toString()
        val txtUserPswd = edit_register_pswd.text.toString()
        val txtUserPswdCon = edit_register_pswd_confirm.text.toString()
        when {
            CommonUtils.isTextEmpty(txtUserId) -> {
                TastyToastUtil.showInfo("请输入登录账号")
                edit_register_userid.requestFocus()
            }
            CommonUtils.isTextEmpty(txtUserNickname) -> {
                TastyToastUtil.showInfo("请输入用户昵称")
                edit_register_nickname.requestFocus()
            }
            CommonUtils.isTextEmpty(txtUserPswd) -> {
                TastyToastUtil.showInfo("请输入登录密码")
                edit_register_pswd.requestFocus()
            }
            CommonUtils.isTextEmpty(txtUserPswdCon) -> {
                TastyToastUtil.showInfo("请输入确认密码")
                edit_register_pswd_confirm.requestFocus()
            }
            else -> {
                if (!txtUserPswd.equals(txtUserPswdCon)) {
                    TastyToastUtil.showInfo("密码不一致请确认")
                    edit_register_pswd_confirm.requestFocus()
                } else {
                    val dialog = DialogUtil.showProgress(this, "注册中...")

                    val user = User()
                    user.userId = txtUserId
                    user.nickName = txtUserNickname
                    user.password = txtUserPswd
                    user.gender = userGender
                    user.save(object : SaveListener<String>() {
                        override fun done(objectId: String?, e: BmobException?) {
                            CommonUtils.dismiss(dialog, mContext)
                            BmobUtil.onHandleBmob(e, object : BmobUtil.OnHandleBmobListener() {
                                override fun onSuccess() {
                                    TastyToastUtil.showOK("注册成功,开启登录吧~")
                                    val intent = Intent()
                                    intent.putExtra(LoginActivity.REGISTER_USER_ID, txtUserId)
                                    setResult(Activity.RESULT_OK, intent)
                                    onBackPressed()
                                }
                            })
                        }
                    })
                }
            }
        }

    }

}
