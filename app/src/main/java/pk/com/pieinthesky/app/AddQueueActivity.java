package pk.com.pieinthesky.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pk.com.pieinthesky.app.beans.QueueDetail;
import pk.com.pieinthesky.app.beans.User;
import pk.com.pieinthesky.app.connectivity.ServiceManager;
import pk.com.pieinthesky.app.helper.ActivityRequest;
import pk.com.pieinthesky.app.helper.UIHelper;
import pk.com.pieinthesky.app.utils.BackgroundRequest;
import pk.com.pieinthesky.app.utils.BarcodeScanner;
import pk.com.pieinthesky.app.widget.RoundCornerImageView;

import org.json.JSONObject;

import java.util.ArrayList;

public class AddQueueActivity extends AppCompatActivity {

    ServiceManager serviceManager = new ServiceManager();
    private boolean isSearchQR = false;
    private boolean isFindPlace = false;

    private RoundCornerImageView ivPlaceImage;
    private TextView tvPlaceName;
    private TextView tvTags;
    private TextView tvPlaceLocation;
    private EditText etFullName;
    private EditText etMobileNo;
    private EditText etNoOfPersons;
    private EditText etSpecialInstruction;
    private Button btnAddQueue;

    private MediaPlayer beepPlayer;
    private static final float BEEP_VOLUME = 0.10f;

    private String placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_queue);

        getSupportActionBar().setTitle("Add in Queue");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ivPlaceImage = findViewById(R.id.ivPlaceImage);
        tvPlaceName = findViewById(R.id.tvPlaceName);
        tvTags = findViewById(R.id.tvTags);
        tvPlaceLocation = findViewById(R.id.tvPlaceLocation);
        etFullName = findViewById(R.id.etFullName);
        etMobileNo = findViewById(R.id.etMobileNo);
        etNoOfPersons = findViewById(R.id.etNoOfPersons);
        etSpecialInstruction = findViewById(R.id.etSpecialInstruction);
        btnAddQueue = findViewById(R.id.btnAddQueue);

        beepPlayer = MediaPlayer.create(this, R.raw.beep);
        beepPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);


        User user = Configuration.getUser();
        etFullName.setText(user.getFirstName() + " " + user.getLastName());
        etMobileNo.setText(user.getMobileNo());

        btnAddQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAddQueue_onClick(view);
            }
        });

        //placeId = "6C0CCCD1-B8C6-4110-9E13-0E9B533BF3F5";
        //QRCodeDetailTask(placeId);
        Intent intent = new Intent(this, BarcodeScannerActivity.class);
        startActivityForResult(intent, ActivityRequest.REQUEST_BARCODE_SCANNER);
        LocalBroadcastManager.getInstance(this).registerReceiver(barcodeDataReceiver, new IntentFilter(BarcodeScanner.ACTION));
    }


    private void QRCodeDetailTask(final String PlaceId) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                isFindPlace = false;
                isSearchQR = true;
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.QueueQRCodeDetail(PlaceId, Configuration.getUserId());

                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {

                try {
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {
                        isFindPlace = true;
                        beepPlayer.start();
                        finishActivity(ActivityRequest.REQUEST_BARCODE_SCANNER);

                        QueueDetail queueDetail = gson.fromJson(jsonObject.getJSONObject("QRCodeDetail").toString(), QueueDetail.class);


                        Glide.with(AddQueueActivity.this)
                                .load(queueDetail.getProfileImageURL())
                                .centerCrop()
                                .placeholder(R.drawable.image_place_loading)
                                .into(ivPlaceImage);

                        tvPlaceName.setText(queueDetail.getPlaceName());
                        tvTags.setText(queueDetail.getCuisines());
                        tvPlaceLocation.setText(queueDetail.getPlaceLocation());

                    } else {
                        UIHelper.showShortToast(AddQueueActivity.this, jsonObject.getString("Message"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                isSearchQR = false;

            }
        }.execute();
    }


    private void AddQueueTask(final String PlaceId, final String FullName, final String NoOfPerson, final String Instruction) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {

            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.AddInQueue(PlaceId, FullName, NoOfPerson, Instruction, Configuration.getUserId());

                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {

                try {
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {

                        QueueDetail queueDetail = gson.fromJson(jsonObject.getJSONObject("QueueData").toString(), QueueDetail.class);

                        Intent intent = new Intent();
                        intent.putExtra("QueueDetail", queueDetail);
                        setResult(RESULT_OK, intent);
                        finish();

                    } else {
                        UIHelper.showShortToast(AddQueueActivity.this, jsonObject.getString("Message"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }

    public void btnAddQueue_onClick(View view) {
        AddQueueTask(placeId, etFullName.getText().toString(), etNoOfPersons.getText().toString(), etSpecialInstruction.getText().toString());
    }

    private BroadcastReceiver barcodeDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", 0);
            placeId = intent.getStringExtra("data");
            if (resultCode == BarcodeScanner.RESULT_SCANNED) {
                if (isSearchQR == false) {
                    QRCodeDetailTask(placeId);
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ActivityRequest.REQUEST_BARCODE_SCANNER && isFindPlace == false) {
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(barcodeDataReceiver);
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
