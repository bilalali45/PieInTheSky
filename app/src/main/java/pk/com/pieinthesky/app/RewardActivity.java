package pk.com.pieinthesky.app;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import pk.com.pieinthesky.app.beans.RewardPointDetail;
import pk.com.pieinthesky.app.beans.User;
import pk.com.pieinthesky.app.widget.CircleImageView;
import pk.com.pieinthesky.app.widget.NonScrollListView;

import java.util.ArrayList;
import java.util.List;

public class RewardActivity extends AppCompatActivity {

    private CircleImageView ivUserPhoto;
    private TextView tvUserFullName;
    private TextView tvPoints;
    private TextView tvPointDetail;
    private NonScrollListView list_PointDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        getSupportActionBar().setTitle("My Rewards");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ivUserPhoto = findViewById(R.id.ivUserPhoto);
        tvUserFullName = findViewById(R.id.tvUserFullName);
        tvPoints = findViewById(R.id.tvPoints);
        tvPointDetail = findViewById(R.id.tvPointDetail);
        list_PointDetail = findViewById(R.id.list_PointDetail);

        User user = Configuration.getUser();
        tvUserFullName.setText(user.getFirstName() + " " + user.getLastName());

        Glide.with(this)
                .load(user.getPhotoURL())
                .centerCrop()
                .placeholder(R.drawable.image_slider_loading)
                .into(ivUserPhoto);

        List<RewardPointDetail> rewardPointDetails=new ArrayList<RewardPointDetail>();


        rewardPointDetails.add(new RewardPointDetail("10","Points for user signup"));
        rewardPointDetails.add(new RewardPointDetail("05","Points for each checking-in"));
        rewardPointDetails.add(new RewardPointDetail("20","Points for each review"));
        rewardPointDetails.add(new RewardPointDetail("05","Points for each picture uploaded"));
        rewardPointDetails.add(new RewardPointDetail("05","Points for your comments get helpful"));
        rewardPointDetails.add(new RewardPointDetail("02","Points for each restaurant liked"));
        rewardPointDetails.add(new RewardPointDetail("01","Points for each review liked"));

        RewardPointDetailAdapter rewardPointDetailAdapter = new RewardPointDetailAdapter(this,rewardPointDetails);

        list_PointDetail.setAdapter(rewardPointDetailAdapter);
    }


    private class RewardPointDetailAdapter extends BaseAdapter {
        private Context context;
        private List<RewardPointDetail> rewardPointDetailList = null;

        public RewardPointDetailAdapter(Context context, List<RewardPointDetail> rewardPointDetailList) {
            this.context = context;
            this.rewardPointDetailList = rewardPointDetailList;
        }

        @Override
        public int getCount() {
            return rewardPointDetailList.size();
        }

        @Override
        public Object getItem(int position) {
            return rewardPointDetailList.get(position);
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
                convertView = layoutInflater.inflate(R.layout.reward_point_detail_row, null);

                TextView tvRewardPoint = convertView.findViewById(R.id.tvRewardPoint);
                TextView tvRewardDescription = convertView.findViewById(R.id.tvRewardDescription);

                viewHolder = new ViewHolder(tvRewardPoint, tvRewardDescription);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tvRewardPoint.setText(rewardPointDetailList.get(position).getRewardPoint());
            viewHolder.tvRewardDescription.setText(rewardPointDetailList.get(position).getRewardDescription());


            return convertView;
        }


        private class ViewHolder {
            TextView tvRewardPoint;
            TextView tvRewardDescription;

            public ViewHolder(TextView tvRewardPoint, TextView tvRewardDescription) {
                this.tvRewardPoint = tvRewardPoint;
                this.tvRewardDescription = tvRewardDescription;
            }
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
