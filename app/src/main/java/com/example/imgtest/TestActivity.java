package com.example.imgtest;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ggh.flexlayout.BaseItemView;
import com.ggh.flexlayout.DragTouchHelper;
import com.ggh.flexlayout.FlexLayout;
import com.ggh.flexlayout.TableLayoutManager;

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
    private boolean tag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mFlexLayout = findViewById(R.id.flex_layout);
        mAdd = findViewById(R.id.add);
        mRemove = findViewById(R.id.remove);
        mBtnReplace = findViewById(R.id.btn_replace);
        mBtnRemoveAll = findViewById(R.id.btn_remove_all);
//        mFlexLayout.setLayoutManager(new TableLayoutManager(TableLayoutManager.HORIZONTAL, 9));
        mFlexLayout.setLayoutManager(new MultiScreenViewLayoutManager(MultiScreenViewLayoutManager.HORIZONTAL,9));
//        mFlexLayout.setLayoutManager(new ArbitrarilyLayoutManager());
        testCommander = new FlexLayout.Commander();
        testCommander.addView(getW());
        mFlexLayout.setCommander(testCommander);
//        setAnim();
//        mDragTouchHelper = new DragTouchHelper();
//        mDragTouchHelper.attachToFlexlayout(mFlexLayout);

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (tag){
//                    tag = false;
//                    testCommander.addView(getH());
//                }else {
//                    tag = true;
                testCommander.addView(getW());
//                }
                testCommander.notifyUpdateAllView();
            }
        });
        mRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testCommander.removeView(0);

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
                Log.d("apk", "apk version name" + BuildConfig.VERSION_NAME);
                String filePath = getDeletePath();
                List<String> filePaths = getFilesAllName(filePath);
                String apkPath = "";
                for (String path : filePaths) {
                    if (path.contains(BuildConfig.VERSION_NAME)) {
                        apkPath = path;
                    }
                }
            }
        });

        mBtnReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestFlexView testFlexView = new TestFlexView(TestActivity.this);
                testCommander.replaceView(testFlexView);
            }
        });

    }

    public void setAnim(){
        LayoutTransition mLayoutTransition = new LayoutTransition();

        //设置每个动画持续的时间
        mLayoutTransition.setStagger(LayoutTransition.CHANGE_APPEARING,50);
        mLayoutTransition.setStagger(LayoutTransition.APPEARING,50);

        PropertyValuesHolder appearingScaleX = PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1.0f);
        PropertyValuesHolder appearingScaleY = PropertyValuesHolder.ofFloat("scaleY", 0.5f, 1.0f);
        PropertyValuesHolder appearingAlpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
        ObjectAnimator mAnimatorAppearing = ObjectAnimator.ofPropertyValuesHolder(this, appearingAlpha, appearingScaleX, appearingScaleY);
        //为LayoutTransition设置动画及动画类型
        mLayoutTransition.setAnimator(LayoutTransition.APPEARING,mAnimatorAppearing);


//        PropertyValuesHolder disappearingAlpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
//        PropertyValuesHolder disappearingRotationY = PropertyValuesHolder.ofFloat("rotationY", 0.0f, 90.0f);
//        ObjectAnimator mAnimatorDisappearing = ObjectAnimator.ofPropertyValuesHolder(this, disappearingAlpha, disappearingRotationY);
//        //为LayoutTransition设置动画及动画类型
//        mLayoutTransition.setAnimator(LayoutTransition.DISAPPEARING,mAnimatorDisappearing);
//
//
//        ObjectAnimator mAnimatorChangeDisappearing = ObjectAnimator.ofFloat(null, "alpha", 1f, 0f);
//        //为LayoutTransition设置动画及动画类型
//        mLayoutTransition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING,mAnimatorChangeDisappearing);

        ObjectAnimator mAnimatorChangeAppearing = ObjectAnimator.ofFloat(null, "alpha", 1f, 0f);
        //为LayoutTransition设置动画及动画类型
        mLayoutTransition.setAnimator(LayoutTransition.CHANGE_APPEARING,mAnimatorChangeAppearing);

        //为mImageViewGroup设置mLayoutTransition对象
        mFlexLayout.setLayoutTransition(mLayoutTransition);
    }

    private TestFlexView getH() {
        TestFlexView testFlexView = new TestFlexView(TestActivity.this);
        testFlexView.setH();
        return testFlexView;
    }

    private TestFlexView getW() {
        TestFlexView testFlexView = new TestFlexView(TestActivity.this);
        testFlexView.setW();
        return testFlexView;
    }

    /**
     * 获取所有文件名称
     *
     * @param path
     * @return
     */
    public static List<String> getFilesAllName(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) {
            Log.e("error", "空目录");
            return null;
        }
        List<String> s = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
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
