package com.example.rose.himalaya.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.rose.himalaya.R;

/**
 * Created by rose on 2019/11/7.
 */

public class ChooseActivity extends AppCompatActivity {
    private boolean isFirst;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("isFirst",MODE_WORLD_READABLE);
        isFirst = preferences.getBoolean("isFirst",true);

        Intent intent = new Intent();
        if (isFirst)
        {
            intent.setClass(ChooseActivity.this,GuideActivity.class);
            startActivity(intent);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirst",false);
            editor.commit();
            return;
        }
        setContentView(R.layout.activity_choose);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                enterHomeActivity();
            }
        }, 2000);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isFirst",false);
        editor.commit();
    }

    private void enterHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
