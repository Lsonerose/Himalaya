package com.example.rose.himalaya.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rose.himalaya.R;
import com.example.rose.himalaya.base.BaseFragment;

/**
 * Created by rose on 2019/11/2.
 * 推荐界面
 */

public class RecommendFragment extends BaseFragment {
    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        View recommentView = layoutInflater.inflate(R.layout.fragment_recommend,container,false);
        return recommentView;
    }
}
