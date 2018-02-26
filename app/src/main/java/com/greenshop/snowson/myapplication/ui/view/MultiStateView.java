package com.greenshop.snowson.myapplication.ui.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.greenshop.snowson.myapplication.R;

/**
 * Created by snowson on 18-2-26.
 */

public class MultiStateView extends FrameLayout {
    private static final String TAG = MultiStateView.class.getSimpleName();

    //视图状态
    public static final int STATE_CONTENT = 10001;
    public static final int STATE_LOADING = 10002;
    public static final int STATE_EMPTY = 10003;
    public static final int STATE_FAIL = 10004;
    public static final int STATE_NONET = 10005;

    private SparseArray<View> mStateViewArray = new SparseArray<>();
    private SparseIntArray mLayoutIdArray = new SparseIntArray();

    private View mContentView;
    private int mCurrentState = STATE_CONTENT;
    private onReLoadlistener mReloadListener;
    private OnInflateListener mInflateListener;

    public MultiStateView(@NonNull Context context) {
        super(context);
    }

    public MultiStateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiStateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MultiStateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
    }

    public void setStateView(int state) {
        if (mCurrentState == state) {
            return;
        }
        if (getCurrentStateView() == null) {
            return;
        }
        getCurrentStateView().setVisibility(View.GONE);
        View view = getViewByState(state);
        if (view == null) {
            int layoutId = mLayoutIdArray.get(state);
            if (layoutId != 0) {
                view = LayoutInflater.from(getContext()).inflate(layoutId, this, false);
                if (view != null) {
                    mStateViewArray.put(state, view);
                    addView(view);
                    if (state == STATE_FAIL) {
                        View btn_retry = view.findViewById(R.id.rl_retry);
                        btn_retry.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mReloadListener != null) {
                                    mReloadListener.onReload();
                                    setStateView(STATE_LOADING);
                                }
                            }
                        });
                    }
                    view.setVisibility(View.VISIBLE);
                    if (mInflateListener != null) {
                        mInflateListener.onInflate(state, view);
                    }
                }
            }
        } else {
            view.setVisibility(View.VISIBLE);
        }


    }

    public void addStateView(int state, int layoutId) {
        mLayoutIdArray.put(state, layoutId);
    }

    public View getViewByState(int state) {
        return mStateViewArray.get(state);
    }

    public View getCurrentStateView() {
        return mStateViewArray.get(mCurrentState);
    }

    public int getCurrentState() {
        return mCurrentState;
    }

    private void validContentView(View view) {
        if (isValidContentView(view)) {
            mContentView = view;
            mStateViewArray.put(STATE_CONTENT, mContentView);
        } else if (mCurrentState != STATE_CONTENT) {
            mContentView.setVisibility(View.GONE);
        }
    }

    private boolean isValidContentView(View view) {
        if (mContentView == null) {
            for (int i = 0; i < mStateViewArray.size(); i++) {
                if (mStateViewArray.indexOfValue(view) != -1) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 重新加载接口
     */
    public interface onReLoadlistener {
        void onReload();
    }

    public void setOnReLoadListener(onReLoadlistener listener) {
        this.mReloadListener = listener;
    }

    public interface OnInflateListener {
        void onInflate(int state, View view);
    }

    public void setOnInflateListener(OnInflateListener listener) {
        this.mInflateListener = listener;
    }
}
