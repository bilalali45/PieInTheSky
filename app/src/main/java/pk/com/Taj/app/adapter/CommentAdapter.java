package pk.com.Taj.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import pk.com.Taj.app.R;
import pk.com.Taj.app.beans.ReviewComment;
import pk.com.Taj.app.widget.CircleImageView;

import java.util.List;

public  class CommentAdapter extends BaseAdapter {
    Context context;
    private List<ReviewComment> reviewCommentList;
    private OnItemClickListener onItemClickListener;

    public CommentAdapter(Context context, List<ReviewComment> reviewCommentList) {
        this.context = context;
        this.reviewCommentList=reviewCommentList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public int getCount() {
        return reviewCommentList.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewCommentList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.comment_row, null);

            LinearLayout lyUserRow = convertView.findViewById(R.id.lyUserRow);
            CircleImageView ivUserPhoto = convertView.findViewById(R.id.ivUserPhoto);
            TextView tvFullName = convertView.findViewById(R.id.tvFullName);
            TextView tvCommentDate = convertView.findViewById(R.id.tvCommentDate);
            TextView tvCommentText = convertView.findViewById(R.id.tvCommentText);

            viewHolder = new ViewHolder(lyUserRow, ivUserPhoto, tvFullName, tvCommentDate,tvCommentText);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(onItemClickListener!=null) {
            viewHolder.lyUserRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onItemClickListener.onUserClick(view,position,view.getId(),viewHolder);
                }
            });
        }

        Glide.with(context)
                .load(reviewCommentList.get(position).getProfileImageURL())
                .centerCrop()
                .placeholder(R.drawable.image_slider_loading)
                .into(viewHolder.ivUserPhoto);

        viewHolder.tvFullName.setText(reviewCommentList.get(position).getFullName());
        viewHolder.tvCommentDate.setText(reviewCommentList.get(position).getCommentsDays());
        viewHolder.tvCommentText.setText(reviewCommentList.get(position).getCommentsText());

        return convertView;
    }


    public class ViewHolder {
     public    LinearLayout lyUserRow;
        public CircleImageView ivUserPhoto;
        public TextView tvFullName;
        public TextView tvCommentDate;
        public TextView tvCommentText;

        public ViewHolder(LinearLayout lyUserRow, CircleImageView ivUserPhoto, TextView tvFullName, TextView tvCommentDate, TextView tvCommentText) {
            this.lyUserRow = lyUserRow;
            this.ivUserPhoto = ivUserPhoto;
            this.tvFullName = tvFullName;
            this.tvCommentDate=tvCommentDate;
            this.tvCommentText=tvCommentText;
        }
    }


    public interface OnItemClickListener {
        public void onUserClick(View view, int position,long id, ViewHolder viewHolder);
    }


}