package com.redrumming.thecreaturehub.contentItems.playlist;

import android.content.Context;
import android.util.Log;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import com.google.api.services.youtube.model.Playlist;
import com.redrumming.thecreaturehub.YouTubeApiKey;
import com.redrumming.thecreaturehub.contentItems.ContentAsync;
import com.redrumming.thecreaturehub.contentItems.ContentAsyncListener;
import com.redrumming.thecreaturehub.contentItems.ContentContainer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by RedRumming on 7/20/2015.
 */
public class PlaylistAsync extends ContentAsync{

    public PlaylistAsync(Context context, ContentAsyncListener listener){
        super(context, listener);
    }

    @Override
    protected PlaylistContainer doInBackground(ContentContainer... container) {

        PlaylistContainer updateContainer = new PlaylistContainer();
        updateContainer.setChannel(container[0].getChannel());
        updateContainer.setPageToken(container[0].getPageToken());

        updateContainer = getPlaylists(updateContainer);

        return updateContainer;
    }

    @Override
    protected void onPostExecute(ContentContainer container) {
        super.onPostExecute(container);

        String className = this.getClass().getName();
        for(int i = 0; i < container.getItems().size(); i++){

            PlaylistItem playlist = (PlaylistItem) container.getItems().get(i);

            Log.d(className, "Playlist Id: " + playlist.getId());
            Log.d(className, "Playlist Title: " + playlist.getTitle());
            Log.d(className, "Playlist Thumbnail URL: " + playlist.getThumbnailURL());
            Log.d(className, "Playlist Published At: " + new Date(playlist.getPublishedDate()));
            Log.d(className, "Playlist Public: " + playlist.isViewable());
            Log.d(className, "Playlist Item Count: " + playlist.getVideoCount());
        }

        Log.d(className, "Next Page Token: " + container.getPageToken());
    }

    private PlaylistContainer getPlaylists(PlaylistContainer container){

        List<Playlist> searchResultList = new ArrayList<Playlist>();

        try{

            YouTube.Playlists.List playlist = getSearchParams(super.getYoutube(), container);

            PlaylistListResponse searchResponse = playlist.execute();
            List<Playlist> searchItems  = searchResponse.getItems();
            searchResultList.addAll(searchItems);

            container.setPageToken(searchResponse.getNextPageToken());
            container = createPlistItems(searchResultList, container);


        }catch(Exception e){

            Log.e(this.getClass().getCanonicalName(), "Error retrieve search results: " + e);
        }

        return container;
    }

    private YouTube.Playlists.List getSearchParams(YouTube youtube, PlaylistContainer container) throws Exception{

        String searchCategories = "id, snippet, contentDetails, status";
        YouTube.Playlists.List playlist = youtube.playlists().list(searchCategories);

        playlist.setKey(YouTubeApiKey.API_KEY);
        playlist.setChannelId(container.getChannel().getChannelId());
        playlist.setPageToken(container.getPageToken());

        String searchFields = "items(" +
                "id," +
                "snippet/title," +
                "snippet/thumbnails/medium/url," +
                "snippet/publishedAt," +
                "status/privacyStatus," +
                "contentDetails/itemCount), nextPageToken";
        playlist.setFields(searchFields);

        playlist.setMaxResults(super.getMaxResults());

        playlist.setPageToken(container.getPageToken());

        return playlist;
    }

    private PlaylistContainer createPlistItems (List<Playlist> searchResults, PlaylistContainer container){

        for(int i = 0; i < searchResults.size(); i++){

            Playlist playlist = searchResults.get(i);
            PlaylistItem playlistWrapper = new PlaylistItem();

            playlistWrapper.setId(playlist.getId());
            playlistWrapper.setTitle(playlist.getSnippet().getTitle());
            playlistWrapper.setThumbnailURL(playlist.getSnippet().getThumbnails().getMedium().getUrl());
            playlistWrapper.setPublishedDate(playlist.getSnippet().getPublishedAt().getValue());
            playlistWrapper.setVideoCount(playlist.getContentDetails().getItemCount());
            playlistWrapper.setViewable(playlist.getStatus().getPrivacyStatus().equalsIgnoreCase("public"));

            container.getItems().add(playlistWrapper);
        }

        return container;
    }
}