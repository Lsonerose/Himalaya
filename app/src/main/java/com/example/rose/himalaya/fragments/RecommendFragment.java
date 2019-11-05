package com.example.rose.himalaya.fragments;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rose.himalaya.R;
import com.example.rose.himalaya.adapters.RecommendRecycleViewAdapter;
import com.example.rose.himalaya.base.BaseFragment;
import com.example.rose.himalaya.interfaces.IRecommendViewCallback;
import com.example.rose.himalaya.presenters.RecommendPresenter;
import com.example.rose.himalaya.utils.Constant;
import com.example.rose.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rose on 2019/11/2.
 * 推荐界面
 */

public class RecommendFragment extends BaseFragment implements IRecommendViewCallback{
    private String RecommendTAG = "RecommendFragment";
    private RecyclerView recommendRecyclerView;
    private View recommentView;
    private RecommendRecycleViewAdapter recommendRecycleViewAdapter;
    private RecommendPresenter recommendPresenterInstance;

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        //推荐View已加载完成
        recommentView = layoutInflater.inflate(R.layout.fragment_recommend,container,false);
//        初始化RecyclerView
        initRecyclerView();
        //获取到逻辑层的接口
        recommendPresenterInstance = RecommendPresenter.getInstance();
        //先要设置通知接口的注册
        recommendPresenterInstance.reqisterViewCallback(this);
        recommendPresenterInstance.getRecommendList();
        //返回View，给界面实现显示
        return recommentView;
    }

    /**
     * 初始化RecycleView
     *  1.实例化控件
     *  2.设置布局管理器
     *  3.设置适配器
     */
    private void initRecyclerView() {
        recommendRecyclerView = recommentView.findViewById(R.id.recommend_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());//设置一个线性布局管理器
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置布局方向
        recommendRecyclerView.setLayoutManager(linearLayoutManager);//把布局管理器给到RecycleView

        recommendRecycleViewAdapter = new RecommendRecycleViewAdapter();//自定义的Adapter
        recommendRecyclerView.setAdapter(recommendRecycleViewAdapter);//把创建好的Adapter设置给RecycleView
    }

    /**
     * 当我们成功获取到推荐内容的时候，这个方法就会被调用
     * 数据回来以后就更新UI
     * 把数据设置给适配器，并且更新UI
     * @param result
     */
    @Override
    public void onRecommendListLoaded(List<Album> result) {
        //把数据设置给适配器，并且更新UI
        recommendRecycleViewAdapter.setData(result);
    }

    @Override
    public void onLoaderMore(List<Album> result) {

    }

    @Override
    public void onRefreshMore(List<Album> result) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //取消接口的注册
        if (recommendPresenterInstance != null) {
            recommendPresenterInstance.unReqisterViewCallback(this);
        }
    }
}
