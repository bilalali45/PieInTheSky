package pk.com.pieinthesky.app;

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
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import pk.com.pieinthesky.app.beans.LocationDetail;
import pk.com.pieinthesky.app.beans.User;
import pk.com.pieinthesky.app.connectivity.ServiceManager;
import pk.com.pieinthesky.app.helper.ActivityRequest;
import pk.com.pieinthesky.app.helper.UIHelper;
import pk.com.pieinthesky.app.utils.BackgroundRequest;
import pk.com.pieinthesky.app.utils.GPSManager;
import pk.com.pieinthesky.app.widget.IconButton;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    private ProgressDialog dialog;

    private ServiceManager serviceManager = new ServiceManager();

    private ScrollView main_window;
    private CardView btnEditCover;
    private IconButton btnAddPhoto;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmailAddress;
    private EditText etMobileNo;
    private TextView tvLocation;
    private EditText etDescription;
    private ImageView ivProfilePhoto;
    private ImageView ivCoverPhoto;
    private Bitmap profilePhoto;
    private Bitmap coverPhoto;
    private int photoType = 0;//1-Profile Photo, 2-Cover Photo

    private double latitude;
    private double longitude;
    private String description;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        main_window = findViewById(R.id.main_window);
        btnEditCover = findViewById(R.id.btnEditCover);
        btnAddPhoto = findViewById(R.id.btnAddPhoto);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        etMobileNo = findViewById(R.id.etMobileNo);
        tvLocation = findViewById(R.id.tvLocation);
        etDescription = findViewById(R.id.etDescription);
        ivProfilePhoto = findViewById(R.id.ivProfilePhoto);
        ivCoverPhoto = findViewById(R.id.ivCoverPhoto);

        User user = Configuration.getUser();

        etFirstName.setText(user.getFirstName());
        etLastName.setText(user.getLastName());
        etEmailAddress.setText(user.getEmail());
        etMobileNo.setText(user.getMobileNo());

        Glide.with(this)
                .load(user.getPhotoURL())
                .centerCrop()
                .placeholder(R.drawable.image_slider_loading)
                .into(ivProfilePhoto);


        btnEditCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnEditCover_onClick(view);
            }
        });

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAddPhoto_onClick(view);
            }
        });


        new Handler().post(new Runnable() {
            @Override
            public void run() {
                main_window.scrollTo(0, 0);
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


    public void tvLocation_onClick(View view) {
        Intent intent = new Intent(this, LocationActivity.class);
        intent.putExtra("Mode", "SelectLocation");
        intent.putExtra("Latitude", latitude);
        intent.putExtra("Longitude", longitude);
        intent.putExtra("Description", description);
        intent.putExtra("Address", address);
        startActivityForResult(intent, ActivityRequest.REQUEST_LOCATION);
    }

    public void ivLocate_onClick(View view) {
        if (checkLocationPermission()) {
            locate();
        }
    }

    private void locate() {

        LocationActivity locationActivity = new LocationActivity();
        LocationDetail locationDetail = locationActivity.getCurrentLocation(this);

        if (locationDetail != null) {
            latitude = locationDetail.getLatitude();
            longitude = locationDetail.getLongitude();
            description = locationDetail.getDescription();
            address = locationDetail.getAddress();

            tvLocation.setText(address);
        }
    }

    public void btnAddPhoto_onClick(View view) {
        photoType = 1;// profile photo
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

    private boolean checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, ActivityRequest.REQUEST_ACCESS_FINE_LOCATION);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == ActivityRequest.REQUEST_ACCESS_FINE_LOCATION && grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            locate();
        } else if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showPhotoDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequest.REQUEST_LOCATION && resultCode == RESULT_OK) {
            latitude = data.getDoubleExtra("Latitude", 0);
            longitude = data.getDoubleExtra("Longitude", 0);
            description = data.getStringExtra("Description");
            address = data.getStringExtra("Address");

            tvLocation.setText(address);

        } else if (requestCode == ActivityRequest.REQUEST_CAMERA && resultCode == RESULT_OK && data != null) {
            //Bitmap selectedImage = getBitmap(fileUri);
            Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
            setPhoto(selectedImage);
        } else if (requestCode == ActivityRequest.REQUEST_MEDIA && resultCode == RESULT_OK && data != null) {
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    String picturePath = getRealPathFromURI(clipData.getItemAt(i).getUri());
                    if (picturePath != null) {
                        Bitmap selectedImage = BitmapFactory.decodeFile(picturePath);
                        setPhoto(selectedImage);
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

    private void setPhoto(Bitmap bitmap) {
        if (photoType == 1) {
            profilePhoto = bitmap;
            ivProfilePhoto.setImageBitmap(bitmap);
        } else if (photoType == 2) {
            coverPhoto = bitmap;
            ivCoverPhoto.setImageBitmap(bitmap);
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
                fileUri = FileProvider.getUriForFile(ProfileActivity.this, BuildConfig.APPLICATION_ID + ".fileProvider", tempFile);

            if (tempFile != null) {
                cameraIntent.putExtra("output", fileUri);
                startActivityForResult(cameraIntent, ActivityRequest.REQUEST_CAMERA);
            }
        }
    }

    private File createTempImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Pits/temp", "TMP_" + timeStamp + ".jpg");
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

    public void btnEditCover_onClick(View view) {
        photoType = 2;// cover photo
        showPhotoDialog();
    }

    public void btnSubmit_onClick(View view) {
        UpdateProfileTask(Configuration.getUserId(), etFirstName.getText().toString(), etLastName.getText().toString(), etEmailAddress.getText().toString(), etMobileNo.getText().toString(), latitude, longitude, tvLocation.getText().toString(), etDescription.getText().toString());
    }

    private void UpdateProfileTask(final String UserId, final String FirstName, final String LastName, final String Email, final String MobileNo, final double Latitude, final double Longitude, final String Location, final String Description) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                dialog = new ProgressDialog(ProfileActivity.this);
                dialog.setTitle("Please wait");
                dialog.setMessage("Updating...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                String ProfilePhotoStream = "";
                String CoverPhotoStream = "";
                try {
                    ByteArrayOutputStream byteArrayOutputStream;

                    if (profilePhoto != null) {
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        profilePhoto.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                        ProfilePhotoStream = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP);
                    }

                    if (coverPhoto != null) {
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        coverPhoto.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                        CoverPhotoStream = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
                jobject = serviceManager.UpdateProfile(UserId, FirstName, LastName, Email, MobileNo, Latitude, Longitude, Location, Description, ProfilePhotoStream, CoverPhotoStream);
                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {

                dialog.dismiss();
                try {

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {
                        //Configuration.getUser().setPhoto(profilePhoto);
                        finish();
                    }
                    UIHelper.showShortToast(ProfileActivity.this, jsonObject.getString("Message"));

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
}
