package com.example.imgtest;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Map;

public class MyRecyclerview extends RecyclerView {
    private static String TAG = "MyRecyclerview";
    private ViewDragHelper viewDragHelper;
    private boolean drager = true;
    private Context mContext;
    private float elevation = 0;
    /**
     * 记录位置信息
     */
    private SparseArray<Point> mPositionXY;
    /**
     * 记录z轴叠层信息
     */
    private SparseArray<Float> mElevation;

    public MyRecyclerview(@NonNull Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public MyRecyclerview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();

    }

    public MyRecyclerview(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView();
    }

    private void initView() {
        mPositionXY = new SparseArray<>();
        mElevation = new SparseArray<>();
        if (drager) {
            viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
                @Override
                public boolean tryCaptureView(View child, int pointerId) {
                    //让视图回到顶层
//                    child.bringToFront();
                    if (elevation > getChildCount()) {
                        elevation++;
                    } else {
                        elevation = getChildCount() + 1;

                    }
                    child.setElevation(elevation);
                    mElevation.put((int)child.getTag(),elevation);
                    return true;
                }

                @Override
                public int clampViewPositionHorizontal(View child, int left, int dx) {
                    /**
                     * 如果横向滑动超过右面边界的时候，控制子视图不能够越界
                     */
                    if (left + child.getMeasuredWidth() >= getMeasuredWidth()) {
                        return getMeasuredWidth() - child.getMeasuredWidth();
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
                    if (child.getMeasuredHeight() + top > getMeasuredHeight()) {
                        return getMeasuredHeight() - child.getMeasuredHeight();
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
                    Log.d("onViewPositionChanged","当前view Tag" + changedView.getTag()+String.format("当前坐标为（%s,%s,%s,%s）",left,top,dx,dy));
                    mPositionXY.put((int) changedView.getTag(),new Point(left,top,dx,dy));
                    //                    SharedPreferencesUtil.saveData(mContext, (String) changedView.getTag(), left + "#" + top);
                }

                //当手指释放的时候回调
                @Override
                public void onViewReleased(View releasedChild, float xvel, float yvel) {
                }

                //如果你拖动View添加了clickable = true 或者为 button 会出现拖不动的情况，原因是拖动的时候onInterceptTouchEvent方法，
                // 判断是否可以捕获，而在判断的过程中会去判断另外两个回调的方法getViewHorizontalDragRange和getViewVerticalDragRange，
                // 只有这两个方法返回大于0的值才能正常的捕获。如果未能正常捕获就会导致手势down后面的move以及up都没有进入到onTouchEvent
                @Override
                public int getViewHorizontalDragRange(View child) {
                    return getMeasuredWidth() - child.getMeasuredWidth();
                }

                @Override
                public int getViewVerticalDragRange(View child) {
                    return getMeasuredHeight() - child.getMeasuredHeight();
                }
            });
            viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        }


    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (drager) {
            return viewDragHelper.shouldInterceptTouchEvent(event);
        } else {
            return super.onInterceptTouchEvent(event);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (drager) {
            viewDragHelper.processTouchEvent(event);
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (drager) {
            if (viewDragHelper.continueSettling(true)) {
                invalidate();
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        final int count = getChildCount();
        Log.d(TAG,"requestLayout");
//        for (int i = 0; i < count; i++) {
//            View child = getChildAt(i);
//                Point mPointXY = mPositionXY.get((int)child.getTag());
//                if (mPointXY!=null){
//                    child.layout(mPointXY.left,mPointXY.top,mPointXY.left + child.getMeasuredWidth(),mPointXY.top+child.getMeasuredHeight());
//                }
//                Float ele = mElevation.get((int)child.getTag());
//                if (ele!=null){
//                    child.setElevation(ele);
//                }
//        }
    }



    /**
     * 设置视图是否可以拖拽
     *
     * @param flag
     */
    public void isDrager(boolean flag) {
        this.drager = flag;
    }

    class Point{
        private int left;
        private int top;
        private int right;
        private int bottom;

        public Point(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public int getLeft() {
            return left;
        }

        public void setLeft(int left) {
            this.left = left;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getRight() {
            return right;
        }

        public void setRight(int right) {
            this.right = right;
        }

        public int getBottom() {
            return bottom;
        }

        public void setBottom(int bottom) {
            this.bottom = bottom;
        }
    }

}
