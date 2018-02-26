package com.greenshop.snowson.myapplication.ui;

import android.app.Application;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;

/**
 * Created by snowson on 18-2-26.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //init 滑动返回lib
        BGASwipeBackManager.getInstance().init(this);
    }
}
