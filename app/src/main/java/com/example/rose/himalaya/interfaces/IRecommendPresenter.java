package com.example.rose.himalaya.interfaces;

import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

/**
 * Created by rose on 2019/11/5.
 */

public interface IRecommendPresenter {

    //获取推荐内容
    void getRecommendList();

    //下拉刷新
    void pullRefreshMore();

    //上拉加载
    void loadMore();

    /**
     * 这个方法用于注册的回调
     * @param iRecommendViewCallback
     */
    void reqisterViewCallback(IRecommendViewCallback iRecommendViewCallback);

    /**
     * 取消UI的回调注册
     * @param iRecommendViewCallback
     */
    void unReqisterViewCallback(IRecommendViewCallback iRecommendViewCallback);
}
