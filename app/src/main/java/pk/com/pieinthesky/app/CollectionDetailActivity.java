package pk.com.pieinthesky.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pk.com.pieinthesky.app.adapter.SearchPlaceAdapter;
import pk.com.pieinthesky.app.beans.CollectionDetail;
import pk.com.pieinthesky.app.beans.PlaceOverview;
import pk.com.pieinthesky.app.connectivity.ServiceManager;
import pk.com.pieinthesky.app.helper.UIHelper;
import pk.com.pieinthesky.app.utils.BackgroundRequest;
import pk.com.pieinthesky.app.widget.NonScrollListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CollectionDetailActivity extends AppCompatActivity {

    private ServiceManager serviceManager = new ServiceManager();

    private String collectionId = null;

    private ShimmerFrameLayout shimmer_CollectionLoading;

    private NestedScrollView main_window;

    private List<PlaceOverview> searchPlaceList = null;
    private SearchPlaceAdapter searchPlaceAdapter = null;

    private NonScrollListView list_SearchPlace;
    private LinearLayout lyLoadPlaces;

    private ImageView ivCollectionImage;
    private TextView tvCollectionName;
    private TextView tvCollectionDescription;
    private TextView tvCollectionPlaces;

    CollectionDetail collectionDetail = null;


    int pageSize = 10;
    boolean isSearching = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_detail);

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

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
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

        shimmer_CollectionLoading = findViewById(R.id.shimmer_CollectionLoading);
        list_SearchPlace = findViewById(R.id.list_SearchPlace);
        lyLoadPlaces = findViewById(R.id.lyLoadPlaces);


        collectionDetail = (CollectionDetail) getIntent().getSerializableExtra("CollectionDetail");
        collectionId = collectionDetail.getCollectionID();

        main_window = findViewById(R.id.main_window);
        ivCollectionImage = findViewById(R.id.ivCollectionImage);
        tvCollectionName = findViewById(R.id.tvCollectionName);
        tvCollectionDescription = findViewById(R.id.tvCollectionDescription);
        tvCollectionPlaces = findViewById(R.id.tvCollectionPlaces);


        Glide.with(this)
                .load(collectionDetail.getCoverImageURL())
                .centerCrop()
                .placeholder(R.drawable.image_slider_loading)
                .into(ivCollectionImage);

        tvCollectionName.setText(collectionDetail.getCollectionTitle());
        tvCollectionDescription.setText(collectionDetail.getDiscription());
        tvCollectionPlaces.setText(collectionDetail.getCollectionPlaces());


        list_SearchPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CollectionDetailActivity.this, PlaceDetailActivity.class);
                intent.putExtra("PlaceId", searchPlaceList.get(position).getPlaceId());
                startActivity(intent);
            }
        });

        searchPlaceList = new ArrayList<PlaceOverview>();
        searchPlaceAdapter = new SearchPlaceAdapter(this, searchPlaceList);
        list_SearchPlace.setAdapter(searchPlaceAdapter);


        main_window.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int x, int y, int oldx, int oldy) {

                View view = (View) main_window.getChildAt(main_window.getChildCount() - 1);
                int diff = (view.getBottom() - (main_window.getHeight() + main_window.getScrollY()));

                if (diff == 0 && isSearching == false) {

                    SearchPlaceTask("", searchPlaceList.size(), pageSize);
                }
            }
        });


        SearchPlaceTask("", searchPlaceList.size(), pageSize);
    }

    private void SearchPlaceTask(final String SearchText, final int Skip, final int PageSize) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                isSearching = true;
                if (searchPlaceList.size() == 0) {
                    shimmer_CollectionLoading.startShimmer();
                    shimmer_CollectionLoading.setVisibility(View.VISIBLE);
                } else {
                    lyLoadPlaces.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.PlaceListByBCollectionID(collectionId, Skip, PageSize);

                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {

                try {
                    Type type;
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    final boolean status = jsonObject.getBoolean("Status");
                    final int statusCode = jsonObject.getInt("StatusCode");
                    if (status) {
                        JSONArray BusinessCollectionPlaceData = jsonObject.getJSONArray("BusinessCollectionPlaceData");

                        type = new TypeToken<List<PlaceOverview>>() {
                        }.getType();

                        List<PlaceOverview> currentSearchPlaceList = gson.fromJson(BusinessCollectionPlaceData.toString(), type);

                        searchPlaceList.addAll(currentSearchPlaceList);

                        //   list_SearchPlace.setAdapter(searchPlaceAdapter);

                        searchPlaceAdapter.notifyDataSetChanged();

                    } else {
                        UIHelper.showShortToast(CollectionDetailActivity.this, jsonObject.getString("Message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                isSearching = false;
                shimmer_CollectionLoading.stopShimmer();
                shimmer_CollectionLoading.setVisibility(View.GONE);

                lyLoadPlaces.setVisibility(View.GONE);


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
