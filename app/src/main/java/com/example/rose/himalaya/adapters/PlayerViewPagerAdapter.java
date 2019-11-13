package com.example.rose.himalaya.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rose.himalaya.R;
import com.example.rose.himalaya.view.RoundRectImageView;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rose on 2019/11/13.
 */

public class PlayerViewPagerAdapter extends PagerAdapter{
    public List<Track> data = new ArrayList<>();
    public static int index = 0 ;
    private View itemView;

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setData(List<Track> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        this.index = position;

        itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_player_viewpager,container,false);
        container.addView(itemView);
        //设置数据
        //找到控件
        RoundRectImageView itemPlayerVpRrimg = itemView.findViewById(R.id.item_player_vp_rrimg);
        //设置图片
        Track track = data.get(position);
        String coverUrlLarge = track.getCoverUrlLarge();
        Picasso.with(container.getContext()).load(coverUrlLarge).into(itemPlayerVpRrimg);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
