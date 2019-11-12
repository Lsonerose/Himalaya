package com.example.rose.himalaya.view;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.rose.himalaya.R;
import com.example.rose.himalaya.base.BaseApplication;
import com.example.rose.himalaya.utils.LogUtil;
import com.ldoublem.loadingviewlib.view.LVBlock;
import com.ldoublem.loadingviewlib.view.LVCircularRing;
import com.ldoublem.loadingviewlib.view.LVGhost;

/**
 * Created by rose on 2019/11/6.
 * UI加载器：帮助解决UI加载
 */

public abstract class UILoader extends FrameLayout{

    private View loadingView;
    private View successView;
    private View networkErrorView;
    private View emptyView;
    private OnRetryClickListener onRetryClickListener = null;
    private String UILoaderTAG = "UILoader";

    //枚举  加载中、成功、网络错误、内容为空、无状态
    public enum UIStatus{ LOADING,SUCCESS,NEWTWORK_ERROR,EMPTY,NONE }

    //当前状态（默认为无状态）
    public UIStatus currentStatus = UIStatus.NONE;

    public UILoader(@NonNull Context context) {
        this(context,null);//跳到第二个构造
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);//跳到第三个构造
    }

    //该类的唯一入口
    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //初始化
        init();
    }

    //改变当前状态
    public void updateStatus(UIStatus uiStatus){
        this.currentStatus = uiStatus;

        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                switchUIByCurrentStatus();
            }
        });
    }

    /**
     * 初始化UI
     */
    private void init() {
        switchUIByCurrentStatus();
    }

    private void switchUIByCurrentStatus() {
        //加载中
        if (loadingView == null) {
            loadingView = getLoadingView();
            addView(loadingView);
        }
        //根据状态设置是否可见(当前状态是否是加载中？是：显示  否：隐藏)
        loadingView.setVisibility(currentStatus == UIStatus.LOADING ? VISIBLE : GONE);

        //成功
        if (successView == null) {
            successView = getSuccessView(this);
            addView(successView);
        }
        //根据状态设置是否可见(当前状态是否是加载中？是：显示  否：隐藏)
        successView.setVisibility(currentStatus == UIStatus.SUCCESS ? VISIBLE : GONE);

        //网络错误页面
        if (networkErrorView == null) {
            networkErrorView = getNetworkErrorView();
            addView(networkErrorView);
       }
        //根据状态设置是否可见(当前状态是否是加载中？是：显示  否：隐藏)
        Log.e("----------------",currentStatus.toString());
        networkErrorView.setVisibility(currentStatus == UIStatus.NEWTWORK_ERROR ? VISIBLE : GONE);

        //数据为空
        if (emptyView == null) {
            emptyView = getEmptyView();
            addView(emptyView);
        }
        //根据状态设置是否可见(当前状态是否是加载中？是：显示  否：隐藏)
        emptyView.setVisibility(currentStatus == UIStatus.EMPTY ? VISIBLE : GONE);
    }

    protected abstract View getSuccessView(ViewGroup container);

    //加载中
    public View getLoadingView() {
        View loadingView = LayoutInflater.from(getContext()).inflate(R.layout.recommend_loading_layout,this,false);
        LVBlock lvBlock = loadingView.findViewById(R.id.loading_view);
        lvBlock.setViewColor(getContext().getResources().getColor(R.color.iconColor));
        lvBlock.startAnim(400);
        return loadingView;
    }

    //网络错误
    public View getNetworkErrorView() {
        View netWorkErrorView = LayoutInflater.from(getContext()).inflate(R.layout.recommend_network_error_layout,this,false);

        //给布局控件设置点击事件、实现点击屏幕重新加载功能
        netWorkErrorView.findViewById(R.id.recommend_network_error_icon).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //重新获取数据
                if (onRetryClickListener != null) {
                    LogUtil.d(UILoaderTAG,"被点击了");
                    onRetryClickListener.onRetryClick();
                }
            }
        });
        return netWorkErrorView;
    }

    //数据为空
    public View getEmptyView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.recommend_empty_layout,this,false);
    }

    public void setOnRetryClickListenter(OnRetryClickListener retryClickListenter){
        this.onRetryClickListener = retryClickListenter;
    }

    public interface OnRetryClickListener{
        void onRetryClick();
    }
}
