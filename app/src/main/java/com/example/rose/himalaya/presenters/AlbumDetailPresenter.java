package com.example.rose.himalaya.presenters;

import android.provider.CalendarContract;

import com.example.rose.himalaya.interfaces.IAlbumDetaialPresenter;
import com.example.rose.himalaya.interfaces.IAlbumDetailViewCallback;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rose on 2019/11/7.
 * 实现专辑接口
 */

public class AlbumDetailPresenter implements IAlbumDetaialPresenter {

    private List<IAlbumDetailViewCallback> iAlbumDetailViewCallbacks = new ArrayList<>();
    private Album mTargetAlbum = null;

    private AlbumDetailPresenter(){
    }

    private static AlbumDetailPresenter sIntance = null;

   public  static AlbumDetailPresenter getsIntance(){
        if (sIntance == null) {
            synchronized (AlbumDetailPresenter.class) {
                if (sIntance == null) {
                    sIntance = new AlbumDetailPresenter();
                }
            }
        }
        return sIntance;
    }

    @Override
    public void pullRefreshMore() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void getAlbumdetail(int albumId, int page) {

    }

    @Override
    public void registerViewCallback(IAlbumDetailViewCallback detaialPresenter) {
        if (!iAlbumDetailViewCallbacks.contains(detaialPresenter)) {
            iAlbumDetailViewCallbacks.add(detaialPresenter);
            if (mTargetAlbum != null) {
                detaialPresenter.onAlbumLoaded(mTargetAlbum);
            }
        }
    }

    @Override
    public void unRegisterViewCallback(IAlbumDetailViewCallback detaialPresenter) {
       iAlbumDetailViewCallbacks.remove(detaialPresenter);
    }

    //加载专辑
    public void setTargetAlbum(Album targetAlbum){
        this.mTargetAlbum = targetAlbum;
    }
}
