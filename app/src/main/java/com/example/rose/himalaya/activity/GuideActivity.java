package com.example.rose.himalaya.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.rose.himalaya.R;
import com.example.rose.himalaya.ScaleCircleNavigator;
import com.example.rose.himalaya.base.BaseActivity;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;

import java.util.ArrayList;

/**
 * Created by rose on 2019/11/7.
 */

public class GuideActivity extends BaseActivity {
    private ViewPager guideVp;//引导页容器
    public int[] imgs = {R.drawable.guide_one,R.drawable.guide_two,R.drawable.guide_three};
    public ArrayList<ImageView> imageViewArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        guideVp = this.findViewById(R.id.guide_vp);
        initVpData();//初始化viewpager数据
        GuideAdapter guideAdapter = new GuideAdapter(imageViewArrayList);//数据传给Adapter
        guideVp.setAdapter(guideAdapter);//把Adapter设置给Vp
        //监听Vp滑动是否完成
        guideVp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(guideVp.getCurrentItem()==imageViewArrayList.size()-1){
                            Intent intent = new Intent();
                            intent.setClass(GuideActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                }
                return false;
            }
        });
        initMagicIndicator();
    }

    private void initVpData() {

        imageViewArrayList = new ArrayList<>();
        for (int i = 0;i < imgs.length;i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imgs[i]);
            imageViewArrayList.add(imageView);
        }
    }

    private void initMagicIndicator() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
        ScaleCircleNavigator scaleCircleNavigator = new ScaleCircleNavigator(this);
        scaleCircleNavigator.setCircleCount(imgs.length);
        scaleCircleNavigator.setNormalCircleColor(getResources().getColor(R.color.normalCircleColor));
        scaleCircleNavigator.setSelectedCircleColor(getResources().getColor(R.color.selectCircleColor));
        scaleCircleNavigator.setCircleClickListener(new ScaleCircleNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                guideVp.setCurrentItem(index);
            }
        });
        magicIndicator.setNavigator(scaleCircleNavigator);
        ViewPagerHelper.bind(magicIndicator, guideVp);
    }

    class GuideAdapter extends PagerAdapter {
        private ArrayList<ImageView> imageViews;

        public GuideAdapter(ArrayList<ImageView> imageViews)
        {
            this.imageViews = imageViews;
        }

        @Override
        public int getCount() {
            return imageViewArrayList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = imageViewArrayList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
