package com.codesniper.looper.main;

import android.media.Image;
import android.widget.ImageView;

public interface PicLoadStrategy<T> {

    public void intoIv(ImageView iv,T t);

}
