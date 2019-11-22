package com.example.imgtest.flex;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * 一款可对子view进行拖拽自定义位置，自动聚焦置顶，放大缩小的杂货箱
 *
 */
public class FlexView extends ViewGroup {

    public FlexView(Context context) {
        super(context);
    }

    public FlexView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }
}
