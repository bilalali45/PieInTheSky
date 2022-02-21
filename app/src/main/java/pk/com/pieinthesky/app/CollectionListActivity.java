package pk.com.pieinthesky.app;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pk.com.pieinthesky.app.beans.CollectionDetail;
import pk.com.pieinthesky.app.connectivity.ServiceManager;
import pk.com.pieinthesky.app.helper.UIHelper;
import pk.com.pieinthesky.app.utils.BackgroundRequest;
import pk.com.pieinthesky.app.widget.NonScrollListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CollectionListActivity extends AppCompatActivity {


    private ServiceManager serviceManager = new ServiceManager();

    private NestedScrollView main_window;
    private ShimmerFrameLayout shimmer_Collection;

    private List<CollectionDetail> collectionList = null;
    private CollectionListAdapter collectionListAdapter = null;

    private NonScrollListView list_Collection;
    private LinearLayout lyLoadCollections;

    int pageSize = 10;
    boolean isSearching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        main_window = findViewById(R.id.main_window);
        shimmer_Collection = findViewById(R.id.shimmer_Collection);

        list_Collection = findViewById(R.id.list_Collection);
        lyLoadCollections = findViewById(R.id.lyLoadCollections);

        list_Collection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CollectionListActivity.this, CollectionDetailActivity.class);
                intent.putExtra("CollectionDetail", collectionList.get(position));
                startActivity(intent);
            }
        });

        collectionList = new ArrayList<CollectionDetail>();
        collectionListAdapter = new CollectionListAdapter(CollectionListActivity.this, collectionList);
        list_Collection.setAdapter(collectionListAdapter);

        main_window.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int x, int y, int oldx, int oldy) {

                View view = (View) main_window.getChildAt(main_window.getChildCount() - 1);
                int diff = (view.getBottom() - (main_window.getHeight() + main_window.getScrollY()));

                if (diff == 0 && isSearching == false) {
                    CollectionListTask(collectionList.size(), pageSize);
                }
            }
        });

        CollectionListTask(collectionList.size(), pageSize);
    }


    private void CollectionListTask(final int Skip, final int PageSize) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                isSearching = true;
                if (collectionList.size() == 0) {
                    shimmer_Collection.startShimmer();
                    shimmer_Collection.setVisibility(View.VISIBLE);
                } else {
                    lyLoadCollections.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.BusinessCollection(Skip,PageSize);

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

                        List<CollectionDetail>   currentCollectionList = gson.fromJson(BusinessCollectionData.toString(), type);

                        collectionList.addAll(currentCollectionList);

                        //   list_Collection.setAdapter(collectionListAdapter);

                        collectionListAdapter.notifyDataSetChanged();

                    } else {
                        UIHelper.showShortToast(CollectionListActivity.this, jsonObject.getString("Message"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                isSearching = false;
                shimmer_Collection.stopShimmer();
                shimmer_Collection.setVisibility(View.GONE);

                lyLoadCollections.setVisibility(View.GONE);

            }
        }.execute();
    }

    private class CollectionListAdapter extends BaseAdapter {
        private Context context;
        private List<CollectionDetail> collectionList = null;

        public CollectionListAdapter(Context context, List<CollectionDetail> collectionList) {
            this.context = context;
            this.collectionList = collectionList;
        }

        @Override
        public int getCount() {
            return collectionList.size();
        }

        @Override
        public Object getItem(int position) {
            return collectionList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final CollectionListAdapter.ViewHolder viewHolder;
            if (convertView == null) {
                final LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(R.layout.collection_detail_row, null);

                ImageView ivCollectionImage = convertView.findViewById(R.id.ivCollectionImage);
                TextView tvCollectionName = convertView.findViewById(R.id.tvCollectionName);
                TextView tvCollectionDescription = convertView.findViewById(R.id.tvCollectionDescription);
                TextView tvCollectionPlaces = convertView.findViewById(R.id.tvCollectionPlaces);

                viewHolder = new CollectionListAdapter.ViewHolder(ivCollectionImage, tvCollectionName, tvCollectionDescription, tvCollectionPlaces);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (CollectionListAdapter.ViewHolder) convertView.getTag();
            }


            Glide.with(context)
                    .load(collectionList.get(position).getCoverImageURL())
                    .centerCrop()
                    .placeholder(R.drawable.image_slider_loading)
                    .into(viewHolder.ivCollectionImage);


            viewHolder.tvCollectionName.setText(collectionList.get(position).getCollectionTitle());
            viewHolder.tvCollectionDescription.setText(collectionList.get(position).getDiscription());
            viewHolder.tvCollectionPlaces.setText(collectionList.get(position).getCollectionPlaces());


            return convertView;
        }


        private class ViewHolder {
            ImageView ivCollectionImage;
            TextView tvCollectionName;
            TextView tvCollectionDescription;
            TextView tvCollectionPlaces;

            public ViewHolder(ImageView ivCollectionImage, TextView tvCollectionName, TextView tvCollectionDescription, TextView tvCollectionPlaces) {
                this.ivCollectionImage = ivCollectionImage;
                this.tvCollectionName = tvCollectionName;
                this.tvCollectionDescription = tvCollectionDescription;
                this.tvCollectionPlaces = tvCollectionPlaces;

            }
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
