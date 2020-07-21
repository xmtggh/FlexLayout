package com.cvte.flexlayout;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;

/**
 * 持有FlexLayout使用的基类Viewholder
 */
public abstract class BaseItemView {
    private static final String TAG = "BaseItemView";
    private static final int NO_POSITION = -1;
    private static final int INVALID_TYPE = -1;
    private View itemView;
    private int mPosition = NO_POSITION;
    private int mItemViewType = INVALID_TYPE;
    private Context mContext;
    private ViewGroup mParents;
    private boolean isAddView = false;
    private boolean isDelete = false;
    private boolean initFlag = false;
    private Object tag;


    public BaseItemView(Context context) {
        this.mContext = context;
    }

    public BaseItemView(Context context, View view) {
        this.mContext = context;
        this.itemView = view;
    }

    public void onCreateView(ViewGroup parent, int position) {
        mItemViewType = setItemViewType();
        mPosition = position;
        if (!initFlag) {
            if (itemView == null) {
                itemView = LayoutInflater.from(mContext).inflate(getItemLayout(), parent, false);
                onCreateView(itemView, parent);
                mParents = parent;
            } else {
                onCreateView(itemView, parent);
                mParents = parent;
            }
            initFlag = true;
        }
        onViewCreateActivity(itemView, parent);
    }

    public void notifyView() {
        onViewCreateActivity(itemView, mParents);
    }

    /**
     * 返回当前的itemview
     *
     * @return
     */
    final View getItemView() {
        return itemView;
    }

    /**
     * 留坑做缓存使用
     * @return The view type of this BaseItemView.
     */
    final int getItemViewType() {
        return mItemViewType;
    }

    public Context getContext() {
        return mContext;
    }

    public int getPosition() {
        return mPosition;
    }

    public boolean isAddView() {
        return isAddView;
    }

    public void setAddView(boolean addView) {
        isAddView = addView;
    }

    public void removeView() {
        isDelete = true;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    /**
     * 获取layout
     *
     * @return
     */
    protected abstract int getItemLayout();

    /**
     * 找取view的id
     *
     * @param view
     */
    protected abstract void onCreateView(View view, ViewGroup parent);

    /**
     * 主逻辑
     */
    protected abstract void onViewCreateActivity(View view, ViewGroup parent);
    /**
     * 设置ItemType
     *
     * @return
     */
    protected abstract int setItemViewType();

}