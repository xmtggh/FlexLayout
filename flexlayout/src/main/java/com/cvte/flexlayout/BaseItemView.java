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
    private AnimatorSet animatorSet;


    public BaseItemView(Context context) {
        this.mContext = context;
    }

    public void onCreateView(ViewGroup parent, int position) {
        mItemViewType = setItemViewType();
        mPosition = position;
        if (itemView == null) {
            itemView = LayoutInflater.from(mContext).inflate(getItemLayout(), parent, false);
            initAnimator();
            setonLayoutChangeListener(itemView);
            onCreateView(itemView, parent);
            mParents = parent;
        }
        onViewCreateActivity(itemView, parent);
    }

    private void initAnimator() {
        animatorSet = new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new LinearInterpolator());
    }

    private void setonLayoutChangeListener(View itemView) {
        itemView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View currentView, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (oldLeft == 0 && oldRight == 0 && oldTop == 0 && oldBottom == 0) {
                    Log.d(TAG, "new View");
                    return;
                }
//                float xNewLenght = right - left;
//                float yNewLenght = bottom - top;
//                float xOldLenght = oldRight - oldLeft;
//                float yOldLenght = oldBottom - oldTop;
//
//                ObjectAnimator xAnim;
//                ObjectAnimator yAnim;
//
//                if (xNewLenght > xOldLenght) {
//                    xAnim = ObjectAnimator.ofFloat(currentView, View.SCALE_X, 1f, xOldLenght / xNewLenght);
//                } else {
//                    xAnim = ObjectAnimator.ofFloat(currentView, View.SCALE_X, xOldLenght / xNewLenght, 1f);
//                }
//
//                if (yNewLenght > yOldLenght) {
//                    yAnim = ObjectAnimator.ofFloat(currentView, View.SCALE_Y, 1f, yOldLenght / yNewLenght);
//                } else {
//                    yAnim = ObjectAnimator.ofFloat(currentView, View.SCALE_Y, yOldLenght / yNewLenght, 1f);
//                }
//                animatorSet.play(xAnim)
//                        .with(yAnim);
//                animatorSet.start();

                Log.d(TAG, "onLayoutChange " + String.format("view change(%s,%s,%s,%s,%s,%s,%s,%s)", left, right, top, bottom, oldLeft, oldRight, oldTop, oldBottom));
            }
        });
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