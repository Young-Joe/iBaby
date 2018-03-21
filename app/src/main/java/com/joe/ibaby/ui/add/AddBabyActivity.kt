package com.joe.ibaby.ui.add

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.DatePicker
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import cn.bmob.v3.listener.UploadFileListener
import com.alibaba.fastjson.JSONArray
import com.joe.customlibrary.common.CommonUtils
import com.joe.customlibrary.common.SystemBarHelper
import com.joe.customlibrary.utils.FileUtils
import com.joe.customlibrary.utils.TimeUtils
import com.joe.customlibrary.utils.ToastUtil
import com.joe.ibaby.R
import com.joe.ibaby.base.BaseActivity
import com.joe.ibaby.dao.beans.Baby
import com.joe.ibaby.dao.beans.BaseBean
import com.joe.ibaby.dao.beans.BaseBean.DEFAULT_GENDER
import com.joe.ibaby.dao.beans.User
import com.joe.ibaby.dao.beans.Vaccine
import com.joe.ibaby.dao.offline.OffLineDataLogic
import com.joe.ibaby.helper.*
import com.soundcloud.android.crop.Crop
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_add_baby.*
import java.io.File
import java.util.*

class AddBabyActivity : BaseActivity() {

    private lateinit var mUser: User
    private lateinit var vaccineAdapter: AddBabyVaccineAdapter

    override fun setContentLayout(): Int {
        return R.layout.activity_add_baby
    }

