package com.example.rose.himalaya.activity;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.rose.himalaya.R;
import com.example.rose.himalaya.adapters.IndicatorAdapter;
import com.example.rose.himalaya.adapters.MainViewPagerAdapter;
import com.example.rose.himalaya.base.BaseActivity;
import com.example.rose.himalaya.utils.LogUtil;
import com.gyf.immersionbar.ImmersionBar;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.category.Category;
import com.ximalaya.ting.android.opensdk.model.category.CategoryList;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
**  程序主入口、主界面
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private MagicIndicator magicIndicator;
    private ViewPager contentViewPager;
    private IndicatorAdapter indicatorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initImmersionBar();//初始化沉浸式状态栏
        initView();//初始化顶部指示器
        initEvent();
    }

    private void initEvent() {
        //
        indicatorAdapter.setOnIndicatorTabClickListener(new IndicatorAdapter.OnIndicatorTabClickListener() {
            @Override
            public void onTabClick(int index) {
                LogUtil.d(TAG,"IndicatorClick index is ---->" + index);
                if (contentViewPager != null){
                    contentViewPager.setCurrentItem(index);
                }
            }
        });
    }

    private void initImmersionBar() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.mainColor)
                .init();
    }

    private void initView() {
        //指示器
        magicIndicator = findViewById(R.id.main_indicator);
        magicIndicator.setBackgroundColor(this.getResources().getColor(R.color.mainColor));
        /*
        ** Viewpager
        ** 创建内容适配器
        ** 绑定给viewpager
         */
        contentViewPager = findViewById(R.id.main_content_pager);
        MainViewPagerAdapter contentViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        contentViewPager.setAdapter(contentViewPagerAdapter);

//        创建indicator适配器
        indicatorAdapter = new IndicatorAdapter(this);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);//自动调节title选项卡的内容位置
        commonNavigator.setAdapter(indicatorAdapter);

        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator,contentViewPager);
    }
}
