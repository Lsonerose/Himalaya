package com.example.rose.himalaya.interfaces;

import com.example.rose.himalaya.base.IBasePresenter;

/**
 * Created by rose on 2019/11/7.
 * 专辑接口
 */

public interface IAlbumDetaialPresenter extends IBasePresenter<IAlbumDetailViewCallback>{

    //下拉刷新
    void pullRefreshMore();

    //上拉加载
    void loadMore();

    //获取专辑详情
    void getAlbumdetail(int albumId,int page);
}
