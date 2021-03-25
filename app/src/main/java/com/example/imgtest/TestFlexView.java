package com.example.imgtest;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ggh.flexlayout.BaseItemView;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestFlexView extends BaseItemView {
    private TextView mTvText;
    private Random random;
    int count = 0;
    int[] color = new int[]{Color.YELLOW, Color.BLUE, Color.BLACK, Color.GREEN};
    private boolean isH = false;

    public TestFlexView(Context context) {
        super(context);
    }

    @Override
    protected int setItemViewType() {
        return 1;
    }

    @Override
    protected int getItemLayout() {
        return R.layout.item_list;
    }

    @Override
    protected void onCreateView(View view, ViewGroup parent) {
        random = new Random();
        mTvText = view.findViewById(R.id.text);
        if (isH) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(1080, 192);
            mTvText.setBackgroundColor(color[1]);
            mTvText.setLayoutParams(layoutParams);
            setLandscape(false);
        } else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(192, 108);
            mTvText.setBackgroundColor(color[2]);
            mTvText.setLayoutParams(layoutParams);
            setLandscape(true);

        }
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
//                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTvText.getLayoutParams();
//                        params.width = 10 + count++;
//                        params.height = 20 + count++;
                        mTvText.setText("count ++");
                    }
                });
            }
        }, 1000 * getPosition(), 100, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void onViewCreateActivity(View view, ViewGroup parent) {
        mTvText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "点击了" + getPosition() + "个数据", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setW() {
        isH = false;

    }

    public void setH() {
        isH = true;
    }

    @Override
    public void setViewSize(int width, int height) {
        super.setViewSize(width, height);
        Log.d("TestFlexView", "width == " + width + "height == " + height);
    }
}
