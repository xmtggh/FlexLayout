package com.cvte.flexlayout;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

class DefaultItemView extends BaseItemView {
    public DefaultItemView(Context context) {
        super(context);
    }

    public DefaultItemView(Context context, View view) {
        super(context, view);
    }

    @Override
    protected int setItemViewType() {
        return 0;
    }

    @Override
    protected int getItemLayout() {
        return 0;
    }

    @Override
    protected void onCreateView(View view, ViewGroup parent) {

    }

    @Override
    protected void onViewCreateActivity(View view, ViewGroup parent) {

    }
}
