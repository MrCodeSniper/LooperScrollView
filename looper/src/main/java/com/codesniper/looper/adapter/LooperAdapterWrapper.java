package com.codesniper.looper.adapter;

import android.view.View;
import android.view.ViewGroup;


/**
 * 实现循环滚动的适配器
 */
public class LooperAdapterWrapper extends BaseAdapter {


    private BaseAdapter adapter;
    private int visiableCount=5;

    public LooperAdapterWrapper(BaseAdapter adapter) {
        super(adapter.getmContext(), adapter.getDatas());
        this.adapter = adapter;
    }

    public int getRealDataCount() {
        return adapter.getCount();
    }

    @Override
    public int getCount() { //重新计算数目 因为需要两个额外View进行循环
        return 7;
    }


    //显示的ITEM
    @Override
    public View getItemView(int pos, View itemView, ViewGroup containerView) {
        if (pos == 0) {
            return adapter.getItemView(adapter.getCount() - 1, itemView, containerView);
        }
        return adapter.getItemView(pos - 1, itemView, containerView);
    }


    /**
     *
     * @param pos
     * @param firstVisiblePos
     * @param itemView
     * @param containerView
     * @return
     */
    public View getItemView(int pos, int firstVisiblePos, View itemView, ViewGroup containerView) {
        int realPos = 0;//在真实集合里的位置 比如10个

        if (pos == 0) {
            realPos = firstVisiblePos - 1;
            if (realPos < 0) {
                realPos += getRealDataCount();
            }
        } else if (pos == getCount()-1) {
            realPos = firstVisiblePos + visiableCount+1;
            if (realPos > getRealDataCount() - 1) {
                realPos -= getRealDataCount();
            }
        } else {
            realPos = firstVisiblePos + pos - 1;
        }

        return adapter.getItemView(realPos, itemView, containerView);
    }


}
