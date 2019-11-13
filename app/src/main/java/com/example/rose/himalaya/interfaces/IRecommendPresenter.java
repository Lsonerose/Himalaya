package com.example.rose.himalaya.interfaces;

import com.example.rose.himalaya.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

/**
 * Created by rose on 2019/11/5.
 */

public interface IRecommendPresenter extends IBasePresenter<IRecommendViewCallback>{

    //获取推荐内容
    void getRecommendList();

    //下拉刷新
    void pullRefreshMore();

    //上拉加载
    void loadMore();
}
