package com.ggh.flexlayout;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.customview.widget.ViewDragHelper;

/**
 * 辅助FlexLayout拖动的帮助类
 */
public class DragTouchHelper implements OnViewTouchEventListener {
    private FlexLayout mFlexLayout;
    private ViewDragHelper viewDragHelper;
    private boolean mDragEnable = true;
    private float elevation = 0;


    public void attachToFlexlayout(FlexLayout flexLayout) {
        if (flexLayout == null || flexLayout == mFlexLayout) {
            return;
        }
        mFlexLayout = flexLayout;
        viewDragHelper = ViewDragHelper.create(mFlexLayout, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //让视图回到顶层
//                    child.bringToFront();
                if (elevation > mFlexLayout.getChildCount()) {
                    elevation++;
                } else {
                    elevation = mFlexLayout.getChildCount() + 1;

                }
                child.setElevation(elevation);
//                mElevation.put((int)child.getTag(),elevation);
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                /**
                 * 如果横向滑动超过右面边界的时候，控制子视图不能够越界
                 */
                if (left + child.getMeasuredWidth() >= mFlexLayout.getMeasuredWidth()) {
                    return mFlexLayout.getMeasuredWidth() - child.getMeasuredWidth();
                }
                /**
                 * 如果横向滑动超过左面边界的时候，控制子视图不能够越界
                 */
                if (left <= 0) {
                    return 0;
                }
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                /**
                 * 控制下边界，子视图不能够越界
                 */
                if (child.getMeasuredHeight() + top > mFlexLayout.getMeasuredHeight()) {
                    return mFlexLayout.getMeasuredHeight() - child.getMeasuredHeight();
                }
                /**
                 * 控制上边界，子视图不能够越界
                 */
                if (top <= 0) {
                    return 0;
                }
                return top;
            }


            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                Log.d("onViewPositionChanged", "当前view Tag" + changedView.getTag() + String.format("当前坐标为（%s,%s,%s,%s）", left, top, dx, dy));
//                mPositionXY.put((int) changedView.getTag(),new Point(left,top,dx,dy));
            }
            /**
             * 当手指释放的时候回调
             * @param releasedChild
             * @param xvel
             * @param yvel
             */
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
            }
            /**
             *  如果你拖动View添加了clickable = true 或者为 button 会出现拖不动的情况，原因是拖动的时候onInterceptTouchEvent方法，
             *  判断是否可以捕获，而在判断的过程中会去判断另外两个回调的方法getViewHorizontalDragRange和getViewVerticalDragRange，
             *  只有这两个方法返回大于0的值才能正常的捕获。如果未能正常捕获就会导致手势down后面的move以及up都没有进入到onTouchEvent
             * @param child
             * @return
             */
            @Override
            public int getViewHorizontalDragRange(View child) {
                return mFlexLayout.getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return mFlexLayout.getMeasuredHeight() - child.getMeasuredHeight();
            }

        });
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        if (mDragEnable) {
            mFlexLayout.setOnViewTouchEventListener(this);
        } else {
            mFlexLayout.setOnViewTouchEventListener(null);
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return viewDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    public void setDragEnable(boolean dragEnable) {
        this.mDragEnable = dragEnable;
        if (dragEnable) {
            mFlexLayout.setOnViewTouchEventListener(this);
        } else {
            mFlexLayout.setOnViewTouchEventListener(null);
        }
    }
}
