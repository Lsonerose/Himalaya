package com.example.rose.himalaya.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rose.himalaya.R;
import com.example.rose.himalaya.adapters.PlayerViewPagerAdapter;
import com.example.rose.himalaya.base.BaseActivity;
import com.example.rose.himalaya.interfaces.IPlayerCallback;
import com.example.rose.himalaya.presenters.PlayerPresenter;
import com.example.rose.himalaya.utils.FastBlurUtil;
import com.example.rose.himalaya.utils.LogUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_LIST;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM;
import static com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP;

/**
 * Created by rose on 2019/11/12.
 */

public class PlayerActivity extends BaseActivity implements IPlayerCallback, View.OnClickListener {

    private ImageView playerBg;
    private ImageView playerBack;
    private ViewPager trackPagerView;
    private TextView playerTitle;
    private ImageView playOrPauseBtn;
    private PlayerPresenter playerPresenter;
    private TextView playerDuration;
    private TextView playerCurrentPosition;
    private SeekBar playerSeekBar;
    private ImageView playerModeSwitchBtn;
    private ImageView playPre;
    private ImageView playNext;

    //格式化时间
    private SimpleDateFormat minFormat = new SimpleDateFormat("mm:ss");//分
    private SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm:ss");//时

    //TAG
    private String PLAYER_ACTIVITY_TAG = "PlayerActivity";

    //播放条数据
    private int currentProgress = 0;//播放进度当前值
    private boolean isUserTouchProgressBar = false;//是否正在触摸
    private ImageView playerList;
    private String trackTitleText;
    private PlayerViewPagerAdapter playerViewPagerAdapter;

    //用户是否滑动ViewPager
    private boolean isUserSildePager = false;

    //毛玻璃使用到的数据
    private int playerIndex;
    private List<Track> playerTrackList;
    private Track playerTrack;

    //播放模式
    private static Map<XmPlayListControl.PlayMode, XmPlayListControl.PlayMode> playModeMap = new HashMap<>();
    private XmPlayListControl.PlayMode currentMode = XmPlayListControl.PlayMode.PLAY_MODEL_LIST;//当前状态，默认列表播放

    /**
     * 处理播放模式的切换
     * 1、默认的是：PLAY_MODEL_LIST 列表播放
     * 2、PLAY_MODEL_LIST_LOOP 列表循环
     * 3、PLAY_MODEL_RANDOM 随机播放
     * 4、PLAY_MODEL_SINGLE_LOOP 单曲循环
     */
    static {
        playModeMap.put(PLAY_MODEL_LIST, PLAY_MODEL_LIST_LOOP);
        playModeMap.put(PLAY_MODEL_LIST_LOOP, PLAY_MODEL_RANDOM);
        playModeMap.put(PLAY_MODEL_RANDOM, PLAY_MODEL_SINGLE_LOOP);
        playModeMap.put(PLAY_MODEL_SINGLE_LOOP, PLAY_MODEL_LIST);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        playerPresenter = PlayerPresenter.getPlayerPresenter();
        playerPresenter.registerViewCallback(this);
        playerPresenter.getPlayList();

        initView();
        //在界面加载完以后 才去获取数据
        playerPresenter.getPlayList();
        initEvent();
    }

