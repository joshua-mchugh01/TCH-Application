package com.redrumming.thecreaturehub.video;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.redrumming.thecreaturehub.ContentItem;
import com.redrumming.thecreaturehub.R;
import com.redrumming.thecreaturehub.channel.Channel;
import com.redrumming.thecreaturehub.util.ImageLoaderUtil;
import com.redrumming.thecreaturehub.util.TimePassedUtil;

import java.util.List;

/**
 * Created by ME on 8/4/2015.
 */
public class VideoRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private VideoContainer container;
    private Context context = null;

    public VideoRecyclerAdapter(VideoContainer container){

        this.container = container;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == VideoItem.VIDEO_ITEM){

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.video_item, parent, false);

            context = parent.getContext();
            VideoViewHolder viewHolder = new VideoViewHolder(view);

            return viewHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ContentItem item = container.getVideos().get(position);

        if(item.getItemType() == VideoItem.VIDEO_ITEM){

            Video video = (Video) item;
            Channel channel = container.getChannel();
            VideoViewHolder viewHolder = (VideoViewHolder) holder;

            ImageLoaderUtil.get(context).displayImage(video.getThumbnailURL(), viewHolder.getVideoThumbnail());
            viewHolder.getVideoTitle().setText(video.getVideoTitle());
            viewHolder.getChannelIcon().setImageDrawable(channel.getDisplayIcon());
            viewHolder.getChannelName().setText(channel.getChannelName());
            viewHolder.getPushishedDate().setText(new TimePassedUtil().getTimeDifference(video.getPublishedDate()));
        }

    }

    @Override
    public int getItemCount() {
        return container.getVideos().size();
    }

    @Override
    public int getItemViewType(int position) {
        return container.getVideos().get(position).getItemType();
    }
}