package com.joe.ibaby.ui.home

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.joe.customlibrary.common.CommonUtils
import com.joe.ibaby.R
import com.joe.ibaby.base.BaseRVAdapter
import com.joe.ibaby.dao.beans.Vaccine

/**
 * @author qiaojianfeng on 18/3/22.
 */
class VaccineAdapter(context: Context?) : BaseRVAdapter<Vaccine, VaccineHolder>(context) {

    override fun onCreateView(): Int {
        return R.layout.item_home_vaccine1
    }

    override fun getViewHolder(itemView: View?): VaccineHolder {
        return VaccineHolder(itemView!!)
    }

    override fun onBindViewHolder(holder: VaccineHolder?, position: Int) {
        super.onBindViewHolder(holder, position)
        val vaccine = dataList.get(position)
        holder?.tvAge?.text = vaccine.age
        holder?.tvVaccine?.text = vaccine.vaccine
        holder?.tvTimes?.text = vaccine.times
        holder?.tvInfo?.text = vaccine.info
        if (CommonUtils.isTextEmpty(vaccine.warn)) {
            holder?.layoutWarn?.visibility = View.GONE
        }else {
            holder?.layoutWarn?.visibility = View.VISIBLE
            holder?.tvWarn?.text = vaccine.warn
        }
        if (CommonUtils.isTextEmpty(vaccine.attention)) {
            holder?.layoutAttention?.visibility = View.GONE
        }else {
            holder?.layoutAttention?.visibility = View.VISIBLE
            holder?.tvAttention?.text = vaccine.attention
        }
        if (CommonUtils.isTextEmpty(vaccine.price)) {
            holder?.layoutPrice?.visibility = View.GONE
        }else {
            holder?.layoutPrice?.visibility = View.VISIBLE
            holder?.tvPrice?.text = vaccine.price
        }
    }

}

class VaccineHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var tvAge: TextView
    var tvVaccine: TextView
    var tvTimes: TextView
    var tvInfo: TextView
    var layoutWarn: LinearLayout
    var tvWarn: TextView
    var layoutAttention: LinearLayout
    var tvAttention: TextView
    var layoutPrice: LinearLayout
    var tvPrice: TextView

    init {
        tvAge = itemView.findViewById(R.id.tv_age)
        tvVaccine = itemView.findViewById(R.id.tv_vaccine)
        tvTimes = itemView.findViewById(R.id.tv_times)
        tvInfo = itemView.findViewById(R.id.tv_info)
        layoutWarn = itemView.findViewById(R.id.layout_warn)
        tvWarn = itemView.findViewById(R.id.tv_warn)
        layoutAttention = itemView.findViewById(R.id.layout_attention)
        tvAttention = itemView.findViewById(R.id.tv_attention)
        layoutPrice = itemView.findViewById(R.id.layout_price)
        tvPrice = itemView.findViewById(R.id.tv_price)
    }

}