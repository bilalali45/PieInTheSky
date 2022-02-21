package pk.com.pieinthesky.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pk.com.pieinthesky.app.beans.AddressDetail;
import pk.com.pieinthesky.app.beans.LocationDetail;
import pk.com.pieinthesky.app.beans.PlacePrediction;
import pk.com.pieinthesky.app.helper.ActivityRequest;
import pk.com.pieinthesky.app.helper.UIHelper;
import pk.com.pieinthesky.app.utils.BackgroundRequest;


import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private String mode = "";

    private GoogleMap mMap;

    private Marker placeMarker;

    private SearchView searchView;
    private ListView listView;
    private TextView tvLocation;
    private LinearLayout lyLocationDetail;
    private LinearLayout container_label;
    private EditText etArea;
    private EditText etAddress;
    private Button btnSubmit;

    private TextView selectedLabel = null;
    private String searchHint = "Search here";

    private String GOOGLE_API_URL = "https://maps.googleapis.com/maps/api/";
    private String GOOGLE_API_KEY = "AIzaSyA9wuP7Rl42YBuU4Xtqay7hMOmz60koYB4";

    LocationDetail locationDetail = new LocationDetail();

    boolean isFetchGeocode = false;

    ArrayAdapter<PlacePrediction> placePredictionArrayAdapter;

    private Handler searchHandler = new Handler();
    private Runnable searchRunnable = null;
    private boolean isSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();

            window.setStatusBarColor(Color.parseColor("#CCCCCC"));
        }

        searchView = findViewById(R.id.searchView);
        listView = findViewById(R.id.listView);
        tvLocation = findViewById(R.id.tvLocation);
        lyLocationDetail = findViewById(R.id.lyLocationDetail);
        container_label = findViewById(R.id.container_label);
        etArea = findViewById(R.id.etArea);
        etAddress = findViewById(R.id.etAddress);
        btnSubmit = findViewById(R.id.btnSubmit);

        mode = getIntent().getStringExtra("Mode");
        LocationDetail newLocationDetail = (LocationDetail) getIntent().getSerializableExtra("LocationDetail");

        if (mode.equals("AddLocation")) {
            btnSubmit.setText("Save Address");
            lyLocationDetail.setVisibility(View.VISIBLE);
        } else {
            lyLocationDetail.setVisibility(View.GONE);
        }

        if (newLocationDetail != null) {
            locationDetail = newLocationDetail;
        }

        selectedLabel = (TextView) container_label.getChildAt(0);

        for (int index = 0; index < container_label.getChildCount(); index++) {
            container_label.getChildAt(index).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView textView = (TextView) view;

                    if (selectedLabel != null) {
                        selectedLabel.setBackground(getResources().getDrawable(R.drawable.bg_gray_corner_filter));
                        selectedLabel.setTextColor(getResources().getColor(R.color.colorGray));
                    }
                    selectedLabel = textView;
                    textView.setBackground(getResources().getDrawable(R.drawable.bg_corner_pink_filled));
                    textView.setTextColor(getResources().getColor(R.color.colorWhite));

                }
            });
        }

        searchView.setActivated(true);
        searchView.setQueryHint(searchHint);
        searchView.onActionViewExpanded();
        searchView.setIconified(false);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                searchView.clearFocus();
            }
        }, 200);


        listView.setVisibility(View.GONE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        View closeButton = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery("", false);
                searchView.clearFocus();
                listView.setVisibility(View.GONE);
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if (newText.trim().length() > 3 && isSearch == false) {
                    if (searchRunnable != null) {
                        searchHandler.removeCallbacks(searchRunnable);
                    }

                    searchRunnable = new Runnable() {
                        @Override
                        public void run() {
                            PlaceAutoCompleteTask(newText);
                        }
                    };

                    searchHandler.postDelayed(searchRunnable, 500);

                    //  PlaceAutoCompleteTask(newText);
                }
                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (mode.equals("AddLocation")) {
                    if (hasFocus) {
                        lyLocationDetail.setVisibility(View.GONE);
                    } else {
                        lyLocationDetail.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                locationDetail.setPlaceId(placePredictionArrayAdapter.getItem(position).getPlace_id());
                locationDetail.setDescription(placePredictionArrayAdapter.getItem(position).getDescription());
                PlaceDetailTask(placePredictionArrayAdapter.getItem(position).getPlace_id(), "name,formatted_address,geometry");
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                locate();
                return false;
            }
        });

        if (checkLocationPermission()) {
            mMap.setMyLocationEnabled(true);
        }

        if (locationDetail.getLatitude() == 0 && locationDetail.getLongitude() == 0) {
            locate();
        } else {
            setMarker(locationDetail.getLatitude(), locationDetail.getLongitude());
        }


    }

    private void locate() {
        Location location = getLastKnownLocation(this);
        if (location != null) {
            locationDetail.setLatitude(location.getLatitude());
            locationDetail.setLongitude(location.getLongitude());
            locationDetail.setDescription("");
            locationDetail.setAddress("");
            locationDetail.setPlaceId("");

            LocationDetail newLocationDetail = GeocodeDetailTask(location.getLatitude(), location.getLongitude());
            if (newLocationDetail != null) {
                locationDetail = newLocationDetail;
            }

            setMarker(locationDetail.getLatitude(), locationDetail.getLongitude());
        }
    }

    private void setMarker(double latitude, double longitude) {
        if (placeMarker == null) {
            placeMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).draggable(true));

            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {

                    locationDetail.setLatitude(marker.getPosition().latitude);
                    locationDetail.setLongitude(marker.getPosition().longitude);
                    locationDetail.setDescription("");
                    locationDetail.setAddress("");
                    locationDetail.setPlaceId("");

                    LocationDetail newLocationDetail = GeocodeDetailTask(marker.getPosition().latitude, marker.getPosition().longitude);
                    if (newLocationDetail != null) {
                        locationDetail = newLocationDetail;
                    }

                    placeMarker.hideInfoWindow();
                    placeMarker.setTitle(locationDetail.getAddress());
                    tvLocation.setText(locationDetail.getAddress());
                }
            });
        }
        placeMarker.hideInfoWindow();
        placeMarker.setPosition(new LatLng(latitude, longitude));
        placeMarker.setTitle(locationDetail.getAddress());
        tvLocation.setText(locationDetail.getAddress());

        adjustCameraPosition(new LatLng(latitude, longitude));
    }

    private void adjustCameraPosition(LatLng latLng) {
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.moveCamera(yourLocation);

    }

    private void PlaceAutoCompleteTask(final String input) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                isSearch = true;
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject = null;

                String url = GOOGLE_API_URL + "place/autocomplete/json?input=" + input + "&key=" + GOOGLE_API_KEY;

                String resp = "";
                try {
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(0, TimeUnit.SECONDS)
                            .readTimeout(0, TimeUnit.SECONDS)
                            .build();
                    Request.Builder builder = new Request.Builder();
                    builder.url(url);


                    Request request = builder.build();
                    Response response = client.newCall(request).execute();
                    resp = response.body().string();

                    if (resp != "") {
                        jobject = new JSONObject(resp);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {


                try {
                    Type type;
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();


                    JSONArray predictionData = jsonObject.getJSONArray("predictions");

                    type = new TypeToken<List<PlacePrediction>>() {
                    }.getType();

                    List<PlacePrediction> predictions = gson.fromJson(predictionData.toString(), type);

                    placePredictionArrayAdapter = new ArrayAdapter<PlacePrediction>(LocationActivity.this, android.R.layout.simple_list_item_1, predictions);
                    listView.setAdapter(placePredictionArrayAdapter);

                    if (predictions.size() > 0) {
                        listView.setVisibility(View.VISIBLE);
                    } else {
                        listView.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                isSearch = false;
            }
        }.execute();
    }


    private void PlaceDetailTask(final String placeId, final String fields) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {

            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject = null;

                String fieldsParam = fields != null ? ("&fields=" + fields) : "";
                String url = GOOGLE_API_URL + "place/details/json?placeid=" + placeId + fieldsParam + "&key=" + GOOGLE_API_KEY;

                String resp = "";
                try {
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(0, TimeUnit.SECONDS)
                            .readTimeout(0, TimeUnit.SECONDS)
                            .build();
                    Request.Builder builder = new Request.Builder();
                    builder.url(url);


                    Request request = builder.build();
                    Response response = client.newCall(request).execute();
                    resp = response.body().string();

                    if (resp != "") {
                        jobject = new JSONObject(resp);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {


                try {
                    JSONObject details = jsonObject.getJSONObject("result");

                    if (details != null) {
                        locationDetail.setAddress(details.getString("formatted_address"));
                        locationDetail.setLatitude(details.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                        locationDetail.setLongitude(details.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));

                        setMarker(locationDetail.getLatitude(), locationDetail.getLongitude());

                        searchView.setQuery("", false);
                        searchView.clearFocus();
                        listView.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }


    public Location getLastKnownLocation(Context context) {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location lastLocation = locationManager.getLastKnownLocation(provider);
            if (lastLocation == null) {
                continue;
            }
            if (bestLocation == null || lastLocation.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = lastLocation;
            }
        }

        if (bestLocation != null) {
            locationDetail.setLatitude(bestLocation.getLatitude());
            locationDetail.setLongitude(bestLocation.getLongitude());
        }
        return bestLocation;
    }

    public LocationDetail getCurrentLocation(Context context) {

        LocationDetail newLocationDetail = null;

        Location location = getLastKnownLocation(context);
        if (location != null) {
            newLocationDetail = GeocodeDetailTask(location.getLatitude(), location.getLongitude());
        }
        return newLocationDetail;
    }

    public LocationDetail GeocodeDetailTask(final double latitude, final double longitude) {
        LocationDetail newLocationDetail = null;
        newLocationDetail = new BackgroundRequest<String, Void, LocationDetail>() {


            @Override
            protected LocationDetail doInBackground(String... params) {
                LocationDetail newLocationDetail = null;


                String url = GOOGLE_API_URL + "geocode/json?latlng=" + latitude + "," + longitude + "&key=" + GOOGLE_API_KEY;

                String resp = "";
                try {
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(0, TimeUnit.SECONDS)
                            .readTimeout(0, TimeUnit.SECONDS)
                            .build();
                    Request.Builder builder = new Request.Builder();
                    builder.url(url);

                    Request request = builder.build();
                    Response response = client.newCall(request).execute();
                    resp = response.body().string();

                    if (resp != "") {
                        JSONObject jobject = new JSONObject(resp);

                        JSONArray details = jobject.getJSONArray("results");

                        if (details != null) {
                            newLocationDetail = new LocationDetail();
                            newLocationDetail.setLatitude(latitude);
                            newLocationDetail.setLongitude(longitude);
                            newLocationDetail.setPlaceId(details.getJSONObject(0).getString("place_id"));
                            newLocationDetail.setDescription(details.getJSONObject(0).getString("formatted_address"));
                            newLocationDetail.setAddress(details.getJSONObject(0).getString("formatted_address"));
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return newLocationDetail;
            }


        }.executeAndWait();

        return newLocationDetail;
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
        if (requestCode == ActivityRequest.REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkLocationPermission()) {
                    mMap.setMyLocationEnabled(true);
                    locate();
                }
            }
        }
    }


    private int densityToPixels(int dp) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    public void btnSubmit_onClick(View view) {
        if (locationDetail.getLatitude() != 0 && locationDetail.getLongitude() != 0) {
            if (locationDetail.getDescription() == null || locationDetail.getDescription() == "" || locationDetail.getAddress() == null || locationDetail.getAddress() == "") {
                LocationDetail newLocationDetail = GeocodeDetailTask(locationDetail.getLatitude(), locationDetail.getLongitude());
                if (newLocationDetail != null) {
                    locationDetail = newLocationDetail;
                    locationSubmit();
                }
            } else {
                locationSubmit();
            }
        } else {
            UIHelper.showShortToast(this, "Please select location");
        }
    }

    private void locationSubmit() {
        Intent intent = new Intent();

        if (mode.equals("AddLocation")) {
            if (etArea.getText().length() == 0) {
                UIHelper.showShortToast(this, "Please enter area");
                return;
            }
            if (etAddress.getText().length() == 0) {
                UIHelper.showShortToast(this, "Please enter address");
                return;
            }
            AddressDetail addressDetail = new AddressDetail();
            addressDetail.setLocation(locationDetail.getAddress());
            addressDetail.setLatitude(locationDetail.getLatitude());
            addressDetail.setLongitude(locationDetail.getLongitude());
            addressDetail.setArea(etArea.getText().toString());
            addressDetail.setAddress(etAddress.getText().toString());
            addressDetail.setLabel(selectedLabel.getText().toString());

            intent.putExtra("AddressDetail", addressDetail);
        }

        intent.putExtra("LocationDetail", locationDetail);
        setResult(RESULT_OK, intent);
        finish();

    }

    public void btnCancel_onClick(View view) {
        finish();
    }
}
