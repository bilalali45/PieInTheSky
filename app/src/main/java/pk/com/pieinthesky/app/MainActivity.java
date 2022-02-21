package pk.com.pieinthesky.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pk.com.pieinthesky.app.beans.AddressDetail;
import pk.com.pieinthesky.app.beans.LocationDetail;
import pk.com.pieinthesky.app.beans.MediaDetail;
import pk.com.pieinthesky.app.beans.PlaceOverview;
import pk.com.pieinthesky.app.beans.RestaurantDetail;
import pk.com.pieinthesky.app.beans.User;
import pk.com.pieinthesky.app.connectivity.ServiceManager;
import pk.com.pieinthesky.app.helper.ActivityRequest;
import pk.com.pieinthesky.app.helper.UIHelper;
import pk.com.pieinthesky.app.utils.BackgroundRequest;
import pk.com.pieinthesky.app.utils.GPSManager;
import pk.com.pieinthesky.app.utils.LocalDataManager;
import pk.com.pieinthesky.app.utils.SessionManager;
import pk.com.pieinthesky.app.widget.NonScrollListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ServiceManager serviceManager = new ServiceManager();

    private GoogleSignInClient googleSignInClient;

    private TextView tvToolbarTitle;
    private ImageView ivUserPhoto;
    private TextView tvUserFullName;
    private TextView tvUserType;
    private TextView tvUserEmail;
    private TextView tvUserContactNo;


    private LocationDetail locationDetail = null;
    private boolean isLocate = false;

    private ImageSlider imageSlider;
    private ShimmerFrameLayout shimmer_Restaurant;
    private NonScrollListView list_Restaurant;

    private List<RestaurantDetail> restaurantList = null;
    private RestaurantOverviewAdapter restaurantOverviewAdapter = null;

    private NavigationView navigationView = null;

    private long lastTimeBackPress = 0;
    private int timePeriodToExit = 2000;
    private Toast exitToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tvToolbarTitle = toolbar.findViewById(R.id.tvToolbarTitle);
        getSupportActionBar().setTitle("");


        ivUserPhoto = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.ivUserPhoto);
        tvUserFullName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvUserFullName);
        tvUserType = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvUserType);
        tvUserEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvUserEmail);
        tvUserContactNo = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvUserContactNo);

        imageSlider = findViewById(R.id.image_slider);
        shimmer_Restaurant = findViewById(R.id.shimmer_Restaurant);

        list_Restaurant = findViewById(R.id.list_Restaurant);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        setUserDetails();

        tvToolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddressSelectionDialog();
            }
        });

        Configuration.setIsLocationSet(LocalDataManager.getInstance().getBoolean("LocationSet"));

        if (Configuration.isLocationSet() == false) {
            isLocate = true;
            if (checkLocationPermission()) {
                locate();
            }
        } else {
            Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            locationDetail = gson.fromJson(LocalDataManager.getInstance().getString("Location"), LocationDetail.class);
            Configuration.setCurrentLocation(locationDetail);
            tvToolbarTitle.setText(locationDetail.getAddress());

            checkLocationPermission();
        }

        /*

        restaurantList = new ArrayList<PlaceOverview>();

        PlaceOverview p1 = new PlaceOverview();
        p1.setPlaceName("Pie in the Sky");
        p1.setTags("13 locations");
        p1.setProfileImageURL("https://i0.wp.com/creativepakistan.pk/wp-content/uploads/2020/01/Pie-in-the-Sky-zamzama.jpg");
        restaurantList.add(p1);

        PlaceOverview p2 = new PlaceOverview();
        p2.setPlaceName("Chatterbox Cafe");
        p2.setTags("8 locations");
        p2.setProfileImageURL("http://1.bp.blogspot.com/_zCIMUWoa_GM/S2A1KbUJawI/AAAAAAAAAFM/tGs00AYIqNs/s640/P1220011.JPG");
        restaurantList.add(p2);

        PlaceOverview p3 = new PlaceOverview();
        p3.setPlaceName("Chatterbox Deli");
        p3.setTags("5 locations");
        p3.setProfileImageURL("https://c.tribune.com.pk/2018/05/1-1525243872.png");
        restaurantList.add(p3);
        restaurantList = new ArrayList<PlaceOverview>();

        PlaceOverview p1 = new PlaceOverview();
        p1.setPlaceName("Pie in the Sky");
        p1.setTags("13 locations");
        p1.setProfileImageURL("https://i0.wp.com/creativepakistan.pk/wp-content/uploads/2020/01/Pie-in-the-Sky-zamzama.jpg");
        restaurantList.add(p1);

        PlaceOverview p2 = new PlaceOverview();
        p2.setPlaceName("Chatterbox Cafe");
        p2.setTags("8 locations");
        p2.setProfileImageURL("http://1.bp.blogspot.com/_zCIMUWoa_GM/S2A1KbUJawI/AAAAAAAAAFM/tGs00AYIqNs/s640/P1220011.JPG");
        restaurantList.add(p2);

        PlaceOverview p3 = new PlaceOverview();
        p3.setPlaceName("Chatterbox Deli");
        p3.setTags("5 locations");
        p3.setProfileImageURL("https://c.tribune.com.pk/2018/05/1-1525243872.png");
        restaurantList.add(p3);
        * */
        RestaurantListTask();

        list_Restaurant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                OrderOnlineFragment dialog = new OrderOnlineFragment();
                Bundle bundle = new Bundle();
                bundle.putString("PlaceId", restaurantList.get(position).getPlaceId());
                bundle.putString("GSTPercentage", "16");
                bundle.putString("DeliveryCharges", "50");
                bundle.putString("PlaceImageURL", restaurantList.get(position).getProfileImageURL());
                bundle.putString("PlaceName", restaurantList.get(position).getPlaceName());
                bundle.putString("Tags", "");
                //bundle.putString("AvgRating", "10");
                //bundle.putString("TotalReview", "10+ Reviews");
                dialog.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up, R.anim.slide_in_up, R.anim.slide_out_up)
                        .add(android.R.id.content, dialog)
                        .addToBackStack("Main")
                        .commit();
            }
        });


        List<SlideModel> imageList = new ArrayList<SlideModel>();


            imageList.add(new SlideModel("https://pieinthesky.com.pk/assets/images/cookiejar.jpg",null ,true));
        imageList.add(new SlideModel("https://pieinthesky.com.pk/assets/images/discountbanner.jpg",null ,true));

        imageSlider.setImageList(imageList, true);

        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        LocalBroadcastManager.getInstance(this).registerReceiver(loginRequestReceiver, new IntentFilter(ActivityRequest.REQUEST_LOGIN_ACTION));

    }


    private BroadcastReceiver loginRequestReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", 0);
            if (resultCode == ActivityRequest.REQUEST_LOGIN) {
                setUserDetails();
            }
        }
    };


    private void RestaurantListTask() {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                shimmer_Restaurant.startShimmer();
                shimmer_Restaurant.setVisibility(View.VISIBLE);
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.RestaurantList();

                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {

                try {
                    Type type;
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    final boolean status = jsonObject.getBoolean("Status");
                    final int statusCode = jsonObject.getInt("StatusCode");
                    if (status) {
                        JSONArray restaurantListData = jsonObject.getJSONArray("highRatingData");

                        type = new TypeToken<List<RestaurantDetail>>() {
                        }.getType();

                        restaurantList = gson.fromJson(restaurantListData.toString(), type);

                        restaurantOverviewAdapter = new RestaurantOverviewAdapter(MainActivity.this, restaurantList);

                        list_Restaurant.setAdapter(restaurantOverviewAdapter);


                    } else {
                        UIHelper.showShortToast(MainActivity.this, jsonObject.getString("Message"));
                    }

                    shimmer_Restaurant.stopShimmer();
                    shimmer_Restaurant.setVisibility(View.GONE);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkStateReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(loginRequestReceiver);
    }

    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager != null) {
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                    if (networkInfo != null) {
                        if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                            finishActivity(ActivityRequest.REQUEST_NO_CONNECTION);
                        } else {
                            Intent noConnectionIntent = new Intent(MainActivity.this, NoConnectionActivity.class);
                            startActivityForResult(noConnectionIntent, ActivityRequest.REQUEST_NO_CONNECTION);
                        }
                    } else {
                        Intent noConnectionIntent = new Intent(MainActivity.this, NoConnectionActivity.class);
                        startActivityForResult(noConnectionIntent, ActivityRequest.REQUEST_NO_CONNECTION);
                    }
                }
            } catch (Exception ex) {
            }
        }
    };


    private boolean checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, ActivityRequest.REQUEST_ACCESS_FINE_LOCATION);
            return false;
        }
        return true;
    }


    private void setUserDetails() {
        if (Configuration.isLogin()) {
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            User user = Configuration.getUser();
            tvUserFullName.setText(user.getFirstName() + " " + user.getLastName());
            tvUserType.setText("User");
            tvUserEmail.setText(user.getEmail());
            tvUserContactNo.setText(user.getMobileNo());

            Glide.with(this)
                    .load(user.getPhotoURL())
                    .centerCrop()
                    .placeholder(R.drawable.image_slider_loading)
                    .into(ivUserPhoto);
        } else {
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            tvUserFullName.setText("Guest Login");
            tvUserEmail.setText("");
            tvUserContactNo.setText("");
        }
    }


    private void locate() {

        if (GPSManager.getInstance().isLocationEnabled()) {
            LocationActivity locationActivity = new LocationActivity();
            locationDetail = locationActivity.getCurrentLocation(this);

            if (locationDetail != null) {
                Configuration.setCurrentLocation(locationDetail);
                tvToolbarTitle.setText(locationDetail.getAddress());

                if (Configuration.isLocationSet() == false) {
                    saveLocation();
                }
            }
        } else {
            UIHelper.showConfirmDialog(this, "", "The app need location service for get your current location, please enable location service or set manually.",
                    "Set Manually", "Enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            openLocationActivity();
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), ActivityRequest.REQUEST_ENABLE_LOCATION);

                        }
                    });
        }
    }

    private void showAddressSelectionDialog() {
        AddressSelectionFragment addressSelectionFragment = new AddressSelectionFragment();
        addressSelectionFragment.setOnItemClickListener(new AddressSelectionFragment.OnOptionItemClickListener() {
            @Override
            public void onSelectLocation(LocationDetail locationDetail) {

                if (locationDetail != null) {
                    LocationDetail newLocationDetail = locationDetail;
                    if (newLocationDetail.getLatitude() != 0 && newLocationDetail.getLongitude() != 0 && newLocationDetail.getAddress() != null && !newLocationDetail.getAddress().equals("")) {
                       MainActivity.this.locationDetail = newLocationDetail;

                        Configuration.setCurrentLocation(locationDetail);
                        tvToolbarTitle.setText(locationDetail.getAddress());

                        saveLocation();
                    }
                }
            }

            @Override
            public void onSelectAddress(AddressDetail addressDetail) {
                if (addressDetail != null) {
                    LocationDetail newLocationDetail = new LocationDetail();

                    newLocationDetail.setAddress(addressDetail.getAddress());
                    newLocationDetail.setLatitude(addressDetail.getLatitude());
                    newLocationDetail.setLongitude(addressDetail.getLongitude());

                    if (newLocationDetail.getLatitude() != 0 && newLocationDetail.getLongitude() != 0 && newLocationDetail.getAddress() != null && !newLocationDetail.getAddress().equals("")) {
                        locationDetail = newLocationDetail;

                        Configuration.setCurrentLocation(locationDetail);
                        tvToolbarTitle.setText(locationDetail.getAddress());

                        saveLocation();
                    }
                }
            }
        });
        addressSelectionFragment.show(getSupportFragmentManager(), addressSelectionFragment.getTag());
    }

    private void openLocationActivity() {
        Intent intent = new Intent(MainActivity.this, LocationActivity.class);
        intent.putExtra("Mode", "SelectLocation");
        intent.putExtra("LocationDetail", locationDetail);
        startActivityForResult(intent, ActivityRequest.REQUEST_LOCATION);
    }

    private void saveLocation() {
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        LocalDataManager.getInstance().putString("Location", gson.toJson(locationDetail));
        LocalDataManager.getInstance().putBoolean("LocationSet", true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequest.REQUEST_LOCATION && resultCode == RESULT_OK) {

            LocationDetail newLocationDetail = (LocationDetail) data.getSerializableExtra("LocationDetail");
            if (newLocationDetail != null) {
                if (newLocationDetail.getLatitude() != 0 && newLocationDetail.getLongitude() != 0 && newLocationDetail.getAddress() != null && !newLocationDetail.getAddress().equals("")) {
                    locationDetail = newLocationDetail;

                    Configuration.setCurrentLocation(locationDetail);
                    tvToolbarTitle.setText(locationDetail.getAddress());

                    saveLocation();
                }
            }
        } else if (requestCode == ActivityRequest.REQUEST_ENABLE_LOCATION) {
            if (GPSManager.getInstance().isLocationEnabled()) {
                locate();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (isLocate == true && requestCode == ActivityRequest.REQUEST_ACCESS_FINE_LOCATION && grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isLocate = false;
            locate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_locate) {
            isLocate = true;
            if (checkLocationPermission()) {
                locate();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            if ((new Date().getTime() - lastTimeBackPress) < timePeriodToExit) {
                exitToast.cancel();
                setResult(RESULT_OK);
                finish();
            } else {
                exitToast = UIHelper.showShortToast(this, "Press again to exit");
                lastTimeBackPress = new Date().getTime();
            }
        }

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_share) {
            shareApp();
        } else if (id == R.id.nav_help_support) {
            Intent intent = new Intent(this, HelpSupportActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            logout();
        } else if (Configuration.isLogin()) {
            if (id == R.id.nav_profile) {
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_my_rewards) {
                Intent intent = new Intent(this, RewardActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_my_addresses) {
                Intent intent = new Intent(this, AddressListActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_my_order) {
                Intent intent = new Intent(this, OrderListActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_my_queues) {
                Intent intent = new Intent(this, PlaceQueueActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_wishlists) {
                Intent intent = new Intent(this, BookmarkActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_notifications) {
                Intent intent = new Intent(this, NotificationActivity.class);
                startActivity(intent);
            } else if (id == R.id.navigation_reservation) {
                Intent intent = new Intent(this, TableBookingActivity.class);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(this, LoginSelectionActivity.class);
            intent.putExtra("IsUserLoginRequest", true);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void btnPlaceDetail_onClick(View view) {
        Intent intent = new Intent(this, PlaceDetailActivity.class);
        intent.putExtra("PlaceId", "3B152A9A-C516-492C-AAAE-8B82A5373FCD");
        startActivity(intent);
    }


    private void shareApp() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.share_text));
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void logout() {

        UIHelper.showConfirmDialog(this, "Log Out", "Are you sure to log out?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Facebook Login
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken != null) {
                    LoginManager.getInstance().logOut();
                }

                //Google Login
                GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
                if (googleSignInAccount != null) {
                    googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {

                        }
                    });
                }
                SessionManager.getInstance().logout();
                Configuration.setLogin(false);
                Configuration.setOfflineLogin(false);
                Configuration.setUser(null);
                Configuration.setAddressList(null);
                finish();

            }
        });
    }

    private class RestaurantOverviewAdapter extends BaseAdapter {
        private Context context;
        private List<RestaurantDetail> restaurantList = null;

        public RestaurantOverviewAdapter(Context context, List<RestaurantDetail> restaurantList) {
            this.context = context;
            this.restaurantList = restaurantList;
        }

        @Override
        public int getCount() {
            return restaurantList.size();
        }

        @Override
        public Object getItem(int position) {
            return restaurantList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder viewHolder;
            if (convertView == null) {
                final LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(R.layout.restaurant_overview_row, null);

                ImageView ivRestaurantImage = convertView.findViewById(R.id.ivRestaurantImage);
                TextView tvRestaurantName = convertView.findViewById(R.id.tvRestaurantName);
                TextView tvRestaurantDescription = convertView.findViewById(R.id.tvRestaurantDescription);
                TextView tvRestaurantPlaces = convertView.findViewById(R.id.tvRestaurantPlaces);

                viewHolder = new ViewHolder(ivRestaurantImage, tvRestaurantName, tvRestaurantDescription, tvRestaurantPlaces);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            Glide.with(context)
                    .load(restaurantList.get(position).getProfileImageURL())
                    .centerCrop()
                    .placeholder(R.drawable.image_slider_loading)
                    .into(viewHolder.ivRestaurantImage);


            viewHolder.tvRestaurantName.setText(restaurantList.get(position).getPlaceName());
            viewHolder.tvRestaurantDescription.setText(restaurantList.get(position).getCuisines());
            viewHolder.tvRestaurantPlaces.setText(String.valueOf(restaurantList.get(position).getRestaurantPlaces()) + " locations");

            return convertView;
        }


        private class ViewHolder {
            ImageView ivRestaurantImage;
            TextView tvRestaurantName;
            TextView tvRestaurantDescription;
            TextView tvRestaurantPlaces;

            public ViewHolder(ImageView ivRestaurantImage, TextView tvRestaurantName, TextView tvRestaurantDescription, TextView tvRestaurantPlaces) {
                this.ivRestaurantImage = ivRestaurantImage;
                this.tvRestaurantName = tvRestaurantName;
                this.tvRestaurantDescription = tvRestaurantDescription;
                this.tvRestaurantPlaces = tvRestaurantPlaces;

            }
        }

    }


}