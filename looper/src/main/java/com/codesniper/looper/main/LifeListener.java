package com.codesniper.looper.main;

import android.os.Bundle;

public interface LifeListener {



    void onCreate(Bundle bundle);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();







}
