package com.joe.ibaby.ui.add;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.joe.customlibrary.common.CommonUtils;
import com.joe.ibaby.R;
import com.joe.ibaby.base.BaseRVAdapter;
import com.joe.ibaby.dao.beans.Baby;
import com.joe.ibaby.dao.beans.Vaccine;


/**
 * @author qiaojianfeng on 18/3/17.
 */

public class AddBabyVaccineAdapter extends BaseRVAdapter<Vaccine, VaccineHolder> {

    private Baby mBaby;

    public AddBabyVaccineAdapter(Context context) {
        super(context);
    }

    public void addBaby(Baby baby) {
        mBaby = baby;
        notifyDataSetChanged();
    }

    @Override
    public int onCreateView() {
        return R.layout.item_add_baby_vaccine1;
    }

    @Override
    public VaccineHolder getViewHolder(View itemView) {
        return new VaccineHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VaccineHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Vaccine item = dataList.get(position);

        if (position >= 1) {
            if (dataList.get(position - 1).getAge().equals(item.getAge())) {
                holder.tvAge.setVisibility(View.INVISIBLE);
            }else {
                holder.tvAge.setVisibility(View.VISIBLE);
                holder.tvAge.setText(item.getAge());
            }
        }else {
            holder.tvAge.setText(item.getAge());
        }
        holder.tvVaccine.setText(item.getVaccine());
        if (CommonUtils.isTextEmpty(item.getTimes())) {
            holder.tvTimes.setVisibility(View.GONE);
        }else {
            holder.tvTimes.setVisibility(View.VISIBLE);
            holder.tvTimes.setText(item.getTimes());
        }
        holder.tvInfo.setText(item.getInfo());

        if (mBaby != null) {
            if (mBaby.getAge() > item.getAge2day()) {
                holder.btnPack.setBackgroundResource(R.mipmap.ic_vaccine);
            } else {
                holder.btnPack.setBackgroundResource(R.mipmap.ic_vaccine_small_active);
            }
        }else {
            holder.btnPack.setBackgroundResource(R.mipmap.ic_vaccine_small_active);
        }


    }

}

class VaccineHolder extends RecyclerView.ViewHolder{

    TextView tvAge;
    Button btnPack;
    TextView tvVaccine;
    TextView tvTimes;
    TextView tvInfo;


    public VaccineHolder(View itemView) {
        super(itemView);
        tvAge = itemView.findViewById(R.id.tv_age);
        btnPack = itemView.findViewById(R.id.btn_pack);
        tvVaccine = itemView.findViewById(R.id.tv_vaccine);
        tvTimes = itemView.findViewById(R.id.tv_times);
        tvInfo = itemView.findViewById(R.id.tv_info);

    }

}
