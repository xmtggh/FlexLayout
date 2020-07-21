package com.example.imgtest;

import android.graphics.Color;
import android.os.Bundle;
import android.os.FileUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cvte.flexlayout.BaseItemView;
import com.cvte.flexlayout.DragTouchHelper;
import com.cvte.flexlayout.FlexLayout;
import com.cvte.flexlayout.TableLayoutManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    private FlexLayout mFlexLayout;
    private Button mAdd;
    private Button mRemove;
    private FlexLayout.Commander testCommander;
    private Button mBtnUpdate;
    private DragTouchHelper mDragTouchHelper;
    private Button mBtnReplace;
    private Button mBtnRemoveAll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mFlexLayout = findViewById(R.id.flex_layout);
        mAdd = findViewById(R.id.add);
        mRemove = findViewById(R.id.remove);
        mBtnReplace = findViewById(R.id.btn_replace);
        mBtnRemoveAll = findViewById(R.id.btn_remove_all);
        mFlexLayout.setLayoutManager(new TableLayoutManager(TableLayoutManager.HORIZONTAL));
//        mFlexLayout.setLayoutManager(new ArbitrarilyLayoutManager());
        testCommander = new FlexLayout.Commander();
        final ImageView imageView = new ImageView(this);
        imageView.setBackgroundColor(Color.YELLOW);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_island_svg));
        testCommander.addView(imageView);
        testCommander.addView(new TestFlexView(TestActivity.this));
        testCommander.addView(new TestFlexView(TestActivity.this));
        testCommander.addView(new TestFlexView(TestActivity.this));
        mFlexLayout.setCommander(testCommander);
        mDragTouchHelper = new DragTouchHelper();
        mDragTouchHelper.attachToFlexlayout(mFlexLayout);

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestFlexView testFlexView = new TestFlexView(TestActivity.this);
                testCommander.addView(testFlexView);
                testCommander.notifyUpdateAllView();
            }
        });
        mRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testCommander.removeView(imageView);
                testCommander.notifyUpdateAllView();

            }
        });
        mBtnUpdate = findViewById(R.id.btn_update);
        mBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testCommander.notifyItemChange(0);
                testCommander.notifyUpdateAllView();

            }
        });
        mBtnRemoveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("apk","apk version name" + BuildConfig.VERSION_NAME);
                String filePath = getDeletePath();
                List<String> filePaths = getFilesAllName(filePath);
                String apkPath = "";
                for (String path : filePaths) {
                    if (path.contains(BuildConfig.VERSION_NAME)) {
                        apkPath = path;
                    }
                }
//                testCommander.removeAllView();
            }
        });

        mBtnReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestFlexView testFlexView = new TestFlexView(TestActivity.this);
                testCommander.replaceView(testFlexView);
            }
        });
        TransitionManager.beginDelayedTransition(mFlexLayout, new AutoTransition());

    }

    /**
     * 获取所有文件名称
     * @param path
     * @return
     */
    public static List<String> getFilesAllName(String path) {
        File file=new File(path);
        File[] files=file.listFiles();
        if (files == null){Log.e("error","空目录");return null;}
        List<String> s = new ArrayList<>();
        for(int i =0;i<files.length;i++){
            s.add(files[i].getAbsolutePath());
        }
        return s;
    }

    private String getDeletePath() {
        File file = getExternalFilesDir("");
        if (file != null) {
            return file.getAbsolutePath() + File.separator;
        } else {
            return getCacheDir().getAbsolutePath() + File.separator;
        }
    }


    public BaseItemView getNewBaseView() {
        BaseItemView itemView = new TestFlexView(TestActivity.this);

        return itemView;
    }
}
