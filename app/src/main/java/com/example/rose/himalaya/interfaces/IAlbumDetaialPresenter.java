package com.example.rose.himalaya.interfaces;

/**
 * Created by rose on 2019/11/7.
 * 专辑接口
 */

public interface IAlbumDetaialPresenter {

    //下拉刷新
    void pullRefreshMore();

    //上拉加载
    void loadMore();

    //获取专辑详情
    void getAlbumdetail(int albumId,int page);

    /**
     * 注册UI通知接口
     * @param detaialPresenter
     */
    void registerViewCallback(IAlbumDetailViewCallback detaialPresenter);

    //删除UI通知接口
    void unRegisterViewCallback(IAlbumDetailViewCallback detaialPresenter);
}
