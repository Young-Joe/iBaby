package com.joe.ibaby.base;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joe.customlibrary.common.CommonUtils;

import java.util.ArrayList;
import java.util.List;


/**
 *
 *  BaseRecyclerViewAdapter
 * Created by QiaoJF on 17/3/6.
 */
public abstract class BaseRVAdapter<T,VH extends RecyclerView.ViewHolder > extends RecyclerView.Adapter<VH> {

    public Context mContext;
    public final LayoutInflater mInflater;
    public List<T> dataList = new ArrayList<T>();

    public int selectPosition = -1;

    public BaseRVAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void addData(List<T> inputList){
        if(inputList == null){
            inputList = new ArrayList<T>();
        }
        dataList.clear();
        dataList.addAll(inputList);
        notifyDataSetChanged();
    }

    public void clearData(){
        dataList.clear();
        this.selectPosition = -1;
        notifyDataSetChanged();
    }

    @LayoutRes
    public abstract int onCreateView();

    public abstract VH getViewHolder(View itemView);

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(onCreateView(),parent,false);
        return getViewHolder(view);
    }

    @CallSuper
    @Override
    public void onBindViewHolder(VH holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener != null){
                    setSelectPosition(position);
                    mOnItemClickListener.onItemClick(position,dataList.get(position),false);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    /**
     * 返回指定position的对象
     * @param position
     * @return
     */
    public T getPositionData(int position){
        if(CommonUtils.isListEmpty(dataList)){
            return null;
        }
        if(dataList.size() > position){
            setSelectPosition(position);
            return dataList.get(position);
        }
        return null;
    }


    public interface OnItemClickListener<T>{
        void onItemClick(int position, T t, boolean isPerformClick);
    }

    OnItemClickListener<T> mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setSelectPosition(int selectPosition){
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }

    /**
     *
     * 代码实现item点击事件
     * @param performPosition
     */
    public void performItemClick(int performPosition){
        if(mOnItemClickListener != null && performPosition < dataList.size()){
            setSelectPosition(performPosition);
            mOnItemClickListener.onItemClick(performPosition,dataList.get(performPosition),true);
        }
    }

}
