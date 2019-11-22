package com.example.imgtest.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;


/**
 * 传屏码的Textview
 * Created by ggh
 */

public class VerificationCodeTextView extends AppCompatTextView {

    /**
     * 外部框的颜色
     */
    private static String COLOR_FRME = "#3384F5";
    /**
     * 中线的颜色
     */
    private static String COLOR_CENTER_LINE = "#CAE5FC";
    /**
     * 文字的颜色
     */
    private static String COLOR_TEXT = "#5D6168";

    private int mEachRectLength = 0;//每个矩形的边长
    private Paint mRectFramePaint;
    private Paint mCenterLinePaint;
    private int widthResult = 0, heightResult = 0;
    private TextPaint mTextPaint;

    private int mFrameLineWidth = dp2px(2)-1;//设置奇数，不然会有意想不到的事情发生，d55旋转屏出现黑线
    private int mFrameRadius = dp2px(1);

    public VerificationCodeTextView(Context context) {
        this(context, null);
    }

    public VerificationCodeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerificationCodeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));//防止出现下划线
        initPaint();
        setFocusableInTouchMode(true);
    }

    /**
     * 初始化paint
     */
    private void initPaint() {
        mRectFramePaint = new Paint();
//        mRectFramePaint.setTextAlign(Paint.Align.CENTER);
        mRectFramePaint.setStrokeWidth(mFrameLineWidth);
        mRectFramePaint.setColor(Color.parseColor(COLOR_FRME));
        mRectFramePaint.setStyle(Paint.Style.STROKE);
        mRectFramePaint.setAntiAlias(true);


        mCenterLinePaint = new Paint();
        mCenterLinePaint.setColor(Color.parseColor(COLOR_CENTER_LINE));
        mCenterLinePaint.setStrokeWidth(1);
        mTextPaint = getPaint();
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        Typeface font = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
        mTextPaint.setTypeface(font);
        mTextPaint.setColor(Color.parseColor(COLOR_TEXT));
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                requestLayout();
            }
        });
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //最终的宽度
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //如果写死就拿死数据，要么那屏幕宽
        if (widthMode == MeasureSpec.EXACTLY) {
            widthResult = widthSize;
        } else {
            widthResult = getScreenWidth(getContext());
        }
        Integer inputStr = getText().toString().length();
        if (inputStr != null && inputStr > 0) {
            //每个矩形形的宽度
            mEachRectLength = widthResult / getText().toString().length();
        } else {
            mEachRectLength = widthResult / 6;
        }
        //最终的高度
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            heightResult = heightSize;
        } else {
            heightResult = (int) (widthResult * 0.2);
        }
        setMeasuredDimension(widthResult, heightResult);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int width = mEachRectLength - getPaddingLeft() - getPaddingRight();
        int height = heightResult - getPaddingTop() - getPaddingBottom();
//        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
        //绘制文字
        String value = getText().toString();
        for (int i = 0; i < value.length(); i++) {
            int start = width * i;
            float x = start + width / 2;
//            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
//            float baseline = (height - fontMetrics.bottom + fontMetrics.top) / 2
//                    - fontMetrics.top;
            //设置基线
            float textBaseY = height / 2 + (Math.abs(mTextPaint.ascent()) - mTextPaint.descent()) / 2;
            canvas.drawText(String.valueOf(value.charAt(i)), x, textBaseY, mTextPaint);
//            canvas.drawText(String.valueOf(value.charAt(i)), x, baseline, mTextPaint);
        }
        //绘制中间线，第一条不用绘制
        for (int i = 1; i < value.length(); i++) {
            //y轴开始的高度
            float startY = (float) (height * 0.37);
            //y轴结束的高度
            float endY = (float) (height * 0.61);
            int lineX = width * i;
            canvas.drawLine(lineX, startY, lineX, endY, mCenterLinePaint);
        }
        //此方法left< top < bottom 这是必须要遵守的，未知原因
        RectF mRectf = new RectF(mFrameRadius / 2, mFrameRadius / 2, widthResult - mFrameRadius / 2, heightResult - mFrameRadius / 2);
        canvas.drawRoundRect(mRectf, mFrameRadius, mFrameRadius, mRectFramePaint);


    }


    /**
     * dp转px
     */
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    /**
     * 获取手机屏幕的宽度
     */
    static int getScreenWidth(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }


}
