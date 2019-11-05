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

public class RecommendFragment extends BaseFragment {
    private String RecommendTAG = "RecommendFragment";
    private RecyclerView recommendRecyclerView;
    private View recommentView;
    private RecommendRecycleViewAdapter recommendRecycleViewAdapter;

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        //推荐View已加载完成
        recommentView = layoutInflater.inflate(R.layout.fragment_recommend,container,false);
//        初始化RecyclerView
        initRecyclerView();
        //去拿数据回来
        getRecommendData();

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

    /*
    ** 获取推荐内容
    ** 这个接口：3.10.6 获取猜你喜欢专辑
     */
    public void getRecommendData() {
        //封装参数
        Map<String, String> map = new HashMap<String, String>();
        //这个参数表示一页数据返回多少条
        map.put(DTransferConstants.LIKE_COUNT, Constant.RECOMMEND_COUNT + "");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(@Nullable GussLikeAlbumList gussLikeAlbumList) {
                //获取数据成功
                if (gussLikeAlbumList != null) {
                    List<Album> albumList = gussLikeAlbumList.getAlbumList();
//                    LogUtil.d(RecommendTAG,"RecommendFragment GussLikeAlbumListSize  ===>"+albumList.size());
//                  数据拿回来之后更新UI
                    upRecommendUI(albumList);
                }
            }

            @Override
            public void onError(int i, String s) {
                //获取数据内容
                LogUtil.d(RecommendTAG,"RecommendFragment errorCode ===>"+i);
                LogUtil.d(RecommendTAG,"RecommendFragment errorMsg ===>"+s);
            }
        });
    }

    private void upRecommendUI(List<Album> albumList) {
        //把数据设置给适配器，并且更新UI
        recommendRecycleViewAdapter.setData(albumList);
    }

}
