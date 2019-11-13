package com.example.rose.himalaya.presenters;

import android.support.annotation.Nullable;

import com.example.rose.himalaya.interfaces.IRecommendPresenter;
import com.example.rose.himalaya.interfaces.IRecommendViewCallback;
import com.example.rose.himalaya.utils.Constant;
import com.example.rose.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rose on 2019/11/5.
 */

public class RecommendPresenter implements IRecommendPresenter {
    private static final String RecommendPresenterTAG = "RecommendPresenter";

    private List<IRecommendViewCallback> recommendViewCallbacks = new ArrayList<>();
    private List<Album> mCurrentRecommend = null;
    private List<Album> mRecommendList;

    private RecommendPresenter(){
    };

    private static RecommendPresenter recommendPresenterInstance = null;

    /**
     * 获取单例对象
     * @return
     */
    public static RecommendPresenter getInstance(){
        if (recommendPresenterInstance == null) {
            synchronized (RecommendPresenter.class){
                if (recommendPresenterInstance == null) {
                    recommendPresenterInstance = new RecommendPresenter();
                }
            }
        }
        return recommendPresenterInstance;
    }

    /*
  ** 获取推荐内容
  ** 这个接口：3.10.6 获取猜你喜欢专辑
   */
    @Override
    public void getRecommendList() {
        //获取推荐内容
        //封装参数
        updateLoding();
        Map<String, String> map = new HashMap<String, String>();
        //这个参数表示一页数据返回多少条
        map.put(DTransferConstants.LIKE_COUNT, Constant.RECOMMEND_COUNT + "");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(@Nullable GussLikeAlbumList gussLikeAlbumList) {
                //获取数据成功
                if (gussLikeAlbumList != null) {
                    List<Album> albumList = gussLikeAlbumList.getAlbumList();
                    //通知UI界面更新
                    handlerRecommendResult(albumList);
                }
            }

            @Override
            public void onError(int i, String s) {
                //获取数据内容
                LogUtil.d(RecommendPresenterTAG,"RecommendFragment errorCode ===>"+i);
                LogUtil.d(RecommendPresenterTAG,"RecommendFragment errorMsg ===>"+s);
                handlerError();
            }
        });
    }

    private void handlerError() {
        if (recommendViewCallbacks != null) {
            for (IRecommendViewCallback recommendViewCallback : recommendViewCallbacks) {
                recommendViewCallback.onNetworkError();
            }
        }
    }

    private void handlerRecommendResult(List<Album> albums) {
        //通知UI更新
        if (albums != null) {
            //测试一下 让内容显示为空
//            albums.clear();
            if (albums.size()==0) {
                for (IRecommendViewCallback recommendViewCallback : recommendViewCallbacks) {
                    recommendViewCallback.onEmpty();
                }
            }else {
                for (IRecommendViewCallback recommendViewCallback : recommendViewCallbacks) {
                    recommendViewCallback.onRecommendListLoaded(albums);
                }
            }
        }
    }

    public void updateLoding(){
        for (IRecommendViewCallback recommendViewCallback : recommendViewCallbacks) {
            recommendViewCallback.onLoading();
        }
    }

    @Override
    public void pullRefreshMore() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void registerViewCallback(IRecommendViewCallback iRecommendViewCallback) {
        if (recommendViewCallbacks != null && !recommendViewCallbacks.contains(iRecommendViewCallback)){
            recommendViewCallbacks.add(iRecommendViewCallback);
        }
    }

    @Override
    public void unRegisterViewCallback(IRecommendViewCallback iRecommendViewCallback) {
        if (recommendViewCallbacks != null) {
            recommendViewCallbacks.remove(iRecommendViewCallback);
        }
    }
}