    /**
     * 给控件设置相关事件
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initEvent() {
        playerBack.setOnClickListener(this);//返回按钮

        playOrPauseBtn.setOnClickListener(this);//控制暂停播放
        playPre.setOnClickListener(this);//上一首
        playNext.setOnClickListener(this);//下一首
        playerModeSwitchBtn.setOnClickListener(this);//播放模式

        //监听Seekbar拖动
        playerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    currentProgress = i;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isUserTouchProgressBar = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isUserTouchProgressBar = false;
                //手离开拖动进度条，更新进度
                playerPresenter.seekTo(currentProgress);
            }
        });

        //监听Viewpager滑动
        trackPagerView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                LogUtil.d(PLAYER_ACTIVITY_TAG,"position ===> "+position);
                //当页面选中时，就切换播放内容
                if (playerPresenter != null && isUserSildePager) {
                    playerIndex = position;
                    playerPresenter.playByIndex(playerIndex);
                    playerTrack = playerTrackList.get(playerIndex);
                    Picasso.with(PlayerActivity.this).load(playerTrack.getCoverUrlLarge()).into(playerBg, new Callback() {
                        @Override
                        public void onSuccess() {
                            Drawable drawable = playerBg.getDrawable();
                            if (drawable != null) {
                                //到这里才说明是有图片的
                                FastBlurUtil.makeBlur(playerBg, PlayerActivity.this);
                            }
                        }

                        @Override
                        public void onError() {
                            LogUtil.d(PLAYER_ACTIVITY_TAG, "Error");
                        }
                    });
                }
                isUserSildePager = false;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //监听ViewPager是否被触摸
        trackPagerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    isUserSildePager = true;
                }
                return false;
            }
        });
    }

    /**
     * 找到各个控件
     */
    private void initView() {
        playerIndex = 0;//初始化播放位置
        //播放器背景
        playerBg = findViewById(R.id.player_bg);
        //返回按钮
        playerBack = findViewById(R.id.player_back);
        //封面图
        trackPagerView = findViewById(R.id.track_pager_view);
        //创建适配器
        playerViewPagerAdapter = new PlayerViewPagerAdapter();
        //设置适配器
        trackPagerView.setAdapter(playerViewPagerAdapter);

        //标题
        playerTitle = findViewById(R.id.player_title);
        if (!TextUtils.isEmpty(trackTitleText)) {
            playerTitle.setText(trackTitleText);
        }

        //当前进度
        playerCurrentPosition = findViewById(R.id.player_current_position);
        //总时长
        playerDuration = findViewById(R.id.player_duration);
        //进度条
        playerSeekBar = findViewById(R.id.player_seek_bar);

        //播放模式
        playerModeSwitchBtn = findViewById(R.id.player_mode_switch_btn);
        //上一首
        playPre = findViewById(R.id.play_pre);
        //播放||暂停控制按钮
        playOrPauseBtn = findViewById(R.id.play_or_pause_btn);
        //下一首
        playNext = findViewById(R.id.play_next);
        //播放列表
        playerList = findViewById(R.id.player_list);
    }

    @Override
    public void onPlayStart() {
        //开始播放，修改UI成暂停按钮
        if (playOrPauseBtn != null) {
            playOrPauseBtn.setImageResource(R.drawable.player_p);

        }
    }

    @Override
    public void onPlayPause() {
        //暂停播放，修改UI成播放按钮
        if (playOrPauseBtn != null) {
            playOrPauseBtn.setImageResource(R.drawable.player_play);
        }
    }

    @Override
    public void onPlayStop() {
        if (playOrPauseBtn != null) {
            playOrPauseBtn.setImageResource(R.drawable.player_play);
        }
    }

    @Override
    public void onPlayError() {

    }

    @Override
    public void nextPlay(Track track) {

    }

    @Override
    public void onPrePlay(Track track) {

    }

    @Override
    public void onListLoaded(List<Track> list) {
        //把数据设置到适配器里
        if (playerViewPagerAdapter != null) {
            playerViewPagerAdapter.setData(list);

        }
        if (list != null) {
            playerTrackList = list;
            playerTrack = playerTrackList.get(playerIndex);
            playerIndex = playerViewPagerAdapter.index;
            //给播放器背景做毛玻璃效果
            if (playerBg != null && null != playerBg) {
                Picasso.with(this).load(playerTrack.getCoverUrlLarge()).into(playerBg, new Callback() {
                    @Override
                    public void onSuccess() {
                        Drawable drawable = playerBg.getDrawable();
                        if (drawable != null) {
                            //到这里才说明是有图片的
                            FastBlurUtil.makeBlur(playerBg, PlayerActivity.this);
                        }
                    }

                    @Override
                    public void onError() {
                        LogUtil.d(PLAYER_ACTIVITY_TAG, "Error");
                    }
                });
            }
        }
    }

    @Override
    public void onPlayMoreChange(XmPlayListControl.PlayMode playMode) {

    }

