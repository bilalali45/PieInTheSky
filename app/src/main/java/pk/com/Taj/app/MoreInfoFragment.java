package pk.com.Taj.app;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pk.com.Taj.app.adapter.ReviewAdapter;
import pk.com.Taj.app.beans.Cuisine;
import pk.com.Taj.app.beans.PlaceDetail;
import pk.com.Taj.app.beans.PlaceReview;
import pk.com.Taj.app.beans.Tag;
import pk.com.Taj.app.connectivity.ServiceManager;
import pk.com.Taj.app.helper.ActivityRequest;
import pk.com.Taj.app.utils.BackgroundRequest;
import pk.com.Taj.app.widget.IconButton;
import pk.com.Taj.app.widget.IconTextView;
import pk.com.Taj.app.widget.NonScrollListView;
import pk.com.Taj.app.widget.RatingGraph;
import pk.com.Taj.app.widget.RoundCornerImageView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoreInfoFragment extends Fragment {

    private static final String MOREINFO_KEY = "more_info_model";

    private ServiceManager serviceManager = new ServiceManager();

    private String placeId = null;
    private String tel = "";

    private LinearLayout lyMainWindow;


    private NestedScrollView main_window;
    private RoundCornerImageView ivPlaceImage;
    private TextView tvPlaceName;
    private TextView tvTags;
    private TextView tvAvgRating;
    private TextView tvTotalReview;
    private TextView tvPlaceLocation;
    private TextView tvPlaceTimings;
    private TextView tvPlaceAbout;
    private TextView tvLikeCount;
    private IconButton btnLike;
    private IconButton btnBookmark;
    private Spinner spPlaceTimings;
    private FlexboxLayout flCuisine;
    private LinearLayout lyMoreInfo;
    private LinearLayout lyMoreInfoLeft;
    private LinearLayout lyMoreInfoRight;
    private AppCompatRatingBar rbPlaceRating;
    private TextView tvTrustworthyText;
    private NonScrollListView list_Review;
    private TextView btnAllReviews;
    private LinearLayout lyReviews;
    private RatingGraph rgPlaceRating;
    private IconTextView itvBookmark;
    private LinearLayout lySliderOverlay;
    private LinearLayout container_back_button;
    PlaceDetail placeDetail = null;
    private List<PlaceReview> placeReviewList = null;
    private Button btnReview;
    private ReviewAdapter reviewAdapter;

    public MoreInfoFragment(PlaceDetail placeDetail) {
        this.placeDetail = placeDetail;
       /* MoreInfoFragment fragment = new MoreInfoFragment();
        Bundle bundle = new Bundle();

        fragment.setArguments(bundle);*/

        //return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more_info, container, false);
        initViews(view);
       /* placeDetail = (PlaceDetail) getArguments().getSerializable(
                MOREINFO_KEY);*/

        placeId = placeDetail.getPlaceId();

        Glide.with(this)
                .load(placeDetail.getProfileImageURL())
                .centerCrop()
                .placeholder(R.drawable.image_place_loading)
                .into(ivPlaceImage);

        tvPlaceName.setText(placeDetail.getPlaceName());
        tvTags.setText(placeDetail.getCuisines());
        tvAvgRating.setText(placeDetail.getRating());
        tvTotalReview.setText(placeDetail.getReviewCount() + "+ Reviews");

        tvPlaceAbout.setText(placeDetail.getPlaceAbout());
        tvLikeCount.setText(placeDetail.getLikeCount() + " Likes");

        rgPlaceRating.setAvgRating(placeDetail.getRating());
        rgPlaceRating.setTotalReviews(placeDetail.getReviewCount() + " reviews");
        rgPlaceRating.setStar(5, 5, 3, 2, 8);

        if (placeDetail.isLike()) {
            btnLike.setTextColor(getResources().getColor(R.color.colorLike));
            btnLike.setText("Liked");
        }


        if (placeDetail.getRating() != null && placeDetail.getRating().equals("") == false) {
            rbPlaceRating.setRating(Float.valueOf((placeDetail.getRating())));
        }

        if (placeDetail.getPlaceTimings() != null) {
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.timing_spinner_item, placeDetail.getPlaceTimings());
            spinnerArrayAdapter.setDropDownViewResource(R.layout.timing_spinner_item);
            spPlaceTimings.setAdapter(spinnerArrayAdapter);

            tvPlaceTimings.setText(placeDetail.getPlaceTimings().get(0));
        }


        //5dp margin in pixel
        int cuisineMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        FlexboxLayout.LayoutParams cuisineLayoutParams = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        cuisineLayoutParams.setMargins(cuisineMargin, cuisineMargin, cuisineMargin, cuisineMargin);

        flCuisine.removeAllViews();
        if (placeDetail.getCuisine() != null) {
            for (Cuisine cuisine : placeDetail.getCuisine()) {
                final TextView tvCuisine = new TextView(new ContextThemeWrapper(getActivity(), R.style.TagTextView));
                tvCuisine.setText(cuisine.getCuisineName());

                tvCuisine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), SearchActivity.class);
                        intent.putExtra("SearchText", tvCuisine.getText().toString());
                        startActivity(intent);
                    }
                });

                flCuisine.addView(tvCuisine, cuisineLayoutParams);
            }
        }


        lyMoreInfoLeft.removeAllViews();
        lyMoreInfoRight.removeAllViews();
        if (placeDetail.getTags() != null) {
            int tagIndex = 0;
            for (Tag tag : placeDetail.getTags()) {
                TextView tvTag = new TextView(new ContextThemeWrapper(getActivity(), R.style.InfoTextView));
                tvTag.setText(tag.getTagsName());

                if (tagIndex % 2 == 0) {
                    lyMoreInfoLeft.addView(tvTag);
                } else {
                    lyMoreInfoRight.addView(tvTag);
                }
                tagIndex++;
            }
        }


        int menuImageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        int menuImageSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams menuLayoutParams = new LinearLayout.LayoutParams(menuImageSize, menuImageSize);

        menuLayoutParams.setMargins(0, 0, menuImageMargin, 0);

        final ArrayList<String> menuList = new ArrayList<String>();

        if (placeDetail.getPlaceMenu() != null) {
            for (int i = 0; i < placeDetail.getPlaceMenu().size(); i++) {
                menuList.add(placeDetail.getPlaceMenu().get(i).getUrl());

                ImageView ivMenuImage = new ImageView(getActivity());


                final int pictureIndex = i;
                ivMenuImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ImageViewerActivity.class);
                        intent.putStringArrayListExtra("PictureList", menuList);
                        intent.putExtra("PictureIndex", pictureIndex);
                        startActivity(intent);
                    }
                });

                Glide.with(getActivity())
                        .load(placeDetail.getPlaceMenu().get(i).getUrl())
                        .centerCrop()
                        .placeholder(R.drawable.image_slider_loading)
                        .into(ivMenuImage);

            }
        }


        //10dp margin in pixel
        int placeImageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        int placeImageSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        FlexboxLayout.LayoutParams placeImageLayoutParams = new FlexboxLayout.LayoutParams(placeImageSize, placeImageSize);


        placeImageLayoutParams.setMargins(0, 0, placeImageMargin, placeImageMargin);

        final ArrayList<String> pictureList = new ArrayList<String>();

        if (placeDetail.getPlacePictures() != null) {
            for (int i = 0; i < placeDetail.getPlacePictures().size(); i++) {
                pictureList.add(placeDetail.getPlacePictures().get(i).getUrl());
                ImageView ivPlaceImage = new ImageView(getActivity());

                final int pictureIndex = i;
                ivPlaceImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ImageViewerActivity.class);
                        intent.putStringArrayListExtra("PictureList", pictureList);
                        intent.putExtra("PictureIndex", pictureIndex);
                        startActivity(intent);
                    }
                });


                Glide.with(getActivity())
                        .load(placeDetail.getPlacePictures().get(i).getUrl())
                        .centerCrop()
                        .placeholder(R.drawable.image_slider_loading)
                        .into(ivPlaceImage);


            }
        }


        tvTrustworthyText.setText(placeDetail.getTrustworthyText());

        if (placeDetail.getPlaceReviews() != null) {
            placeReviewList = placeDetail.getPlaceReviews();
            reviewAdapter = new ReviewAdapter(getActivity(), placeReviewList);
            reviewAdapter.setOnItemClickListener(onItemClickListener);
            list_Review.setAdapter(reviewAdapter);
        } else {
            placeReviewList = new ArrayList<>();
            reviewAdapter = new ReviewAdapter(getActivity(), placeReviewList);
            reviewAdapter.setOnItemClickListener(onItemClickListener);
            list_Review.setAdapter(reviewAdapter);
        }


        if (placeReviewList != null) {

            if (placeReviewList.size() == 0) {
                lyReviews.setVisibility(View.GONE);
            }
        } else {
            lyReviews.setVisibility(View.GONE);
        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                main_window.scrollTo(0, 0);
            }
        });

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLike_onClick(view);
            }
        });

        //PlaceDetailTask(placeId);

        container_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity()
                        .getSupportFragmentManager();
                fm.popBackStack("MoreInfo", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        btnAllReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReviewListActivity.class);
                intent.putExtra("PlaceId", placeDetail.getPlaceId());
                intent.putExtra("PlaceImageURL", placeDetail.getProfileImageURL());
                intent.putExtra("PlaceName", placeDetail.getPlaceName());
                intent.putExtra("Tags", placeDetail.getCuisines());
                intent.putExtra("AvgRating", placeDetail.getRating());
                intent.putExtra("TotalReview", placeDetail.getReviewCount() + "+ Reviews");
                intent.putExtra("ModeId", 1);

                startActivityForResult(intent, ActivityRequest.REQUEST_REVIEW_LIST);
            }
        });

        rbPlaceRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReviewActivity.class);
                intent.putExtra("PlaceId", placeId);
                intent.putExtra("PlaceName", tvPlaceName.getText().toString());
                intent.putExtra("ModeId", 1);
                startActivityForResult(intent, ActivityRequest.REQUEST_REVIEW);
            }
        });


        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReviewActivity.class);
                intent.putExtra("PlaceId", placeDetail.getPlaceId());
                intent.putExtra("PlaceImageURL", placeDetail.getProfileImageURL());
                intent.putExtra("PlaceName", placeDetail.getPlaceName());
                intent.putExtra("Tags", placeDetail.getCuisines());
                intent.putExtra("AvgRating", placeDetail.getRating());
                intent.putExtra("TotalReview", placeDetail.getReviewCount() + "+ Reviews");
                if (placeDetail.getMyRatingDetail().getReviewsId() == null) {
                    intent.putExtra("ModeId", 1);
                } else {
                    intent.putExtra("RatingDetail", placeDetail.getMyRatingDetail());
                    intent.putExtra("ModeId", 2);
                }
                getActivity().startActivityForResult(intent, ActivityRequest.REQUEST_REVIEW);
            }
        });


        return view;
    }

    public void tvPlaceTimings_onClick(View view) {
        if (placeDetail.getPlaceTimings() != null) {
            showPlaceTimingsDialog();
        }
    }

    private void showPlaceTimingsDialog() {
        Dialog placeTimingsDialog = new Dialog(getActivity());
        placeTimingsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        placeTimingsDialog.setContentView(R.layout.custom_dialog);

        LinearLayout lyContent = placeTimingsDialog.findViewById(R.id.lyContent);
        LinearLayout lyFooter = placeTimingsDialog.findViewById(R.id.lyFooter);

        TextView tvTitle = new TextView(getActivity());
        tvTitle.setText("Opening Hours");
        tvTitle.setTextColor(Color.parseColor("#383838"));
        tvTitle.setTypeface(tvTitle.getTypeface(), Typeface.BOLD);
        tvTitle.setTextSize(getPixelSize(60));
        tvTitle.setPadding(30, 30, 30, 20);

        lyContent.addView(tvTitle);

        for (final String timing : placeDetail.getPlaceTimings()) {

            TextView tvTiming = new TextView(getActivity());
            tvTiming.setText(timing);
            tvTiming.setTextColor(Color.parseColor("#444444"));
            tvTiming.setTextSize(getPixelSize(40));
            tvTiming.setPadding(70, 5, 0, 5);

            lyContent.addView(tvTiming);
        }

        lyFooter.setPadding(0, 0, 0, 40);

        placeTimingsDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        placeTimingsDialog.show();

    }

    private float getPixelSize(float dimens) {
        float scaleRatio = getResources().getDisplayMetrics().density;
        return dimens / scaleRatio;
    }


    public void btnLike_onClick(View view) {
        PlaceLikeTask(placeId, btnLike);
    }

    public void btnBookmark_onClick(View view) {
        AddUserCollectionTask(placeId);
    }

    public void btnDirection_onClick(View view) {
        if (placeDetail.getLatitude() != 0 && placeDetail.getLongitude() != 0) {
            showDirectionDialog();
        }

    }

    public void btnReviews_onClick(View view) {
        Rect rectf = new Rect();
        list_Review.getGlobalVisibleRect(rectf);

        main_window.scrollTo(rectf.left, rectf.top - 100);
    }

    public void btnPhone_onClick(View view) {
        if (placeDetail.getContactNumber() != null) {
            showPhoneDialog();
        }
    }

    private ReviewAdapter.OnItemClickListener onItemClickListener = new ReviewAdapter.OnItemClickListener() {
        @Override
        public void onUserClick(View view, int position, long id, ReviewAdapter.ViewHolder viewHolder) {
            Intent intent = new Intent(getActivity(), UserTimelineActivity.class);
            intent.putExtra("UserId", placeReviewList.get(position).getUserId());
            startActivity(intent);
        }

        @Override
        public void onUserFollowClick(View view, int position, long id, ReviewAdapter.ViewHolder viewHolder) {
            UserFollowTask(position, placeReviewList.get(position).getUserId(), viewHolder);
        }

        @Override
        public void onUserLikeClick(View view, int position, long id, ReviewAdapter.ViewHolder viewHolder) {
            ReviewLikeTask(position, placeReviewList.get(position).getReviewsId(), viewHolder);
        }

        @Override
        public void onUserCommentClick(View view, int position, long id, ReviewAdapter.ViewHolder viewHolder) {
            Intent intent = new Intent(getActivity(), CommentActivity.class);
            intent.putExtra("PlaceId", placeDetail.getPlaceId());
            intent.putExtra("ReviewId", placeReviewList.get(position).getReviewsId());
            startActivityForResult(intent, ActivityRequest.REQUEST_COMMENT);
        }
    };

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
                        if (placeReviewList.get(position).isFollow() == false) {
                            viewHolder.btnFollow.setText("followed");
                            viewHolder.btnFollow.setBackgroundResource(R.drawable.button_user_follow_fill);
                            viewHolder.btnFollow.setTextColor(getResources().getColor(R.color.colorWhite));
                            placeReviewList.get(position).setFollow(true);
                        } else {
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

    public void setPlaceDetail(PlaceDetail placeDetail) {
        this.placeDetail = placeDetail;
        rbPlaceRating.setRating(Float.valueOf(placeDetail.getRating()));
        if (placeDetail.getPlaceReviews() != null) {
            placeReviewList.clear();
            for (int i = 0; i < placeDetail.getPlaceReviews().size(); i++) {
                placeReviewList.add(placeDetail.getPlaceReviews().get(i));
            }
        }
        reviewAdapter.notifyDataSetChanged();
        if (placeReviewList.size() == 0) {
            lyReviews.setVisibility(View.GONE);
        } else {
            lyReviews.setVisibility(View.VISIBLE);
        }
    }

    private void showDirectionDialog() {
        final Dialog directionDialog = new Dialog(getActivity(), R.style.FullViewDialog);
        directionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        directionDialog.setContentView(R.layout.direction_dialog);


        IconTextView btnClose = directionDialog.findViewById(R.id.btnClose);
        TextView tvLocation = directionDialog.findViewById(R.id.tvLocation);
        final MapView mMapView = directionDialog.findViewById(R.id.mapView);

        tvLocation.setText(placeDetail.getPlaceLocation());

        mMapView.onCreate(directionDialog.onSaveInstanceState());

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng latLng = new LatLng(placeDetail.getLatitude(), placeDetail.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(latLng).title(placeDetail.getPlaceName()));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                mMapView.onResume();

                if (checkLocationPermission()) {
                    //googleMap.setMyLocationEnabled(true);
                }
            }
        });

        //directionDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        directionDialog.getWindow().setGravity(Gravity.BOTTOM);
        directionDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        directionDialog.show();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directionDialog.dismiss();
            }
        });

    }


    private void showPhoneDialog() {
        Dialog phoneDialog = new Dialog(getActivity(), R.style.FullViewDialog);
        phoneDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        phoneDialog.setContentView(R.layout.custom_dialog);

        LinearLayout lyContent = phoneDialog.findViewById(R.id.lyContent);
        LinearLayout lyFooter = phoneDialog.findViewById(R.id.lyFooter);

        TextView tvTitle = new TextView(getActivity());
        tvTitle.setText("Tab a number to call");
        tvTitle.setTextColor(Color.parseColor("#e93539"));
        tvTitle.setPadding(30, 60, 30, 10);
        tvTitle.setTextSize(getPixelSize(70));
        tvTitle.setGravity(Gravity.CENTER);

        lyContent.addView(tvTitle);

        for (final String contactNo : placeDetail.getContactNumber()) {

            TextView tvPhoneNo = new TextView(getActivity());
            tvPhoneNo.setText(contactNo);
            tvPhoneNo.setTextColor(Color.parseColor("#3f9d2f"));
            tvPhoneNo.setTextSize(getPixelSize(80));
            tvPhoneNo.setGravity(Gravity.CENTER);

            tvPhoneNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tel = contactNo;
                    if (checkPhonePermission()) {
                        callPhone();
                    }
                }
            });

            lyContent.addView(tvPhoneNo);
        }

        lyFooter.setPadding(0, 0, 0, 60);

        phoneDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        phoneDialog.show();

    }

    private void callPhone() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + tel));
        startActivity(callIntent);
    }

    private boolean checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= 23 && getActivity().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, ActivityRequest.REQUEST_ACCESS_FINE_LOCATION);
            return false;
        }
        return true;
    }

    private boolean checkPhonePermission() {
        if (Build.VERSION.SDK_INT >= 23 && getActivity().checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, ActivityRequest.REQUEST_ACCESS_CALL_PHONE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == ActivityRequest.REQUEST_ACCESS_CALL_PHONE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone();
            }
        } else if (requestCode == ActivityRequest.REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }


    private void showReviewDialog() {
        final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View reviewDialogView = inflater.inflate(R.layout.review_dialog, null);


        final EditText etReviewText = reviewDialogView.findViewById(R.id.etReviewText);

        Button btnCancel = reviewDialogView.findViewById(R.id.btnCancel);
        final Button btnSendReview = reviewDialogView.findViewById(R.id.btnSendReview);

        final AlertDialog reviewDialog = new AlertDialog.Builder(getActivity()).create();

        reviewDialog.setView(reviewDialogView);
        reviewDialog.show();


        btnSendReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewDialog.dismiss();
            }
        });

    }


    private void PlaceLikeTask(final String PlaceId, final IconButton btnLike) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {

            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.PlaceLike(PlaceId);
                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                try {

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {
                        if (placeDetail.isLike() == false) {
                            btnLike.setTextColor(getResources().getColor(R.color.colorLike));
                            btnLike.setText("Liked");
                            placeDetail.setLike(true);

                            placeDetail.setLikeCount(placeDetail.getLikeCount() + 1);
                            tvLikeCount.setText(placeDetail.getLikeCount() + " Likes");
                        } else {
                            btnLike.setTextColor(getResources().getColor(R.color.colorNotLike));
                            btnLike.setText("Like");
                            placeDetail.setLike(false);

                            placeDetail.setLikeCount(placeDetail.getLikeCount() - 1);
                            tvLikeCount.setText(placeDetail.getLikeCount() + " Likes");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }

    private void AddUserCollectionTask(final String PlaceId) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {

            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.AddUserCollection(PlaceId);
                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                try {

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {
                        if (placeDetail.isBookmark() == false) {
                            //setTextViewDrawableColor(btnBookmark, Color.parseColor("#f93324"));
                            btnBookmark.setTextColor(getResources().getColor(R.color.colorBookmark));
                            itvBookmark.setTextColor(getResources().getColor(R.color.colorBookmark));
                            placeDetail.setBookmark(true);
                        } else {
                            //setTextViewDrawableColor(btnBookmark, Color.parseColor("#8d8d8d"));
                            btnBookmark.setTextColor(getResources().getColor(R.color.colorNotBookmark));
                            itvBookmark.setTextColor(getResources().getColor(R.color.colorNotBookmark));
                            placeDetail.setBookmark(false);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }


    private void PlaceReviewTask(final String PlaceId, final int Skip, final int PageSize) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                 /* shimmer_PlaceReviewLoading.startShimmer();
                  shimmer_PlaceReviewLoading.setVisibility(View.VISIBLE);*/
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.PlaceReview(PlaceId, Skip, PageSize);
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

                        placeReviewList = gson.fromJson(jsonObject.getJSONArray("ReviewData").toString(), type);

                        ReviewAdapter reviewAdapter = new ReviewAdapter(getActivity(), placeReviewList);
                        reviewAdapter.setOnItemClickListener(onItemClickListener);
                        list_Review.setAdapter(reviewAdapter);

                        if (placeReviewList.size() != 0) {
                            lyReviews.setVisibility(View.VISIBLE);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

//                  shimmer_PlaceReviewLoading.stopShimmer();
//                  shimmer_PlaceReviewLoading.setVisibility(View.GONE);
            }
        }.execute();
    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            }
        }
    }


    private void sharePlace() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.share_text));
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


    private void initViews(View view) {
        btnReview = view.findViewById(R.id.btnReview);
        lyMainWindow = view.findViewById(R.id.lyMainWindow);
        container_back_button = view.findViewById(R.id.container_back_button);
        main_window = view.findViewById(R.id.main_window);
        ivPlaceImage= view.findViewById(R.id.ivPlaceImage);
        tvPlaceName = view.findViewById(R.id.tvPlaceName);
        tvTags = view.findViewById(R.id.tvTags);
        tvAvgRating = view.findViewById(R.id.tvAvgRating);
        tvTotalReview = view.findViewById(R.id.tvTotalReview);
        tvPlaceLocation = view.findViewById(R.id.tvPlaceLocation);
        tvPlaceTimings = view.findViewById(R.id.tvPlaceTimings);
        tvPlaceAbout = view.findViewById(R.id.tvPlaceAbout);
        tvLikeCount = view.findViewById(R.id.tvLikeCount);
        btnLike = view.findViewById(R.id.btnLike);
        btnBookmark = view.findViewById(R.id.btnBookmark);
        spPlaceTimings = view.findViewById(R.id.spPlaceTimings);
        flCuisine = view.findViewById(R.id.flCuisine);
        lyMoreInfo = view.findViewById(R.id.lyMoreInfo);
        lyMoreInfoLeft = view.findViewById(R.id.lyMoreInfoLeft);
        lyMoreInfoRight = view.findViewById(R.id.lyMoreInfoRight);
        rbPlaceRating = view.findViewById(R.id.rbPlaceRating);
        tvTrustworthyText = view.findViewById(R.id.tvTrustworthyText);
        list_Review = view.findViewById(R.id.list_Review);
        btnAllReviews = view.findViewById(R.id.btnAllReviews);
        lyReviews = view.findViewById(R.id.lyReviews);
        rgPlaceRating = view.findViewById(R.id.rgPlaceRating);
        itvBookmark = view.findViewById(R.id.itvBookmark);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PlaceDetailActivity.isMoreInfoOpen = false;
    }
}
