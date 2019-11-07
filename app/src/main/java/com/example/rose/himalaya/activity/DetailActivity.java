package com.example.rose.himalaya.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.rose.himalaya.R;
import com.example.rose.himalaya.base.BaseActivity;

/**
 * Created by rose on 2019/11/7.
 */

public class DetailActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }
}
