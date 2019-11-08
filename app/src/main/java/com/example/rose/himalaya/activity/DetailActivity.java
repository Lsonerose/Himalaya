package com.example.rose.himalaya.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rose.himalaya.R;
import com.example.rose.himalaya.base.BaseActivity;
import com.example.rose.himalaya.interfaces.IAlbumDetailViewCallback;
import com.example.rose.himalaya.presenters.AlbumDetailPresenter;
import com.example.rose.himalaya.utils.FastBlurUtil;
import com.example.rose.himalaya.utils.LogUtil;
import com.example.rose.himalaya.view.RoundRectImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

/**
 * Created by rose on 2019/11/7.
 */

public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallback {

    private static final String TAG = "DetailActivity";
    private ImageView ivLargeCover;
    private RoundRectImageView vivSmallCover;
    private TextView tvAlbumTitle;
    private TextView tvAlbumAuthor;

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
        AlbumDetailPresenter detailPresenter = AlbumDetailPresenter.getsIntance();
        detailPresenter.registerViewCallback(this);
    }

    private void initView() {
        ivLargeCover = findViewById(R.id.iv_large_cover);
        vivSmallCover = findViewById(R.id.viv_small_cover);
        tvAlbumTitle = findViewById(R.id.tv_album_title);
        tvAlbumAuthor = findViewById(R.id.tv_album_author);

    }

    @Override
    public void onDetailListLoaded(List<Track> tracks) {

    }

    @Override
    public void onAlbumLoaded(Album album) {
        //做毛玻璃效果
        if (ivLargeCover != null && null!= ivLargeCover) {
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

    }
}
