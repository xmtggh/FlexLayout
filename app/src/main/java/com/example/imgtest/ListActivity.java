package com.example.imgtest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mRecyclerview = findViewById(R.id.recyclerview);
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            datas.add("我是第" + i + "条数据");
        }
        final ListAdapter adapter = new ListAdapter(datas);
        mRecyclerview.setLayoutManager(new OverLayCardLayoutManager());
//        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setAdapter(adapter);
//        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Toast.makeText(ListActivity.this, "当前view的tag ——"+view.getTag(), Toast.LENGTH_SHORT).show();
//            }
//        });

//        ItemTouchHelper helper = new ItemTouchHelper(new DragHelper(adapter));
//        helper.attachToRecyclerView(mRecyclerview);
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.getData().add(new String("我是新添加的"+(adapter.getData().size())));
                adapter.notifyDataSetChanged();
//                adapter.notifyItemInserted(adapter.getData().size()+1);
            }
        });
        findViewById(R.id.btn_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int removeItem = adapter.getData().size()-1;
                adapter.getData().remove(removeItem);
                adapter.notifyDataSetChanged();
//                adapter.notifyItemRemoved(removeItem);
            }
        });

        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.notifyDataSetChanged();
            }
        });
    }
}
