package com.example.rose.himalaya.interfaces;

import android.os.Trace;

import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

/**
 * Created by rose on 2019/11/12.
 */

public interface IPlayerCallback {
    //开始播放
    void onPlayStart();

    //暂停播放
    void onPlayPause();

    //播放停止
    void onPlayStop();

    //播放错误
    void onPlayError();

    //下一首
    void nextPlay(Track track);

    //上一首
    void onPrePlay(Track track);

    //播放列表数据加载完成
    void onListLoaded(List<Track> list);

    //播放模式改变
    void onPlayMoreChange(XmPlayListControl.PlayMode playMode);

    //进度条的改变
    void onProgressChange(int currentProgress, int total);

    //广告正在加载
    void onAdLoading();

    //广告结束
    void onAdFinished();

    /**
     * 更新当前节目
     */
    void onTrackUpDate(Track track, int playIndex);
}
