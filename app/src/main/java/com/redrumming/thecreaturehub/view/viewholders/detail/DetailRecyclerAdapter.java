package com.redrumming.thecreaturehub.view.viewholders.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.redrumming.thecreaturehub.R;
import com.redrumming.thecreaturehub.models.detail.DetailItem;
import com.redrumming.thecreaturehub.models.detail.channel.ChannelSectionItem;
import com.redrumming.thecreaturehub.view.viewholders.detail.channel.ChannelSectionViewHolder;
import com.redrumming.thecreaturehub.async.comments.CommentAsyncListener;
import com.redrumming.thecreaturehub.models.detail.comments.CommentContainer;
import com.redrumming.thecreaturehub.models.detail.comments.CommentItemType;
import com.redrumming.thecreaturehub.view.viewholders.detail.comments.header.CommentsHeaderViewHolder;
import com.redrumming.thecreaturehub.async.comments.reply.ReplyCommentAsync;
import com.redrumming.thecreaturehub.models.detail.comments.reply.ReplyCommentContainer;
import com.redrumming.thecreaturehub.models.detail.comments.reply.ReplyCommentItem;
import com.redrumming.thecreaturehub.view.viewholders.detail.comments.reply.ReplyCommentView;
import com.redrumming.thecreaturehub.view.viewholders.detail.comments.NoCommentsViewHolder;
import com.redrumming.thecreaturehub.models.detail.comments.top.TopLevelCommentItem;
import com.redrumming.thecreaturehub.view.viewholders.detail.comments.TopLevelCommentLoadMoreViewHolder;
import com.redrumming.thecreaturehub.view.viewholders.detail.comments.TopLevelCommentLoadingViewHolder;
import com.redrumming.thecreaturehub.view.viewholders.detail.comments.top.TopLevelCommentViewHolder;
import com.redrumming.thecreaturehub.models.detail.description.DescriptionItem;
import com.redrumming.thecreaturehub.view.viewholders.detail.description.DescriptionViewHolder;
import com.redrumming.thecreaturehub.util.CategoryFormatter;
import com.redrumming.thecreaturehub.util.LicenseFormatter;
import com.redrumming.thecreaturehub.util.NumberFormatterUtil;
import com.redrumming.thecreaturehub.util.TimePassedUtil;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ME on 12/5/2015.
 */
public class DetailRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements CommentAsyncListener {

    private List<DetailItem> items;
    private Context context;
    private DetailRecyclerAdapterListener listener;

    public DetailRecyclerAdapter(List<DetailItem> items, DetailRecyclerAdapterListener listener) {

        this.items = items;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == DetailItem.DESCRIPTION_ITEM){

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_description, parent, false);
            DescriptionViewHolder viewHolder = new DescriptionViewHolder(view);

