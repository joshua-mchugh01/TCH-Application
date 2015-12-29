package com.redrumming.thecreaturehub.contentItems.playlist;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.redrumming.thecreaturehub.contentItems.ContentType;
import com.redrumming.thecreaturehub.contentItems.ContentRecyclerAdapter;
import com.redrumming.thecreaturehub.R;
import com.redrumming.thecreaturehub.channel.ChannelItem;
import com.squareup.picasso.Picasso;

import java.util.Date;

/**
 * Created by ME on 8/6/2015.
 */
public class PlaylistRecyclerAdapter extends ContentRecyclerAdapter{

    public PlaylistRecyclerAdapter(PlaylistContainer container){
        super(container);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == ContentType.PLAYLIST_ITEM){

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_playlist, parent, false);

            super.setContext(parent.getContext());
            PlaylistViewHolder viewHolder = new PlaylistViewHolder(view);

            return viewHolder;

        }

        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ContentType item = super.getContainer().getItems().get(position);

        if(item.getItemType() == ContentType.PLAYLIST_ITEM){

            PlaylistItem playlistItem = (PlaylistItem) item;
            ChannelItem channelItem = super.getContainer().getChannelItem();
            PlaylistViewHolder viewHolder = (PlaylistViewHolder) holder;

            Picasso.with(super.getContext()).load(playlistItem.getThumbnailURL()).into(viewHolder.getThumbnail());
            viewHolder.getTitle().setText(playlistItem.getTitle());
            viewHolder.getChannelIcon().setImageBitmap(channelItem.getDisplayIcon());

            String numberOfVideos = playlistItem.getVideoCount() > 1 ? playlistItem.getVideoCount() + " videos" : playlistItem.getVideoCount() + " video";
            viewHolder.getNumberOfVideos().setText(numberOfVideos);

            viewHolder.getPublishDate().setText(DateUtils.getRelativeTimeSpanString(playlistItem.getPublishedAt(), new Date().getTime(), DateUtils.WEEK_IN_MILLIS));
        }
    }
}
