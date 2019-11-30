package com.example.imgtest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cvte.flexlayout.ArbitrarilyLayoutManager;
import com.cvte.flexlayout.DragTouchHelper;
import com.cvte.flexlayout.FlexLayout;
import com.cvte.flexlayout.TableLayoutManager;

public class TestActivity extends AppCompatActivity {
    private FlexLayout mFlexLayout;
    private Button mAdd;
    private Button mRemove;
    private TestCommander testCommander;
    private Button mBtnUpdate;
    private DragTouchHelper mDragTouchHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mFlexLayout = findViewById(R.id.flex_layout);
        mFlexLayout.setLayoutManager(new ArbitrarilyLayoutManager());
        testCommander = new TestCommander(this);
        mFlexLayout.setCommander(testCommander);
        mDragTouchHelper = new DragTouchHelper();
        mDragTouchHelper.attachToFlexlayout(mFlexLayout);
        mAdd = findViewById(R.id.add);
        mRemove = findViewById(R.id.remove);
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestFlexView testFlexView = new TestFlexView(TestActivity.this);
                testCommander.addView(testFlexView);
                testCommander.notifyChange();
            }
        });
        mRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testCommander.removeView(0);
                testCommander.notifyChange();
            }
        });
        mBtnUpdate = findViewById(R.id.btn_update);
        mBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testCommander.notifyItemChange(0);
            }
        });
    }
}
