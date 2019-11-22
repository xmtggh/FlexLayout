package com.example.imgtest;

import android.content.Context;

import com.cvte.flexlayout.BaseItemView;
import com.cvte.flexlayout.FlexLayout;

import java.util.List;

public class TestCommander extends FlexLayout.Commander {


    public TestCommander(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        addView(new TestFlexView(mContext));
        addView(new TestFlexView(mContext));
        addView(new TestFlexView(mContext));
        addView(new TestFlexView(mContext));

    }
}
