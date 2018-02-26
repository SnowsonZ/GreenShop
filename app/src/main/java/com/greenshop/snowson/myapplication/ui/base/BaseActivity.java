package com.greenshop.snowson.myapplication.ui.base;

import android.app.Dialog;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greenshop.snowson.myapplication.ApplicationComponent;
import com.greenshop.snowson.myapplication.DialogHelper;
import com.greenshop.snowson.myapplication.R;
import com.greenshop.snowson.myapplication.ui.inter.IBase;
import com.greenshop.snowson.myapplication.ui.view.MultiStateView;
import com.greenshop.snowson.myapplication.ui.view.SimpleMultiStateView;
import com.trello.rxlifecycle2.LifecycleTransformer;

import javax.inject.Inject;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

public abstract class BaseActivity<T extends BaseContract.BasePresenter> extends SupportActivity implements
        IBase, BaseContract.BaseView, BGASwipeBackHelper.Delegate {

    protected View mRootView;
    protected Dialog mLoadingDialog = null;
    Unbinder unbinder;

    //TODO 添加stateView
    @Nullable
    SimpleMultiStateView mStateView;

    @Nullable
    @Inject
    protected T mPresenter;
    protected BGASwipeBackHelper mSwipeBackHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        mRootView = createView(null, null, savedInstanceState);
        setContentView(mRootView);
        //TODO inject APPComponent
        attachView();
        bindView(mRootView, savedInstanceState);
        initStateView();
        initData();
        mLoadingDialog = DialogHelper.getLoadingDialog(this);
    }

    private void initStateView() {
        if (mStateView != null) {
            mStateView.setEmptyResource(R.layout.view_empty)
                    .setFailResource(R.layout.view_retry)
                    .setLoadingResource(R.layout.view_loading)
                    .setNonetResource(R.layout.view_nonet)
                    .build()
                    .setOnReLoadListener(this::onRetry);
        }
    }

    @SuppressWarnings("unchecked")
    private void attachView() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackManager.getInstance().init(this) 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。
        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(false);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
    }

    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(getContentLayout(), container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void showLoading() {
        if (mStateView != null) {
            mStateView.showLoadingView();
        }
    }

    @Override
    public void showSuccess() {
        if (mStateView != null) {
            mStateView.showContentView();
        }
    }

    @Override
    public void showFaild() {
        if (mStateView != null) {
            mStateView.showFailView();
        }
    }

    @Override
    public void showNoNet() {
        if (mStateView != null) {
            mStateView.showNonetView();
        }
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLife();
    }
}
