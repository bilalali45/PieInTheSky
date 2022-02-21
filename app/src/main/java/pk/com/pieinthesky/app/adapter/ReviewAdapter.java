package pk.com.pieinthesky.app.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import pk.com.pieinthesky.app.Configuration;
import pk.com.pieinthesky.app.R;
import pk.com.pieinthesky.app.beans.PlaceReview;
import pk.com.pieinthesky.app.helper.CommonUtils;
import pk.com.pieinthesky.app.widget.IconButton;
import pk.com.pieinthesky.app.widget.CircleImageView;

import java.util.List;

public class ReviewAdapter extends BaseAdapter {
    Context context;
    private List<PlaceReview> placeReviewList;
    private OnItemClickListener onItemClickListener;

    public ReviewAdapter(Context context, List<PlaceReview> placeReviewList) {
        this.context = context;
        this.placeReviewList = placeReviewList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public int getCount() {
        return placeReviewList.size();
    }

    @Override
    public Object getItem(int position) {
        return placeReviewList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.review_row, null);

            LinearLayout lyUserRow = convertView.findViewById(R.id.lyUserRow);
            TextView btnFollow = convertView.findViewById(R.id.btnFollow);
            IconButton btnLike = convertView.findViewById(R.id.btnLike);
            IconButton btnComment = convertView.findViewById(R.id.btnComment);
            CircleImageView ivUserPhoto = convertView.findViewById(R.id.ivUserPhoto);
            TextView tvFullName = convertView.findViewById(R.id.tvFullName);
            TextView tvSubDetail = convertView.findViewById(R.id.tvSubDetail);
            AppCompatRatingBar rbPlaceRating = convertView.findViewById(R.id.rbPlaceRating);
            TextView tvReviewDate = convertView.findViewById(R.id.tvReviewDate);
            TextView tvReviewText = convertView.findViewById(R.id.tvReviewText);
            TextView tvReviewLikeComment = convertView.findViewById(R.id.tvReviewLikeComment);

            viewHolder = new ViewHolder(lyUserRow, btnFollow, btnLike, btnComment, ivUserPhoto, tvFullName, tvSubDetail, rbPlaceRating, tvReviewDate, tvReviewText, tvReviewLikeComment);
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

            viewHolder.btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onUserFollowClick(view, position, view.getId(), viewHolder);
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
                .load(placeReviewList.get(position).getProfileImageURL())
                .centerCrop()
                .placeholder(R.drawable.image_slider_loading)
                .into(viewHolder.ivUserPhoto);

        if (placeReviewList.get(position).isLike()) {
            viewHolder.btnLike.setTextColor(context.getResources().getColor(R.color.colorLike));
            viewHolder.btnLike.setText("Liked");
        } else {
            viewHolder.btnLike.setTextColor(context.getResources().getColor(R.color.colorNotLike));
            viewHolder.btnLike.setText("Like");
        }
        if (placeReviewList.get(position).getUserId().equals(Configuration.getUser().getUserId())) {
            viewHolder.btnFollow.setVisibility(View.GONE);
        } else {
            viewHolder.btnFollow.setVisibility(View.VISIBLE);
        }

        if (placeReviewList.get(position).isFollow()) {
            viewHolder.btnFollow.setText("followed");
            viewHolder.btnFollow.setBackgroundResource(R.drawable.button_user_follow_fill);
            viewHolder.btnFollow.setTextColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            viewHolder.btnFollow.setText("follow");
            viewHolder.btnFollow.setBackgroundResource(R.drawable.button_user_follow);
            viewHolder.btnFollow.setTextColor(context.getResources().getColor(R.color.colorFollow));
        }

        viewHolder.tvFullName.setText(placeReviewList.get(position).getFullName());
        viewHolder.tvSubDetail.setText(placeReviewList.get(position).getSubDetail());
        viewHolder.rbPlaceRating.setRating(CommonUtils.parseFloat(placeReviewList.get(position).getBusinessAvgRating()));
        viewHolder.tvReviewDate.setText(placeReviewList.get(position).getReviewDays());
        viewHolder.tvReviewText.setText(placeReviewList.get(position).getReviewText());
        viewHolder.tvReviewLikeComment.setText(placeReviewList.get(position).getTotalLike() + " Likes, " + placeReviewList.get(position).getTotalComments() + " Comments");

        return convertView;
    }


    public class ViewHolder {
        public LinearLayout lyUserRow;
        public TextView btnFollow;
        public IconButton btnLike;
        public IconButton btnComment;
        public CircleImageView ivUserPhoto;
        public TextView tvFullName;
        public TextView tvSubDetail;
        public AppCompatRatingBar rbPlaceRating;
        public TextView tvReviewDate;
        public TextView tvReviewText;
        public TextView tvReviewLikeComment;

        public ViewHolder(LinearLayout lyUserRow, TextView btnFollow, IconButton btnLike, IconButton btnComment, CircleImageView ivUserPhoto, TextView tvFullName, TextView tvSubDetail, AppCompatRatingBar rbPlaceRating, TextView tvReviewDate, TextView tvReviewText, TextView tvReviewLikeComment) {
            this.lyUserRow = lyUserRow;
            this.btnFollow = btnFollow;
            this.btnLike = btnLike;
            this.btnComment = btnComment;
            this.ivUserPhoto = ivUserPhoto;
            this.tvFullName = tvFullName;
            this.tvSubDetail = tvSubDetail;
            this.rbPlaceRating = rbPlaceRating;
            this.tvReviewDate = tvReviewDate;
            this.tvReviewText = tvReviewText;
            this.tvReviewLikeComment = tvReviewLikeComment;
        }
    }

    public interface OnItemClickListener {
        public void onUserClick(View view, int position, long id, ViewHolder viewHolder);

        public void onUserFollowClick(View view, int position, long id, ViewHolder viewHolder);

        public void onUserLikeClick(View view, int position, long id, ViewHolder viewHolder);

        public void onUserCommentClick(View view, int position, long id, ViewHolder viewHolder);
    }


}