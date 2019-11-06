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
     * 网络错误
     */
    void onNetworkError();

    /**
     * 数据为空
     */
    void onEmpty();

    /**
     *
     */
    void onLoading();
}
