package com.codesniper.looper.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;

import android.os.Bundle;
import android.util.AttributeSet;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.codesniper.looper.R;
import com.codesniper.looper.adapter.BaseAdapter;
import com.codesniper.looper.adapter.LooperAdapterWrapper;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class LooperScrollContainer<T> extends ViewGroup implements LifeListener {

    private static final String TAG = LooperScrollContainer.class.getName();

    private Context mContext;

    ///////////////////////////////////////////////////////////////////////////
    // View里监听生命周期
    ///////////////////////////////////////////////////////////////////////////


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Activity activity = getActivity();
        if (activity != null) {
            addLifeListener(activity);
        }
    }

    //获取宿主Activity，此处是否有问题？
    private Activity getActivity() {
        final Context context = getContext();
        if (context != null && context instanceof Activity) {
            return (Activity) context;
        }
        return null;
    }

    private void addLifeListener(Activity activity) {
        LifeListenerFragment fragment = getLifeListenerFragment(activity);
        fragment.addLifeListener(this);
    }


    private LifeListenerFragment getLifeListenerFragment(Activity activity) {
        FragmentManager manager = activity.getFragmentManager();
        return getLifeListenerFragment(manager);
    }

    //添加空白fragment
    private LifeListenerFragment getLifeListenerFragment(FragmentManager manager) {
        LifeListenerFragment fragment = (LifeListenerFragment) manager.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new LifeListenerFragment();
            manager.beginTransaction().add(fragment, TAG).commitAllowingStateLoss();
        }

        return fragment;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 构造参数
    ///////////////////////////////////////////////////////////////////////////

    public LooperScrollContainer(Context context) {
        this(context, null);
        init(context);
    }

    public LooperScrollContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public LooperScrollContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //它获得的是触发移动事件的最短距离，如果小于这个距离就不触发移动控件，如viewpager就是用这个距离来判断用户是否翻页
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        selectedPos = SCREEN_SHOW_COUNT / 2; //居中为2
        mContext = context;
        setConfig(LooperConfig.getBuilder().build());
    }


    ///////////////////////////////////////////////////////////////////////////
    // 监听回调
    ///////////////////////////////////////////////////////////////////////////

    private OnItemSelectedListener onItemSelectedListener; //每次滑动后中间选中的位置

    private Timer mTimer;

    public void startScroll() {
        mTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                LooperScrollContainer.this.postDelayed(() -> moveToPosition(4), loopIntervalTime);
            }
        };
        mTimer.schedule(timerTask, 0, loopIntervalTime);
    }

    public void stopScroll() {
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        if (autoLoop) {
            startScroll();
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        stopScroll();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
    }

    public interface OnItemSelectedListener {
        void onSelected(int position);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }


    ///////////////////////////////////////////////////////////////////////////
    // Config配置相关
    ///////////////////////////////////////////////////////////////////////////

    private int SCREEN_SHOW_COUNT = 5;//屏幕显示View个数
    private float SCALE_FACTOR = 0.5F; //缩放倍率
    private boolean autoLoop = false;
    private PicLoadStrategy picLoadStrategy;
    private int smoothDuration = 300;
    private static int INTERVAL_TIME = 1250;   // 点击间隔时间
    private int loopIntervalTime = 5000;


    public void setConfig(LooperConfig config) {
        this.picLoadStrategy = config.getPicLoadStrategy();
        this.loopIntervalTime = config.getLoopIntervalTime();
        this.autoLoop = config.isAutoLoop();
        this.smoothDuration = config.getMovingDuration();
        this.INTERVAL_TIME = config.getClickIntervalTime();


        this.SCREEN_SHOW_COUNT = config.getScreenShowCount();
        this.SCALE_FACTOR = config.getScaleFactory();
    }


    private static final int STATUS_REST = 0;
    private static final int STATUS_MOVE = 1;
    private int status = STATUS_REST;

    private int selectedPos;

    public int getSelectedPos() {
        return selectedPos;
    }

    private int childWidth;
    private int childHeight;

    private int offsetX;
    private int downX, downY;
    private int lastX, lastY;
    private int touchSlop; //最小翻页距离
    private int firstVisiblePos;
    private int retainWidth; //容器宽度根据显示的个数等分的余数
    private List<View> viewlist = new ArrayList<>();

    private LooperAdapterWrapper looperAdapterWrapper;


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        childWidth = width / SCREEN_SHOW_COUNT;//基本上为屏幕宽度/5
        childHeight = childWidth;//宽==高
        retainWidth = width - childWidth * SCREEN_SHOW_COUNT;

        //子view进行宽高赋值 基本上以等比划分
        measureChildren(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY));

        if (getChildCount() > 0) {
            height = (int) (childHeight * SCALE_FACTOR + childHeight + 0.5f);//将高度进行缩放
        }

        setMeasuredDimension(width, height);//重新进行测量
    }

    /**
     * 摆放子view的位置
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        //中心点坐标
        int centerX = width / 2;
        int centerY = height / 2;
        int halfChildWidth = childWidth / 2; //屏幕宽的/5/2
        int left = -1 * childWidth;//距离左边view的距离
        int top = centerY - childHeight / 2;//距离顶部的距离

        float maxRatio = 0;
        for (int i = 0; i < getChildCount(); i++) {
            //  HrzUtil.log("onLayout:"+getChildCount()+"&&"+i+"&&"+viewlist.size()); onLayout:7&&5&&7
            View child = viewlist.get(i);

            if (i == 0) {
                left += offsetX;
            } else {
                left += childWidth;
            }

            //中间相邻的childView添加间距，调整无法填满的情况
            if (i == getChildCount() / 2 || i == getChildCount() / 2 + 1) { //i=2 或 i=3
                left += retainWidth / 2;
            }

            int distance = Math.abs(centerX - (left + halfChildWidth));//两个圆边的距离

            int scaleDistance = centerX - halfChildWidth;//完成缩放的移动距离

            scaleDistance = scaleDistance / 2;

            //如果完成缩放的移动距离
            float scaleRatio = 1 + (scaleDistance - distance) * 1.0f / scaleDistance * SCALE_FACTOR;
            //+ 0.25 0.5    300

            if (maxRatio < scaleRatio) {
                maxRatio = scaleRatio;
            }

            //如果小于1就不缩放
            if (scaleRatio < 1) {
                scaleRatio = 1;
            }

            child.setScaleY(scaleRatio);
            child.setScaleX(scaleRatio);
            child.layout(left, top, left + childWidth, top + childHeight);
        }

    }


    //结束绘制
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            viewlist.add(child);
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = x;
                downY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (status != STATUS_MOVE) {
                    if (Math.abs(x - downX) > touchSlop) {
                        lastX = x;
                        lastY = y;
                        status = STATUS_MOVE;
                        return true;
                    } else {
                        status = STATUS_REST;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                status = STATUS_REST;
        }
        lastX = x;
        lastY = y;
        return super.onInterceptTouchEvent(ev);
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
                isAnimation = true;
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
                    moveBy(x - lastX);//手指移动在很小时间内的位移差
                }
                break;
            case MotionEvent.ACTION_UP:
                //停留位置在中间
                autoMove();
                status = STATUS_REST;
                isAnimation = false;
        }

        lastX = x;
        lastY = y;
        return true;
    }

    public void moveBy(int deltaX) {
        //通过改变offset来改变布局位置
        offsetX += deltaX;
        adjustViewOrder();
        requestLayout();
    }

    /**
     * 位置调整
     */
    public void autoMove() {
        int from = offsetX;
        int to = from;
        int retainX = offsetX % childWidth;
        Log.d(TAG, "retainX:" + retainX);
        if (Math.abs(retainX) > childWidth / 2) {
            Log.d(TAG, "retainX:>");
            if (retainX > 0) {
                to += childWidth - retainX;
            } else {
                to -= childWidth + retainX;
            }
        } else {
            Log.d(TAG, "retainX:<");
            to -= retainX;
        }
        //属性动画进行位移
        ValueAnimator valueAnimator = ValueAnimator.ofInt(from, to);
        valueAnimator.addUpdateListener(animation -> {
            offsetX = (int) animation.getAnimatedValue();
            if (animation.getAnimatedFraction() == 1) {
                adjustViewOrder();
                offsetX = 0;
            }
            requestLayout();
        });
        valueAnimator.setDuration(smoothDuration);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }

    private int preX = 0;
    private boolean isAnimation = false;

    public void setAnimation(boolean animation) {
        isAnimation = animation;
    }

    //属性动画模仿手部滑动
    private void moveToPosition(int position) {
        if (isAnimation) return;
        int between = position - (SCREEN_SHOW_COUNT - 2);
        int unit = Math.abs(between);
        if (unit == 0) return;
        isAnimation = true;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, -1 * between * childWidth);
        valueAnimator.addUpdateListener(animation -> {
            int nowX = (int) animation.getAnimatedValue();
            int delX = nowX - preX;
            moveBy(delX);
            preX = nowX;
            if (animation.getAnimatedFraction() == 1) {
                preX = 0;
            }
        });
        valueAnimator.setDuration(unit * smoothDuration);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimation = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isAnimation = true;
            }

            @Override
            public void onAnimationPause(Animator animation) {
                super.onAnimationPause(animation);
            }

            @Override
            public void onAnimationResume(Animator animation) {
                super.onAnimationResume(animation);
            }
        });
    }


    //无限循环
    private void adjustViewOrder() {
        View view = null;
        if (offsetX <= -1 * childWidth) { //左移
            view = viewlist.get(0);
            viewlist.remove(0);
            viewlist.add(view);
            offsetX += childWidth;
            selectedPos++;
        } else if (offsetX >= childWidth) { //当移动到达对应坐标距离 View交换 选中下标更新 offset位移差置为0
            view = viewlist.get(viewlist.size() - 1);
            viewlist.remove(viewlist.size() - 1);
            viewlist.add(0, view);
            offsetX -= childWidth;
            selectedPos--;
        }

        int dataCount = looperAdapterWrapper.getRealDataCount();
        if (selectedPos >= dataCount) {
            selectedPos = 0;
        } else if (selectedPos < 0) {
            selectedPos += dataCount;
        }
        //最左边显示的位置0~4
        firstVisiblePos = selectedPos - 2;
        if (firstVisiblePos < 0) {
            firstVisiblePos += dataCount;
        }

        //回调箭头选中位置
        if (onItemSelectedListener != null) {
            onItemSelectedListener.onSelected(selectedPos);
        }

        //根据正确的坐标显示相应的图片
        if (looperAdapterWrapper.getRealDataCount() >= SCREEN_SHOW_COUNT) {
            setView(looperAdapterWrapper.getDatas());
        }
    }


    //上一次点击时间
    static long lastclicktime = 0;


    private void setView(List<T> imgList) {
        for (int i = 0; i < viewlist.size(); i++) {
            int clickPos = i;
            View viewItem = viewlist.get(i);
            ImageView iv = viewItem.findViewById(R.id.iv);
            iv.setOnClickListener(view -> {
                if (System.currentTimeMillis() - lastclicktime > INTERVAL_TIME) {
                    moveToPosition(clickPos);
                    lastclicktime = System.currentTimeMillis();
                }
            });
            if (i == 0) { //屏幕左边的
                int pos = selectedPos - (SCREEN_SHOW_COUNT - 2);
                if (pos < 0) {
                    pos += looperAdapterWrapper.getRealDataCount();
                }
                setIV(iv, imgList.get(pos));
            } else if (i == viewlist.size() - 1) {//屏幕右边的
                int pos = selectedPos + (SCREEN_SHOW_COUNT - 2);
                if (pos >= looperAdapterWrapper.getRealDataCount()) {
                    pos -= looperAdapterWrapper.getRealDataCount();
                }
                setIV(iv, imgList.get(pos));
            }
        }
    }


    private void setIV(ImageView iv, T t) {
        if (picLoadStrategy != null) {
            picLoadStrategy.intoIv(iv, t);
        }
    }


    public void setAdapter(BaseAdapter adapter) {
        viewlist.clear();
        removeAllViews();
        selectedPos = 2;
        //循环添加7个view
        looperAdapterWrapper = new LooperAdapterWrapper(adapter);
        for (int i = 0; i < looperAdapterWrapper.getCount(); i++) {
            View itemView = looperAdapterWrapper.getItemView(i, firstVisiblePos, null, this);
            viewlist.add(itemView);
            addView(itemView);
        }
        offsetX = 0;
        requestLayout();
    }


    /**
     * 调整层级
     */
    private void reorderZ() {
        int centerIndex = getChildCount() / 2; //0 1 2
        for (int i = 0; i < centerIndex; i++) {
            viewlist.get(i).bringToFront();//改变view的z轴，使其处在他父view的顶端
        }
        for (int i = viewlist.size() - 1; i >= centerIndex; i--) {
            viewlist.get(i).bringToFront();
        }
    }
}
