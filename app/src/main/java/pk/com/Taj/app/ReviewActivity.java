package pk.com.Taj.app;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;

import pk.com.Taj.app.beans.MyRatingDetail;
import pk.com.Taj.app.connectivity.ServiceManager;
import pk.com.Taj.app.helper.ActivityRequest;
import pk.com.Taj.app.helper.UIHelper;
import pk.com.Taj.app.utils.BackgroundRequest;
import pk.com.Taj.app.widget.IconTextView;
import pk.com.Taj.app.widget.RoundCornerImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class ReviewActivity extends AppCompatActivity {

    private ProgressDialog dialog;

    private ServiceManager serviceManager = new ServiceManager();

    private String placeId;

    private int modeId;
    private MyRatingDetail ratingDetail;

    private RoundCornerImageView ivPlaceImage;
    private TextView tvPlaceName;
    private TextView tvTags;
    private TextView tvAvgRating;
    private TextView tvTotalReview;
    private AppCompatRatingBar rbPlaceRating;
    private AppCompatRatingBar rbComfort;
    private AppCompatRatingBar rbLocation;
    private AppCompatRatingBar rbFacilities;
    private AppCompatRatingBar rbStaff;
    private AppCompatRatingBar rbMoneyValue;
    private EditText etReviewText;
    private FlexboxLayout flPhoto;
    private IconTextView btnAddPhoto;

    private HashMap<String, Bitmap> reviewPhotos = new HashMap<String, Bitmap>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        getSupportActionBar().setTitle("Add Review");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        placeId = getIntent().getStringExtra("PlaceId");
        modeId = getIntent().getIntExtra("ModeId", 0);

        if (modeId == 2) {
            ratingDetail = (MyRatingDetail) getIntent().getSerializableExtra("RatingDetail");
            getSupportActionBar().setTitle("Edit Review");
        }

        ivPlaceImage = findViewById(R.id.ivPlaceImage);
        tvPlaceName = findViewById(R.id.tvPlaceName);
        tvTags = findViewById(R.id.tvTags);
        tvAvgRating = findViewById(R.id.tvAvgRating);
        tvTotalReview = findViewById(R.id.tvTotalReview);
        rbPlaceRating = findViewById(R.id.rbPlaceRating);
        rbComfort = findViewById(R.id.rbComfort);
        rbLocation = findViewById(R.id.rbLocation);
        rbFacilities = findViewById(R.id.rbFacilities);
        rbStaff = findViewById(R.id.rbStaff);
        rbMoneyValue = findViewById(R.id.rbMoneyValue);
        etReviewText = findViewById(R.id.etReviewText);
        flPhoto = findViewById(R.id.flPhoto);
        btnAddPhoto = findViewById(R.id.btnAddPhoto);


        Glide.with(this)
                .load(getIntent().getStringExtra("PlaceImageURL"))
                .centerCrop()
                .placeholder(R.drawable.image_place_loading)
                .into(ivPlaceImage);

        tvPlaceName.setText(getIntent().getStringExtra("PlaceName"));
        tvTags.setText(getIntent().getStringExtra("Tags"));
        tvAvgRating.setText(getIntent().getStringExtra("AvgRating"));
        tvTotalReview.setText(getIntent().getStringExtra("TotalReview"));

        if (modeId == 2) {
            rbPlaceRating.setRating(ratingDetail.getRating());
            etReviewText.setText(ratingDetail.getReviewstext());
        }
        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAddPhoto_onClick(view);
            }
        });
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, ActivityRequest.REQUEST_CAMERA_PERMISSION);
            return false;
        } else if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, ActivityRequest.REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showPhotoDialog();
        }

    }

    private void BusinessRatingTask(final String PlaceId, final float Comfort, final float Location, final float Facilities, final float Staff, final float Valueformoney, final String ReviewText) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                dialog = new ProgressDialog(ReviewActivity.this);
                dialog.setTitle("Please wait");
                dialog.setMessage("Updating...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;

                JSONArray jaPhotos = new JSONArray();

                for (Bitmap bitmap : reviewPhotos.values()) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                    String fileStream = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP);

                    jaPhotos.put(fileStream);
                }

                jobject = serviceManager.BusinessRating(PlaceId, Comfort, Location, Facilities, Staff, Valueformoney, ReviewText);
                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {

                dialog.dismiss();
                try {

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {
                        Intent intent = new Intent();
                        intent.putExtra("PlaceRating", ((Comfort + Location + Facilities + Staff + Valueformoney) / 5f));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    UIHelper.showShortToast(ReviewActivity.this, jsonObject.getString("Message"));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }

    private void PlaceRatingTask(final String PlaceId, final float Rating, final String ReviewText) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                dialog = new ProgressDialog(ReviewActivity.this);
                dialog.setTitle("Please wait");
                dialog.setMessage("Updating...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;

                JSONArray jaPhotos = new JSONArray();

                for (Bitmap bitmap : reviewPhotos.values()) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                    String fileStream = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP);

                    jaPhotos.put(fileStream);
                }

                jobject = serviceManager.PlaceRating(PlaceId, Rating, ReviewText, jaPhotos);
                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {

                dialog.dismiss();
                try {
                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {
                        Intent intent = new Intent();
                        intent.putExtra("PlaceRating", Rating);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    UIHelper.showShortToast(ReviewActivity.this, jsonObject.getString("Message"));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }

    public void btnRemovePhoto_onClick(View view) {

        View reviewPhoto = (View) view.getParent();

        flPhoto.removeView(reviewPhoto);

        reviewPhotos.remove(reviewPhoto.getTag().toString());

    }

    public void btnAddPhoto_onClick(View view) {
        showPhotoDialog();
    }

    private void showPhotoDialog() {
        if (checkPermission()) {
            final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Photo");

            builder.setItems(options, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (options[item].equals("Take Photo")) {
                        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, ActivityRequest.REQUEST_CAMERA);

                    } else if (options[item].equals("Choose from Gallery")) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        pickPhoto.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        startActivityForResult(pickPhoto, ActivityRequest.REQUEST_MEDIA);

                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequest.REQUEST_CAMERA && resultCode == RESULT_OK) {
            addPhoto();
        }
    }
*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ActivityRequest.REQUEST_CAMERA && resultCode == RESULT_OK && data != null) {
            //Bitmap selectedImage = getBitmap(fileUri);
            Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
            addPhoto(selectedImage);
        } else if (requestCode == ActivityRequest.REQUEST_MEDIA && resultCode == RESULT_OK && data != null) {
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    String picturePath = getRealPathFromURI(clipData.getItemAt(i).getUri());
                    if (picturePath != null) {
                        Bitmap selectedImage = BitmapFactory.decodeFile(picturePath);
                        addPhoto(selectedImage);
                    }
                }
            }
        }

    }


    private String getRealPathFromURI(Uri contentUri) {

        String picturePath = null;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(contentUri, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
        }

        return picturePath;
    }


    private void addPhoto(Bitmap bitmap) {
        try {
            String imageId = UUID.randomUUID().toString();

            final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View reviewPhoto = inflater.inflate(R.layout.review_photo, null);

            ImageView ivPhoto = reviewPhoto.findViewById(R.id.ivPhoto);
            IconTextView btnRemovePhoto = reviewPhoto.findViewById(R.id.btnRemovePhoto);

            btnRemovePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnRemovePhoto_onClick(view);
                }
            });


            ivPhoto.setImageBitmap(bitmap);

            reviewPhoto.setTag(imageId);

            flPhoto.addView(reviewPhoto);

            reviewPhotos.put(imageId, bitmap);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private Uri fileUri;
    private File tempFile;

    private void openCamera() {
        if (checkPermission()) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            tempFile = createTempImageFile();
            tempFile.getParentFile().mkdirs();

            if (Build.VERSION.SDK_INT < 25)
                fileUri = Uri.fromFile(tempFile);
            else
                fileUri = FileProvider.getUriForFile(ReviewActivity.this, BuildConfig.APPLICATION_ID + ".fileProvider", tempFile);

            if (tempFile != null) {
                cameraIntent.putExtra("output", fileUri);
                startActivityForResult(cameraIntent, ActivityRequest.REQUEST_CAMERA);
            }
        }
    }

    private File createTempImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/taj/temp", "TMP_" + timeStamp + ".jpg");
        return file;
    }

    private Bitmap getBitmap(Uri selectedimg) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 3;
            AssetFileDescriptor fileDescriptor = null;
            fileDescriptor = getContentResolver().openAssetFileDescriptor(selectedimg, "r");
            Bitmap original
                    = BitmapFactory.decodeFileDescriptor(
                    fileDescriptor.getFileDescriptor(), null, options);
            return original;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void btnSubmitReview_onClick(View view) {

        if (etReviewText.getText().length() != 0) {
            //BusinessRatingTask(placeId, rbComfort.getRating(), rbLocation.getRating(), rbFacilities.getRating(), rbStaff.getRating(), rbMoneyValue.getRating(), etReviewText.getText().toString());
            PlaceRatingTask(placeId, rbPlaceRating.getRating(), etReviewText.getText().toString());
        } else {
            UIHelper.showShortToast(this, "Please write a review");
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
