package com.example.rose.himalaya.presenters;

import android.provider.CalendarContract;
import android.support.annotation.Nullable;

import com.example.rose.himalaya.interfaces.IAlbumDetaialPresenter;
import com.example.rose.himalaya.interfaces.IAlbumDetailViewCallback;
import com.example.rose.himalaya.utils.Constant;
import com.example.rose.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
       //根据页码和专辑Id获取列表

        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.ALBUM_ID, albumId +"");
        map.put(DTransferConstants.SORT, "asc");
        map.put(DTransferConstants.PAGE, page+"");
        map.put(DTransferConstants.PAGE_SIZE, Constant.COUNT_DETAIL+"");
        CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(@Nullable TrackList trackList) {
                if (trackList != null) {
                    List<Track> tracks = trackList.getTracks();
                    LogUtil.d("---------","trackList size ====>" + tracks.size());
                    handlerAlbumDetailResult(tracks);
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.d("---------","error code ====>" + i);
                LogUtil.d("---------","error msg ====>" + s);
                handlerError(i,s);
            }
        });

    }

    /**
     * 如果发生错误，就通知UI
     * @param i
     * @param s
     */
    private void handlerError(int i, String s) {
        for (IAlbumDetailViewCallback iAlbumDetailViewCallback : iAlbumDetailViewCallbacks) {
            iAlbumDetailViewCallback.onNetworkError(i,s);
        }
    }

    //抛到UI里面去
    private void handlerAlbumDetailResult(List<Track> tracks) {
        for (IAlbumDetailViewCallback iAlbumDetailViewCallback : iAlbumDetailViewCallbacks) {
            iAlbumDetailViewCallback.onDetailListLoaded(tracks);
        }
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
