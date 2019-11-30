package com.cvte.flexlayout;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class ArbitrarilyLayoutManager extends FlexLayout.AbstractLayoutManager {
    private static final int MAX_ITEM = 20;

    @Override
    protected FlexLayout.LayoutParams generateDefaultLayoutParams() {
        return new FlexLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onLayoutChildren() {
        removeAllView();
        int itemCount = getItemCount();
        if (itemCount < 1) {
            return;
        }
        //top-3View的position
        int bottomPosition;
        //边界处理
        if (itemCount < MAX_ITEM) {
            bottomPosition = 0;
        } else {
            bottomPosition = itemCount - MAX_ITEM;
        }

        //从可见的最底层View开始layout，依次层叠上去
        for (int position = bottomPosition; position < itemCount; position++) {
            View view = getViewForPosition(position);
            view.setTag(position);
            addView(view);
            measureChildWithMargins(view, 0, 0);
            int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
            int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);
            layoutDecoratedWithMargins(view, widthSpace - position * 50, 0,
                    widthSpace + getDecoratedMeasuredWidth(view) - position * 50,
                    getDecoratedMeasuredHeight(view));
        }
    }

}