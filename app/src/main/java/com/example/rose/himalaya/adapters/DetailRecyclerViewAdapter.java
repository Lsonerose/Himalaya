package com.example.rose.himalaya.adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.example.rose.himalaya.R;
import com.example.rose.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rose on 2019/11/11.
 */

public class DetailRecyclerViewAdapter extends RecyclerView.Adapter<DetailRecyclerViewAdapter.innerHolder> {
    private List<Track> detailData = new ArrayList<>();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//格式化日期
    private SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("mm:ss");//格式化时间
    private ItemClickListener mItemClickListener = null;

    public void setData(List<Track> data) {
        //清除原来的数据
        detailData.clear();
        //添加新数据
        detailData.addAll(data);
        //更新UI
        notifyDataSetChanged();
    }

    public class innerHolder extends RecyclerView.ViewHolder{

        public innerHolder(View itemView) {
            super(itemView);
        }
    }
    @Override
    public DetailRecyclerViewAdapter.innerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_recycleview,parent,false);
        return new innerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DetailRecyclerViewAdapter.innerHolder holder, int position) {
        //找到控件
        TextView orderText = holder.itemView.findViewById(R.id.order_text);//顺序ID
        TextView detailItemTitle = holder.itemView.findViewById(R.id.detail_item_title);//标题
        TextView detailPlayCount = holder.itemView.findViewById(R.id.detail_play_count);//播放次数
        TextView detailItemDuration = holder.itemView.findViewById(R.id.detail_item_duration);//时长
        TextView detailItemUpdateTime = holder.itemView.findViewById(R.id.detail_item_update_time);//更新时间

        //设置数据
        Track track = detailData.get(position);

        if (position<3){
            switch (position){
                case 0:
                    orderText.setTextColor(holder.itemView.getResources().getColor(R.color.detailTvOne));
                    break;
                case 1:
                    orderText.setTextColor(holder.itemView.getResources().getColor(R.color.detailTvTwo));
                    break;
                case 2:
                    orderText.setTextColor(holder.itemView.getResources().getColor(R.color.detailTvThree));
                    break;
            }
        }else {
            orderText.setTextColor(holder.itemView.getResources().getColor(R.color.detailTvOthre));
        }
        orderText.setText((position+1)+"");//顺序ID
        detailItemTitle.setText(track.getTrackTitle());//标题
        detailPlayCount.setText(track.getPlayCount()+"");//播放次数
        detailItemDuration.setText(simpleDateTimeFormat.format( track.getDuration()*1000));//时长
        detailItemUpdateTime.setText(simpleDateFormat.format(track.getUpdatedAt()));//更新时间

        //给Item设置点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:
               // Toast.makeText(view.getContext(),"点击了",Toast.LENGTH_SHORT).show();
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return detailData.size();
    }

    public void setItemClickListener(ItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public interface ItemClickListener{
        void onItemClick();
    }

}
