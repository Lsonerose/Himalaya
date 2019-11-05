package com.example.rose.himalaya.adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rose.himalaya.R;
import com.example.rose.himalaya.view.RoundRectImageView;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rose on 2019/11/5.
 */

public class RecommendRecycleViewAdapter extends RecyclerView.Adapter<RecommendRecycleViewAdapter.InnerHolder> {
    private static final String RecommendRecycleViewAdapterTAG = "RecommendRecycleViewAdapter";
   private List<Album> recommendRecycleViewData = new ArrayList<>();

    @Override
    public InnerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //这里是载入布局
        View recommendRecycleViewItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_recycleview,parent,false);
        return new InnerHolder(recommendRecycleViewItemView);
    }

    @Override
    public void onBindViewHolder(InnerHolder holder, int position) {
        //这里是设置数据
        holder.itemView.setTag(position);
        holder.setData(recommendRecycleViewData.get(position));

    }

    @Override
    public int getItemCount() {
        //  返回要显示的个数
        if (recommendRecycleViewData != null) {
                return recommendRecycleViewData.size();
        }
        return 0;
    }

    public void setData(List<Album> data) {
        if (recommendRecycleViewData != null) {
                recommendRecycleViewData.clear();
                recommendRecycleViewData.addAll(data);
        }
//        更新UI
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(View itemView) {
            super(itemView);
        }

        public void setData(Album data) {
//            this.data = data;
            /**
             * 找到各个控，并且设置数据
             * 1.recommendRvItemAlbumCover：专辑封面
             * 2.recommendRvItemAlbumTitleTv：专辑名称
             * 3.recommendRvItemAlbumBescriptionTv：专辑富文本简介
             * 4.recommendRvItemAlbumContentSize：播放数量
             * 5.recommendRvItemAlbumContentSize：专辑内容数量
             */
            RoundRectImageView recommendRvItemAlbumCover = itemView.findViewById(R.id.recommend_rv_item_album_cover);
            TextView recommendRvItemAlbumTitleTv = itemView.findViewById(R.id.recommend_rv_item_album_title_tv);
            TextView recommendRvItem_AlbumDescriptionTv = itemView.findViewById(R.id.recommend_rv_item_album_description_tv);
            TextView recommendRvItemAlbumPlayCount = itemView.findViewById(R.id.recommend_rv_item_album_play_count);
            TextView recommendRvItemAlbumContentSize = itemView.findViewById(R.id.recommend_rv_item_album_content_size);

            Glide.with(itemView.getContext()).load(data.getCoverUrlLarge()).into(recommendRvItemAlbumCover);
            recommendRvItemAlbumTitleTv.setText(data.getAlbumTitle());
            recommendRvItem_AlbumDescriptionTv.setText(data.getAlbumIntro());
            recommendRvItemAlbumPlayCount.setText(data.getPlayCount()+"");
            recommendRvItemAlbumContentSize.setText(data.getIncludeTrackCount()+"");
        }
    }

}
