package com.ggh.flexlayout;

import android.view.MotionEvent;

public interface OnViewTouchEventListener {
    boolean onInterceptTouchEvent(MotionEvent event);

    boolean onTouchEvent(MotionEvent event);
}
