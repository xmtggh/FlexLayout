package com.example.imgtest;

import android.graphics.Color;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class ListAdapter extends BaseQuickAdapter<String, BaseViewHolder>{
    String[] colors = {"#DDA0DD","#DB7093","#D8BFD8","#D2B48C","#D15FEE","#DDA0DD","#DB7093","#D8BFD8","#D2B48C","#D15FEE","#DDA0DD","#DB7093","#D8BFD8","#D2B48C","#D15FEE","#DDA0DD","#DB7093","#D8BFD8","#D2B48C","#D15FEE"};
    public ListAdapter(@Nullable List<String> data) {
        super(R.layout.item_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.text,item);
        helper.setBackgroundColor(R.id.text, Color.parseColor(colors[helper.getAdapterPosition()]));
    }



}
