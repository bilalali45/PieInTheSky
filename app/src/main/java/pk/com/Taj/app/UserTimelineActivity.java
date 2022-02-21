package pk.com.Taj.app;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pk.com.Taj.app.adapter.TimelineAdapter;
import pk.com.Taj.app.beans.TimelineDetail;
import pk.com.Taj.app.beans.UserDetail;
import pk.com.Taj.app.connectivity.ServiceManager;
import pk.com.Taj.app.helper.ActivityRequest;
import pk.com.Taj.app.helper.UIHelper;
import pk.com.Taj.app.utils.BackgroundRequest;
import pk.com.Taj.app.widget.CircleImageView;
import pk.com.Taj.app.widget.NonScrollListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserTimelineActivity extends AppCompatActivity {

    private ServiceManager serviceManager = new ServiceManager();

    private String userId;

    UserDetail userDetail = null;

    private ShimmerFrameLayout shimmer_UserLoading;
    private LinearLayout lyMainWindow;

    private ImageView ivCoverPhoto;
    private CircleImageView ivUserPhoto;
    private TextView tvUserFullName;
    private TextView tvPlaceLocation;
    private TextView tvUserDescription;
    private TextView tvFollowers;
    private TextView tvFollowing;
    private TextView btnFollow;

    private TabLayout tabLayout;

    private NestedScrollView main_window;
    private LinearLayout tabTimeline;
    private LinearLayout tabReviews;
    private LinearLayout tabPhotos;

    private NonScrollListView list_Timeline;
    private LinearLayout lyLoadTimeline;
    private NonScrollListView list_Review;
    private LinearLayout lyLoadReviews;

    private List<TimelineDetail> timelineList = null;
    private TimelineAdapter timelineAdapter = null;

    private List<TimelineDetail> placeReviewList = null;
    private TimelineAdapter reviewAdapter = null;


    boolean isFollow = false;

    int pageSize=10;
    boolean isTimelineFetching = false;
    boolean isReviewsFetching = false;

    public enum TabName {
        Timeline(0), Reviews(1), Photos(2);
        public int value;

        private TabName(int value) {
            this.value = value;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_timeline);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userId = getIntent().getStringExtra("UserId");

        shimmer_UserLoading = findViewById(R.id.shimmer_UserLoading);
        lyMainWindow = findViewById(R.id.lyMainWindow);

        shimmer_UserLoading.setVisibility(View.VISIBLE);
        lyMainWindow.setVisibility(View.GONE);

        main_window = findViewById(R.id.main_window);
        ivCoverPhoto = findViewById(R.id.ivCoverPhoto);
        ivUserPhoto = findViewById(R.id.ivUserPhoto);
        tvUserFullName = findViewById(R.id.tvUserFullName);
        tvPlaceLocation = findViewById(R.id.tvPlaceLocation);
        tvUserDescription = findViewById(R.id.tvUserDescription);
        tvFollowers = findViewById(R.id.tvFollowers);
        tvFollowing = findViewById(R.id.tvFollowing);
        btnFollow = findViewById(R.id.btnFollow);
        tabLayout = findViewById(R.id.tabLayout);


        tabTimeline = findViewById(R.id.tabTimeline);
        tabReviews = findViewById(R.id.tabReviews);
        tabPhotos = findViewById(R.id.tabPhotos);
        list_Timeline = findViewById(R.id.list_Timeline);
        lyLoadTimeline = findViewById(R.id.lyLoadTimeline);
        list_Review = findViewById(R.id.list_Review);
        lyLoadReviews = findViewById(R.id.lyLoadReviews);

        timelineList = new ArrayList<TimelineDetail>();
        timelineAdapter = new TimelineAdapter(this, timelineList);
        timelineAdapter.setOnItemClickListener(onTimelineItemClickListener);
        list_Timeline.setAdapter(timelineAdapter);

        placeReviewList = new ArrayList<TimelineDetail>();
        reviewAdapter = new TimelineAdapter(this, placeReviewList);
        reviewAdapter.setOnItemClickListener(onReviewItemClickListener);
        list_Review.setAdapter(reviewAdapter);



        main_window.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int x, int y, int oldx, int oldy) {

                View view = (View) main_window.getChildAt(main_window.getChildCount() - 1);
                int diff = (view.getBottom() - (main_window.getHeight() + main_window.getScrollY()));

                if (tabLayout.getSelectedTabPosition() == TabName.Timeline.value) {
                    if (diff == 0 && isTimelineFetching == false) {
                        UserTimelineTask(userId, timelineList.size(), pageSize);
                    }
                } else if (tabLayout.getSelectedTabPosition() == TabName.Reviews.value) {
                    if (diff == 0 && isReviewsFetching == false) {
                        UserReviewsTask(userId, placeReviewList.size(), pageSize);
                    }
                }
            }
        });




        tabLayout.addTab(tabLayout.newTab().setText("Timeline"));
        tabLayout.addTab(tabLayout.newTab().setText("Reviews"));
        //tabLayout.addTab(tabLayout.newTab().setText("Photos (1)"));

        tabTimeline.setVisibility(View.VISIBLE);
        tabReviews.setVisibility(View.GONE);
        tabPhotos.setVisibility(View.GONE);

        if (userId.equals(Configuration.getUser().getUserId())) {
            btnFollow.setVisibility(View.GONE);
        } else {
            btnFollow.setVisibility(View.VISIBLE);
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == TabName.Timeline.value) {
                    tabReviews.setVisibility(View.GONE);
                    tabPhotos.setVisibility(View.GONE);
                    tabTimeline.setVisibility(View.VISIBLE);
                } else if (tabLayout.getSelectedTabPosition() == TabName.Reviews.value) {
                    tabTimeline.setVisibility(View.GONE);
                    tabPhotos.setVisibility(View.GONE);
                    tabReviews.setVisibility(View.VISIBLE);
                    if (placeReviewList.size() == 0) {
                        UserReviewsTask(userId, placeReviewList.size(), pageSize);
                    }
                } else if (tabLayout.getSelectedTabPosition() == TabName.Photos.value) {
                    tabTimeline.setVisibility(View.GONE);
                    tabReviews.setVisibility(View.GONE);
                    tabPhotos.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        PublicUserDataTask(userId);

        UserTimelineTask(userId, timelineList.size(), pageSize);

    }


    private TimelineAdapter.OnItemClickListener onTimelineItemClickListener = new TimelineAdapter.OnItemClickListener() {
        @Override
        public void onUserClick(View view, int position, long id, TimelineAdapter.ViewHolder viewHolder) {
            Intent intent = new Intent(UserTimelineActivity.this, PlaceDetailActivity.class);
            intent.putExtra("PlaceId", timelineList.get(position).getPlaceId());
            startActivity(intent);
        }


        @Override
        public void onUserLikeClick(View view, int position, long id, TimelineAdapter.ViewHolder viewHolder) {
            ReviewLikeTask(position, timelineList.get(position).getReviewsId(), viewHolder);
        }

        @Override
        public void onUserCommentClick(View view, int position, long id, TimelineAdapter.ViewHolder viewHolder) {
            Intent intent = new Intent(UserTimelineActivity.this, CommentActivity.class);
            intent.putExtra("PlaceId", timelineList.get(position).getPlaceId());
            intent.putExtra("ReviewId", timelineList.get(position).getReviewsId());
            startActivityForResult(intent, ActivityRequest.REQUEST_COMMENT);
        }
    };

    private TimelineAdapter.OnItemClickListener onReviewItemClickListener = new TimelineAdapter.OnItemClickListener() {
        @Override
        public void onUserClick(View view, int position, long id, TimelineAdapter.ViewHolder viewHolder) {
            Intent intent = new Intent(UserTimelineActivity.this, PlaceDetailActivity.class);
            intent.putExtra("PlaceId", timelineList.get(position).getPlaceId());
            startActivity(intent);
        }


        @Override
        public void onUserLikeClick(View view, int position, long id, TimelineAdapter.ViewHolder viewHolder) {
            ReviewLikeTask(position, timelineList.get(position).getReviewsId(), viewHolder);
        }

        @Override
        public void onUserCommentClick(View view, int position, long id, TimelineAdapter.ViewHolder viewHolder) {
            Intent intent = new Intent(UserTimelineActivity.this, CommentActivity.class);
            intent.putExtra("PlaceId", timelineList.get(position).getPlaceId());
            intent.putExtra("ReviewId", timelineList.get(position).getReviewsId());
            startActivityForResult(intent, ActivityRequest.REQUEST_COMMENT);
        }
    };


    private void ReviewLikeTask(final int position, final String ReviewID, final TimelineAdapter.ViewHolder viewHolder) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.ReviewLike(ReviewID);
                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                try {

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {
                        if (timelineList.get(position).isLike() == false) {
                            viewHolder.btnLike.setTextColor(getResources().getColor(R.color.colorLike));
                            viewHolder.btnLike.setText("Liked");
                            timelineList.get(position).setLike(true);

                            timelineList.get(position).setTotalLike(timelineList.get(position).getTotalLike() + 1);
                            viewHolder.tvReviewLikeComment.setText(timelineList.get(position).getTotalLike() + " Likes, " + timelineList.get(position).getTotalComments() + " Comments");
                        } else {
                            viewHolder.btnLike.setTextColor(getResources().getColor(R.color.colorNotLike));
                            viewHolder.btnLike.setText("Like");
                            timelineList.get(position).setLike(false);

                            timelineList.get(position).setTotalLike(timelineList.get(position).getTotalLike() - 1);
                            viewHolder.tvReviewLikeComment.setText(timelineList.get(position).getTotalLike() + " Likes, " + timelineList.get(position).getTotalComments() + " Comments");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }


    public void btnFollowFollowing_onClick(View view) {
        int listType = 0;

        if (view.getId() == R.id.tvFollowing) {
            listType = 1;
        }

        Intent intent = new Intent(this, FollowFollowingActivity.class);
        intent.putExtra("UserId", userId);
        intent.putExtra("ListType", listType);
        startActivity(intent);
    }

    public void btnFollow_onClick(View view) {
        UserFollowTask(userId);


    }


    private void PublicUserDataTask(final String UserId) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {

            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.PublicUserData(UserId);
                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                try {

                    Type type;
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {
                        type = new TypeToken<UserDetail>() {
                        }.getType();

                        userDetail = gson.fromJson(jsonObject.getJSONObject("PublicData").toString(), type);


                        Glide.with(UserTimelineActivity.this)
                                .load(userDetail.getProfileImageURL())
                                .centerCrop()
                                .placeholder(R.drawable.image_slider_loading)
                                .into(ivUserPhoto);

                        tvUserFullName.setText(userDetail.getFirstName() + " " + userDetail.getLastName());
                        if (userDetail.getUserDescription() != null && userDetail.getUserDescription().length() > 0) {
                            tvUserDescription.setText(userDetail.getUserDescription());
                        } else {
                            tvUserDescription.setVisibility(View.GONE);
                        }
                        tvFollowers.setText(userDetail.getTotalFollower() + " followers");
                        tvFollowing.setText(userDetail.getTotalFollowing() + " following");

                    } else {
                        UIHelper.showShortToast(UserTimelineActivity.this, jsonObject.getString("Message"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                lyMainWindow.setVisibility(View.VISIBLE);

                shimmer_UserLoading.stopShimmer();
                shimmer_UserLoading.setVisibility(View.GONE);

            }
        }.execute();
    }


    private void UserTimelineTask(final String UserId, final int Skip, final int PageSize) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                isTimelineFetching=true;
                lyLoadTimeline.setVisibility(View.VISIBLE);
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.UserTimeline(UserId, Skip, PageSize);
                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {

                try {
                    Type type;
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    final boolean status = jsonObject.getBoolean("Status");
                    final int statusCode = jsonObject.getInt("StatusCode");
                    if (status) {
                        JSONArray DineLineData = jsonObject.getJSONArray("DineLineData");

                        type = new TypeToken<List<TimelineDetail>>() {
                        }.getType();

                        List<TimelineDetail> currentTimelineList = gson.fromJson(DineLineData.toString(), type);

                        timelineList.addAll(currentTimelineList);

                        //   list_Timeline.setAdapter(timelineAdapter);

                        timelineAdapter.notifyDataSetChanged();


                    } else {
                        UIHelper.showShortToast(UserTimelineActivity.this, jsonObject.getString("Message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isTimelineFetching=false;
                lyLoadTimeline.setVisibility(View.GONE);
            }
        }.execute();
    }


    private void UserReviewsTask(final String UserId, final int Skip, final int PageSize) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                isReviewsFetching = true;
                lyLoadReviews.setVisibility(View.VISIBLE);
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.UserReviews(UserId, false, Skip, PageSize);
                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {

                try {
                    Type type;
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    final boolean status = jsonObject.getBoolean("Status");
                    final int statusCode = jsonObject.getInt("StatusCode");
                    if (status) {
                        JSONArray ReviewData = jsonObject.getJSONArray("ReviewData");

                        type = new TypeToken<List<TimelineDetail>>() {
                        }.getType();

                        List<TimelineDetail> currentPlaceReviewList = gson.fromJson(ReviewData.toString(), type);

                        placeReviewList.addAll(currentPlaceReviewList);

                        //   list_Review.setAdapter(reviewAdapter);

                        reviewAdapter.notifyDataSetChanged();


                    } else {
                        UIHelper.showShortToast(UserTimelineActivity.this, jsonObject.getString("Message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isReviewsFetching = false;
                lyLoadReviews.setVisibility(View.GONE);
            }
        }.execute();
    }

    private void UserFollowTask(final String UserID) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {

            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.UserFollow(UserID);
                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                try {

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {

                        if (isFollow == false) {
                            btnFollow.setText("followed");
                            btnFollow.setBackgroundResource(R.drawable.button_user_follow_fill_large);
                            btnFollow.setTextColor(getResources().getColor(R.color.colorWhite));

                            isFollow = true;
                        } else {
                            btnFollow.setText("follow");
                            btnFollow.setBackgroundResource(R.drawable.button_user_follow_large);
                            btnFollow.setTextColor(getResources().getColor(R.color.colorFollow));

                            isFollow = false;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }

    public void btnPlaceDetail_onClick(View view) {
        Intent intent = new Intent(this, PlaceDetailActivity.class);
        intent.putExtra("PlaceId", "3B152A9A-C516-492C-AAAE-8B82A5373FCD");
        startActivity(intent);
    }


    public void btnComment_onClick(View view) {
        Intent intent = new Intent(this, CommentActivity.class);
        startActivity(intent);
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
