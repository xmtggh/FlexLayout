package com.cvte.flexlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ggh
 */
public class FlexLayout extends ViewGroup {
    private static final String TAG = "FlexLayout";
    private OnViewTouchEventListener mOnViewTouchEventListener;
    private AbstractLayoutManager mLayoutManager;
    private Commander mCommander;

    public FlexLayout(Context context) {
        super(context);
    }

    public FlexLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlexLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);

    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);

    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p != null && p instanceof LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        if (mLayoutManager == null) {
            throw new IllegalStateException("FlexLayout has no LayoutManager");
        }
        return mLayoutManager.generateDefaultLayoutParams();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "进入ViewGroup测量过程");
        if (mLayoutManager == null) {
            defaultOnMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        mLayoutManager.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthModel = MeasureSpec.getMode(widthMeasureSpec);
        int heightModel = MeasureSpec.getMode(heightMeasureSpec);
        boolean measureSpecModelIsExactly = widthModel == MeasureSpec.EXACTLY && heightModel == MeasureSpec.EXACTLY;
        if (measureSpecModelIsExactly) {
            Log.d(TAG, "父布局已确认");

            return;
        } else {
            Log.d(TAG, "父布局未确认，执行子view的测量和布局");
            mLayoutManager.onLayoutChildren();
            mLayoutManager.setMeasureSpecs(widthMeasureSpec, heightMeasureSpec);
            mLayoutManager.setMeasuredDimensionFromChildren(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public Commander getCommander() {
        return mCommander;
    }

    /**
     * 默认的测量方式
     *
     * @param widthSpec
     * @param heightSpec
     */
    void defaultOnMeasure(int widthSpec, int heightSpec) {
        final int width = AbstractLayoutManager.chooseSize(widthSpec,
                getPaddingLeft() + getPaddingRight(),
                ViewCompat.getMinimumWidth(this));
        final int height = AbstractLayoutManager.chooseSize(heightSpec,
                getPaddingTop() + getPaddingBottom(),
                ViewCompat.getMinimumHeight(this));
        Log.d(TAG, "默认测量方式");
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        if (mLayoutManager != null) {
            Log.d(TAG, "执行Layout");
            mLayoutManager.setExactMeasureSpecsFrom(this);
            mLayoutManager.onLayoutChildren();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mOnViewTouchEventListener != null) {
            return mOnViewTouchEventListener.onInterceptTouchEvent(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mOnViewTouchEventListener != null) {
            return mOnViewTouchEventListener.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    public void setOnViewTouchEventListener(OnViewTouchEventListener onViewTouchEventListener) {
        this.mOnViewTouchEventListener = onViewTouchEventListener;
    }

    /**
     * 设置布局管理器
     *
     * @param layoutManager
     */
    public void setLayoutManager(AbstractLayoutManager layoutManager) {
        if (layoutManager == mLayoutManager) {
            return;
        }
        Log.d(TAG, "设置LayoutManager");
        //若原来是有layoutManager的，先清理现场
        if (mLayoutManager != null) {
            mLayoutManager.setFlexLayout(null);
            mLayoutManager = null;
        }
        mLayoutManager = layoutManager;

        if (layoutManager != null) {
            mLayoutManager.setFlexLayout(this);
        }
        requestLayout();
    }

    /**
     * 设置管理者
     *
     * @param commander
     */
    public void setCommander(Commander commander) {
        if (mCommander == commander) {
            return;
        }
        //原先是有管理者，先清场
        Log.d(TAG, "设置Commander");
        if (mCommander != null) {
            mCommander.clearAllView();
            mCommander = null;
        }
        mCommander = commander;

        if (commander != null) {
            commander.setFlexLayout(this);
        }
        requestLayout();
    }


    public abstract static class AbstractLayoutManager {
        FlexLayout mFlexLayout;
        private int mWidthMode, mHeightMode;
        private int mWidth, mHeight;

        /**
         * 设置FlexLayout
         *
         * @param flexLayout
         */
        void setFlexLayout(FlexLayout flexLayout) {
            if (flexLayout == null) {
                mWidth = 0;
                mHeight = 0;
                mFlexLayout = null;
            } else {
                mFlexLayout = flexLayout;
                mWidth = flexLayout.getWidth();
                mHeight = flexLayout.getHeight();
            }
            mWidthMode = MeasureSpec.EXACTLY;
            mHeightMode = MeasureSpec.EXACTLY;
        }

        void setMeasureSpecs(int wSpec, int hSpec) {
            mWidth = MeasureSpec.getSize(wSpec);
            mWidthMode = MeasureSpec.getMode(wSpec);
            if (mWidthMode == MeasureSpec.UNSPECIFIED) {
                mWidth = 0;
            }

            mHeight = MeasureSpec.getSize(hSpec);
            mHeightMode = MeasureSpec.getMode(hSpec);
            if (mHeightMode == MeasureSpec.UNSPECIFIED) {
                mHeight = 0;
            }
        }

        BaseItemView getViewForPosition(int position) {
            Commander commander = mFlexLayout.getCommander();
            BaseItemView itemView = commander.getViewForPosition(mFlexLayout, position);
            View internalView = itemView.getItemView();
            ViewGroup.LayoutParams params = internalView.getLayoutParams();
            LayoutParams lvParams;
            if (params == null) {
                lvParams = (LayoutParams) generateDefaultLayoutParams();
                internalView.setLayoutParams(lvParams);
            } else if (!mFlexLayout.checkLayoutParams(params)) {
                lvParams = (LayoutParams) mFlexLayout.generateLayoutParams(params);
                internalView.setLayoutParams(lvParams);
            }
            return itemView;
        }

        int getItemCount() {
            if (mFlexLayout == null) {
                return 0;
            }
            Commander commander = mFlexLayout.getCommander();
            if (commander == null) {
                return 0;
            }
            return commander.getItemCount();
        }


        void removeAllView() {
            mFlexLayout.removeAllViews();
        }

        void addView(BaseItemView view) {
            if (!view.isAddView()){
                mFlexLayout.addView(view.getItemView());
                view.setAddView(true);
            }
        }

        /**
         * 测量子view
         *
         * @param child
         * @param widthUsed
         * @param heightUsed
         */
        void measureChildWithMargins(@NonNull View child, int widthUsed, int heightUsed) {
            final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            final int widthSpec = getChildMeasureSpec(getWidth(), getWidthMode(),
                    getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin
                            + widthUsed, lp.width, false);
            final int heightSpec = getChildMeasureSpec(getHeight(), getHeightMode(),
                    getPaddingTop() + getPaddingBottom()
                            + lp.topMargin + lp.bottomMargin + heightUsed, lp.height,
                    false);
            child.measure(widthSpec, heightSpec);
        }

        /**
         * 获取子view的MeasureSpec
         *
         * @param parentSize
         * @param parentMode
         * @param padding
         * @param childDimension 子布局的宽度
         * @param canScroll      ViewGroup是否能滚动
         * @return
         */
        private static int getChildMeasureSpec(int parentSize, int parentMode, int padding,
                                               int childDimension, boolean canScroll) {
            int size = Math.max(0, parentSize - padding);
            int resultSize = 0;
            int resultMode = 0;
            if (childDimension >= 0) {
                resultSize = childDimension;
                resultMode = MeasureSpec.EXACTLY;
            } else if (childDimension == LayoutParams.MATCH_PARENT) {
                resultSize = size;
                resultMode = parentMode;
            } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                resultSize = size;
                if (parentMode == MeasureSpec.AT_MOST || parentMode == MeasureSpec.EXACTLY) {
                    resultMode = MeasureSpec.AT_MOST;
                } else {
                    resultMode = MeasureSpec.UNSPECIFIED;
                }

            }
            return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
        }

        /**
         * 在有layoutManager的情况下也要先测量flexLayout的布局
         *
         * @param widthSpec
         * @param heightSpec
         */
        private void onMeasure(int widthSpec, int heightSpec) {
            mFlexLayout.defaultOnMeasure(widthSpec, heightSpec);
        }


        int getDecoratedMeasuredWidth(@NonNull View child) {
            final LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            return child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
        }


        int getDecoratedMeasuredHeight(@NonNull View child) {
            final LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            return child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
        }

        void layoutDecoratedWithMargins(@NonNull View child, int left, int top, int right,
                                        int bottom) {
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            child.layout(left + lp.leftMargin,
                    top + lp.topMargin,
                    right - lp.rightMargin,
                    bottom - lp.bottomMargin);
        }

        void setExactMeasureSpecsFrom(FlexLayout flexLayout) {
            setMeasureSpecs(
                    MeasureSpec.makeMeasureSpec(flexLayout.getWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(flexLayout.getHeight(), MeasureSpec.EXACTLY)
            );
        }

        /**
         * 根据childView测绘出的结果设置当前viewGroup的大小
         *
         * @param widthSpec
         * @param heightSpec
         */
        void setMeasuredDimensionFromChildren(int widthSpec, int heightSpec) {
            final int count = mFlexLayout.getChildCount();
            if (count == 0) {
                mFlexLayout.defaultOnMeasure(widthSpec, heightSpec);
                return;
            }
            int realWidth = 0;
            int realHeight = 0;

            for (int i = 0; i < count; i++) {
                View child = mFlexLayout.getChildAt(i);
                realWidth = Math.max(realWidth, child.getRight());
                realHeight = Math.max(realHeight, child.getBottom());
            }
            mFlexLayout.setMeasuredDimension(MeasureSpec.makeMeasureSpec(realWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(realHeight, MeasureSpec.EXACTLY));

        }

        /**
         * 布局子view
         */
        protected abstract void onLayoutChildren();

        /**
         * 获取默认的layoutParams
         *
         * @return
         */
        protected abstract LayoutParams generateDefaultLayoutParams();

        /**
         * 选择一个当前合适的size
         *
         * @param spec    父布局建议view的spec，需要从中解析size
         * @param desired 当前view padding间距大小
         * @param min     ViewCompat的测量出的尺寸
         * @return
         */
        private static int chooseSize(int spec, int desired, int min) {
            final int mode = View.MeasureSpec.getMode(spec);
            final int size = View.MeasureSpec.getSize(spec);
            switch (mode) {
                case View.MeasureSpec.EXACTLY:
                    return size;
                case View.MeasureSpec.AT_MOST:
                    return Math.min(size, Math.max(desired, min));
                case View.MeasureSpec.UNSPECIFIED:
                default:
                    return Math.max(desired, min);
            }
        }

        int getWidthMode() {
            return mWidthMode;
        }

        int getHeightMode() {
            return mHeightMode;
        }

        int getWidth() {
            return mWidth;
        }

        int getHeight() {
            return mHeight;
        }

        int getPaddingLeft() {
            return mFlexLayout != null ? mFlexLayout.getPaddingLeft() : 0;
        }


        int getPaddingTop() {
            return mFlexLayout != null ? mFlexLayout.getPaddingTop() : 0;
        }

        int getPaddingRight() {
            return mFlexLayout != null ? mFlexLayout.getPaddingRight() : 0;
        }

        int getPaddingBottom() {
            return mFlexLayout != null ? mFlexLayout.getPaddingBottom() : 0;
        }

        public int getPaddingStart() {
            return mFlexLayout != null ? ViewCompat.getPaddingStart(mFlexLayout) : 0;
        }

        public int getPaddingEnd() {
            return mFlexLayout != null ? ViewCompat.getPaddingEnd(mFlexLayout) : 0;
        }


    }

    public static class LayoutParams extends android.view.ViewGroup.MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(LayoutParams source) {
            super((ViewGroup.LayoutParams) source);
        }


    }

    public static class Commander {
        private static final String TAG = "Commander";
        private List<BaseItemView> mViewLists;
        protected FlexLayout mFlexLayout;
        protected Context mContext;

        public Commander(Context context) {
            mContext = context;
            mViewLists = new ArrayList<>();
            Log.d(TAG, "初始化Commander");
        }

        public BaseItemView getViewForPosition(ViewGroup parent, int position) {
            if (position >= 0 && position < mViewLists.size()) {
                BaseItemView baseItemView = mViewLists.get(position);
                baseItemView.onCreateView(parent, position);
                return baseItemView;
            }
            return null;
        }


        public void setFlexLayout(FlexLayout flexLayout) {
            if (flexLayout == null) {
                throw new RuntimeException("flexLayout is null");
            }
            mFlexLayout = flexLayout;
            Log.d(TAG, "Commander 绑定FlexLayout");
        }

        private void addLists(List<BaseItemView> lists) {
            if (lists != null) {
                mViewLists.addAll(lists);
            }
            notifyUpdateAllView();
        }

        public void addView(BaseItemView baseItemView) {
            Log.d(TAG, "Commander addView");
            if (baseItemView != null) {
                mViewLists.add(baseItemView);
            }
        }

        public void removeView(BaseItemView baseItemView) {
            if (baseItemView != null) {
                mViewLists.remove(baseItemView);
                mFlexLayout.removeView(baseItemView.getItemView());
            }
        }

        public void removeView(int index) {
            if (index >= 0 && index < mViewLists.size()) {
                BaseItemView itemView = mViewLists.get(index);
                itemView.removeView();
                mFlexLayout.removeView(itemView.getItemView());
                mViewLists.remove(itemView);
            }
        }

        public void notifyUpdateAllView() {
            mFlexLayout.requestLayout();
        }

        public void notifyItemAdd(BaseItemView baseItemView) {
            if (baseItemView != null) {
                mViewLists.add(baseItemView);
            }
            mFlexLayout.requestLayout();
        }

        public void notifyItemChange(int pos) {
            if (mViewLists.get(pos) != null) {
                mViewLists.get(pos).notifyView();
            }
        }

        public void clearAllView() {
            for (BaseItemView itemView : mViewLists) {
                itemView.removeView();
                mFlexLayout.removeView(itemView.getItemView());
            }
            mViewLists.clear();
        }

        int getItemCount() {
            return mViewLists.size();
        }

    }
}