    @Override
    public void onProgressChange(int currentProgress, int total) {
        playerSeekBar.setMax(total);
        //更新播放进度（更新进度条）
        String totalDuration;
        String currentPosition;
        if (total > 1000 * 60 * 60) {
            totalDuration = hourFormat.format(total);
            currentPosition = hourFormat.format(currentProgress);
        } else {
            totalDuration = minFormat.format(total);
            currentPosition = minFormat.format(currentProgress);
        }

        //更新总时长
        if (playerDuration != null) {
            playerDuration.setText(totalDuration);
        }
        //更新当前时间
        if (playerCurrentPosition != null) {
            playerCurrentPosition.setText(currentPosition);
        }
        //更新进度
        if (!isUserTouchProgressBar) {
            playerSeekBar.setProgress(currentProgress);
        }
    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFinished() {

    }

    @Override
    public void onTrackUpDate(Track track, int playIndex) {
        this.trackTitleText = track.getTrackTitle();
        if (playerTitle != null) {
            //设置当前节目的标题
            playerTitle.setText(track.getTrackTitle());
        }
        //当节目改变的时候，我们就能获取到当前播放位置
        //当前节目改变以后，要修改页面图片
        if (trackPagerView != null) {
            trackPagerView.setCurrentItem(playIndex, true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        if (playerPresenter != null) {
            playerPresenter.unRegisterViewCallback(this);
            playerPresenter = null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.player_back:
                finish();
                break;
            case R.id.play_or_pause_btn:
                /**
                 * 如果现在的状态是正在播放的，那么就暂停
                 * 如果现在的状态是非播放的，就让播放器播放
                 */
                if (playerPresenter.isPlay()) {
                    playerPresenter.pause();
                } else {
                    playerPresenter.play();
                }
                break;
            case R.id.play_pre:
                //上一首
                if (playerPresenter != null) {
                    playerPresenter.playPre();
                    playerIndex = playerViewPagerAdapter.index;
                    LogUtil.d(PLAYER_ACTIVITY_TAG, "playerIndex--  ===>  " + playerIndex);
                    playerTrack = playerTrackList.get(playerIndex);
                    Picasso.with(PlayerActivity.this).load(playerTrack.getCoverUrlLarge()).into(playerBg, new Callback() {
                        @Override
                        public void onSuccess() {
                            Drawable drawable = playerBg.getDrawable();
                            if (drawable != null) {
                                //到这里才说明是有图片的
                                FastBlurUtil.makeBlur(playerBg, PlayerActivity.this);
                            }
                        }

                        @Override
                        public void onError() {
                            LogUtil.d(PLAYER_ACTIVITY_TAG, "Error");
                        }
                    });
                    playerIndex--;
                }
                break;
            case R.id.play_next:
                //下一首
                if (playerPresenter != null) {
                    playerPresenter.playNext();
                    playerIndex = playerViewPagerAdapter.index;
                    LogUtil.d(PLAYER_ACTIVITY_TAG, "playerIndex++  ===>  " + playerIndex);
                    playerTrack = playerTrackList.get(playerIndex);
                    Picasso.with(PlayerActivity.this).load(playerTrack.getCoverUrlLarge()).into(playerBg, new Callback() {
                        @Override
                        public void onSuccess() {
                            Drawable drawable = playerBg.getDrawable();
                            if (drawable != null) {
                                //到这里才说明是有图片的
                                FastBlurUtil.makeBlur(playerBg, PlayerActivity.this);
                            }
                        }

                        @Override
                        public void onError() {
                            LogUtil.d(PLAYER_ACTIVITY_TAG, "Error");
                        }
                    });
                }
                playerIndex++;
                break;
            case R.id.player_mode_switch_btn:
                //根据当前mode获取下一个mode
                XmPlayListControl.PlayMode playMode = playModeMap.get(currentMode);
                //修改播放模式
                if (playerPresenter != null) {
                    playerPresenter.switchPlayMode(playMode);
                    currentMode = playMode;
                    upDatePlayModeBtnImg();
                }
                break;
        }
    }

    /**
     * 根据当前状态，切换播放模式图标
     * 1、PLAY_MODEL_LIST 列表播放
     * 2、PLAY_MODEL_LIST_LOOP 列表循环
     * 3、PLAY_MODEL_RANDOM 随机播放
     * 4、PLAY_MODEL_SINGLE_LOOP 单曲循环
     */
    private void upDatePlayModeBtnImg() {
        switch (currentMode) {
            case PLAY_MODEL_LIST:
                playerModeSwitchBtn.setImageResource(R.drawable.menu_detail_w);
                break;
            case PLAY_MODEL_LIST_LOOP:
                playerModeSwitchBtn.setImageResource(R.drawable.mode_list);
                break;
            case PLAY_MODEL_RANDOM:
                playerModeSwitchBtn.setImageResource(R.drawable.mode_random);
                break;
            case PLAY_MODEL_SINGLE_LOOP:
                playerModeSwitchBtn.setImageResource(R.drawable.sing_loop);
                break;
        }
    }
}
