package com.greenshop.snowson.myapplication.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import com.greenshop.snowson.myapplication.R;

/**
 * Created by snowson on 18-2-26.
 */

public class SimpleMultiStateView extends MultiStateView {

    private int mResIdNone;
    private int mResIdLoading;
    private int mResIdFail;
    private int mResIdContent;
    private int mResIdEmpty;

    private static final String TAG = SimpleMultiStateView.class.getSimpleName();

    private static final int MIN_SHOW_TIME = 400; // ms
    private static final int MIN_DELAY = 600; // ms

    private int mTargetState = -1;
    private long mLoadingStartTime = -1;

    private final Runnable mLoadingHide = () -> {
        setStateView(mTargetState);
        mLoadingStartTime = -1;
        mTargetState = -1;
    };

    public SimpleMultiStateView(@NonNull Context context) {
        super(context);
    }

    public SimpleMultiStateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleMultiStateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SimpleMultiStateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MultiStateView);
        mResIdEmpty = ta.getResourceId(R.styleable.MultiStateView_emptyView, 0);
        mResIdContent = ta.getResourceId(R.styleable.MultiStateView_contentView, 0);
        mResIdFail = ta.getResourceId(R.styleable.MultiStateView_failView, 0);
        mResIdLoading = ta.getResourceId(R.styleable.MultiStateView_loadingView, 0);
        mResIdNone = ta.getResourceId(R.styleable.MultiStateView_noneView, 0);
        ta.recycle();

        if (mResIdEmpty != 0) {
            addStateView(STATE_EMPTY, mResIdEmpty);
        }
        if (mResIdContent != 0) {
            addStateView(STATE_CONTENT, mResIdContent);
        }
        if (mResIdFail != 0) {
            addStateView(STATE_FAIL, mResIdFail);
        }
        if (mResIdLoading != 0) {
            addStateView(STATE_LOADING, mResIdLoading);
        }
        if (mResIdNone != 0) {
            addStateView(STATE_NONET, mResIdNone);
        }
    }

    /**
     * loading显示的最短时间
     *
     * @param state
     */
    @Override
    public void setStateView(int state) {
        if (getCurrentState() == STATE_LOADING && state != STATE_LOADING) {
            long diff = System.currentTimeMillis() - mLoadingStartTime;
            if (diff < MIN_SHOW_TIME) {
                mTargetState = state;
                postDelayed(mLoadingHide, MIN_DELAY);
            } else {
                super.setStateView(state);
            }
        } else if (state == STATE_LOADING) {
            mLoadingStartTime = System.currentTimeMillis();
            super.setStateView(state);
        }
    }

    public void showEmptyView() {
        if (getCurrentState() != STATE_CONTENT) {
            postDelayed(() -> setStateView(STATE_EMPTY), 100);
        }
    }

    public void showContentView() {
        postDelayed(() -> setStateView(STATE_CONTENT), 100);
    }

    public void showFailView() {
        if (getCurrentState() != STATE_CONTENT) {
            postDelayed(() -> setStateView(STATE_FAIL), 100);
        }
    }

    public void showLoadingView() {
        setStateView(STATE_LOADING);
    }

    public void showNonetView() {
        if (getCurrentState() != STATE_CONTENT) {
            postDelayed(() -> setStateView(STATE_NONET), 100);
        }
    }

    public SimpleMultiStateView setNonetResource(int nonetResource) {
        this.mResIdNone = nonetResource;
        addStateView(STATE_NONET, nonetResource);
        return this;
    }

    public SimpleMultiStateView setLoadingResource(int loadingResource) {
        this.mResIdLoading = loadingResource;
        addStateView(STATE_LOADING, loadingResource);
        return this;
    }

    public SimpleMultiStateView setFailResource(int failResource) {
        this.mResIdFail = failResource;
        addStateView(STATE_FAIL, mResIdFail);
        return this;
    }

    public SimpleMultiStateView setContentResource(int contentResource) {
        this.mResIdContent = contentResource;
        addStateView(STATE_CONTENT, mResIdContent);
        return this;
    }

    public SimpleMultiStateView setEmptyResource(int emptyResource) {
        this.mResIdEmpty = emptyResource;
        addStateView(STATE_EMPTY, mResIdEmpty);
        return this;
    }

    public SimpleMultiStateView build() {
        showLoadingView();
        return this;
    }


}
