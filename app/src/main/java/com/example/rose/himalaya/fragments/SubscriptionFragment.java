package com.example.rose.himalaya.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rose.himalaya.R;
import com.example.rose.himalaya.base.BaseFragment;

/**
 * Created by rose on 2019/11/2.
 * 订阅界面
 */

public class SubscriptionFragment extends BaseFragment {
    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        View subscreptionView = layoutInflater.inflate(R.layout.fragment_subscreption,container,false);
        return subscreptionView;
    }
}
