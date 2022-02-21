package pk.com.pieinthesky.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pk.com.pieinthesky.app.adapter.ReviewAdapter;
import pk.com.pieinthesky.app.adapter.TimelineAdapter;
import pk.com.pieinthesky.app.beans.PlaceReview;
import pk.com.pieinthesky.app.beans.TimelineDetail;
import pk.com.pieinthesky.app.beans.UserDetail;
import pk.com.pieinthesky.app.connectivity.ServiceManager;
import pk.com.pieinthesky.app.helper.ActivityRequest;
import pk.com.pieinthesky.app.helper.UIHelper;
import pk.com.pieinthesky.app.utils.BackgroundRequest;
import pk.com.pieinthesky.app.widget.CircleImageView;
import pk.com.pieinthesky.app.widget.NonScrollListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

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
    private CardView btnEditProfile;

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

    int pageSize = 10;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        userId = Configuration.getUserId();

        shimmer_UserLoading = getView().findViewById(R.id.shimmer_UserLoading);
        lyMainWindow = getView().findViewById(R.id.lyMainWindow);

        shimmer_UserLoading.setVisibility(View.VISIBLE);
        lyMainWindow.setVisibility(View.GONE);

        main_window = getView().findViewById(R.id.main_window);
        ivCoverPhoto = getView().findViewById(R.id.ivCoverPhoto);
        ivUserPhoto = getView().findViewById(R.id.ivUserPhoto);
        tvUserFullName = getView().findViewById(R.id.tvUserFullName);
        tvPlaceLocation = getView().findViewById(R.id.tvPlaceLocation);
        tvUserDescription = getView().findViewById(R.id.tvUserDescription);
        tvFollowers = getView().findViewById(R.id.tvFollowers);
        tvFollowing = getView().findViewById(R.id.tvFollowing);
        btnEditProfile = getView().findViewById(R.id.btnEditProfile);
        tabLayout = getView().findViewById(R.id.tabLayout);


        tabTimeline = getView().findViewById(R.id.tabTimeline);
        tabReviews = getView().findViewById(R.id.tabReviews);
        tabPhotos = getView().findViewById(R.id.tabPhotos);
        list_Timeline = getView().findViewById(R.id.list_Timeline);
        lyLoadTimeline = getView().findViewById(R.id.lyLoadTimeline);
        list_Review = getView().findViewById(R.id.list_Review);
        lyLoadReviews = getView().findViewById(R.id.lyLoadReviews);

        timelineList = new ArrayList<TimelineDetail>();
        timelineAdapter = new TimelineAdapter(getActivity(), timelineList);
        timelineAdapter.setOnItemClickListener(onTimelineItemClickListener);
        list_Timeline.setAdapter(timelineAdapter);

        placeReviewList = new ArrayList<TimelineDetail>();
        reviewAdapter = new TimelineAdapter(getActivity(), placeReviewList);
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

        tvUserFullName.setText(Configuration.getUser().getFirstName() + " " + Configuration.getUser().getLastName());

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnEditProfile_onClick(view);
            }
        });


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

        tvFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), FollowFollowingActivity.class);
                intent.putExtra("UserId", userId);
                intent.putExtra("ListType", 0);
                startActivity(intent);

            }
        });

        tvFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FollowFollowingActivity.class);
                intent.putExtra("UserId", userId);
                intent.putExtra("ListType", 1);
                startActivity(intent);
            }
        });


        UserDataTask(userId);

        UserTimelineTask(userId, timelineList.size(), pageSize);


        new Handler().post(new Runnable() {
            @Override
            public void run() {
                main_window.scrollTo(0, 0);
            }
        });

    }


    private TimelineAdapter.OnItemClickListener onTimelineItemClickListener = new TimelineAdapter.OnItemClickListener() {
        @Override
        public void onUserClick(View view, int position, long id, TimelineAdapter.ViewHolder viewHolder) {
            Intent intent = new Intent(getActivity(), PlaceDetailActivity.class);
            intent.putExtra("PlaceId", timelineList.get(position).getPlaceId());
            startActivity(intent);
        }


        @Override
        public void onUserLikeClick(View view, int position, long id, TimelineAdapter.ViewHolder viewHolder) {
            ReviewLikeTask(position, timelineList.get(position).getReviewsId(), viewHolder);
        }

        @Override
        public void onUserCommentClick(View view, int position, long id, TimelineAdapter.ViewHolder viewHolder) {
            Intent intent = new Intent(getActivity(), CommentActivity.class);
            intent.putExtra("PlaceId", timelineList.get(position).getPlaceId());
            intent.putExtra("ReviewId", timelineList.get(position).getReviewsId());
            startActivityForResult(intent, ActivityRequest.REQUEST_COMMENT);
        }
    };


    private TimelineAdapter.OnItemClickListener onReviewItemClickListener = new TimelineAdapter.OnItemClickListener() {
        @Override
        public void onUserClick(View view, int position, long id, TimelineAdapter.ViewHolder viewHolder) {
            Intent intent = new Intent(getActivity(), PlaceDetailActivity.class);
            intent.putExtra("PlaceId", placeReviewList.get(position).getPlaceId());
            startActivity(intent);
        }


        @Override
        public void onUserLikeClick(View view, int position, long id, TimelineAdapter.ViewHolder viewHolder) {
            ReviewLikeTask(position, placeReviewList.get(position).getReviewsId(), viewHolder);
        }

        @Override
        public void onUserCommentClick(View view, int position, long id, TimelineAdapter.ViewHolder viewHolder) {
            Intent intent = new Intent(getActivity(), CommentActivity.class);
            intent.putExtra("PlaceId", placeReviewList.get(position).getPlaceId());
            intent.putExtra("ReviewId", placeReviewList.get(position).getReviewsId());
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


    public void btnEditProfile_onClick(View view) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        startActivity(intent);
    }


    private void UserDataTask(final String UserId) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {

            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.UserData(UserId);
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

                        userDetail = gson.fromJson(jsonObject.getJSONObject("UserData").toString(), type);


                        Glide.with(getContext())
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
                        UIHelper.showShortToast(getContext(), jsonObject.getString("Message"));
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
                isTimelineFetching = true;
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
                        UIHelper.showShortToast(getActivity(), jsonObject.getString("Message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isTimelineFetching = false;
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
                        UIHelper.showShortToast(getActivity(), jsonObject.getString("Message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isReviewsFetching = false;
                lyLoadReviews.setVisibility(View.GONE);
            }
        }.execute();
    }


}
