package com.greenshop.snowson.myapplication.ui.inter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greenshop.snowson.myapplication.component.ApplicationComponent;

/**
 * author: snowson
 * created on: 18-2-25 下午10:54
 * description:
 */

public interface IBase {
    View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    View getView();

    int getContentLayout();

    void initInjector(ApplicationComponent appComponent);

    void bindView(View view, Bundle savedInstanceState);

    void initData();
}
