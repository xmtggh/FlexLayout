package com.example.imgtest;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cvte.flexlayout.ArbitrarilyLayoutManager;
import com.cvte.flexlayout.BaseItemView;
import com.cvte.flexlayout.DragTouchHelper;
import com.cvte.flexlayout.FlexLayout;
import com.cvte.flexlayout.TableLayoutManager;

public class TestActivity extends AppCompatActivity {
    private FlexLayout mFlexLayout;
    private Button mAdd;
    private Button mRemove;
    private FlexLayout.Commander testCommander;
    private Button mBtnUpdate;
    private DragTouchHelper mDragTouchHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mFlexLayout = findViewById(R.id.flex_layout);
        mAdd = findViewById(R.id.add);
        mRemove = findViewById(R.id.remove);

        mFlexLayout.setLayoutManager(new TableLayoutManager(TableLayoutManager.VERTICAL));
//        mFlexLayout.setLayoutManager(new ArbitrarilyLayoutManager());
        testCommander = new FlexLayout.Commander(this);
        testCommander.addView(new TestFlexView(TestActivity.this));
        testCommander.addView(new TestFlexView(TestActivity.this));
        testCommander.addView(new TestFlexView(TestActivity.this));
        testCommander.addView(new TestFlexView(TestActivity.this));
        mFlexLayout.setCommander(testCommander);
//        mDragTouchHelper = new DragTouchHelper();
//        mDragTouchHelper.attachToFlexlayout(mFlexLayout);

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestFlexView testFlexView = new TestFlexView(TestActivity.this);
                testCommander.addView(testFlexView);
            }
        });
        mRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testCommander.removeView(testCommander.getViewSize() - 1);
            }
        });
        mBtnUpdate = findViewById(R.id.btn_update);
        mBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testCommander.notifyItemChange(0);
            }
        });
/
        TransitionManager.beginDelayedTransition(mFlexLayout, new AutoTransition());
    }


    public BaseItemView getNewBaseView() {
        BaseItemView itemView = new TestFlexView(TestActivity.this);

        return itemView;
    }
}
