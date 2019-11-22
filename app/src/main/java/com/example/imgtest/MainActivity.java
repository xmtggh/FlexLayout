package com.example.imgtest;

import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.imgtest.widget.VerificationCodeTextView;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private VerificationCodeTextView mTvScreenCode;
    private int code = 2;
    private Button mBtnChange;
    private ImageView mImg1;
    private ImageView mImg2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvScreenCode = findViewById(R.id.tv_screen_code);
        mBtnChange = findViewById(R.id.btn_change);
        final ArrayMap<String, String> key = new ArrayMap<>();
        mBtnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               key.put("111","123"+code++);
                mTvScreenCode.setText("1ASD23" + code++);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int i= 0;
                        while (true){
//                            LogUtils.v("我是线程一,我是测试log，我的等级为v，我是第"+i+"条数据");
//                            LogUtils.d("我是线程一,我是测试log，我的等级为d，我是第"+i+"条数据");
//                            LogUtils.i("我是线程一,我是测试log，我的等级为i，我是第"+i+"条数据");
//                            LogUtils.w("我是线程一,我是测试log，我的等级为w，我是第"+i+"条数据");
                            LogUtils.e("我是线程一,我是测试log1，我的等级为e，我是第"+i+"条数据");

                            i++;
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }).start();
//                LogUtils.i("我是测试用例2的log");
//                Glide.with(MainActivity.this).load(getDrawable(R.mipmap.bg1)).into(mImg1);
//                Picasso.get().load(R.mipmap.bg2).into(mImg2);
//                Glide.with(MainActivity.this).load(getDrawable(R.mipmap.bg2));

            }
        });
        mImg1 = findViewById(R.id.img1);
        mImg2 = findViewById(R.id.img2);

//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true){
//                    int i= 0;
//                    LogUtils.v("我是线程二,我是测试log，我的等级为v，我是第"+i+"条数据");
//                    LogUtils.d("我是线程二,我是测试log，我的等级为d，我是第"+i+"条数据");
//                    LogUtils.i("我是线程二,我是测试log，我的等级为i，我是第"+i+"条数据");
//                    LogUtils.w("我是线程二,我是测试log，我的等级为w，我是第"+i+"条数据");
//                    LogUtils.e("我是线程二,我是测试log，我的等级为e，我是第"+i+"条数据");
//                    i++;
//                    try {
//                        Thread.sleep(300);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        }).start();
    }
}
