package pk.com.Taj.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import pk.com.Taj.app.beans.CollectionDetail;
import pk.com.Taj.app.beans.PlaceOverview;
import pk.com.Taj.app.connectivity.ServiceManager;
import pk.com.Taj.app.helper.UIHelper;
import pk.com.Taj.app.utils.BackgroundRequest;
import pk.com.Taj.app.widget.NonScrollListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class GoOutFragment extends Fragment {

    private ServiceManager serviceManager = new ServiceManager();

    private NestedScrollView main_window;
    private ShimmerFrameLayout shimmer_Collection;
    private RecyclerView recycler_Collection;
    private LinearLayout btnSeeAll;
    private NonScrollListView list_PlaceCategory;
    private List<CollectionDetail> collectionList = null;

    private EditText etSearch;

    int pageSize = 10;
    boolean isSearching = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_go_out, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        main_window = getView().findViewById(R.id.main_window);
        shimmer_Collection = getView().findViewById(R.id.shimmer_Collection);
        recycler_Collection = getView().findViewById(R.id.recycler_Collection);
        list_PlaceCategory = getView().findViewById(R.id.list_PlaceCategory);
        btnSeeAll = getView().findViewById(R.id.btnSeeAll);
        etSearch = getView().findViewById(R.id.etSearch);

        recycler_Collection.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        CollectionListTask();

        btnSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CollectionListActivity.class);
                startActivity(intent);
            }
        });

        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (etSearch.getText().length() > 0) {
                        Intent intent = new Intent(getActivity(), SearchActivity.class);
                        intent.putExtra("SearchText", etSearch.getText().toString());
                        startActivity(intent);
                    }
                }
                return false;
            }
        });


        main_window.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int x, int y, int oldx, int oldy) {

                View view = (View) main_window.getChildAt(main_window.getChildCount() - 1);
                int diff = (view.getBottom() - (main_window.getHeight() + main_window.getScrollY()));

                if (diff == 0 && isSearching == false) {


                }
            }
        });

        PlaceListTask();

    }


    private class PlaceCategoryAdapter extends BaseAdapter {
        Context context;
        private List<CollectionDetail> placeCategoryList;

        public PlaceCategoryAdapter(Context context, List<CollectionDetail> placeCategoryList) {
            this.context = context;
            this.placeCategoryList = placeCategoryList;
        }

        @Override
        public int getCount() {
            return placeCategoryList.size();
        }

        @Override
        public Object getItem(int position) {
            return placeCategoryList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final PlaceCategoryAdapter.ViewHolder viewHolder;
            if (convertView == null) {
                final LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(R.layout.place_category_row, null);


                TextView tvCategoryName = convertView.findViewById(R.id.tvCategoryName);
                LinearLayout btnSeeAll = convertView.findViewById(R.id.btnSeeAll);
                RecyclerView recycler_Place = convertView.findViewById(R.id.recycler_Place);

                viewHolder = new PlaceCategoryAdapter.ViewHolder(tvCategoryName, btnSeeAll, recycler_Place);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (PlaceCategoryAdapter.ViewHolder) convertView.getTag();
            }


            viewHolder.tvCategoryName.setText(placeCategoryList.get(position).getCollectionTitle());
            viewHolder.btnSeeAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), CollectionDetailActivity.class);
                    intent.putExtra("CollectionDetail", collectionList.get(position));
                    startActivity(intent);
                }
            });

            viewHolder.recycler_Place.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

            final List<PlaceOverview> placeOverviewList = placeCategoryList.get(position).getResturantProfileList();
            PlaceOverviewAdapter placeOverviewAdapter = new PlaceOverviewAdapter(getActivity(), placeOverviewList);

            placeOverviewAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void onClick(View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), PlaceDetailActivity.class);
                    intent.putExtra("PlaceId", placeOverviewList.get(position).getPlaceId());
                    startActivity(intent);
                }
            });

            viewHolder.recycler_Place.setAdapter(placeOverviewAdapter);

            return convertView;
        }


        private class ViewHolder {
            TextView tvCategoryName;
            LinearLayout btnSeeAll;
            RecyclerView recycler_Place;


            public ViewHolder(TextView tvCategoryName, LinearLayout btnSeeAll, RecyclerView recycler_Place) {
                this.tvCategoryName = tvCategoryName;
                this.btnSeeAll = btnSeeAll;
                this.recycler_Place = recycler_Place;
            }
        }

    }

    private void PlaceListTask() {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {

            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.Top10BusinessCollection();

                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {

                try {
                    Type type;
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    final boolean status = true;
                    final int statusCode = jsonObject.getInt("StatusCode");
                    if (status) {
                        JSONArray BusinessCollectionData = jsonObject.getJSONArray("BusinessCollectionData");

                        type = new TypeToken<List<CollectionDetail>>() {
                        }.getType();

                        List<CollectionDetail> collectionList  = gson.fromJson(BusinessCollectionData.toString(), type);

                        PlaceCategoryAdapter placeCategoryAdapter = new PlaceCategoryAdapter(getActivity(), collectionList);

                        list_PlaceCategory.setAdapter(placeCategoryAdapter);

                    } else {
                        UIHelper.showShortToast(getActivity(), jsonObject.getString("Message"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }.execute();
    }


    public class PlaceOverviewAdapter extends RecyclerView.Adapter<PlaceOverviewAdapter.CustomViewHolder> {
        private Context context;
        private List<PlaceOverview> placeOverviewList;
        private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

        public PlaceOverviewAdapter(Context context, List<PlaceOverview> placeOverviewList) {
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


    private void CollectionListTask() {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {

            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.BusinessCollection(0,10);

                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {

                try {
                    Type type;
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    final boolean status = true;
                    final int statusCode = jsonObject.getInt("StatusCode");
                    if (status) {
                        JSONArray BusinessCollectionData = jsonObject.getJSONArray("BusinessCollectionData");

                        type = new TypeToken<List<CollectionDetail>>() {
                        }.getType();

                        collectionList = gson.fromJson(BusinessCollectionData.toString(), type);

                        CollectionListAdapter collectionListAdapter = new CollectionListAdapter(getActivity(), collectionList);

                        collectionListAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
                            @Override
                            public void onClick(View view, int position, long id) {
                                Intent intent = new Intent(getActivity(), CollectionDetailActivity.class);
                                intent.putExtra("CollectionDetail", collectionList.get(position));
                                startActivity(intent);
                            }
                        });

                        recycler_Collection.setAdapter(collectionListAdapter);

                    } else {
                        UIHelper.showShortToast(getActivity(), jsonObject.getString("Message"));
                    }

                    shimmer_Collection.stopShimmer();
                    shimmer_Collection.setVisibility(View.GONE);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }


    public class CollectionListAdapter extends RecyclerView.Adapter<CollectionListAdapter.CustomViewHolder> {
        private Context context;
        private List<CollectionDetail> collectionList = null;
        private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

        public CollectionListAdapter(Context context, List<CollectionDetail> collectionList) {
            this.context = context;
            this.collectionList = collectionList;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.collection_detail_short_item, parent, false));
        }

        @Override
        public void onBindViewHolder(CustomViewHolder customViewHolder, int position) {

            Glide.with(getActivity())
                    .load(collectionList.get(position).getCoverImageURL())
                    .centerCrop()
                    .placeholder(R.drawable.image_slider_loading)
                    .into(customViewHolder.ivCollectionImage);

            customViewHolder.tvCollectionName.setText(collectionList.get(position).getCollectionTitle());
            customViewHolder.tvCollectionPlaces.setText(collectionList.get(position).getCollectionPlaces());

            customViewHolder.setOnRecyclerViewItemClickListener(onRecyclerViewItemClickListener);

        }

        @Override
        public int getItemCount() {
            return collectionList.size();
        }


        public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
            this.onRecyclerViewItemClickListener = listener;
        }

        public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

            ImageView ivCollectionImage;
            TextView tvCollectionName;
            TextView tvCollectionPlaces;

            public CustomViewHolder(View view) {
                super(view);
                ivCollectionImage = view.findViewById(R.id.ivCollectionImage);
                tvCollectionName = view.findViewById(R.id.tvCollectionName);
                tvCollectionPlaces = view.findViewById(R.id.tvCollectionPlaces);
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

}