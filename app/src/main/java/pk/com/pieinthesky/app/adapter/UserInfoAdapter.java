package pk.com.pieinthesky.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import pk.com.pieinthesky.app.Configuration;
import pk.com.pieinthesky.app.R;
import pk.com.pieinthesky.app.beans.UserInfo;
import pk.com.pieinthesky.app.widget.CircleImageView;

import java.util.List;

public class UserInfoAdapter extends BaseAdapter {
    Context context;
    private List<UserInfo> userInfoList;
    private OnItemClickListener onItemClickListener;

    public UserInfoAdapter(Context context, List<UserInfo> userInfoList) {
        this.context = context;
        this.userInfoList = userInfoList;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public int getCount() {
        return userInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return userInfoList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.user_info_row, null);


            LinearLayout lyUserRow = convertView.findViewById(R.id.lyUserRow);
            TextView btnFollow = convertView.findViewById(R.id.btnFollow);
            CircleImageView ivUserPhoto = convertView.findViewById(R.id.ivUserPhoto);
            TextView tvFullName = convertView.findViewById(R.id.tvFullName);
            TextView tvSubDetail = convertView.findViewById(R.id.tvSubDetail);


            viewHolder = new ViewHolder(lyUserRow, btnFollow, ivUserPhoto, tvFullName, tvSubDetail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (onItemClickListener != null) {
            viewHolder.lyUserRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onItemClickListener.onUserClick(view, position, view.getId(),viewHolder);
                }
            });

            viewHolder.btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onUserFollowClick(view, position, view.getId(),viewHolder);
                }
            });
        }
        Glide.with(context)
                .load(userInfoList.get(position).getProfileImageURL())
                .centerCrop()
                .placeholder(R.drawable.image_slider_loading)
                .into(viewHolder.ivUserPhoto);


        if(userInfoList.get(position).getId().equals(Configuration.getUser().getUserId())) {
            viewHolder.btnFollow.setVisibility(View.GONE);
        }
        else
        {
            viewHolder.btnFollow.setVisibility(View.VISIBLE);
        }

        if(userInfoList.get(position).isFollow()) {
            viewHolder.btnFollow.setText("followed");
            viewHolder.btnFollow.setBackgroundResource(R.drawable.button_user_follow_fill);
            viewHolder.btnFollow.setTextColor(context.getResources().getColor(R.color.colorWhite));
        }
        else
        {
            viewHolder.btnFollow.setText("follow");
            viewHolder.btnFollow.setBackgroundResource(R.drawable.button_user_follow);
            viewHolder.btnFollow.setTextColor(context.getResources().getColor(R.color.colorFollow));
        }

        viewHolder.tvFullName.setText(userInfoList.get(position).getFirstName() + " " + userInfoList.get(position).getLastName());
        viewHolder.tvSubDetail.setText(userInfoList.get(position).getTotalReview() + " reviews, " + userInfoList.get(position).getTotalFollower() + " follwers");


        return convertView;
    }


    public class ViewHolder {
        public  LinearLayout lyUserRow;
        public  TextView btnFollow;
        public  CircleImageView ivUserPhoto;
        public TextView tvFullName;
        public  TextView tvSubDetail;

        public ViewHolder(LinearLayout lyUserRow, TextView btnFollow, CircleImageView ivUserPhoto, TextView tvFullName, TextView tvSubDetail) {
            this.lyUserRow = lyUserRow;
            this.btnFollow = btnFollow;
            this.ivUserPhoto = ivUserPhoto;
            this.tvFullName = tvFullName;
            this.tvSubDetail = tvSubDetail;
        }
    }


    public   interface OnItemClickListener {
        public void onUserClick(View view, int position, long id, ViewHolder viewHolder);

        public void onUserFollowClick(View view, int position, long id, ViewHolder viewHolder);
    }
}