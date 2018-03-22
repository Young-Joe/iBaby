package com.joe.ibaby.ui.home

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.joe.customlibrary.utils.DensityUtil
import com.joe.ibaby.R
import com.joe.ibaby.base.BaseLazyFragment
import com.joe.ibaby.helper.SpaceItemDecoration
import com.joe.ibaby.helper.VaccineUtil

/**
 * @author qiaojianfeng on 18/3/21.
 */
class Vaccine2Fragment : BaseLazyFragment() {

    lateinit var adapter: VaccineAdapter

    override fun onCreateContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.fragment_vaccine1, null)
    }

    override fun initFragment(view: View) {
        view.findViewById<TextView>(R.id.tv_vaccine_class).setText(R.string.txt_vaccine2_info)

        val rcvVaccine = view.findViewById<RecyclerView>(R.id.rcv_vaccine)
        val layoutManager = LinearLayoutManager(context)
        rcvVaccine.layoutManager = layoutManager
        rcvVaccine.addItemDecoration(SpaceItemDecoration(DensityUtil.dip2px(context, 8F)))
        adapter = VaccineAdapter(context)
        rcvVaccine.adapter = adapter

        adapter.addData(VaccineUtil.getVaccine2(context))
    }

    override fun createdChange2Visible() {
    }
}