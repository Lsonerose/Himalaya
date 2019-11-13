package com.example.rose.himalaya.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rose.himalaya.R;
import com.example.rose.himalaya.adapters.DetailRecyclerViewAdapter;
import com.example.rose.himalaya.base.BaseActivity;
import com.example.rose.himalaya.interfaces.IAlbumDetailViewCallback;
import com.example.rose.himalaya.presenters.AlbumDetailPresenter;
import com.example.rose.himalaya.presenters.PlayerPresenter;
import com.example.rose.himalaya.utils.FastBlurUtil;
import com.example.rose.himalaya.utils.LogUtil;
import com.example.rose.himalaya.view.RoundRectImageView;
import com.example.rose.himalaya.view.UILoader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

/**
 * Created by rose on 2019/11/7.
 */

public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallback, View.OnClickListener ,UILoader.OnRetryClickListener, DetailRecyclerViewAdapter.ItemClickListener {

    private static final String TAG = "DetailActivity";
    private ImageView ivLargeCover;
    private RoundRectImageView vivSmallCover;
    private TextView tvAlbumTitle;
    private TextView tvAlbumAuthor;
    private AlbumDetailPresenter detailPresenter;
    private int currentPage = 1;
    private long currentId = -1;
    private RecyclerView detailRv;
    private DetailRecyclerViewAdapter detailRecyclerViewAdapter;
    private FrameLayout detailListContainer;
    private UILoader detailUiLoader;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //设置状态栏为透明
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        //初始化控件
        initView();
        //拿到数据
        detailPresenter = AlbumDetailPresenter.getsIntance();
        detailPresenter.registerViewCallback(this);

    }

    private void initView() {
        ivLargeCover = findViewById(R.id.iv_large_cover);
        vivSmallCover = findViewById(R.id.viv_small_cover);
        tvAlbumTitle = findViewById(R.id.tv_album_title);
        tvAlbumAuthor = findViewById(R.id.tv_album_author);

        detailListContainer = findViewById(R.id.detail_list_container);
        if (detailUiLoader == null) {
            detailUiLoader = new UILoader(this) {
                @Override
                protected View getSuccessView(ViewGroup container) {
                    return createSuccessView(container);
                }
            };
            detailListContainer.removeAllViews();//添加之前先干掉之前的
            detailListContainer.addView(detailUiLoader);//把UILoader添加进去
            detailUiLoader.setOnRetryClickListenter(DetailActivity.this);
        }

    }

    private View createSuccessView(ViewGroup container) {
        View detailListView = LayoutInflater.from(this).inflate(R.layout.item_detail_list, container, false);
        detailRv = detailListView.findViewById(R.id.detail_rv);
        //1.设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        detailRv.setLayoutManager(linearLayoutManager);
        //2.设置适配器
        detailRecyclerViewAdapter = new DetailRecyclerViewAdapter();
        detailRv.setAdapter(detailRecyclerViewAdapter);
        detailRecyclerViewAdapter.setItemClickListener(this);
        return detailListView;
    }

    @Override
    public void onDetailListLoaded(List<Track> tracks) {
        //判断数据结果，根据结果控制UI显示
        if (tracks == null || tracks.size() == 0) {
            if (detailUiLoader != null) {
                detailUiLoader.updateStatus(UILoader.UIStatus.EMPTY);
            }
        }
        if (detailUiLoader != null) {
            detailUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);
        }
        //更新||设置UI数据
        detailRecyclerViewAdapter.setData(tracks);
    }

    @Override
    public void onAlbumLoaded(Album album) {
        long id = album.getId();
        currentId = id;
        //拿数据，显示Loading状态
        if (detailUiLoader != null) {
            detailUiLoader.updateStatus(UILoader.UIStatus.LOADING);
        }
        //做毛玻璃效果
        if (ivLargeCover != null && null != ivLargeCover) {
            Picasso.with(this).load(album.getCoverUrlLarge()).into(ivLargeCover, new Callback() {
                @Override
                public void onSuccess() {
                    Drawable drawable = ivLargeCover.getDrawable();
                    if (drawable != null) {
                        //到这里才说明是有图片的
                        FastBlurUtil.makeBlur(ivLargeCover, DetailActivity.this);
                    }
                }

                @Override
                public void onError() {
                    LogUtil.d(TAG, "onError");
                }
            });
        }
        if (vivSmallCover != null) {
            Glide.with(this).load(album.getCoverUrlLarge()).into(vivSmallCover);
        }
        if (tvAlbumTitle != null) {
            tvAlbumTitle.setText(album.getAlbumTitle());
        }
        if (tvAlbumAuthor != null) {
            tvAlbumAuthor.setText(album.getAnnouncer().getNickname());
        }
        //获取专辑详细内容
        detailPresenter.getAlbumdetail((int) album.getId(), currentPage);
    }

    @Override
    public void onNetworkError(int i, String s) {
        //请求发生错误，显示网络异常
        detailUiLoader.updateStatus(UILoader.UIStatus.NEWTWORK_ERROR);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onRetryClick() {
        //这里面表示用户网络不佳的时候 去点击了重新加载
        if (detailPresenter != null) {
            detailPresenter.getAlbumdetail((int) currentId,currentPage);
        }
    }

    @Override
    public void onItemClick(List<Track> detailData, int position) {
        //设置播放数据
        PlayerPresenter playerPresenter = PlayerPresenter.getPlayerPresenter();
         playerPresenter.setPlayList(detailData,position);
        //跳转到播放器界面
        Intent intent = new Intent(this,PlayerActivity.class);
        startActivity(intent);
    }
}
