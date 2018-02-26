package com.greenshop.snowson.myapplication.ui.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.greenshop.snowson.myapplication.ui.inter.IBase;
import com.greenshop.snowson.myapplication.ui.view.SimpleMultiStateView;
import com.trello.rxlifecycle2.LifecycleTransformer;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author: snowson
 * created on: 18-2-25 下午9:00
 * description:
 */

public abstract class BaseFragment<T extends BaseContract.BasePresenter> extends SupportFragment
        implements IBase, BaseContract.BaseView {

    @Nullable
    //TODO bindview add
    private SimpleMultiStateView mStateView;
    private View mRootView;
    private Unbinder unbinder;
    @Nullable
    @Inject
    protected T mPresenter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
        } else {
            mRootView = createView(inflater, container, savedInstanceState);
        }
        return mRootView;
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getContentLayout(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @SuppressWarnings("unchecked")
    private void attachView() {
        if (mRootView != null && mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void showLoading() {
        if(mStateView != null) {
            mStateView.showLoadingView();
        }
    }

    @Override
    public void showSuccess() {
        if(mStateView != null) {
            mStateView.showContentView();
        }
    }

    @Override
    public void showFaild() {
        if(mStateView != null) {
            mStateView.showFailView();
        }
    }

    @Override
    public void showNoNet() {
        if(mStateView != null) {
            mStateView.showNonetView();
        }
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }
}
