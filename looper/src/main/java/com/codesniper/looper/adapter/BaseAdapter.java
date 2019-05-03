package com.codesniper.looper.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 适配器基类
 */
public abstract class BaseAdapter<T> {

    private List<T> datas;
    protected Context mContext;


    public BaseAdapter(Context context, List<T> datas) {
        this.datas = datas;
        this.mContext=context;
    }

    /**
     * 获取数据个数
     *
     * @return
     */
    public  int getCount(){
        return datas == null ? 0 : datas.size();
    }
//
    /**
     * 获取itemView
     *
     * @param pos
     * @param itemView
     * @param containerView * @return
     */
    public abstract View getItemView(int pos, View itemView, ViewGroup containerView);


    /**
     * 获取数据源
     * @return
     */
    public List<T> getDatas(){
        return datas;
    }


    public Context getmContext() {
        return mContext;
    }
}
