package com.ggh.flexlayout;

import android.transition.AutoTransition;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class TestLayoutManager extends FlexLayout.AbstractLayoutManager {
    private static final String TAG = "TestLayoutManager";

    @Override
    protected FlexLayout.LayoutParams generateDefaultLayoutParams() {
        return new FlexLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected TransitionSet getLayoutTransition() {
        return new AutoTransition();
    }

    @Override
    protected void onLayoutChildren() {
        removeAllView();
        int childSize = getItemCount();
        int heightAdds = 0;
        if (childSize > 0) {
            for (int i = 0; i < childSize; i++) {
                BaseItemView itemView = getViewForPosition(i);
                View view = itemView.getItemView();
                measureChildWithMargins(view, 0, 0);
                int widthSpace = getDecoratedMeasuredWidth(view);
                int heightSpace = getDecoratedMeasuredHeight(view);
                layoutDecoratedWithMargins(itemView,0, heightAdds, widthSpace, heightAdds + heightSpace);
                heightAdds = heightSpace + heightAdds;
                addView(itemView);
            }
        }
    }


}
