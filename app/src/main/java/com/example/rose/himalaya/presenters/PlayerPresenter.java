package com.example.rose.himalaya.presenters;

import android.media.MediaPlayer;
import android.os.Trace;

import com.example.rose.himalaya.base.BaseApplication;
import com.example.rose.himalaya.interfaces.IPlayerCallback;
import com.example.rose.himalaya.interfaces.IPlayerPresenter;
import com.example.rose.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.constants.PlayerConstants;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rose on 2019/11/12.
 */

public class PlayerPresenter implements IPlayerPresenter, IXmAdsStatusListener, IXmPlayerStatusListener {
    private List<IPlayerCallback> mIPlayerCallbacks = new ArrayList<>();

    private String PLAYER_PRESENTER_TAG = "PlayerPresenter";
    private final XmPlayerManager xmPlayerManager;
    private Track currentTrack;
    private int currentIndex = 0;

    private PlayerPresenter() {
        xmPlayerManager = XmPlayerManager.getInstance(BaseApplication.getAppContext());
        //广告相关接口
        xmPlayerManager.addAdsStatusListener(this);
        //注册播放器状态相关接口
        xmPlayerManager.addPlayerStatusListener(this);
    }

    private static PlayerPresenter playerPresenter;

    public static PlayerPresenter getPlayerPresenter() {
        if (playerPresenter == null) {
            synchronized (PlayerPresenter.class) {
                if (playerPresenter == null) {
                    playerPresenter = new PlayerPresenter();
                }
            }
        }
        return playerPresenter;
    }

    private boolean isPlayListSet = false;

    public void setPlayList(List<Track> list, int playIndex) {
        if (xmPlayerManager != null) {
            xmPlayerManager.setPlayList(list, playIndex);
            isPlayListSet = true;
            currentTrack = list.get(playIndex);
            currentIndex = playIndex;
        } else {
            LogUtil.d(PLAYER_PRESENTER_TAG, "xmPlayerManager = NULL");
        }
    }

    @Override
    public void registerViewCallback(IPlayerCallback iPlayerCallback) {
        iPlayerCallback.onTrackUpDate(currentTrack,currentIndex);
        if (!mIPlayerCallbacks.contains(iPlayerCallback)) {
            mIPlayerCallbacks.add(iPlayerCallback);
        }
    }

    @Override
    public void unRegisterViewCallback(IPlayerCallback iPlayerCallback) {
        mIPlayerCallbacks.remove(iPlayerCallback);
    }

    @Override
    public void play() {
        if (isPlayListSet) {
            xmPlayerManager.play();
        }
    }

    @Override
    public void pause() {
        if (xmPlayerManager != null) {
            xmPlayerManager.pause();
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void playPre() {
        //前一首
        if (xmPlayerManager != null) {
            xmPlayerManager.playPre();
        }
    }

    @Override
    public void playNext() {
        //下一首
        if (xmPlayerManager != null) {
            xmPlayerManager.playNext();
        }
    }

    @Override
    public void switchPlayMode(XmPlayListControl.PlayMode mode) {
        if (xmPlayerManager != null) {
            xmPlayerManager.setPlayMode(mode);
        }
    }

    @Override
    public void getPlayList() {
        if (xmPlayerManager != null) {
            List<Track> playList = xmPlayerManager.getPlayList();
            for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
                iPlayerCallback.onListLoaded(playList);
            }
        }
//        xmPlayerManager.getCommonTrackList();
    }

    @Override
    public void playByIndex(int index) {
        //切换播放器到第index的位置进行播放
        if (xmPlayerManager != null) {
            xmPlayerManager.play(index);
        }
    }

    @Override
    public void seekTo(int progress) {
        //更新播放器进度
        xmPlayerManager.seekTo(progress);
    }

    @Override
    public boolean isPlay() {
        //返回当前是否正在播放
        return xmPlayerManager.isPlaying();
    }

    //==========================广告相关的回调方法  start=================================//
    @Override
    public void onStartGetAdsInfo() {
        LogUtil.d(PLAYER_PRESENTER_TAG, "onStartGetAdsInfo");

    }

    @Override
    public void onGetAdsInfo(AdvertisList advertisList) {
        LogUtil.d(PLAYER_PRESENTER_TAG, "onGetAdsInfo");

    }

    @Override
    public void onAdsStartBuffering() {
        LogUtil.d(PLAYER_PRESENTER_TAG, "onAdsStartBuffering");

    }

    @Override
    public void onAdsStopBuffering() {
        LogUtil.d(PLAYER_PRESENTER_TAG, "onAdsStopBuffering");

    }

    @Override
    public void onStartPlayAds(Advertis advertis, int i) {
        LogUtil.d(PLAYER_PRESENTER_TAG, "onStartPlayAds");

    }

    @Override
    public void onCompletePlayAds() {
        LogUtil.d(PLAYER_PRESENTER_TAG, "onCompletePlayAds");

    }

    @Override
    public void onError(int i, int i1) {
        LogUtil.d(PLAYER_PRESENTER_TAG, "onError");

    }
    //==========================广告相关的回调方法  end=================================//

    //==========================播放器接口相关的回调 start ============================//

    @Override
    public void onPlayStart() {
        LogUtil.d(PLAYER_PRESENTER_TAG, "onPlayStart");
        for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
            iPlayerCallback.onPlayStart();
        }

    }

