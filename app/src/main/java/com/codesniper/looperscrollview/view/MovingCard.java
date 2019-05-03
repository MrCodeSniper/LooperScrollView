package com.codesniper.looperscrollview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.codesniper.looper.main.LooperScrollContainer;

/**
 * 触摸共享
 */
public class MovingCard extends RelativeLayout {


    private Context mContext;

    public MovingCard(Context context) {
        super(context);
        this.mContext=context;
    }

    public MovingCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
    }

    public MovingCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
    }

    private static final int STATUS_REST = 0;
    private static final int STATUS_MOVE = 1;
    private int status = STATUS_REST;

    private double radio=0.5;

    private int downX, downY;
    private int lastX, lastY;
    private int touchSlop; //最小翻页距离

    private LooperScrollContainer view;

    public void setView(LooperScrollContainer view) {
        this.view = view;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = x;
                downY = y;
                if(view!=null) view.setAnimation(true);
                break;
            case MotionEvent.ACTION_MOVE:
                if (status != STATUS_MOVE) {
                    if (Math.abs(x - downX) > touchSlop) {
                        status = STATUS_MOVE;
                        return true;
                    } else {
                        status = STATUS_REST;
                    }
                } else {
                    //跟手势移动
                    if(view!=null) view.moveBy((int) ((x - lastX)*radio));//手指移动在很小时间内的位移差
                }
                break;
            case MotionEvent.ACTION_UP:
                //停留位置在中间
                if(view!=null) view.autoMove();
                status = STATUS_REST;
                if(view!=null) view.setAnimation(false);
        }
        lastX = x;
        lastY = y;
        return true;
    }





}
