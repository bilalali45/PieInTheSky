package pk.com.pieinthesky.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import pk.com.pieinthesky.app.beans.PlaceOverview;
import pk.com.pieinthesky.app.beans.ShortcutFilterDetail;
import pk.com.pieinthesky.app.connectivity.ServiceManager;
import pk.com.pieinthesky.app.dialog.FilterDialog;
import pk.com.pieinthesky.app.helper.SpinnerItem;
import pk.com.pieinthesky.app.helper.UIHelper;
import pk.com.pieinthesky.app.utils.BackgroundRequest;
import pk.com.pieinthesky.app.widget.IconTextView;
import pk.com.pieinthesky.app.widget.NonScrollListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashboardFragment extends Fragment {

    private ServiceManager serviceManager = new ServiceManager();

    private NestedScrollView main_window;
    private ShimmerFrameLayout shimmer_HighRatingList;
    private ShimmerFrameLayout shimmer_SearchPlace;
    private ShimmerFrameLayout shimmer_FilterLoading;
    private RecyclerView recycler_HighRatingList;
    private RecyclerView recycler_SubCategoryList;

    private List<PlaceOverview> placeOverviewList = null;
    private List<PlaceOverview> searchPlaceList = null;
    private SearchPlaceAdapter searchPlaceAdapter = null;

    private NonScrollListView list_SearchPlace;
    private EditText etSearch;
    private TextView tvFilterBadge;
    private ImageView btnFilter;
    private LinearLayout lyLoadPlaces;

    private FilterDialog filterDialog;

    private HashMap<String, List<String>> filter = new HashMap<String, List<String>>();
    List<ShortcutFilterDetail> shortcutFilterDetails;
    int pageSize = 10;
    boolean isSearching = false;
    private LinearLayout container_filter;
    private LinearLayout lyFilterBarButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        main_window = getView().findViewById(R.id.main_window);
        shimmer_HighRatingList = getView().findViewById(R.id.shimmer_HighRatingList);
        shimmer_SearchPlace = getView().findViewById(R.id.shimmer_SearchPlace);
        shimmer_FilterLoading = getView().findViewById(R.id.shimmer_FilterLoading);

        recycler_HighRatingList = getView().findViewById(R.id.recycler_HighRatingList);
        recycler_SubCategoryList = getView().findViewById(R.id.recycler_SubCategoryList);
        list_SearchPlace = getView().findViewById(R.id.list_SearchPlace);
        etSearch = getView().findViewById(R.id.etSearch);
        tvFilterBadge = getView().findViewById(R.id.tvFilterBadge);
        btnFilter = getView().findViewById(R.id.btnFilter);
        lyLoadPlaces = getView().findViewById(R.id.lyLoadPlaces);
        container_filter = getView().findViewById(R.id.container_filter);
        lyFilterBarButton = getView().findViewById(R.id.lyFilterBarButton);
        recycler_HighRatingList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recycler_SubCategoryList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        container_filter.setVisibility(View.GONE);
        tvFilterBadge.setVisibility(View.GONE);

        List<SpinnerItem> spinnerItems = new ArrayList<SpinnerItem>();
        spinnerItems.add(new SpinnerItem("m_breakfast", "Breakfast"));
        spinnerItems.add(new SpinnerItem("m_coffee", "Coffee"));
        spinnerItems.add(new SpinnerItem("m_lunch", "Lunch"));
        spinnerItems.add(new SpinnerItem("m_dinner", "Dinner"));

        SubCategoryAdapter subCategoryAdapter = new SubCategoryAdapter(getActivity(), spinnerItems);
        recycler_SubCategoryList.setAdapter(subCategoryAdapter);

        list_SearchPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PlaceDetailActivity.class);
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

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Configuration.getFilterDetails() != null && Configuration.getFilterDetails().size() != 0) {
                    if (filterDialog == null) {
                        filterDialog = new FilterDialog(getContext(), R.style.FullViewDialog, Configuration.getFilterDetails());

                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                        int filterDialogHeight = (displayMetrics.heightPixels / 100) * 75;

                        //filterDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        filterDialog.getWindow().setGravity(Gravity.BOTTOM);
                        filterDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, filterDialogHeight);

                        filterDialog.setOnApplyClickListener(new FilterDialog.OnApplyClickListener() {
                            @Override
                            public void onClick(View view) {
                                filter = filterDialog.getFilter();

                                searchPlaceList.clear();
                                searchPlaceAdapter.notifyDataSetChanged();
                                SearchPlaceTask(etSearch.getText().toString(), searchPlaceList.size(), pageSize);

                                if (filterDialog.getMultipleChoiceSelectedItemCount() != 0) {
                                    if (filterDialog.getMultipleChoiceSelectedItemCount() < 100) {
                                        tvFilterBadge.setText(filterDialog.getMultipleChoiceSelectedItemCount() + "");
                                    } else {
                                        tvFilterBadge.setText("99+");
                                    }
                                    tvFilterBadge.setVisibility(View.VISIBLE);
                                } else {
                                    tvFilterBadge.setText("0");
                                    tvFilterBadge.setVisibility(View.GONE);
                                }
                                filterDialog.dismiss();
                            }
                        });

                        filterDialog.setOnClearClickListener(new FilterDialog.OnClearClickListener() {
                            @Override
                            public void onClick(View view) {
                                filter = filterDialog.getFilter();
                                clearShortcutFilter();

                                searchPlaceList.clear();
                                searchPlaceAdapter.notifyDataSetChanged();
                                SearchPlaceTask(etSearch.getText().toString(), searchPlaceList.size(), pageSize);

                                tvFilterBadge.setText("0");
                                tvFilterBadge.setVisibility(View.GONE);

                                filterDialog.dismiss();
                            }
                        });
                    }
                    filterDialog.show();
                }

            }
        });

        searchPlaceList = new ArrayList<PlaceOverview>();
        searchPlaceAdapter = new SearchPlaceAdapter(getActivity(), searchPlaceList);
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

        HighRatingListTask();

        SearchPlaceTask(etSearch.getText().toString(), searchPlaceList.size(), pageSize);
    }


    public void setShortcutFilter() {

        shimmer_FilterLoading.setVisibility(View.GONE);
        container_filter.setVisibility(View.VISIBLE);

        lyFilterBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFilter.performClick();
            }
        });

        shortcutFilterDetails = Configuration.getShortcutFilterDetails();


        if (shortcutFilterDetails != null) {

            for (ShortcutFilterDetail shortcutFilterDetail : shortcutFilterDetails) {

                View lyFilterItem = LayoutInflater.from(getActivity()).inflate(R.layout.filter_items, null);
                LinearLayout container_layout = lyFilterItem.findViewById(R.id.container_layout);
                TextView tvFilterTitle = lyFilterItem.findViewById(R.id.tvFilterTitle);
                tvFilterTitle.setTextColor(getResources().getColor(R.color.colorGray));
                tvFilterTitle.setText(shortcutFilterDetail.getText());
                tvFilterTitle.setTag(R.id.Id, shortcutFilterDetail.getId());
                tvFilterTitle.setTag(R.id.Key, shortcutFilterDetail.getKey());

                container_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        IconTextView itvFilterRemove = view.findViewById(R.id.itvFilterRemove);
                        TextView tvFilterTitle = view.findViewById(R.id.tvFilterTitle);

                        if (itvFilterRemove.getVisibility() == View.GONE) {
                            view.setBackground(getResources().getDrawable(R.drawable.bg_corner_pink_filled));
                            tvFilterTitle.setTextColor(getResources().getColor(R.color.colorWhite));
                            itvFilterRemove.setVisibility(View.VISIBLE);

                            searchFilter(tvFilterTitle.getTag(R.id.Key).toString(), tvFilterTitle.getTag(R.id.Id).toString(), false);

                        } else {
                            view.setBackground(getResources().getDrawable(R.drawable.bg_gray_corner_filter));
                            tvFilterTitle.setTextColor(getResources().getColor(R.color.colorGray));
                            itvFilterRemove.setVisibility(View.GONE);

                            searchFilter(tvFilterTitle.getTag(R.id.Key).toString(), tvFilterTitle.getTag(R.id.Id).toString(), true);
                        }
                    }
                });

                container_filter.addView(lyFilterItem);
            }
        }

    }


    private void clearShortcutFilter() {
        for (int i = 1; i < container_filter.getChildCount(); i++) {
            LinearLayout container_layout = container_filter.getChildAt(i).findViewById(R.id.container_layout);
            TextView tvFilterTitle = container_layout.findViewById(R.id.tvFilterTitle);
            IconTextView itvFilterRemove = container_layout.findViewById(R.id.itvFilterRemove);

            container_layout.setBackground(getResources().getDrawable(R.drawable.bg_gray_corner_filter));
            tvFilterTitle.setTextColor(getResources().getColor(R.color.colorGray));
            itvFilterRemove.setVisibility(View.GONE);

        }
    }

    private void searchFilter(String Key, String Id, boolean isRemove) {
        if (filter.containsKey(Key) == false) {
            filter.put(Key, new ArrayList<String>());
        }

        if (filter.get(Key).contains(Id) == false && isRemove == false) {
            filter.get(Key).add(Id);
        } else if (isRemove) {
            filter.get(Key).remove(Id);
        }

        searchPlaceList.clear();
        searchPlaceAdapter.notifyDataSetChanged();
        SearchPlaceTask(etSearch.getText().toString(), searchPlaceList.size(), pageSize);
    }

    public class HighRatingListAdapter extends RecyclerView.Adapter<HighRatingListAdapter.CustomViewHolder> {
        private Context context;
        private List<PlaceOverview> placeOverviewList;
        private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

        public HighRatingListAdapter(Context context, List<PlaceOverview> placeOverviewList) {
            this.context = context;
            this.placeOverviewList = placeOverviewList;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.place_overview_short_item, parent, false));
        }

        @Override
        public void onBindViewHolder(CustomViewHolder customViewHolder, int position) {

            Glide.with(getActivity())
                    .load(placeOverviewList.get(position).getProfileImageURL())
                    .centerCrop()
                    .placeholder(R.drawable.image_slider_loading)
                    .into(customViewHolder.ivPlaceImage);

            customViewHolder.tvPlaceName.setText(placeOverviewList.get(position).getPlaceName());
            customViewHolder.tvTags.setText(placeOverviewList.get(position).getCuisines());
            customViewHolder.tvLocation.setText(placeOverviewList.get(position).getPlaceLocation());
            customViewHolder.tvTotalReview.setText(placeOverviewList.get(position).getTotalReview());
            customViewHolder.tvAvgRating.setText(placeOverviewList.get(position).getBusinessAvgRating());

            customViewHolder.setOnRecyclerViewItemClickListener(onRecyclerViewItemClickListener);

        }

        @Override
        public int getItemCount() {
            return placeOverviewList.size();
        }


        public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
            this.onRecyclerViewItemClickListener = listener;
        }

        public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

            ImageView ivPlaceImage;
            TextView tvPlaceName;
            TextView tvTags;
            TextView tvLocation;
            TextView tvTotalReview;
            TextView tvAvgRating;

            public CustomViewHolder(View view) {
                super(view);
                ivPlaceImage = view.findViewById(R.id.ivPlaceImage);
                tvPlaceName = view.findViewById(R.id.tvPlaceName);
                tvTags = view.findViewById(R.id.tvTags);
                tvLocation = view.findViewById(R.id.tvLocation);
                tvTotalReview = view.findViewById(R.id.tvTotalReview);
                tvAvgRating = view.findViewById(R.id.tvAvgRating);
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

    public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.CustomViewHolder> {
        private Context context;
        private List<SpinnerItem> subCategoryList;
        private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

        public SubCategoryAdapter(Context context, List<SpinnerItem> subCategoryList) {
            this.context = context;
            this.subCategoryList = subCategoryList;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.sub_category_item, parent, false));
        }

        @Override
        public void onBindViewHolder(CustomViewHolder customViewHolder, int position) {
            customViewHolder.ivCategoryImage.setImageResource(getResources().getIdentifier(subCategoryList.get(position).getValue(), "drawable", getActivity().getPackageName()));
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

    private void HighRatingListTask() {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                shimmer_HighRatingList.startShimmer();
                shimmer_HighRatingList.setVisibility(View.VISIBLE);
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.HighRatingList();

                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {

                try {
                    Type type;
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    final boolean status = jsonObject.getBoolean("Status");
                    final int statusCode = jsonObject.getInt("StatusCode");
                    if (status) {
                        JSONArray highRatingData = jsonObject.getJSONArray("highRatingData");

                        type = new TypeToken<List<PlaceOverview>>() {
                        }.getType();

                        placeOverviewList = gson.fromJson(highRatingData.toString(), type);

                        HighRatingListAdapter highRatingListAdapter = new HighRatingListAdapter(getActivity(), placeOverviewList);

                        highRatingListAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
                            @Override
                            public void onClick(View view, int position, long id) {
                                Intent intent = new Intent(getActivity(), PlaceDetailActivity.class);
                                intent.putExtra("PlaceId", placeOverviewList.get(position).getPlaceId());
                                startActivity(intent);
                            }
                        });

                        recycler_HighRatingList.setAdapter(highRatingListAdapter);

                    } else {
                        UIHelper.showShortToast(getActivity(), jsonObject.getString("Message"));
                    }

                    shimmer_HighRatingList.stopShimmer();
                    shimmer_HighRatingList.setVisibility(View.GONE);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute();
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

                jobject = serviceManager.SearchPlace(SearchText, latitude, longitude, filter, Skip, PageSize);

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
                        UIHelper.showShortToast(getActivity(), jsonObject.getString("Message"));
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


}