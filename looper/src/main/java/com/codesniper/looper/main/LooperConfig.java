package com.codesniper.looper.main;

import android.content.Context;

import java.util.HashMap;

public class LooperConfig {


    private int clickIntervalTime;//点击间隔

    private float scaleFactory;//缩放比例

    private int  movingDuration;//移动块距离的时间

    private PicLoadStrategy picLoadStrategy;//图片加载策略

    private float smoothFactory;//默认的滑动同步参数

    private boolean autoLoop;//自动轮播

    private int screenShowCount=5;//屏幕显示View数 目前只支持5个

    private int loopIntervalTime=5000;//自动轮播间隔时间


    public LooperConfig(Builder builder) {
        this.clickIntervalTime=builder.clickIntervalTime;
        this.scaleFactory=builder.scaleFactory;
        this.movingDuration=builder.movingDuration;
        this.picLoadStrategy=builder.picLoadStrategy;
        this.smoothFactory=builder.smoothFactory;
        this.autoLoop=builder.autoLoop;
        this.loopIntervalTime=builder.loopIntervalTime;

    }

    public static LooperConfig.Builder getBuilder() {
        return new LooperConfig.Builder();
    }

    public int getClickIntervalTime() {
        return clickIntervalTime;
    }

    public int getLoopIntervalTime() {
        return loopIntervalTime;
    }

    public float getScaleFactory() {
        return scaleFactory;
    }

    public int getMovingDuration() {
        return movingDuration;
    }

    public PicLoadStrategy getPicLoadStrategy() {
        return picLoadStrategy;
    }

    public float getSmoothFactory() {
        return smoothFactory;
    }

    public boolean isAutoLoop() {
        return autoLoop;
    }

    public int getScreenShowCount() {
        return screenShowCount;
    }

    public static class Builder {

        private int clickIntervalTime=1250;//点击间隔

        private float scaleFactory=0.5f;//缩放比例

        private int  movingDuration=300;//移动块距离的时间

        private PicLoadStrategy picLoadStrategy;//图片加载策略

        private float smoothFactory=0.5f;//默认的滑动同步参数

        private boolean autoLoop=false;//自动轮播

        private int loopIntervalTime=5000;//自动轮播间隔时间


        public Builder() {
        }

        public Builder setClickInterverTime(int time){
            clickIntervalTime=time;
            return this;
        }

        public Builder setLoopInterverTime(int time){
            loopIntervalTime=time;
            return this;
        }

        public Builder setScaleFactory(float ratio){
            scaleFactory=ratio;
            return this;
        }

        public Builder setSmoothFactory(float ratio){
            smoothFactory=ratio;
            return this;
        }

        public Builder setClickDuration(int time){
            movingDuration=time;
            return this;
        }

        public Builder setLoadWay(PicLoadStrategy way){
            picLoadStrategy=way;
            return this;
        }

        public Builder enableAutoLoop(boolean enable){
            autoLoop=enable;
            return this;
        }

        public LooperConfig build() {
            return new LooperConfig(this);
        }


    }









}
