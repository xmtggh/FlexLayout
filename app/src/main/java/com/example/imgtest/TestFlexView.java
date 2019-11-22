package com.example.imgtest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cvte.flexlayout.BaseItemView;

public class TestFlexView extends BaseItemView {
    private TextView mTvText;

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
        mTvText.setText("我是第" + getPosition() + "个数据");

        LogUtils.d("我初始化了");

    }

    @Override
    protected void onViewCreateActivity(View view, ViewGroup parent) {
        LogUtils.d("我被刷新了" + getPosition());
        mTvText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "点击了" + getPosition() + "个数据", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
