package pk.com.pieinthesky.app;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.facebook.shimmer.ShimmerFrameLayout;
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
import pk.com.pieinthesky.app.adapter.ReviewAdapter;
import pk.com.pieinthesky.app.beans.Cuisine;
import pk.com.pieinthesky.app.beans.MediaDetail;
import pk.com.pieinthesky.app.beans.PlaceDetail;
import pk.com.pieinthesky.app.beans.PlaceReview;
import pk.com.pieinthesky.app.beans.Tag;
import pk.com.pieinthesky.app.connectivity.ServiceManager;
import pk.com.pieinthesky.app.helper.ActivityRequest;
import pk.com.pieinthesky.app.helper.UIHelper;
import pk.com.pieinthesky.app.utils.BackgroundRequest;
import pk.com.pieinthesky.app.widget.IconButton;
import pk.com.pieinthesky.app.widget.IconTextView;
import pk.com.pieinthesky.app.widget.NonScrollListView;
import pk.com.pieinthesky.app.widget.RatingGraph;
import pk.com.pieinthesky.app.widget.RoundCornerImageView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlaceDetailActivity extends AppCompatActivity {

    //private ProgressDialog dialog;

    private ServiceManager serviceManager = new ServiceManager();

    private String placeId = null;
    private String tel = "";
    private ShimmerFrameLayout shimmer_PlaceLoading;
    private ShimmerFrameLayout shimmer_PlaceReviewLoading;
    private CardView cvNavigation;
    private LinearLayout lyMainWindow;

    private CollapsingToolbarLayout collapsingToolbarLayout;

    private NestedScrollView main_window;

    private ImageSlider imageSlider;
    private TextView tvPlaceName;
    private TextView tvPlaceSubTitle;
    private LinearLayout lyAvgRating;
    private CardView cvAvgRating;
    private TextView tvAvgRating;
    private TextView tvTotalReview;
    private TextView tvTags;
    private TextView tvPlaceLocation;
    private TextView tvPlaceTimings;
    private TextView tvPlaceAbout;
    private TextView tvLikeCount;
    private IconButton btnLike;
    private IconButton btnBookmark;
    private Spinner spPlaceTimings;
    private LinearLayout lyCheckIn;
    private ImageView ivCheckIn;
    private TextView tvHerePeople;
    private LinearLayout lyMenu;
    private FlexboxLayout flPhoto;
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
    private LinearLayout barBtnReview;
    private LinearLayout barBtnBookmark;
    private LinearLayout barBtnDirection;
    private LinearLayout barBtnPhone;
    private LinearLayout lySliderOverlay;
    private TextView lbl_location;
    private PlaceDetail placeDetail = null;
    private List<PlaceReview> placeReviewList = null;
    private LinearLayout layout_order_online;
    private LinearLayout layout_table_reservation;
    private LinearLayout layout_more_info;
    private LinearLayout copy_layout;
    private LinearLayout layout_get_direction;
    private TextView tvOrderOnlineDetail;
    private TextView tvTableBookingDetail;

    public static boolean isMoreInfoOpen = false;
    private MoreInfoFragment moreInfoFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar.setTitle("");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();

            window.setStatusBarColor(Color.TRANSPARENT);
        }

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    collapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
                } else if (isShow) {
                    isShow = false;
                    collapsingToolbarLayout.setTitle(" ");
                }
            }
        });

        layout_get_direction = findViewById(R.id.layout_get_direction);
        copy_layout = findViewById(R.id.copy_layout);
        shimmer_PlaceLoading = findViewById(R.id.shimmer_PlaceLoading);
        shimmer_PlaceReviewLoading = findViewById(R.id.shimmer_PlaceReviewLoading);
        cvNavigation = findViewById(R.id.cvNavigation);
        lySliderOverlay = findViewById(R.id.lySliderOverlay);
        lyMainWindow = findViewById(R.id.lyMainWindow);

        shimmer_PlaceReviewLoading.stopShimmer();
        shimmer_PlaceReviewLoading.setVisibility(View.GONE);

        shimmer_PlaceLoading.setVisibility(View.VISIBLE);

        cvNavigation.setVisibility(View.GONE);
        lySliderOverlay.setVisibility(View.GONE);
        lyMainWindow.setVisibility(View.GONE);

        placeId = getIntent().getStringExtra("PlaceId");

        main_window = findViewById(R.id.main_window);
        imageSlider = findViewById(R.id.image_slider);
        tvPlaceName = findViewById(R.id.tvPlaceName);
        tvPlaceSubTitle = findViewById(R.id.tvPlaceSubTitle);
        lyAvgRating = findViewById(R.id.lyAvgRating);
        cvAvgRating = findViewById(R.id.cvAvgRating);
        tvAvgRating = findViewById(R.id.tvAvgRating);
        tvTotalReview = findViewById(R.id.tvTotalReview);
        tvTags = findViewById(R.id.tvTags);
        tvPlaceLocation = findViewById(R.id.tvPlaceLocation);
        tvPlaceTimings = findViewById(R.id.tvPlaceTimings);
        tvPlaceAbout = findViewById(R.id.tvPlaceAbout);
        tvLikeCount = findViewById(R.id.tvLikeCount);
        btnLike = findViewById(R.id.btnLike);
        btnBookmark = findViewById(R.id.btnBookmark);
        spPlaceTimings = findViewById(R.id.spPlaceTimings);
        lyCheckIn = findViewById(R.id.lyCheckIn);
        ivCheckIn = findViewById(R.id.ivCheckIn);
        tvHerePeople = findViewById(R.id.tvHerePeople);
        lyMenu = findViewById(R.id.lyMenu);
        flPhoto = findViewById(R.id.flPhoto);
        flCuisine = findViewById(R.id.flCuisine);
        lyMoreInfo = findViewById(R.id.lyMoreInfo);
        lyMoreInfoLeft = findViewById(R.id.lyMoreInfoLeft);
        lyMoreInfoRight = findViewById(R.id.lyMoreInfoRight);
        rbPlaceRating = findViewById(R.id.rbPlaceRating);
        tvTrustworthyText = findViewById(R.id.tvTrustworthyText);
        list_Review = findViewById(R.id.list_Review);
        btnAllReviews = findViewById(R.id.btnAllReviews);
        lyReviews = findViewById(R.id.lyReviews);
        rgPlaceRating = findViewById(R.id.rgPlaceRating);
        itvBookmark = findViewById(R.id.itvBookmark);
        barBtnReview = findViewById(R.id.barBtnReview);
        barBtnBookmark = findViewById(R.id.barBtnBookmark);
        barBtnDirection = findViewById(R.id.barBtnDirection);
        barBtnPhone = findViewById(R.id.barBtnPhone);
        layout_table_reservation = findViewById(R.id.layout_table_reservation);
        layout_order_online = findViewById(R.id.layout_order_online);
        layout_more_info = findViewById(R.id.layout_more_info);
        lbl_location = findViewById(R.id.lbl_location);
        tvOrderOnlineDetail = findViewById(R.id.tvOrderOnlineDetail);
        tvTableBookingDetail = findViewById(R.id.tvTableBookingDetail);

        lyAvgRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAllReviews_onClick(view);
            }
        });

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLike_onClick(view);
            }
        });

        barBtnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnReview_onClick(view);
            }
        });


        barBtnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnBookmark_onClick(view);
            }
        });


        barBtnDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDirection_onClick(view);
            }
        });


        barBtnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPhone_onClick(view);
            }
        });

        layout_order_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (placeDetail.isOrderOnlineAvailable()) {
                    OrderOnlineFragment dialog = new OrderOnlineFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("PlaceId", placeDetail.getPlaceId());
                    bundle.putString("GSTPercentage", placeDetail.getGSTPercentage());
                    bundle.putString("DeliveryCharges", placeDetail.getDeliveryCharges());
                    bundle.putString("PlaceImageURL", placeDetail.getProfileImageURL());
                    bundle.putString("PlaceName", placeDetail.getPlaceName());
                    bundle.putString("Tags", placeDetail.getCuisines());
                    bundle.putString("AvgRating", placeDetail.getRating());
                    bundle.putString("TotalReview", placeDetail.getReviewCount() + "+ Reviews");
                    dialog.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up, R.anim.slide_in_up, R.anim.slide_out_up)
                            .add(android.R.id.content, dialog)
                            .addToBackStack("PlaceDetail")
                            .commit();
                }
            }
        });

        layout_table_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (placeDetail.isTableReservationAvailable()) {
                    TableReservationFragment dialog = new TableReservationFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("PlaceId", placeDetail.getPlaceId());
                    bundle.putString("PlaceImageURL", placeDetail.getProfileImageURL());
                    bundle.putString("PlaceName", placeDetail.getPlaceName());
                    bundle.putString("Tags", placeDetail.getCuisines());
                    bundle.putString("AvgRating", placeDetail.getRating());
                    bundle.putString("TotalReview", placeDetail.getReviewCount() + "+ Reviews");
                    dialog.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up, R.anim.slide_in_up, R.anim.slide_out_up)
                            .add(android.R.id.content, dialog)
                            .addToBackStack("PlaceDetail")
                            .commit();
                }
            }
        });

        layout_more_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                moreInfoFragment = new MoreInfoFragment(placeDetail);
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)
                        .add(android.R.id.content, moreInfoFragment)
                        .addToBackStack("MoreInfo")
                        .commit();
                isMoreInfoOpen = true;
               /* FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment fragment = MoreInfoFragment.newInstance(placeDetail);
                ft.add(android.R.id.content, fragment);
                ft.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
                ft.addToBackStack("MoreInfo");
                ft.commit();
*/
            }
        });

        copy_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("ADDRESS", lbl_location.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(PlaceDetailActivity.this, "Location Copied.", Toast.LENGTH_SHORT).show();
            }
        });

        layout_get_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Geocoder coder = new Geocoder(PlaceDetailActivity.this);
                List<Address> address;

                try {
                    address = coder.getFromLocationName(lbl_location.getText().toString(), 1);
                    if (address != null) {
                        Address location = address.get(0);
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?daddr=" + location.getLatitude() + "," + location.getLongitude()));
                        startActivity(intent);
                    }
                } catch (Exception e) {

                }


            }
        });

        lyCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceVisitedTask(placeId);
            }
        });

        PlaceDetailTask(placeId);
    }


    public void btnReview_onClick(View view) {
        Intent intent = new Intent(this, ReviewActivity.class);
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
        startActivityForResult(intent, ActivityRequest.REQUEST_REVIEW);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ActivityRequest.REQUEST_REVIEW && resultCode == RESULT_OK) {
            placeDetail.setRating(String.valueOf(data.getFloatExtra("PlaceRating", 0)));
            rbPlaceRating.setRating(data.getFloatExtra("PlaceRating", 0));
            PlaceReviewTask(placeId, 0, 20);
        } else if (requestCode == ActivityRequest.REQUEST_COMMENT && resultCode == RESULT_OK) {
            boolean isChange = data.getBooleanExtra("IsChange", false);
            if (isChange) {
                PlaceReviewTask(placeId, 0, 20);
            }
        }
    }

    public void tvPlaceTimings_onClick(View view) {
        if (placeDetail.getPlaceTimings() != null) {
            showPlaceTimingsDialog();
        }
    }

    private void showPlaceTimingsDialog() {
        Dialog placeTimingsDialog = new Dialog(this);
        placeTimingsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        placeTimingsDialog.setContentView(R.layout.custom_dialog);

        LinearLayout lyContent = placeTimingsDialog.findViewById(R.id.lyContent);
        LinearLayout lyFooter = placeTimingsDialog.findViewById(R.id.lyFooter);

        TextView tvTitle = new TextView(this);
        tvTitle.setText("Opening Hours");
        tvTitle.setTextColor(Color.parseColor("#383838"));
        tvTitle.setTypeface(tvTitle.getTypeface(), Typeface.BOLD);
        tvTitle.setTextSize(getPixelSize(60));
        tvTitle.setPadding(30, 30, 30, 20);

        lyContent.addView(tvTitle);

        for (final String timing : placeDetail.getPlaceTimings()) {

            TextView tvTiming = new TextView(this);
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

    private void showDirectionDialog() {
        final Dialog directionDialog = new Dialog(this, R.style.FullViewDialog);
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


    public void btnAllReviews_onClick(View view) {
        Intent intent = new Intent(this, ReviewListActivity.class);
        intent.putExtra("PlaceId", placeDetail.getPlaceId());
        intent.putExtra("PlaceImageURL", placeDetail.getProfileImageURL());
        intent.putExtra("PlaceName", placeDetail.getPlaceName());
        intent.putExtra("Tags", placeDetail.getCuisines());
        intent.putExtra("AvgRating", placeDetail.getRating());
        intent.putExtra("TotalReview", placeDetail.getReviewCount() + "+ Reviews");
        intent.putExtra("ModeId", 1);

        startActivityForResult(intent, ActivityRequest.REQUEST_REVIEW_LIST);
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

    private void showPhoneDialog() {
        Dialog phoneDialog = new Dialog(this, R.style.FullViewDialog);
        phoneDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        phoneDialog.setContentView(R.layout.custom_dialog);

        LinearLayout lyContent = phoneDialog.findViewById(R.id.lyContent);
        LinearLayout lyFooter = phoneDialog.findViewById(R.id.lyFooter);

        TextView tvTitle = new TextView(this);
        tvTitle.setText("Tab a number to call");
        tvTitle.setTextColor(Color.parseColor("#e93539"));
        tvTitle.setPadding(30, 60, 30, 10);
        tvTitle.setTextSize(getPixelSize(70));
        tvTitle.setGravity(Gravity.CENTER);

        lyContent.addView(tvTitle);

        for (final String contactNo : placeDetail.getContactNumber()) {

            TextView tvPhoneNo = new TextView(this);
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
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, ActivityRequest.REQUEST_ACCESS_FINE_LOCATION);
            return false;
        }
        return true;
    }

    private boolean checkPhonePermission() {
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

    private float getPixelSize(float dimens) {
        float scaleRatio = getResources().getDisplayMetrics().density;
        return dimens / scaleRatio;
    }

    private void showReviewDialog() {
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View reviewDialogView = inflater.inflate(R.layout.review_dialog, null);


        final EditText etReviewText = reviewDialogView.findViewById(R.id.etReviewText);

        Button btnCancel = reviewDialogView.findViewById(R.id.btnCancel);
        final Button btnSendReview = reviewDialogView.findViewById(R.id.btnSendReview);

        final AlertDialog reviewDialog = new AlertDialog.Builder(this).create();

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


    private void PlaceVisitedTask(final String PlaceId) {

        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View checkInDialogLayout = inflater.inflate(R.layout.check_in_dialog, null);

        RoundCornerImageView ivPlaceImage = checkInDialogLayout.findViewById(R.id.ivPlaceImage);
        TextView tvPlaceName = checkInDialogLayout.findViewById(R.id.tvPlaceName);
        TextView tvLocation = checkInDialogLayout.findViewById(R.id.tvLocation);
        final ProgressBar pbCheckIn = checkInDialogLayout.findViewById(R.id.pbCheckIn);
        final LinearLayout lyCheckInSuccess = checkInDialogLayout.findViewById(R.id.lyCheckInSuccess);

        Glide.with(this)
                .load(placeDetail.getProfileImageURL())
                .centerCrop()
                .placeholder(R.drawable.image_place_loading)
                .into(ivPlaceImage);
        tvPlaceName.setText(placeDetail.getPlaceName());
        tvLocation.setText(placeDetail.getPlaceLocation());

        lyCheckInSuccess.setVisibility(View.GONE);

        final AlertDialog checkInDialog = new AlertDialog.Builder(this).create();
        checkInDialog.setView(checkInDialogLayout);
        checkInDialog.setCancelable(false);

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                checkInDialog.show();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.PlaceVisited(PlaceId, 0, 0);
                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {

                try {

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {
                        pbCheckIn.setVisibility(View.GONE);
                        lyCheckInSuccess.setVisibility(View.VISIBLE);

                        tvHerePeople.setText(jsonObject.getString("VistorText"));
                        ivCheckIn.setImageResource(R.drawable.check_in_green);
                        lyCheckIn.setEnabled(false);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkInDialog.dismiss();
                    }
                }, 1200);


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
                shimmer_PlaceReviewLoading.startShimmer();
                shimmer_PlaceReviewLoading.setVisibility(View.VISIBLE);
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

                        placeDetail.setPlaceReviews(placeReviewList);

                        ReviewAdapter reviewAdapter = new ReviewAdapter(PlaceDetailActivity.this, placeReviewList);
                        reviewAdapter.setOnItemClickListener(onItemClickListener);
                        list_Review.setAdapter(reviewAdapter);

                        if (placeReviewList.size() != 0) {
                            lyReviews.setVisibility(View.VISIBLE);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                shimmer_PlaceReviewLoading.stopShimmer();
                shimmer_PlaceReviewLoading.setVisibility(View.GONE);

                if (isMoreInfoOpen) {
                    moreInfoFragment.setPlaceDetail(placeDetail);
                }

            }
        }.execute();
    }


    private void PlaceDetailTask(final String PlaceId) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {

            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.PlaceDetail(PlaceId);

                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                try {
                    Type type;
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {

                        type = new TypeToken<PlaceDetail>() {
                        }.getType();

                        placeDetail = gson.fromJson(jsonObject.getJSONObject("PlaceData").toString(), type);

                        if (placeDetail.getBannerPictures() != null) {
                            List<SlideModel> imageList = new ArrayList<SlideModel>();

                            for (MediaDetail mediaDetail : placeDetail.getBannerPictures()) {
                                if (mediaDetail.getDescription().equals("")) {
                                    mediaDetail.setDescription(null);
                                }
                                imageList.add(new SlideModel(mediaDetail.getUrl(), mediaDetail.getDescription(), true));
                            }
                            imageSlider.setImageList(imageList, true);
                        } else {
                            imageSlider.setVisibility(View.GONE);
                        }

                        tvPlaceName.setText(placeDetail.getPlaceName());
                        tvPlaceSubTitle.setText(placeDetail.getPlaceSubTitle());
                        tvAvgRating.setText(placeDetail.getRating());
                        tvTotalReview.setText(placeDetail.getReviewCount() + "+ Reviews");
                        tvHerePeople.setText(placeDetail.getVistorText());
                        //tvPlaceLocation.setText(placeDetail.getPlaceLocation());
                        lbl_location.setText(placeDetail.getPlaceLocation());
                        tvPlaceAbout.setText(placeDetail.getPlaceAbout());
                        tvLikeCount.setText(placeDetail.getLikeCount() + " Likes");

                        rgPlaceRating.setAvgRating(placeDetail.getRating());
                        rgPlaceRating.setTotalReviews(placeDetail.getReviewCount() + " reviews");
                        rgPlaceRating.setStar(5, 5, 3, 2, 8);

                        if (placeDetail.isLike()) {
                            btnLike.setTextColor(getResources().getColor(R.color.colorLike));
                            btnLike.setText("Liked");
                        }


                        if (placeDetail.isBookmark()) {
                            //setTextViewDrawableColor(btnBookmark, Color.parseColor("#f93324"));
                            btnBookmark.setTextColor(getResources().getColor(R.color.colorBookmark));
                            itvBookmark.setTextColor(getResources().getColor(R.color.colorBookmark));
                        }

                        if (placeDetail.getRating() != null && placeDetail.getRating().equals("") == false) {
                            rbPlaceRating.setRating(Float.valueOf((placeDetail.getRating())));
                        }

                        if (placeDetail.getPlaceTimings() != null) {
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(PlaceDetailActivity.this, R.layout.timing_spinner_item, placeDetail.getPlaceTimings());
                            spinnerArrayAdapter.setDropDownViewResource(R.layout.timing_spinner_item);
                            spPlaceTimings.setAdapter(spinnerArrayAdapter);

                            tvPlaceTimings.setText(placeDetail.getPlaceTimings().get(0));
                        }

                        tvTags.setText(placeDetail.getCuisines());

                        //5dp margin in pixel
                        int cuisineMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
                        FlexboxLayout.LayoutParams cuisineLayoutParams = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
                        cuisineLayoutParams.setMargins(cuisineMargin, cuisineMargin, cuisineMargin, cuisineMargin);

                        flCuisine.removeAllViews();
                        if (placeDetail.getCuisine() != null) {
                            for (Cuisine cuisine : placeDetail.getCuisine()) {
                                final TextView tvCuisine = new TextView(new ContextThemeWrapper(PlaceDetailActivity.this, R.style.TagTextView));
                                tvCuisine.setText(cuisine.getCuisineName());

                                tvCuisine.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(PlaceDetailActivity.this, SearchActivity.class);
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
                                TextView tvTag = new TextView(new ContextThemeWrapper(PlaceDetailActivity.this, R.style.InfoTextView));
                                tvTag.setText(tag.getTagsName());

                                if (tagIndex % 2 == 0) {
                                    lyMoreInfoLeft.addView(tvTag);
                                } else {
                                    lyMoreInfoRight.addView(tvTag);
                                }
                                tagIndex++;
                            }
                        }


                        //10dp margin in pixel
                        int menuImageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                        int menuImageSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams menuLayoutParams = new LinearLayout.LayoutParams(menuImageSize, menuImageSize);

                        menuLayoutParams.setMargins(0, 0, menuImageMargin, 0);

                        final ArrayList<String> menuList = new ArrayList<String>();

                        lyMenu.removeAllViews();
                        if (placeDetail.getPlaceMenu() != null) {
                            for (int i = 0; i < placeDetail.getPlaceMenu().size(); i++) {
                                menuList.add(placeDetail.getPlaceMenu().get(i).getUrl());

                                ImageView ivMenuImage = new ImageView(PlaceDetailActivity.this);


                                final int pictureIndex = i;
                                ivMenuImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(PlaceDetailActivity.this, ImageViewerActivity.class);
                                        intent.putStringArrayListExtra("PictureList", menuList);
                                        intent.putExtra("PictureIndex", pictureIndex);
                                        startActivity(intent);
                                    }
                                });

                                Glide.with(PlaceDetailActivity.this)
                                        .load(placeDetail.getPlaceMenu().get(i).getUrl())
                                        .centerCrop()
                                        .placeholder(R.drawable.image_slider_loading)
                                        .into(ivMenuImage);

                                lyMenu.addView(ivMenuImage, menuLayoutParams);
                            }
                        }


                        //10dp margin in pixel
                        int placeImageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                        int placeImageSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                        FlexboxLayout.LayoutParams placeImageLayoutParams = new FlexboxLayout.LayoutParams(placeImageSize, placeImageSize);


                        placeImageLayoutParams.setMargins(0, 0, placeImageMargin, placeImageMargin);

                        final ArrayList<String> pictureList = new ArrayList<String>();

                        flPhoto.removeAllViews();
                        if (placeDetail.getPlacePictures() != null) {
                            for (int i = 0; i < placeDetail.getPlacePictures().size(); i++) {
                                pictureList.add(placeDetail.getPlacePictures().get(i).getUrl());
                                ImageView ivPlaceImage = new ImageView(PlaceDetailActivity.this);

                                final int pictureIndex = i;
                                ivPlaceImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(PlaceDetailActivity.this, ImageViewerActivity.class);
                                        intent.putStringArrayListExtra("PictureList", pictureList);
                                        intent.putExtra("PictureIndex", pictureIndex);
                                        startActivity(intent);
                                    }
                                });


                                Glide.with(PlaceDetailActivity.this)
                                        .load(placeDetail.getPlacePictures().get(i).getUrl())
                                        .centerCrop()
                                        .placeholder(R.drawable.image_slider_loading)
                                        .into(ivPlaceImage);

                                flPhoto.addView(ivPlaceImage, placeImageLayoutParams);

                            }
                        }


                        tvTrustworthyText.setText(placeDetail.getTrustworthyText());

                        placeReviewList = placeDetail.getPlaceReviews();

                        if (placeReviewList != null) {
                            ReviewAdapter reviewAdapter = new ReviewAdapter(PlaceDetailActivity.this, placeReviewList);
                            reviewAdapter.setOnItemClickListener(onItemClickListener);
                            list_Review.setAdapter(reviewAdapter);

                            if (placeReviewList.size() == 0) {
                                lyReviews.setVisibility(View.GONE);
                            }
                        } else {
                            lyReviews.setVisibility(View.GONE);
                        }

                        if (placeDetail.isAllowCheckIn() == false) {
                            ivCheckIn.setImageResource(R.drawable.check_in_green);
                            lyCheckIn.setEnabled(false);
                        }

                        if (placeDetail.isOrderOnline() == false) {
                            layout_order_online.setVisibility(View.GONE);
                        }

                        if (placeDetail.isTableReservation() == false) {
                            layout_table_reservation.setVisibility(View.GONE);
                        }

                        if (placeDetail.isOrderOnlineAvailable() == false) {
                            tvOrderOnlineDetail.setText("Currently not available");
                            tvOrderOnlineDetail.setTextColor(getResources().getColor(R.color.colorRed));
                        }

                        if (placeDetail.isTableReservationAvailable() == false) {
                            tvTableBookingDetail.setText("Currently not available");
                            tvTableBookingDetail.setTextColor(getResources().getColor(R.color.colorRed));
                        }

                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                main_window.scrollTo(0, 0);
                            }
                        });


                    } else {
                        UIHelper.showShortToast(PlaceDetailActivity.this, jsonObject.getString("Message"));
                    }


                } catch (Exception e) {
                    finish();
                    e.printStackTrace();
                }

                cvNavigation.setVisibility(View.VISIBLE);
                lySliderOverlay.setVisibility(View.VISIBLE);
                lyMainWindow.setVisibility(View.VISIBLE);

                shimmer_PlaceLoading.stopShimmer();
                shimmer_PlaceLoading.setVisibility(View.GONE);

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

    private ReviewAdapter.OnItemClickListener onItemClickListener = new ReviewAdapter.OnItemClickListener() {
        @Override
        public void onUserClick(View view, int position, long id, ReviewAdapter.ViewHolder viewHolder) {
            Intent intent = new Intent(PlaceDetailActivity.this, UserTimelineActivity.class);
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
            Intent intent = new Intent(PlaceDetailActivity.this, CommentActivity.class);
            intent.putExtra("PlaceId", placeDetail.getPlaceId());
            intent.putExtra("ReviewId", placeReviewList.get(position).getReviewsId());
            startActivityForResult(intent, ActivityRequest.REQUEST_COMMENT);
        }
    };

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.place, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_bookmark) {
            Intent intent = new Intent(this, BookmarkActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_share) {
            sharePlace();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
