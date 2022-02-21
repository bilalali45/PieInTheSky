package pk.com.pieinthesky.app.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import pk.com.pieinthesky.app.R;
import pk.com.pieinthesky.app.beans.TimelineDetail;
import pk.com.pieinthesky.app.widget.IconButton;

import java.util.List;

public class TimelineAdapter extends BaseAdapter {
    Context context;
    private List<TimelineDetail> timelineList;
    private OnItemClickListener onItemClickListener;

    public TimelineAdapter(Context context, List<TimelineDetail> timelineList) {
        this.context = context;
        this.timelineList = timelineList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public int getCount() {
        return timelineList.size();
    }

    @Override
    public Object getItem(int position) {
        return timelineList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.timeline_row, null);

            LinearLayout lyPlaceRow = convertView.findViewById(R.id.lyPlaceRow);
            IconButton btnLike = convertView.findViewById(R.id.btnLike);
            IconButton btnComment = convertView.findViewById(R.id.btnComment);
            TextView tvCategoryName = convertView.findViewById(R.id.tvCategoryName);
            ImageView ivPlaceImage = convertView.findViewById(R.id.ivPlaceImage);
            TextView tvPlaceName = convertView.findViewById(R.id.tvPlaceName);
            TextView tvPlaceSubTitle = convertView.findViewById(R.id.tvPlaceSubTitle);
            AppCompatRatingBar rbPlaceRating = convertView.findViewById(R.id.rbPlaceRating);
            TextView tvReviewDate = convertView.findViewById(R.id.tvReviewDate);
            TextView tvReviewText = convertView.findViewById(R.id.tvReviewText);
            TextView tvReviewLikeComment = convertView.findViewById(R.id.tvReviewLikeComment);

            viewHolder = new ViewHolder(lyPlaceRow, btnLike, btnComment, tvCategoryName, ivPlaceImage, tvPlaceName, tvPlaceSubTitle, rbPlaceRating, tvReviewDate, tvReviewText, tvReviewLikeComment);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (onItemClickListener != null) {

            viewHolder.lyUserRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onItemClickListener.onUserClick(view, position, view.getId(), viewHolder);
                }
            });

            viewHolder.btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onUserLikeClick(view, position, view.getId(), viewHolder);
                }
            });

            viewHolder.btnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onUserCommentClick(view, position, view.getId(), viewHolder);
                }
            });
        }

        Glide.with(context)
                .load(timelineList.get(position).getPlaceProfileImageURL())
                .centerCrop()
                .placeholder(R.drawable.image_place_loading)
                .into(viewHolder.ivPlaceImage);

        if (timelineList.get(position).isLike()) {
            viewHolder.btnLike.setTextColor(context.getResources().getColor(R.color.colorLike));
            viewHolder.btnLike.setText("Liked");
        } else {
            viewHolder.btnLike.setTextColor(context.getResources().getColor(R.color.colorNotLike));
            viewHolder.btnLike.setText("Like");
        }

        viewHolder.tvCategoryName.setText(timelineList.get(position).getCategoryName());
        viewHolder.tvPlaceName.setText(timelineList.get(position).getPlaceName());
        viewHolder.tvPlaceSubTitle.setText(timelineList.get(position).getPlaceLocation());
        viewHolder.rbPlaceRating.setRating(timelineList.get(position).getBusinessAvgRating());
        if (timelineList.get(position).getCategoryName() == null || timelineList.get(position).getCategoryName().equals("Reviews")) {
            viewHolder.tvReviewDate.setVisibility(View.VISIBLE);
            viewHolder.tvReviewText.setVisibility(View.VISIBLE);
            viewHolder.tvReviewLikeComment.setVisibility(View.VISIBLE);
            viewHolder.btnLike.setVisibility(View.VISIBLE);
            viewHolder.btnComment.setVisibility(View.VISIBLE);

            viewHolder.tvReviewDate.setText(timelineList.get(position).getReviewDays());
            viewHolder.tvReviewText.setText(timelineList.get(position).getReviewText());
            viewHolder.tvReviewLikeComment.setText(timelineList.get(position).getTotalLike() + " Likes, " + timelineList.get(position).getTotalComments() + " Comments");
        } else {
            viewHolder.tvReviewDate.setVisibility(View.GONE);
            viewHolder.tvReviewText.setVisibility(View.GONE);
            viewHolder.tvReviewLikeComment.setVisibility(View.GONE);
            viewHolder.btnLike.setVisibility(View.GONE);
            viewHolder.btnComment.setVisibility(View.GONE);

        }
        return convertView;
    }


    public class ViewHolder {
        public LinearLayout lyUserRow;
        public IconButton btnLike;
        public IconButton btnComment;
        public TextView tvCategoryName;
        public ImageView ivPlaceImage;
        public TextView tvPlaceName;
        public TextView tvPlaceSubTitle;
        public AppCompatRatingBar rbPlaceRating;
        public TextView tvReviewDate;
        public TextView tvReviewText;
        public TextView tvReviewLikeComment;

        public ViewHolder(LinearLayout lyUserRow, IconButton btnLike, IconButton btnComment, TextView tvCategoryName, ImageView ivPlaceImage, TextView tvPlaceName, TextView tvPlaceSubTitle, AppCompatRatingBar rbPlaceRating, TextView tvReviewDate, TextView tvReviewText, TextView tvReviewLikeComment) {

            this.lyUserRow = lyUserRow;
            this.btnLike = btnLike;
            this.btnComment = btnComment;
            this.tvCategoryName = tvCategoryName;
            this.ivPlaceImage = ivPlaceImage;
            this.tvPlaceName = tvPlaceName;
            this.tvPlaceSubTitle = tvPlaceSubTitle;
            this.rbPlaceRating = rbPlaceRating;
            this.tvReviewDate = tvReviewDate;
            this.tvReviewText = tvReviewText;
            this.tvReviewLikeComment = tvReviewLikeComment;
        }
    }

    public interface OnItemClickListener {
        public void onUserClick(View view, int position, long id, ViewHolder viewHolder);

        public void onUserLikeClick(View view, int position, long id, ViewHolder viewHolder);

        public void onUserCommentClick(View view, int position, long id, ViewHolder viewHolder);
    }


}

