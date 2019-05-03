package com.codesniper.looperscrollview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.codesniper.looper.R;
import com.codesniper.looper.adapter.BaseAdapter;


import java.util.List;

/**
 * 基础适配器
 */
public class CircleAdapter extends BaseAdapter<String> {

    public CircleAdapter(Context context, List<String> datas) {
        super(context, datas);
    }


    @Override
    public View getItemView(int pos, View itemView, ViewGroup containerView) {
        ViewHolder viewHolder = null;
        if (itemView == null) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.common_widget, containerView,false);
            viewHolder = new ViewHolder();
            viewHolder.itemView = itemView;
            viewHolder.iv = (ImageView) itemView.findViewById(R.id.iv);
            itemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) itemView.getTag();
        }
        if(getCount()>pos){
            String  url = getDatas().get(pos);
            Glide.with(mContext).load(url).into(viewHolder.iv);
        }
        return itemView;
    }


    private static class ViewHolder {
        private View itemView;
        private ImageView iv;
    }
}
