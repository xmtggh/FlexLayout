package com.cvte.flexlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 持有FlexLayout使用的基类Viewholder
 */
public abstract class BaseItemView {
    private static final int NO_POSITION = -1;
    private static final int INVALID_TYPE = -1;
    private View itemView;
    int mPosition = NO_POSITION;
    int mItemViewType = INVALID_TYPE;
    private Context mContext;
    private ViewGroup mParents;

    public BaseItemView(Context context) {
        this.mContext = context;
    }

    public void onCreateView(ViewGroup parent, int position) {
        mItemViewType = setItemViewType();
        mPosition = position;
        if (itemView == null) {
            itemView = LayoutInflater.from(mContext).inflate(getItemLayout(), parent, false);
            onCreateView(itemView, parent);
            mParents = parent;
        }
        onViewCreateActivity(itemView, parent);
    }

    public void notifyView() {
        onViewCreateActivity(itemView, mParents);
    }

    /**
     * 设置ItemType
     *
     * @return
     */
    protected abstract int setItemViewType();

    /**
     * 返回当前的itemview
     *
     * @return
     */
    final View getItemView() {
        return itemView;
    }

    /**
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
}