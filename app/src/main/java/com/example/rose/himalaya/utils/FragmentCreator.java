package com.example.rose.himalaya.utils;

import com.example.rose.himalaya.base.BaseApplication;
import com.example.rose.himalaya.base.BaseFragment;
import com.example.rose.himalaya.fragments.HistoryFragment;
import com.example.rose.himalaya.fragments.RecommendFragment;
import com.example.rose.himalaya.fragments.SubscriptionFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rose on 2019/11/2.
 * Fragment创建工具类
 */

public class FragmentCreator {
    private static Map<Integer,BaseFragment> cache = new HashMap<>();//保存以及创建过的Fragment、避免浪费资源

    private final static int INDEX_RECOMMENT = 0;
    private final static int INDEX_SUBSCRIPTION = 1;
    private final static int INDEX_HISTORY = 2;

    public final static int PAGE_COUNT = 3;

    public static BaseFragment getFragment(int index){
        BaseFragment baseFragment = cache.get(index);
        if (baseFragment != null)//判断集合里面是否有值 有的话就直接返回集合里面的内容 没有则继续往下走
            return baseFragment;

        switch (index){//创建Fragment内容
            case INDEX_HISTORY:
                baseFragment = new HistoryFragment();
                break;
            case INDEX_RECOMMENT:
                baseFragment = new RecommendFragment();
                break;
            case INDEX_SUBSCRIPTION:
                baseFragment = new SubscriptionFragment();
                break;
        }
        cache.put(index,baseFragment);//存到集合里面去
        return baseFragment;//返回创建的内容
    }
}
