package pk.com.pieinthesky.app;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pk.com.pieinthesky.app.beans.ReservationDetail;
import pk.com.pieinthesky.app.connectivity.ServiceManager;
import pk.com.pieinthesky.app.helper.ActivityRequest;
import pk.com.pieinthesky.app.helper.UIHelper;
import pk.com.pieinthesky.app.utils.BackgroundRequest;
import pk.com.pieinthesky.app.widget.RoundCornerImageView;

import org.json.JSONObject;

public class TableBookingDetailActivity extends AppCompatActivity  {

    private ServiceManager serviceManager = new ServiceManager();

    ProgressDialog dialog;

    private RoundCornerImageView ivPlaceImage;
    private TextView tvPlaceName;
    private TextView tvTags;
    private TextView tvAvgRating;
    private TextView tvTotalReview;
    private TextView tvReservationStatus;
    private TextView ReservationMessage;
    private TextView tvReservationNo;
    private TextView tvReservationDate;
    private TextView tvNoOfPerson;
    private TextView tvByName;
    private TextView tvContactNo;
    private TextView tvPlaceLocation;
    private LinearLayout lyCopy;
    private LinearLayout lyCall;
    private ProgressBar more_progress;
    private RelativeLayout rlBookingDetail;
    private Button btnCancel;

    private int reservationTypeId;
    private String reservationId = null;
    private ReservationDetail reservationDetail;
    private String tel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_booking_detail);
        getSupportActionBar().setTitle("Reservation Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ivPlaceImage = findViewById(R.id.ivPlaceImage);
        tvPlaceName = findViewById(R.id.tvPlaceName);
        tvTags = findViewById(R.id.tvTags);
        tvAvgRating = findViewById(R.id.tvAvgRating);
        tvTotalReview = findViewById(R.id.tvTotalReview);
        tvReservationStatus = findViewById(R.id.tvReservationStatus);
        ReservationMessage = findViewById(R.id.ReservationMessage);
        tvReservationNo = findViewById(R.id.tvReservationNo);
        tvReservationDate = findViewById(R.id.tvReservationDate);
        tvNoOfPerson = findViewById(R.id.tvNoOfPerson);
        tvByName = findViewById(R.id.tvByName);
        tvContactNo = findViewById(R.id.tvContactNo);
        tvPlaceLocation = findViewById(R.id.tvPlaceLocation);
        lyCopy = findViewById(R.id.lyCopy);
        lyCall = findViewById(R.id.lyCall);
        more_progress = findViewById(R.id.more_progress);
        rlBookingDetail = findViewById(R.id.rlBookingDetail);
        btnCancel = findViewById(R.id.btnCancel);

        reservationTypeId = getIntent().getIntExtra("ReservationTypeId", 0);
        reservationId = getIntent().getStringExtra("ReservationId");

        if (reservationTypeId == 2) {
            btnCancel.setVisibility(View.GONE);
        }

        lyCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("ADDRESS", tvPlaceLocation.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(TableBookingDetailActivity.this, "Location Copied.", Toast.LENGTH_SHORT).show();
            }
        });

        lyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reservationDetail.getPlaceContactNo() != null) {
                    showPhoneDialog();
                }
            }
        });

        ReservationDetailTask(reservationId);
    }

    public void ReservationDetailTask(final String ReservationId) {
        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                rlBookingDetail.setVisibility(View.GONE);
                more_progress.setVisibility(View.VISIBLE);
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.ReservationDetail(ReservationId);

                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                rlBookingDetail.setVisibility(View.VISIBLE);
                more_progress.setVisibility(View.GONE);
                try {
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    final boolean status = jsonObject.getBoolean("Status");

                    if (status) {

                        reservationDetail = gson.fromJson(jsonObject.getJSONObject("ReservationData").toString(), ReservationDetail.class);

                        tvPlaceName.setText(reservationDetail.getPlaceName());
                        tvTags.setText(reservationDetail.getCuisines());
                        Glide.with(TableBookingDetailActivity.this)
                                .load(reservationDetail.getPlaceImageURL())
                                .centerCrop()
                                .placeholder(R.drawable.image_place_loading)
                                .into(ivPlaceImage);

                        tvAvgRating.setText(reservationDetail.getBusinessAvgRating());
                        tvTotalReview.setText(reservationDetail.getTotalReview() + "+ Reviews");

                        tvReservationStatus.setText(reservationDetail.getReservationStatus());
                        ReservationMessage.setText(reservationDetail.getReservationMessage());
                        tvReservationNo.setText(reservationDetail.getReservationNo());
                        tvReservationDate.setText(reservationDetail.getReservationDate());
                        tvNoOfPerson.setText(String.valueOf(reservationDetail.getNoOfGuest()));
                        tvByName.setText(reservationDetail.getByName());
                        tvContactNo.setText(reservationDetail.getContactNo());
                        tvPlaceLocation.setText(reservationDetail.getPlaceLocation());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
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

        for (final String contactNo : reservationDetail.getPlaceContactNo()) {

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

    private float getPixelSize(float dimens) {
        float scaleRatio = getResources().getDisplayMetrics().density;
        return dimens / scaleRatio;
    }


    private void callPhone() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + tel));
        startActivity(callIntent);
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
        }
    }

    private void CancelReservationTask(final String ReservationId, final String Reason) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                dialog = new ProgressDialog(TableBookingDetailActivity.this);
                dialog.setMessage("Please wait...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.CancelReservation(Configuration.getUserId(), ReservationId, Reason);
                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                dialog.dismiss();
                try {

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {

                        setResult(RESULT_OK);
                        finish();
                    } else {
                        UIHelper.showErrorDialog(TableBookingDetailActivity.this, "", jsonObject.getString("Message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute();
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

    public void btnCancel_onClick(View view) {
        UIHelper.showConfirmDialog(this, "", "Are you sure to cancel reservation?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                CancelReservationTask(reservationId, "");
            }
        });
    }

}
