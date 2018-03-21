package com.joe.ibaby.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joe.ibaby.R
import com.joe.ibaby.base.BaseLazyFragment

/**
 * @author qiaojianfeng on 18/3/21.
 */
class Vaccine1Fragment : BaseLazyFragment() {

    override fun onCreateContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.fragment_vaccine1, null)
    }

    override fun initFragment(view: View) {
    }

    override fun createdChange2Visible() {
    }

}