package com.ggh.flexlayout;

import android.transition.AutoTransition;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author ggh
 */
public class TableLayoutManager extends FlexLayout.AbstractLayoutManager {
    private static final String TAG = "TableLayoutManager";
    /**
     * 最大item的数目
     */
    private static final int MAX_ITEM = 40;
    /**
     * 水平方向优先扩展
     */
    public static final int HORIZONTAL = 0;
    /**
     * 垂直方向优先扩展
     */
    public static final int VERTICAL = 1;
    private int mCurrentModel = HORIZONTAL;
    private int mHorizontalNum = 0;
    private int mVerticalNum = 0;
    /**
     * 最多的数目,默认是4个
     */
    private int mMaxView = 4;

    public TableLayoutManager(int direction, int maxView) {
        initLayout(direction, maxView);
    }

    public TableLayoutManager(int direction) {
        initLayout(direction, MAX_ITEM);

    }

    private void initLayout(int direction, int maxView) {
        if (direction != 0 && direction != 1) {
            throw new RuntimeException("init error");
        }
        this.mCurrentModel = direction;
        this.mMaxView = maxView;
    }


    @Override
    protected void onLayoutChildren() {
        mHorizontalNum = 0;
        mVerticalNum = 0;
        int childSize = getItemCount();
        int tableSize =Math.min(getItemCount(),mMaxView);
        calculationAllItemNum(tableSize);
        if (childSize > 0) {
            int widthSpace = getWidth() / mHorizontalNum;
            int heightSpace = getHeight() / mVerticalNum;
            for (int i = 0; i < childSize; i++) {
                //第几行
                int row = (int) Math.floor(i / mHorizontalNum);
                //第几列
                int column = i - row * mHorizontalNum;
                BaseItemView itemView = getViewForPosition(i);
                View view = itemView.getItemView();
                //获取宽度其余空间，在子view测量时 会用parents减去此值获取当前view最大的值
                int widthUsed = getWidth() - widthSpace;
                int heightUsed = getHeight() - heightSpace;
                if (i < mMaxView) {
                    measureChildWithMargins(view, widthUsed, heightUsed);
                    int widthMesured = getDecoratedMeasuredWidth(view);
                    int heightMesured = getDecoratedMeasuredHeight(view);
                    layoutDecoratedWithMargins(itemView, column * widthMesured, row * heightMesured, widthMesured + column * widthMesured, row * heightMesured + heightMesured);
                    addView(itemView);
                }
            }
        }
    }

    /**
     * 计算当前界面展示最大item数目
     *
     * @param num
     */

    private void calculationAllItemNum(int num) {
        if (num < 1) {
            return;
        }
        if (num == 1) {
            mHorizontalNum = 1;
            mVerticalNum = 1;
            return;
        }
        int maxLine = (int) Math.ceil(Math.sqrt(num));
        int minLine = maxLine - 1;
        if (num > minLine * maxLine && num <= maxLine * maxLine) {
            mVerticalNum = mHorizontalNum = maxLine;
        } else if (num > minLine * minLine && num <= maxLine * minLine) {
            if (mCurrentModel == HORIZONTAL) {
                mHorizontalNum = maxLine;
                mVerticalNum = minLine;
            } else {
                mHorizontalNum = minLine;
                mVerticalNum = maxLine;
            }
        } else {
            mVerticalNum = mHorizontalNum = minLine;
        }


    }

    @Override
    protected FlexLayout.LayoutParams generateDefaultLayoutParams() {
        return new FlexLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected TransitionSet getLayoutTransition() {
        return new AutoTransition();
    }
}
