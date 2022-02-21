package pk.com.Taj.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
import pk.com.Taj.app.beans.LocationDetail;
import pk.com.Taj.app.beans.PlacePrediction;
import pk.com.Taj.app.helper.ActivityRequest;
import pk.com.Taj.app.utils.BackgroundRequest;


import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GoogleLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    private Marker placeMarker;

    private SearchView searchView;
    private ListView listView;
    private String searchHint = "Search here";

    private String GOOGLE_API_URL = "https://maps.googleapis.com/maps/api/";
    private String GOOGLE_API_KEY = "AIzaSyA9wuP7Rl42YBuU4Xtqay7hMOmz60koYB4";

    double latitude;
    double longitude;
    String place_id;
    String description;
    String address;
    boolean isFetchGeocode = false;

    ArrayAdapter<PlacePrediction> placePredictionArrayAdapter;

    private Handler searchHandler = new Handler();
    private Runnable searchRunnable = null;
    private boolean isSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_location);

        latitude = getIntent().getDoubleExtra("Latitude", 0);
        longitude = getIntent().getDoubleExtra("Longitude", 0);
        description = getIntent().getStringExtra("Description");
        address = getIntent().getStringExtra("Address");

        searchView = findViewById(R.id.searchView);
        listView = findViewById(R.id.listView);

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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                GoogleLocationActivity.this.place_id = placePredictionArrayAdapter.getItem(position).getPlace_id();
                GoogleLocationActivity.this.description = placePredictionArrayAdapter.getItem(position).getDescription();
                PlaceDetailTask(GoogleLocationActivity.this.place_id, "name,formatted_address,geometry");
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (checkLocationPermission()) {
            mMap.setMyLocationEnabled(true);
        }

        if (this.latitude == 0 && this.longitude == 0) {
            locate();
        } else {
            setMarker(latitude, longitude);
        }


    }

    private void locate() {
        Location location = getLastKnownLocation(this);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            setMarker(latitude, longitude);
        }
    }

    private void setMarker(double latitude, double longitude) {
        if (placeMarker == null) {
            placeMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(address).draggable(true));

            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {

                    GoogleLocationActivity.this.latitude = marker.getPosition().latitude;
                    GoogleLocationActivity.this.longitude = marker.getPosition().longitude;
                    GoogleLocationActivity.this.description = "";
                    GoogleLocationActivity.this.address = "";
                    placeMarker.setTitle(GoogleLocationActivity.this.address);
                }
            });
        } else {
            placeMarker.setPosition(new LatLng(latitude, longitude));
            placeMarker.setTitle(GoogleLocationActivity.this.address);
        }

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

                    placePredictionArrayAdapter = new ArrayAdapter<PlacePrediction>(GoogleLocationActivity.this, android.R.layout.simple_list_item_1, predictions);
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
                        address = details.getString("formatted_address");
                        latitude = details.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                        longitude = details.getJSONObject("geometry").getJSONObject("location").getDouble("lng");

                        setMarker(latitude, longitude);

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
            latitude = bestLocation.getLatitude();
            longitude = bestLocation.getLongitude();
        }
        return bestLocation;
    }

    public LocationDetail getCurrentLocation(Context context) {

        LocationDetail locationDetail = null;

        Location location = getLastKnownLocation(context);
        if (location != null) {
            locationDetail = GeocodeDetailTask(location.getLatitude(), location.getLongitude());
        }
        return locationDetail;
    }

    public LocationDetail GeocodeDetailTask(final double latitude, final double longitude) {
        LocationDetail locationDetail = null;
        locationDetail = new BackgroundRequest<String, Void, LocationDetail>() {


            @Override
            protected LocationDetail doInBackground(String... params) {
                LocationDetail locationDetail = null;


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
                            locationDetail = new LocationDetail();
                            locationDetail.setLatitude(latitude);
                            locationDetail.setLongitude(longitude);
                            locationDetail.setPlaceId(details.getJSONObject(0).getString("place_id"));
                            locationDetail.setDescription(details.getJSONObject(0).getString("formatted_address"));
                            locationDetail.setAddress(details.getJSONObject(0).getString("formatted_address"));
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return locationDetail;
            }


        }.executeAndWait();

        return locationDetail;
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
        if (this.latitude != 0 && this.longitude != 0) {
            if (this.description == null || this.description == "" || this.address==null || this.address=="") {
                LocationDetail locationDetail = GeocodeDetailTask(latitude, longitude);
                if (locationDetail != null) {
                    Intent intent = new Intent();
                    intent.putExtra("PlaceId", locationDetail.getPlaceId());
                    intent.putExtra("Description", locationDetail.getDescription());
                    intent.putExtra("Address", locationDetail.getAddress());
                    intent.putExtra("Latitude", locationDetail.getLatitude());
                    intent.putExtra("Longitude", locationDetail.getLongitude());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            } else {
                Intent intent = new Intent();
                intent.putExtra("PlaceId", this.place_id);
                intent.putExtra("Description", this.description);
                intent.putExtra("Address", this.address);
                intent.putExtra("Latitude", this.latitude);
                intent.putExtra("Longitude", this.longitude);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    public void btnCancel_onClick(View view) {
        finish();
    }
}
