package pk.com.Taj.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pk.com.Taj.app.adapter.CommentAdapter;
import pk.com.Taj.app.beans.PlaceReview;
import pk.com.Taj.app.beans.ReviewComment;
import pk.com.Taj.app.connectivity.ServiceManager;
import pk.com.Taj.app.helper.UIHelper;
import pk.com.Taj.app.utils.BackgroundRequest;
import pk.com.Taj.app.widget.IconButton;
import pk.com.Taj.app.widget.NonScrollListView;
import pk.com.Taj.app.widget.CircleImageView;
import pk.com.Taj.app.widget.RoundCornerImageView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private ProgressDialog dialog;

    private ServiceManager serviceManager = new ServiceManager();

    private String placeId;
    private String reviewId;

    private PlaceReview placeReview = null;
    private List<ReviewComment> reviewCommentList = null;

    private ShimmerFrameLayout shimmer_CommentLoading;
    private LinearLayout lyMainWindow;
    private RoundCornerImageView ivPlaceImage;
    private TextView tvPlaceName;
    private TextView tvTags;
    private TextView tvAvgRating;
    private TextView tvTotalReview;
    private LinearLayout lyUserRow;
    private CircleImageView ivUserPhoto;
    private TextView tvFullName;
    private TextView tvSubDetail;
    private AppCompatRatingBar rbPlaceRating;
    private TextView tvReviewDate;
    private TextView tvReviewText;
    private TextView tvReviewLikeComment;
    private IconButton btnLike;
    private EditText etComment;
    private NonScrollListView list_Comment;

    boolean isChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        getSupportActionBar().setTitle("Add Comment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        placeId = getIntent().getStringExtra("PlaceId");
        reviewId = getIntent().getStringExtra("ReviewId");

        shimmer_CommentLoading = findViewById(R.id.shimmer_CommentLoading);
        lyMainWindow = findViewById(R.id.lyMainWindow);
        ivPlaceImage = findViewById(R.id.ivPlaceImage1);
        tvPlaceName = findViewById(R.id.tvPlaceName);
        tvTags = findViewById(R.id.tvTags);
        tvAvgRating = findViewById(R.id.tvAvgRating);
        tvTotalReview = findViewById(R.id.tvTotalReview);
        lyUserRow = findViewById(R.id.lyUserRow);
        ivUserPhoto = findViewById(R.id.ivUserPhoto);
        tvFullName = findViewById(R.id.tvFullName);
        tvSubDetail = findViewById(R.id.tvSubDetail);
        rbPlaceRating = findViewById(R.id.rbPlaceRating);
        tvReviewDate = findViewById(R.id.tvReviewDate);
        tvReviewText = findViewById(R.id.tvReviewText);
        tvReviewLikeComment = findViewById(R.id.tvReviewLikeComment);
        btnLike = findViewById(R.id.btnLike);
        list_Comment = findViewById(R.id.list_Comment);
        etComment = findViewById(R.id.etComment);
        shimmer_CommentLoading.setVisibility(View.VISIBLE);
        lyMainWindow.setVisibility(View.GONE);

        lyUserRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnUserTimeline_onClick(view);
            }
        });

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLike_onClick(view);
            }
        });

        ReviewDetailTask(reviewId);
    }

    private void ReviewDetailTask(final String ReviewId) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                shimmer_CommentLoading.startShimmer();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.ReviewDetail(reviewId);
                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                try {
                    Type type;
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {
                        type = new TypeToken<PlaceReview>() {
                        }.getType();

                        placeReview = gson.fromJson(jsonObject.getJSONObject("ReviewData").toString(), type);


                        Glide.with(CommentActivity.this)
                                .load(placeReview.getPlaceImageURL())
                                .centerCrop()
                                .placeholder(R.drawable.image_place_loading)
                                .into(ivPlaceImage);

                        tvPlaceName.setText(placeReview.getPlaceName());
                        tvTags.setText(placeReview.getCuisines());
                        tvAvgRating.setText(placeReview.getBusinessAvgRating());
                        tvTotalReview.setText(placeReview.getTotalReview() + "+ Reviews");

                        Glide.with(CommentActivity.this)
                                .load(placeReview.getProfileImageURL())
                                .centerCrop()
                                .placeholder(R.drawable.image_slider_loading)
                                .into(ivUserPhoto);

                        tvFullName.setText(placeReview.getFullName());
                        tvSubDetail.setText(placeReview.getSubDetail());
                        tvReviewDate.setText(placeReview.getReviewDays());
                        tvReviewText.setText(placeReview.getReviewText());
                        tvReviewLikeComment.setText(placeReview.getTotalLike() + " Likes, " + placeReview.getTotalComments() + " Comments");

                        if (placeReview.isLike()) {
                            btnLike.setTextColor(getResources().getColor(R.color.colorLike));
                            btnLike.setText("Liked");
                        } else {
                            btnLike.setTextColor(getResources().getColor(R.color.colorNotLike));
                            btnLike.setText("Like");
                        }

                        reviewCommentList = placeReview.getCommentOnReview();
                        if (reviewCommentList != null) {
                            CommentAdapter commentAdapter = new CommentAdapter(CommentActivity.this, reviewCommentList);
                            commentAdapter.setOnItemClickListener(onItemClickListener);
                            list_Comment.setAdapter(commentAdapter);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                lyMainWindow.setVisibility(View.VISIBLE);

                shimmer_CommentLoading.stopShimmer();
                shimmer_CommentLoading.setVisibility(View.GONE);
            }
        }.execute();
    }


    CommentAdapter.OnItemClickListener onItemClickListener = new CommentAdapter.OnItemClickListener() {
        @Override
        public void onUserClick(View view, int position, long id, CommentAdapter.ViewHolder viewHolder) {
            Intent intent = new Intent(CommentActivity.this, UserTimelineActivity.class);
            intent.putExtra("UserId", reviewCommentList.get(position).getUserId());
            startActivity(intent);
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (isChange) {
            Intent intent = new Intent();
            intent.putExtra("IsChange", isChange);
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }

    public void btnUserTimeline_onClick(View view) {
        Intent intent = new Intent(this, UserTimelineActivity.class);
        intent.putExtra("UserId", placeReview.getUserId());
        startActivity(intent);
    }

    public void btnLike_onClick(View view) {
        ReviewLikeTask(reviewId);
    }


    public void btnPost_onClick(View view) {
        if (etComment.getText().length() > 0) {
            ReviewCommentTask(placeId, reviewId, etComment.getText().toString());
        } else {
            UIHelper.showShortToast(this, "Comment cannot be blank");
        }
    }


    private void ReviewLikeTask(final String ReviewID) {

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
                        isChange = true;

                        if (placeReview.isLike() == false) {
                            btnLike.setTextColor(getResources().getColor(R.color.colorLike));
                            btnLike.setText("Liked");
                            placeReview.setLike(true);

                            placeReview.setTotalLike(placeReview.getTotalLike() + 1);
                            tvReviewLikeComment.setText(placeReview.getTotalLike() + " Likes, " + placeReview.getTotalComments() + " Comments");
                        } else {
                            btnLike.setTextColor(getResources().getColor(R.color.colorNotLike));
                            btnLike.setText("Like");
                            placeReview.setLike(false);

                            placeReview.setTotalLike(placeReview.getTotalLike() - 1);
                            tvReviewLikeComment.setText(placeReview.getTotalLike() + " Likes, " + placeReview.getTotalComments() + " Comments");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }

    private void ReviewCommentTask(final String PlaceID, final String ReviewId, final String TextReview) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                dialog = new ProgressDialog(CommentActivity.this);
                dialog.setTitle("Please wait");
                dialog.setMessage("Updating...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.CommentsOnReview(PlaceID, ReviewId, TextReview);
                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {

                dialog.dismiss();
                try {

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {
                        isChange = true;
                        etComment.setText("");
                        ReviewDetailTask(reviewId);
                    }
                    UIHelper.showShortToast(CommentActivity.this, jsonObject.getString("Message"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }


}
