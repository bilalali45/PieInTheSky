package pk.com.pieinthesky.app;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import pk.com.pieinthesky.app.beans.PlaceOverview;
import pk.com.pieinthesky.app.connectivity.ServiceManager;
import pk.com.pieinthesky.app.helper.SpinnerItem;
import pk.com.pieinthesky.app.helper.UIHelper;
import pk.com.pieinthesky.app.utils.BackgroundRequest;
import pk.com.pieinthesky.app.widget.NonScrollListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends AppCompatActivity {


    private ServiceManager serviceManager = new ServiceManager();

    private NestedScrollView main_window;
    private ShimmerFrameLayout shimmer_SearchPlace;
    private RecyclerView recycler_SubCategoryList;

    private List<PlaceOverview> searchPlaceList = null;
    private SearchPlaceAdapter searchPlaceAdapter = null;

    private NonScrollListView list_SearchPlace;
    private EditText etSearch;
    private LinearLayout lyLoadPlaces;

    int pageSize = 10;
    boolean isSearching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String searchText = getIntent().getStringExtra("SearchText");

        main_window = findViewById(R.id.main_window);
        shimmer_SearchPlace = findViewById(R.id.shimmer_SearchPlace);

        recycler_SubCategoryList = findViewById(R.id.recycler_SubCategoryList);
        list_SearchPlace = findViewById(R.id.list_SearchPlace);
        etSearch = findViewById(R.id.etSearch);
        lyLoadPlaces = findViewById(R.id.lyLoadPlaces);

        recycler_SubCategoryList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        etSearch.setText(searchText);

        final List<SpinnerItem> spinnerItems = new ArrayList<SpinnerItem>();
        spinnerItems.add(new SpinnerItem("m_breakfast", "Breakfast"));
        spinnerItems.add(new SpinnerItem("m_coffee", "Coffee"));
        spinnerItems.add(new SpinnerItem("m_lunch", "Lunch"));
        spinnerItems.add(new SpinnerItem("m_dinner", "Dinner"));

        SubCategoryAdapter subCategoryAdapter = new SubCategoryAdapter(this, spinnerItems);


        subCategoryAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position, long id) {
                searchPlaceList.clear();
                searchPlaceAdapter.notifyDataSetChanged();
                SearchPlaceTask(spinnerItems.get(position).getText(), searchPlaceList.size(), pageSize);
            }
        });

        recycler_SubCategoryList.setAdapter(subCategoryAdapter);

        list_SearchPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, PlaceDetailActivity.class);
                intent.putExtra("PlaceId", searchPlaceList.get(position).getPlaceId());
                startActivity(intent);
            }
        });


        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (etSearch.getText().length() > 0) {
                        searchPlaceList.clear();
                        searchPlaceAdapter.notifyDataSetChanged();
                        SearchPlaceTask(etSearch.getText().toString(), searchPlaceList.size(), pageSize);
                    }
                }
                return false;
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

                    SearchPlaceTask(etSearch.getText().toString(), searchPlaceList.size(), pageSize);
                }
            }
        });

        SearchPlaceTask(etSearch.getText().toString(), searchPlaceList.size(), pageSize);
    }

    public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.CustomViewHolder> {
        private Context context;
        private List<SpinnerItem> subCategoryList;
        private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

        public SubCategoryAdapter(Context context, List<SpinnerItem> subCategoryList) {
            this.context = context;
            this.subCategoryList = subCategoryList;
        }

        @Override
        public SubCategoryAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SubCategoryAdapter.CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.sub_category_item, parent, false));
        }

        @Override
        public void onBindViewHolder(SubCategoryAdapter.CustomViewHolder customViewHolder, int position) {
            customViewHolder.ivCategoryImage.setImageResource(getResources().getIdentifier(subCategoryList.get(position).getValue(), "drawable", context.getPackageName()));
            customViewHolder.tvCategoryName.setText(subCategoryList.get(position).getText());

            customViewHolder.setOnRecyclerViewItemClickListener(onRecyclerViewItemClickListener);
        }

        @Override
        public int getItemCount() {
            return subCategoryList.size();
        }

        public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
            this.onRecyclerViewItemClickListener = listener;
        }

        public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

            ImageView ivCategoryImage;
            TextView tvCategoryName;

            public CustomViewHolder(View view) {
                super(view);
                ivCategoryImage = view.findViewById(R.id.ivCategoryImage);
                tvCategoryName = view.findViewById(R.id.tvCategoryName);
                view.setOnClickListener(this);
            }

            public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
                this.onRecyclerViewItemClickListener = listener;
            }

            @Override
            public void onClick(View view) {
                if (onRecyclerViewItemClickListener != null) {
                    onRecyclerViewItemClickListener.onClick(view, getPosition(), view.getId());
                }
            }
        }
    }

    public interface OnRecyclerViewItemClickListener {
        public void onClick(View view, int position, long id);
    }


    private void SearchPlaceTask(final String SearchText, final int Skip, final int PageSize) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                isSearching = true;
                if (searchPlaceList.size() == 0) {
                    shimmer_SearchPlace.startShimmer();
                    shimmer_SearchPlace.setVisibility(View.VISIBLE);
                } else {
                    lyLoadPlaces.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;

                double latitude = 0;
                double longitude = 0;

                if (Configuration.getCurrentLocation() != null) {
                    latitude = Configuration.getCurrentLocation().getLatitude();
                    longitude = Configuration.getCurrentLocation().getLongitude();
                }

                jobject = serviceManager.SearchPlace(SearchText, latitude, longitude, new HashMap<String, List<String>>(), Skip, PageSize);

                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {

                try {
                    Type type;
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    final boolean status = jsonObject.getBoolean("Status");
                    final int statusCode = jsonObject.getInt("StatusCode");
                    if (status) {
                        JSONArray SearchData = jsonObject.getJSONArray("SearchData");

                        type = new TypeToken<List<PlaceOverview>>() {
                        }.getType();

                        List<PlaceOverview> currentSearchPlaceList = gson.fromJson(SearchData.toString(), type);

                        searchPlaceList.addAll(currentSearchPlaceList);

                        //   list_SearchPlace.setAdapter(searchPlaceAdapter);

                        searchPlaceAdapter.notifyDataSetChanged();

                    } else {
                        UIHelper.showShortToast(SearchActivity.this, jsonObject.getString("Message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                isSearching = false;
                shimmer_SearchPlace.stopShimmer();
                shimmer_SearchPlace.setVisibility(View.GONE);

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
