package com.example.rose.himalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
 * Created by rose on 2019/11/5.
 */

public interface IRecommendViewCallback {
    /**
     * 获取推荐内容的接口
     * @param result
     */
    void onRecommendListLoaded(List<Album> result);

    /**
     * 加载更多
     * @param result
     */
    void onLoaderMore(List<Album> result);

    /**
     * 下拉加载更多接口
     * @param result
     */
    void onRefreshMore(List<Album> result);
}