    override fun initView() {
        mUser = intent.getSerializableExtra(AppUtil.CURRENT_USER) as User
        setSupportActionBar(toolbar)

        if (mUser.baby != null && mUser.baby.babyName != null) {
            supportActionBar!!.title = mUser.nickName + " - " + mUser.baby.babyName
        } else {
            supportActionBar!!.title = mUser.nickName + " - " + "爱宝"
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener({ onBackPressed() })

        SystemBarHelper.immersiveStatusBar(this, 0F)
        SystemBarHelper.setHeightAndPadding(this, toolbar)

        fab.setOnClickListener {
            if (mUser.baby == null) {
                TastyToastUtil.showInfo("请先上传宝宝信息~")
            } else {
                MatisseUtil.turn2album(this)
            }
        }

        ctv_edit_baby.setOnClickListener { observeCtvEdit() }
        tv_birth.setOnClickListener { showDatePickDialog() }
        btn_save_baby.setOnClickListener { saveBaby() }
        babyInfoViewState(true)

        val layoutManager = LinearLayoutManager(this)
        rcv_vaccine.layoutManager = layoutManager
        vaccineAdapter = AddBabyVaccineAdapter(this)
        rcv_vaccine.adapter = vaccineAdapter

    }

    private fun observeCtvEdit() {
        ctv_edit_baby.toggle()
        showBabyInfo(mUser.baby)
        babyInfoViewState(ctv_edit_baby.isChecked)
    }

    override fun initData() {
//        RxObservable<PackageBean>().getObservableIo {
//            return@getObservableIo OffLineDataLogic.instance?.getBabyByIdInPkg(mUser!!.userId)
//        }
//                .compose(Live.bindLifecycle(this))
//                .subscribe {
//                    if (it.isDataRight) {
//                        ctv_edit_baby.visibility = View.VISIBLE
//                        mUser!!.baby = it.obj as Baby?
//                        showBabyInfo(it.obj as Baby)
//                        babyInfoViewState(false)
//                    }
//                }

        if (mUser.baby != null) {
            ctv_edit_baby.visibility = View.VISIBLE
            showBabyInfo(mUser.baby)
            babyInfoViewState(false)
        }

        val inputS = assets.open("vaccine_1")
        val buffer = ByteArray(inputS.available())
        inputS.read(buffer)
        val vaccines = JSONArray.parseArray(String(buffer), Vaccine::class.java)
        vaccineAdapter.addData(vaccines)
        vaccineAdapter.addBaby(mUser.baby)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppUtil.REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            val result = Matisse.obtainResult(data)
            val babyPicPath = AppUtil.getBabyPicPath(mUser.baby!!)
            val outputUri = Uri.fromFile(File(babyPicPath) )
            Crop.of(result[0], outputUri).asSquare().start(this)
        }else if (requestCode == Crop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            TastyToastUtil.showOK("宝宝照片设置成功")
            loadLocalBabyPic(mUser.baby!!)
            if (CommonUtils.isTextEmpty(mUser.baby!!.bmobObjId)) return
            val bmobFile = BmobFile(File(AppUtil.getBabyPicPath(mUser.baby!!)))
            bmobFile.uploadblock(object : UploadFileListener() {
                override fun done(p0: BmobException?) {
                    if(p0==null){
                        mUser.baby!!.babyPic = bmobFile
                        mUser.baby!!.update(mUser.baby!!.bmobObjId, object : UpdateListener() {
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

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
        super.onBackPressed()
    }

    private fun showDatePickDialog() {
        val ca = Calendar.getInstance()
        val syear = ca.get(Calendar.YEAR)
        val smonth = ca.get(Calendar.MONTH)
        val sday = ca.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                val selectDate = year.toString() + "-" + (month+1)+ "-" + dayOfMonth
                if (TimeUtils.getDateDif(selectDate) > -30) {
                    tv_birth.text = selectDate
                }
            }
        }, syear, smonth, sday)
                .show()
    }

    private fun saveBaby() {
        val babyNickname = edit_baby_nickname.text.toString()
        if (babyNickname.contains("(")) {

        }
        val babyBirth = tv_birth.text.toString()
        var babyGender = DEFAULT_GENDER
        if (rg_gender.checkedRadioButtonId == R.id.rdbtn_girl) {
            babyGender = 2
        }
        if (CommonUtils.isTextEmpty(babyNickname)) {
            TastyToastUtil.showInfo(String.format(getString(R.string.toast_info_baby_nickname), AppUtil.getBabyCall(mUser)))
        }else if (babyBirth.equals(ResourceUtil.getString(R.string.txt_baby_birth_default))) {
            TastyToastUtil.showInfo(String.format(getString(R.string.toast_info_baby_birth), AppUtil.getBabyCall(mUser)))
        }else {
            val dialog = DialogUtil.showProgress(this, "保存中...")

            if (mUser.baby == null) {
                val baby = Baby()
                saveBaby2local(baby, babyNickname, babyBirth, babyGender)
                mUser.baby = baby
                baby.save(object : SaveListener<String>(){
                    override fun done(p0: String?, p1: BmobException?) {
                        CommonUtils.dismiss(dialog, mContext)
                        TastyToastUtil.showOK("保存成功,开启疫苗提醒吧~")
                        babyInfoViewState(false)
                        BmobUtil.onHandleBmob(p1, object : BmobUtil.OnHandleBmobListener() {
                            override fun onSuccess() {
                                baby.bmobObjId = p0
                                mUser.baby = baby
                                OffLineDataLogic.instance?.saveBaby(baby)
                            }
                        })
                    }
                })
            }else {
                saveBaby2local(mUser.baby, babyNickname, babyBirth, babyGender)
                if (isBabyUpload()) {
                    mUser.baby.update(mUser.baby.bmobObjId, object : UpdateListener() {
                        override fun done(p0: BmobException?) {
                            TastyToastUtil.showOK("保存成功,开启疫苗接种提醒吧~")
                            babyInfoViewState(false)
                        }

                    })
                }
            }
            CommonUtils.dismiss(dialog, mContext)

        }

    }

    /**
     * baby信息是否上传成功,返回bmobObjId
     */
    private fun isBabyUpload() = mUser.baby.bmobObjId != null

    private fun saveBaby2local(baby: Baby, babyNickname: String, babyBirth: String, babyGender: Int) {
        baby.userId = mUser.userId
        baby.babyName = babyNickname
        baby.babyBirth = babyBirth
        baby.age = AppUtil.getBabyAge(babyBirth)
        baby.gender = babyGender
        OffLineDataLogic.instance?.saveBaby(baby)
        vaccineAdapter.addBaby(mUser.baby)
    }

    /**
     * 宝宝信息 编辑/不可编辑 状态
     */
    private fun babyInfoViewState(edit: Boolean) {
        edit_baby_nickname.isEnabled = edit
        edit_baby_nickname.isFocusable = edit
        edit_baby_nickname.isFocusableInTouchMode = true
        tv_birth.isEnabled = edit
        rdbtn_boy.isEnabled = edit
        rdbtn_girl.isEnabled = edit
        if (edit) {
            btn_save_baby.visibility = View.VISIBLE
        }else {
            btn_save_baby.visibility = View.GONE
        }
    }


    private fun showBabyInfo(baby: Baby) {
        loadLocalBabyPic(baby)
        edit_baby_nickname.setText(baby.babyName)
        edit_baby_nickname.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                edit_baby_nickname.setText(baby.babyName)
            }
        }
        tv_birth.text = baby.babyBirth
        if (baby.gender == BaseBean.DEFAULT_GENDER) {
            rdbtn_boy.isChecked = true
        }else {
            rdbtn_girl.isChecked = true
        }
    }

    private fun loadLocalBabyPic(baby: Baby) {
        if (FileUtils.isFileExist(AppUtil.getBabyPicPath(baby))) {
            iv_baby?.setImageBitmap(BitmapFactory.decodeFile(AppUtil.getBabyPicPath(baby)))
        }
    }

}
