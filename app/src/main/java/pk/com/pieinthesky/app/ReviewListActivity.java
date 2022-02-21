package pk.com.pieinthesky.app;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pk.com.pieinthesky.app.adapter.ReviewAdapter;
import pk.com.pieinthesky.app.beans.PlaceReview;
import pk.com.pieinthesky.app.connectivity.ServiceManager;
import pk.com.pieinthesky.app.helper.ActivityRequest;
import pk.com.pieinthesky.app.helper.UIHelper;
import pk.com.pieinthesky.app.utils.BackgroundRequest;
import pk.com.pieinthesky.app.widget.IconButton;
import pk.com.pieinthesky.app.widget.NonScrollListView;
import pk.com.pieinthesky.app.widget.RoundCornerImageView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ReviewListActivity extends AppCompatActivity {

    private ServiceManager serviceManager = new ServiceManager();

    private String placeId;
    private String placeImageURL;
    private String placeName;
    private String tags;
    private String avgRating;
    private String totalReview;
    private int modeId;

    private RoundCornerImageView ivPlaceImage;
    private TextView tvPlaceName;
    private TextView tvTags;
    private TextView tvAvgRating;
    private TextView tvTotalReview;
    private List<PlaceReview> placeReviewList = null;
    private ReviewAdapter reviewAdapter =null;

    private NestedScrollView main_window;
    private ShimmerFrameLayout shimmer_PlaceReviewLoading;
    private NonScrollListView list_Review;

    private LinearLayout lyLoadReviews;

    int pageSize = 10;
    boolean isSearching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        getSupportActionBar().setTitle("Reviews");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        placeId = getIntent().getStringExtra("PlaceId");
        placeImageURL = getIntent().getStringExtra("PlaceImageURL");
        placeName = getIntent().getStringExtra("PlaceName");
        tags = getIntent().getStringExtra("Tags");
        avgRating = getIntent().getStringExtra("AvgRating");
        totalReview = getIntent().getStringExtra("TotalReview");
        modeId = getIntent().getIntExtra("ModeId", 0);

        ivPlaceImage = findViewById(R.id.ivPlaceImage);
        tvPlaceName = findViewById(R.id.tvPlaceName);
        tvTags = findViewById(R.id.tvTags);
        tvAvgRating = findViewById(R.id.tvAvgRating);
        tvTotalReview = findViewById(R.id.tvTotalReview);
        main_window = findViewById(R.id.main_window);
        shimmer_PlaceReviewLoading = findViewById(R.id.shimmer_PlaceReviewLoading);

        list_Review = findViewById(R.id.list_Review);
        lyLoadReviews = findViewById(R.id.lyLoadReviews);

        Glide.with(this)
                .load(placeImageURL)
                .centerCrop()
                .placeholder(R.drawable.image_place_loading)
                .into(ivPlaceImage);

        tvPlaceName.setText(placeName);
        tvTags.setText(tags);
        tvAvgRating.setText(avgRating);
        tvTotalReview.setText(totalReview);

        placeReviewList = new ArrayList<PlaceReview>();
        reviewAdapter = new ReviewAdapter(this, placeReviewList);
        reviewAdapter.setOnItemClickListener(onItemClickListener);
        list_Review.setAdapter(reviewAdapter);

        main_window.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int x, int y, int oldx, int oldy) {

                View view = (View) main_window.getChildAt(main_window.getChildCount() - 1);
                int diff = (view.getBottom() - (main_window.getHeight() + main_window.getScrollY()));

                if (diff == 0 && isSearching == false) {

                    PlaceReviewTask(placeId, placeReviewList.size(), pageSize);
                }
            }
        });

        PlaceReviewTask(placeId, placeReviewList.size(), pageSize);
    }

    private void PlaceReviewTask(final String PlaceId, final int Skip, final int PageSize) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                isSearching = true;
                if (placeReviewList.size() == 0) {
                    shimmer_PlaceReviewLoading.startShimmer();
                    shimmer_PlaceReviewLoading.setVisibility(View.VISIBLE);
                } else {
                    lyLoadReviews.setVisibility(View.VISIBLE);
                }

            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.PlaceReview(PlaceId,Skip,PageSize);
                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                try {
                    Type type;
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {
                        type = new TypeToken<List<PlaceReview>>() {
                        }.getType();

                        List<PlaceReview> currentplaceReviewList = gson.fromJson(jsonObject.getJSONArray("ReviewData").toString(), type);

                        placeReviewList.addAll(currentplaceReviewList);

                        //   list_Review.setAdapter(reviewAdapter);

                        reviewAdapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                isSearching = false;
                shimmer_PlaceReviewLoading.stopShimmer();
                shimmer_PlaceReviewLoading.setVisibility(View.GONE);

                lyLoadReviews.setVisibility(View.GONE);

            }
        }.execute();
    }

    private void ReviewLikeTask(final int position, final String ReviewID, final ReviewAdapter.ViewHolder viewHolder) {

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
                        if (placeReviewList.get(position).isLike() == false) {
                            viewHolder.btnLike.setTextColor(getResources().getColor(R.color.colorLike));
                            viewHolder.btnLike.setText("Liked");
                            placeReviewList.get(position).setLike(true);

                            placeReviewList.get(position).setTotalLike(placeReviewList.get(position).getTotalLike() + 1);
                            viewHolder.tvReviewLikeComment.setText(placeReviewList.get(position).getTotalLike() + " Likes, " + placeReviewList.get(position).getTotalComments() + " Comments");
                        } else {
                            viewHolder.btnLike.setTextColor(getResources().getColor(R.color.colorNotLike));
                            viewHolder.btnLike.setText("Like");
                            placeReviewList.get(position).setLike(false);

                            placeReviewList.get(position).setTotalLike(placeReviewList.get(position).getTotalLike() - 1);
                            viewHolder.tvReviewLikeComment.setText(placeReviewList.get(position).getTotalLike() + " Likes, " + placeReviewList.get(position).getTotalComments() + " Comments");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }


    private void UserFollowTask(final int position, final String UserID, final ReviewAdapter.ViewHolder viewHolder) {

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
                        if (placeReviewList.get(position).isFollow() == false)
                        {
                            viewHolder.btnFollow.setText("followed");
                            viewHolder.btnFollow.setBackgroundResource(R.drawable.button_user_follow_fill);
                            viewHolder.btnFollow.setTextColor(getResources().getColor(R.color.colorWhite));
                            placeReviewList.get(position).setFollow(true);
                        }
                        else
                        {
                            viewHolder.btnFollow.setText("follow");
                            viewHolder.btnFollow.setBackgroundResource(R.drawable.button_user_follow);
                            viewHolder.btnFollow.setTextColor(getResources().getColor(R.color.colorFollow));
                            placeReviewList.get(position).setFollow(false);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }


    private ReviewAdapter.OnItemClickListener onItemClickListener = new ReviewAdapter.OnItemClickListener() {
        @Override
        public void onUserClick(View view, int position, long id, ReviewAdapter.ViewHolder viewHolder) {
            Intent intent = new Intent(ReviewListActivity.this, UserTimelineActivity.class);
            intent.putExtra("UserId", placeReviewList.get(position).getUserId());
            startActivity(intent);
        }

        @Override
        public void onUserFollowClick(View view, int position, long id, ReviewAdapter.ViewHolder viewHolder) {
            UserFollowTask(position,placeReviewList.get(position).getUserId(),viewHolder);
        }

        @Override
        public void onUserLikeClick(View view, int position, long id, ReviewAdapter.ViewHolder viewHolder) {
            ReviewLikeTask(position, placeReviewList.get(position).getReviewsId(), viewHolder);
        }

        @Override
        public void onUserCommentClick(View view, int position, long id, ReviewAdapter.ViewHolder viewHolder) {
            Intent intent = new Intent(ReviewListActivity.this, CommentActivity.class);

            intent.putExtra("PlaceId", placeId);
            intent.putExtra("ReviewId", placeReviewList.get(position).getReviewsId());
            startActivityForResult(intent, ActivityRequest.REQUEST_COMMENT);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequest.REQUEST_REVIEW && resultCode == RESULT_OK) {
            placeReviewList.clear();
            reviewAdapter.notifyDataSetChanged();
            PlaceReviewTask(placeId, placeReviewList.size(), pageSize);
        } else if (requestCode == ActivityRequest.REQUEST_COMMENT && resultCode == RESULT_OK) {
            boolean isChange = data.getBooleanExtra("IsChange", false);
            if (isChange) {
                placeReviewList.clear();
                reviewAdapter.notifyDataSetChanged();
                PlaceReviewTask(placeId, placeReviewList.size(), pageSize);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.review, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_Review) {
            Intent intent = new Intent(this, ReviewActivity.class);
            intent.putExtra("PlaceId", placeId);
            intent.putExtra("PlaceImageURL", placeImageURL);
            intent.putExtra("PlaceName", placeName);
            intent.putExtra("Tags", tags);
            intent.putExtra("AvgRating", avgRating);
            intent.putExtra("TotalReview", totalReview);
            intent.putExtra("ModeId", modeId);

            startActivityForResult(intent, ActivityRequest.REQUEST_REVIEW);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
