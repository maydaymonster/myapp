package com.ttit.myapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ttit.myapp.MainActivity;
import com.ttit.myapp.R;
import com.ttit.myapp.adapter.VpAdapter;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPager vPager;
    private VpAdapter vpAdapter;
    private static  int[] imgs = {R.drawable.welcomepage1,R.drawable.welcomepage2, R.drawable.welcomepage3};
    private ArrayList<ImageView> imageViews;
    private ImageView[] dotViews;//小圆点


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        vPager = (ViewPager)findViewById(R.id.guide_ViewPager);
        initImages();
        initDots();
        vpAdapter = new VpAdapter(imageViews);
        vPager.setAdapter(vpAdapter);
        vPager.setOnPageChangeListener(this);
    }
    private void initImages(){
        //设置每一张图片都填充窗口
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);//设置每一张图片都填充窗口
        imageViews = new ArrayList<ImageView>();

        for(int i = 0; i < imgs.length; i++)
        {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);//设置布局
            iv.setImageResource(imgs[i]);//为Imageview添加图片资源
            iv.setScaleType(ImageView.ScaleType.FIT_XY);//设置图片拉伸效果
            imageViews.add(iv);
            if(i== imgs.length - 1)//为最后一张添加点击事件
            {
                iv.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Toast.makeText(GuideActivity.this, "开启你的学习之旅", Toast.LENGTH_SHORT).show();
                        Intent toMainActivity = new Intent(GuideActivity.this, MainActivity.class);//跳转到主界面
                        startActivity(toMainActivity);
                    }
                });
            }
        }


    }
    private void initDots(){
        LinearLayout layout = (LinearLayout)findViewById(R.id.dot_Layout);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(20, 20);
        mParams.setMargins(10, 0, 10,0);//设置小圆点左右之间的间隔
        dotViews = new ImageView[imgs.length];
        //判断小圆点的数量，从0开始，0表示第一个
        for(int i = 0; i < imageViews.size(); i++)
        {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(mParams);
            imageView.setImageResource(R.drawable.dotselector);
            if(i== 0)
            {
                imageView.setSelected(true);//默认启动时，选中第一个小圆点
            }
            else {
                imageView.setSelected(false);
            }
            dotViews[i] = imageView;//得到每个小圆点的引用，用于滑动页面时，（onPageSelected方法中）更改它们的状态。
            layout.addView(imageView);//添加到布局里面显示
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int arg0) {
        for (int i = 0; i < dotViews.length; i++) {
            if (arg0 == i) {
                dotViews[i].setSelected(true);
            } else {
                dotViews[i].setSelected(false);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        moveTaskToBack(false);
    }
}