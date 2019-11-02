package com.example.rose.himalaya.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.example.rose.himalaya.R;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

/**
 * Created by rose on 2019/11/1.
 */

public class IndicatorAdapter extends CommonNavigatorAdapter {

    private final String[] titles;

    public IndicatorAdapter(Context context) {
        titles = context.getResources().getStringArray(R.array.indicator_name);
    }

    @Override
    public int getCount() {
        if (titles != null)
            return titles.length;
        return 0;
    }

    @Override
    public IPagerTitleView getTitleView(Context context, int index) {
//        创建View
        ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
//        设置一般情况下为灰色
        colorTransitionPagerTitleView.setNormalColor(Color.parseColor("#aaffffff"));
//        设置选中情况下为黑色
        colorTransitionPagerTitleView.setSelectedColor(Color.parseColor("#FFFFFF"));
//        单位是Sp
        colorTransitionPagerTitleView.setTextSize(18);
//        设置要显示的内容
        colorTransitionPagerTitleView.setText(titles[index]);
//        设置TITLE的点击事件，这里需要做到当我们点击了title的时候下面的viewpager也会切换的相应的内容上去
        colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TODO:
            }
        });
//        把创建好的view返回出去
        return colorTransitionPagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
        indicator.setColors(Color.parseColor("#FFFFFF"));

        return indicator;
    }
}