    @Override
    public void onPlayPause() {
        LogUtil.d(PLAYER_PRESENTER_TAG, "onPlayPause");
        for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
            iPlayerCallback.onPlayPause();
        }
    }

    @Override
    public void onPlayStop() {
        LogUtil.d(PLAYER_PRESENTER_TAG, "onPlayStop");
        for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
            iPlayerCallback.onPlayStop();
        }
    }

    @Override
    public void onSoundPlayComplete() {
        LogUtil.d(PLAYER_PRESENTER_TAG, "onSoundPlayComplete");
        if (xmPlayerManager.getPlayerStatus() == PlayerConstants.STATE_PREPARED) {
            //播放器准备完了 可以播放了
            xmPlayerManager.play();
        }
    }

    @Override
    public void onSoundPrepared() {
        LogUtil.d(PLAYER_PRESENTER_TAG, "onSoundPrepared");

    }

    @Override
    public void onSoundSwitch(PlayableModel lastModel, PlayableModel curModel) {
//        LogUtil.d(PLAYER_PRESENTER_TAG,"onSoundSwitch");
//        if (lastModel != null) {
//
//        }
        currentIndex = xmPlayerManager.getCurrentIndex();
        /**
         * curModel代表当前播放的内容
         * 通过getKind（）方法获取它是什么类型的
         */
        if (curModel instanceof Track) {
            Track currentTrack = (Track) curModel;
            this.currentTrack = currentTrack;
            //更新UI
            for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
                iPlayerCallback.onTrackUpDate(currentTrack, currentIndex);
            }
        }

    }

    @Override
    public void onBufferingStart() {
        LogUtil.d(PLAYER_PRESENTER_TAG, "onBufferingStart");
    }

    @Override
    public void onBufferingStop() {
        LogUtil.d(PLAYER_PRESENTER_TAG, "onBufferingStop");
    }

    @Override
    public void onBufferProgress(int i) {
        LogUtil.d(PLAYER_PRESENTER_TAG, "onBufferProgress");
    }

    /**
     * 更新进度（单位是毫秒）
     *
     * @param i  当前位置
     * @param i1 总共时长
     */
    @Override
    public void onPlayProgress(int i, int i1) {
//        LogUtil.d(PLAYER_PRESENTER_TAG,"onPlayProgress");
        for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
            iPlayerCallback.onProgressChange(i, i1);
        }
    }

    @Override
    public boolean onError(XmPlayerException e) {
        LogUtil.d(PLAYER_PRESENTER_TAG, "onError" + e);
        return false;
    }
    //==========================播放器接口相关的回调 end ============================//
}
