package com.codesniper.looperscrollview.view;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.codesniper.looper.main.LooperConfig;
import com.codesniper.looper.main.LooperScrollContainer;
import com.codesniper.looper.main.PicLoadStrategy;
import com.codesniper.looper.widget.CircleImageView;
import com.codesniper.looperscrollview.R;
import com.codesniper.looperscrollview.adapter.CircleAdapter;
import com.codesniper.looperscrollview.utils.NumberUtils;
import com.codesniper.looperscrollview.utils.RxTextTool;
import com.codesniper.looperscrollview.bean.TalkBean;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TalkView extends LinearLayout {

    private List<TalkBean.AccountDesListBean> datas;
    private Context mContext;


    private TextView mTvIncome;

    private TextView mTvWord;

    private TextView mTvUsername;

    private TextView mTvUserCareer;

    private CircleImageView mIvUserIcon;

    private LooperScrollContainer mContainer;

    private MovingCard movingCard;

    public TalkView(Context context) {
        super(context);
        initView(context);
    }

    public TalkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TalkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TalkView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    public void setDatas(TalkBean datas) {
        this.datas = datas.getAccountDesList();
        CircleAdapter circleAdapter = new CircleAdapter(mContext, datas.getUrls());
        mContainer.setAdapter(circleAdapter);
        initTalkView(mContainer.getSelectedPos());
    }

    private void initView(Context context) {
        this.mContext=context;
        View view = LayoutInflater.from(context).inflate(R.layout.common_talk_view, this);
        mContainer = view.findViewById(R.id.looperSrollContainer);
        mTvIncome = view.findViewById(R.id.tv_income);
        mTvWord = view.findViewById(R.id.tv_user_word);
        mTvUserCareer = view.findViewById(R.id.tv_user_career);
        movingCard=view.findViewById(R.id.moving_card);
        mTvUsername = view.findViewById(R.id.tv_username);
        mIvUserIcon = view.findViewById(R.id.iv_icon);
        mContainer.setOnItemSelectedListener(position -> {
            initTalkView(position);
        });
        movingCard.setView(mContainer);
        mContainer.setConfig(LooperConfig.getBuilder()
                .enableAutoLoop(true)
                .setLoadWay((PicLoadStrategy<String>) (iv, url) -> Glide.with(mContext).load(url).into(iv))
                .setLoopInterverTime(5000)
                .setClickInterverTime(1250)
                .build());
    }

    private void initTalkView(int position){
        if(mContext instanceof Activity){
            Activity activity= (Activity) mContext;
            if(activity==null||activity.isFinishing()){
                return;
            }
        }
        TalkBean.AccountDesListBean item = datas.get(position);
        Glide.with(mContext).load(item.getAvatarUrl()).into(mIvUserIcon);
        mTvUsername.setText(item.getNickName());
        mTvUserCareer.setText(item.getAddress() + " " + item.getJob());
        mTvWord.setText(item.getContent());
        String finalPrice = NumberUtils.deleteZero(item.getBalance());
        String finalPriceInt = finalPrice;
        String finalPriceFloat = "";
        if (finalPrice.contains(".")) {
            String[] filter = finalPrice.split("\\.");
            if (filter != null & filter.length >= 2) {
                finalPriceInt = filter[0] + ".";
                finalPriceFloat = filter[1];
            }
        }
        RxTextTool.getBuilder("上月收益  ", mContext)
                .append("¥").setForegroundColor(mContext.getResources().getColor(R.color.color_red_ed4143)).setProportion(1.0f)
                .append(finalPriceInt).setForegroundColor(mContext.getResources().getColor(R.color.main_color)).setBold().setProportion(1.5f)
                .append(finalPriceFloat).setForegroundColor(mContext.getResources().getColor(R.color.main_color)).setProportion(1f)
                .into(mTvIncome);
    }



    private float lastX;
    private float lastY;


    /**
     * 当接收到触点或者滑动X>Y 父控件不拦截触摸
     * @param e
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = e.getX();
                lastY = e.getY();
                //父容器禁止拦截
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                // 只要横向大于竖向，就拦截掉事件。
                // 部分机型点击事件（slopx==slopy==0），会触发MOVE事件。
                // 所以要加判断(slopX > 0 || sloy > 0)
                float slopX = Math.abs(e.getX() - lastX);
                float slopY = Math.abs(e.getY() - lastY);
                if ((slopX > 0 || slopY > 0) && slopX <= slopY) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(e);
    }


}
