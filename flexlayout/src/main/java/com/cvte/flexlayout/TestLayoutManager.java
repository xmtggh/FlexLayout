package com.cvte.flexlayout;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class TestLayoutManager extends FlexLayout.AbstractLayoutManager {
    private static final String TAG = "TestLayoutManager";

    @Override
    FlexLayout.LayoutParams generateDefaultLayoutParams() {
        return new FlexLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    void onChildLayout() {
        Log.d(TAG,"子view开始布局");
        clearAllView();
        int childSize = getItemCount();
        Log.d(TAG,"子view数量"+ childSize);
        int heightAdds = 0;
        if (childSize > 0) {
            for (int i = 0; i < childSize; i++) {
                View view = getViewForPosition(i);
                measureChildWithMargins(view, 0, 0);
                int widthSpace = getDecoratedMeasuredWidth(view);
                int heightSpace = getDecoratedMeasuredHeight(view);
                Log.d(TAG,"子view测量大小"+ widthSpace+"*"+heightSpace);
                layoutDecoratedWithMargins(view,0, heightAdds, widthSpace, heightAdds + heightSpace);
                heightAdds = heightSpace + heightAdds;
                addView(view,i);
            }
        }
    }


}
