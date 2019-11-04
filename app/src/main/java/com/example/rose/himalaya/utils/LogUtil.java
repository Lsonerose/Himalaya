package com.example.rose.himalaya.utils;

import android.util.Log;

/**
 * Created by rose on 2019/10/31.
 * 封装Log类 方便控制打印内容
 */

public class LogUtil {
    public static String HiTAD = "LogUtil";

    //控制是否要输出log
    public static boolean HiIsRelease = false;

    /*
    ** 如果要是发布了，可以在application里面把这里release一下，这样子就没有log输出了
     */
    public static void init(String baseTag,boolean hiIsRelease){
        HiTAD =  baseTag;
        HiIsRelease = hiIsRelease;
    }
    public static void d(String TAG,String content){
        if (!HiIsRelease){
            Log.d("[ "+HiTAD+" ]"+TAG,content);
        }
    }
    public static void v(String TAG,String content){
        if (!HiIsRelease){
            Log.d("[ "+HiTAD+" ]"+TAG,content);
        }
    }
    public static void i(String TAG,String content){
        if (!HiIsRelease){
            Log.d("[ "+HiTAD+" ]"+TAG,content);
        }
    }
}
