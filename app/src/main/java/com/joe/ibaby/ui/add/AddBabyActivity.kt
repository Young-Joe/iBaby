package com.joe.ibaby.ui.add

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.DatePicker
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.joe.customlibrary.common.CommonUtils
import com.joe.customlibrary.common.SystemBarHelper
import com.joe.ibaby.MainApplication
import com.joe.ibaby.R
import com.joe.ibaby.base.BaseActivity
import com.joe.ibaby.dao.beans.Baby
import com.joe.ibaby.dao.offline.OffLineDataLogic
import com.joe.ibaby.helper.*
import com.soundcloud.android.crop.Crop
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_add_baby.*
import java.io.File
import java.util.*

class AddBabyActivity : BaseActivity() {

    private val mUser = MainApplication.getUser()

    override fun setContentLayout(): Int {
        return R.layout.activity_add_baby
    }

    override fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = mUser.nickName + " - " + "爱宝"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener({ onBackPressed() })

        SystemBarHelper.immersiveStatusBar(this, 0F)
        SystemBarHelper.setHeightAndPadding(this, toolbar)

        fab.setOnClickListener { MatisseUtil.turn2album(this) }

        tv_birth.setOnClickListener { showDatePickDialog() }
        btn_save_baby.setOnClickListener { saveBaby() }


    }

    override fun initData() {

    }

    var userHeaderPath: String? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppUtil.REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            val result = Matisse.obtainResult(data)
            userHeaderPath = AppUtil.getUserHeaderPath(mUser!!)
            val outputUri = Uri.fromFile(File(userHeaderPath) )
            Crop.of(result[0], outputUri).asSquare().start(this)
        }else if (requestCode == Crop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            TastyToastUtil.showOK("头像设置成功")
            val bmobFile = BmobFile(File(userHeaderPath))

            iv_baby?.setImageBitmap(BitmapFactory.decodeFile(userHeaderPath))

//            bmobFile.uploadblock(object : UploadFileListener() {
//                override fun done(p0: BmobException?) {
//                    if(p0==null){
//                        mUser!!.userHead = bmobFile
//                        mUser!!.update(mUser!!.bmobObjId, object : UpdateListener() {
//                            override fun done(p0: BmobException?) {
//                                if(p0==null){
//                                    ToastUtil.showToast("上传文件成功")
//                                }else{
//                                    ToastUtil.showToast("上传文件失败")
//                                }
//                            }
//                        })
//                    }else{
//                        ToastUtil.showToast("上传文件失败")
//                    }
//                }
//            })
        }
    }


    private fun showDatePickDialog() {
        val ca = Calendar.getInstance()
        val year = ca.get(Calendar.YEAR)
        val month = ca.get(Calendar.MONTH)
        val day = ca.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                tv_birth.text = year.toString() + "-" + (month+1)+ "-" + day
            }
        }, year, month, day)
                .show()
    }

    private fun saveBaby() {
        val babyNickname = edit_baby_nickname.text.toString()
        val babyBirth = tv_birth.text.toString()
        if (CommonUtils.isTextEmpty(babyNickname)) {
            TastyToastUtil.showInfo(String.format(getString(R.string.toast_info_baby_nickname), AppUtil.getBabyCall(mUser)))
        }else if (babyBirth.equals(ResourceUtil.getString(R.string.txt_baby_birth_default))) {
            TastyToastUtil.showInfo(String.format(getString(R.string.toast_info_baby_birth), AppUtil.getBabyCall(mUser)))
        }else {
            val dialog = DialogUtil.showProgress(this, "保存中...")
            val baby = Baby()
            baby.userId = mUser.userId
            baby.babyName = babyNickname
            baby.babyBirth = babyBirth

            OffLineDataLogic.instance?.saveBaby(baby)
            baby.save(object : SaveListener<String>(){
                override fun done(p0: String?, p1: BmobException?) {
                    CommonUtils.dismiss(dialog, mContext)
                    BmobUtil.onHandleBmob(p1, object : BmobUtil.OnHandleBmobListener {
                        override fun onSuccess() {
                            TastyToastUtil.showOK("保存成功,开启疫苗提醒吧~")
                            //TODO

                        }
                    })
                }

            })
        }

    }


}
