package com.example.rose.himalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

/**
 * Created by rose on 2019/11/7.
 */

public interface IAlbumDetailViewCallback {
    /**
     * 专辑详情内容加载出来了
     * @param tracks
     */
    void onDetailListLoaded(List<Track> tracks);

    /**
     * 把album传给UI使用
     * @param album
     */
    void onAlbumLoaded(Album album);

    /**
     * 网络错误
     */
    void onNetworkError(int i, String s);
}
