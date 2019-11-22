package com.example.imgtest;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.imgtest.flex.MyRecyclerView;

public class MyListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        MyRecyclerView recyclerView = findViewById(R.id.myrecyclerview);
        recyclerView.setAdapter(new MyRecyclerView.Adapter() {
            @Override
            public View onCreateViewHodler(int position, View convertView, ViewGroup parent) {
                convertView=  getLayoutInflater().inflate( R.layout.item_list,parent,false);
                TextView textView= (TextView) convertView.findViewById(R.id.text);
                textView.setText("name "+position);
                return convertView;
            }

            @Override
            public View onBinderViewHodler(int position, View convertView, ViewGroup parent) {
                TextView textView= (TextView) convertView.findViewById(R.id.text);
                textView.setText("name "+position);
                return convertView;
            }
            @Override
            public int getItemViewType(int row) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public int getCount() {
                return 40;
            }

            @Override
            public int getHeight(int index) {
                return 150;
            }
        });
    }
}