            return viewHolder;

        }else if(viewType == DetailItem.CHANNEL_SECTION_ITEM){

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel_section, parent, false);
            ChannelSectionViewHolder viewHolder = new ChannelSectionViewHolder(view);

            return viewHolder;

        }else if(viewType == DetailItem.COMMENT_HEADER_ITEM){

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_header, parent, false);
            CommentsHeaderViewHolder viewHolder = new CommentsHeaderViewHolder(view);

            return viewHolder;

        }else if(viewType == DetailItem.COMMENT_TOP_LEVEL_ITEM){

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_level_comment, parent, false);
            context = parent.getContext();
            TopLevelCommentViewHolder viewHolder = new TopLevelCommentViewHolder(view);

            return viewHolder;

        }else if(viewType == DetailItem.COMMENT_TOP_LEVEL_LOADING){

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_comment_loading, parent, false);
            TopLevelCommentLoadingViewHolder viewHolder = new TopLevelCommentLoadingViewHolder(view);

            return viewHolder;

        }else if(viewType == DetailItem.COMMENT_TOP_LEVEL_LOAD_MORE_ITEM){

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_level_comments_load_more, parent, false);
            TopLevelCommentLoadMoreViewHolder viewHolder = new TopLevelCommentLoadMoreViewHolder(view);

            return viewHolder;

        }else if(viewType == DetailItem.COMMENT_NO_COMMENTS_FOUND){

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_not_found, parent, false);
            NoCommentsViewHolder viewHolder = new NoCommentsViewHolder(view);

            return viewHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        DetailItem item = items.get(position);

        if(item.getType() == DetailItem.DESCRIPTION_ITEM){

            DescriptionItem descriptionItem = (DescriptionItem) item;
            DescriptionViewHolder viewHolder = (DescriptionViewHolder) holder;
            bindDescriptionItem(descriptionItem, viewHolder);

        }else if(item.getType() == DetailItem.CHANNEL_SECTION_ITEM){

            ChannelSectionItem channelSectionItem = (ChannelSectionItem) item;
            ChannelSectionViewHolder viewHolder = (ChannelSectionViewHolder) holder;
            bindChannelSectionItem(channelSectionItem, viewHolder);

        }else if(item.getType() == DetailItem.COMMENT_TOP_LEVEL_ITEM){

            TopLevelCommentItem topLevelCommentItem = (TopLevelCommentItem) item;
            TopLevelCommentViewHolder viewHolder = (TopLevelCommentViewHolder) holder;
            viewHolder.setIsRecyclable(false);
            bindTopLevelCommentItem(topLevelCommentItem, viewHolder);

        }else if(item.getType() == DetailItem.COMMENT_TOP_LEVEL_LOAD_MORE_ITEM){

            TopLevelCommentLoadMoreViewHolder viewHolder = (TopLevelCommentLoadMoreViewHolder) holder;
            bindTopLevelLoadMoreItem(viewHolder);
        }
    }

    @Override
    public int getItemViewType(int position) {

        return items.get(position).getType();
    }

    @Override
    public int getItemCount() {

        return items.size();
    }

    private void bindDescriptionItem(DescriptionItem descriptionItem, final DescriptionViewHolder viewHolder){

        viewHolder.getHeaderContainer().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (viewHolder.getContentContainer().getVisibility() == View.GONE) {

                    viewHolder.getContentExpander().setImageResource(R.drawable.expanded_arrow);
                    viewHolder.getContentContainer().setVisibility(View.VISIBLE);

                } else {

                    viewHolder.getContentExpander().setImageResource(R.drawable.expand_arrow);
                    viewHolder.getContentContainer().setVisibility(View.GONE);
                }
            }
        });

        viewHolder.getTitle().setText(descriptionItem.getVideoItem().getTitle());

        String viewCount = NumberFormatterUtil.formatViewCount(descriptionItem.getVideoItem().getViewCount());
        viewHolder.getViewCount().setText(viewCount);

        String date = "Published on " + new SimpleDateFormat("LLL d, yyyy", Locale.getDefault()).format(new Date(descriptionItem.getVideoItem().getPublishedAt()));
        viewHolder.getPublishedAt().setText(date);
        viewHolder.getDescription().setText(descriptionItem.getVideoItem().getDescription());

        String categoryValue = CategoryFormatter.formatCategory(descriptionItem.getVideoItem().getCategoryId());
        viewHolder.getCategoryValue().setText(categoryValue);

        String licenseValueText = LicenseFormatter.formatLicense(descriptionItem.getVideoItem().getLicense());
        viewHolder.getLicenseValue().setText(licenseValueText);

        String likeCount = NumberFormatterUtil.formatLikeCount(descriptionItem.getVideoItem().getLikeCount());
        viewHolder.getLikeCount().setText(likeCount);

        String dislikeCount = NumberFormatterUtil.formatLikeCount(descriptionItem.getVideoItem().getDislikeCount());
        viewHolder.getDislikeCount().setText(dislikeCount);
    }

    private void bindChannelSectionItem(ChannelSectionItem channelSectionItem, ChannelSectionViewHolder viewHolder){

        viewHolder.getChannelIcon().setImageBitmap(channelSectionItem.getChannelItem().getDisplayIcon());
        viewHolder.getChannelName().setText(channelSectionItem.getChannelItem().getChannelName());

        String subscriberCount = NumberFormatterUtil.formatSubscriberCount(channelSectionItem.getChannelItem().getSubscriberCount());
        viewHolder.getSubscriberCount().setText(subscriberCount);
    }

    private void bindTopLevelCommentItem(final TopLevelCommentItem topLevelCommentItem, final TopLevelCommentViewHolder viewHolder){

        Picasso.with(context)
                .load(topLevelCommentItem.getProfileImageURL())
                .error(R.drawable.display_user_profile_image_default)
                .noFade()
                .into(viewHolder.getProfileImage());

        viewHolder.getProfileName().setText(topLevelCommentItem.getDisplayName());
        viewHolder.getComment().setText(topLevelCommentItem.getTextDisplay());

        String publishedAt = TimePassedUtil.getTimeDifference(topLevelCommentItem.getPublishedAt());
        viewHolder.getPublishedAt().setText(publishedAt);

        if(topLevelCommentItem.hasReplies()){

            if(topLevelCommentItem.getTotalReplyCount() > topLevelCommentItem.getReplies().getCommentItems().size()){

                viewHolder.getLoadMoreRepliesContainer().setVisibility(View.VISIBLE);
                viewHolder.getLoadMoreRepliesContainer().setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        ReplyCommentAsync async = new ReplyCommentAsync(context, DetailRecyclerAdapter.this);
                        async.execute(topLevelCommentItem.getReplies());

                        viewHolder.getLoadMoreRepliesText().setVisibility(View.INVISIBLE);
                        viewHolder.getLoadMoreRepliesArrowImage().setVisibility(View.INVISIBLE);
                        viewHolder.getLoadMoreRepliesProgressBar().setVisibility(View.VISIBLE);
                    }
                });
            }

            viewHolder.getRepliesContainer().setVisibility(View.VISIBLE);

            for(int i = viewHolder.getRepliesContainer().getChildCount(); i < topLevelCommentItem.getReplies().getCommentItems().size(); i++){

                CommentItemType commentItemType = topLevelCommentItem.getReplies().getCommentItems().get(i);

                if(commentItemType.getItemType() == CommentItemType.REPLY_COMMENT){

                    ReplyCommentItem replyCommentItem = (ReplyCommentItem) commentItemType;

                    viewHolder.getRepliesContainer().addView(new ReplyCommentView(context, replyCommentItem), 0);
                }
            }
        }
    }

    private void bindTopLevelLoadMoreItem(final TopLevelCommentLoadMoreViewHolder viewHolder){

        viewHolder.getLoadMoreText().setVisibility(View.VISIBLE);
        viewHolder.getLoadMoreProgressBar().setVisibility(View.INVISIBLE);

        viewHolder.getLoadMoreWrapper().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                viewHolder.getLoadMoreText().setVisibility(View.INVISIBLE);
                viewHolder.getLoadMoreProgressBar().setVisibility(View.VISIBLE);

                listener.onLoadMoreCommentsClick();

                viewHolder.getLoadMoreWrapper().setOnClickListener(null);
            }
        });
    }

    @Override
    public void onSuccess(CommentContainer container) {

        if(container.getCommentItems() != null || container.getCommentItems().size() > 0){

            ReplyCommentContainer replyCommentContainer = (ReplyCommentContainer) container;

            if(replyCommentContainer.getParentId() != null || replyCommentContainer.getParentId().isEmpty() == false){

                updateParentLevelComment(replyCommentContainer);
            }
        }
    }

    private void updateParentLevelComment(ReplyCommentContainer replyCommentContainer){

        for(int i = 0; i < items.size(); i++){

            DetailItem detailItem = items.get(i);

            if(detailItem.getType() == DetailItem.COMMENT_TOP_LEVEL_ITEM){

                TopLevelCommentItem topLevelCommentItem = (TopLevelCommentItem) detailItem;

                if(topLevelCommentItem.getId().equals(replyCommentContainer.getParentId())){

                    topLevelCommentItem.getReplies().getCommentItems().addAll(replyCommentContainer.getCommentItems());
                    topLevelCommentItem.getReplies().setPageToken(replyCommentContainer.getPageToken());
                }
            }
        }

        this.notifyDataSetChanged();
    }
}