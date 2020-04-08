package com.example.imgtest;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cvte.flexlayout.BaseItemView;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestFlexView extends BaseItemView {
    private TextView mTvText;
    int count = 0;
    int[] color = new int[]{Color.YELLOW,Color.BLUE,Color.BLACK,Color.GREEN};

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
        mTvText = view.findViewById(R.id.text);
        mTvText.setBackgroundColor(color[1]);
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mTvText.setText("我是第" + count++ + "个数据");
                    }
                });
            }
        },1000*getPosition(),100, TimeUnit.MILLISECONDS);
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
}
