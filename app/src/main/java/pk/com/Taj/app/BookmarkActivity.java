package pk.com.Taj.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pk.com.Taj.app.adapter.SearchPlaceAdapter;
import pk.com.Taj.app.beans.PlaceOverview;
import pk.com.Taj.app.connectivity.ServiceManager;
import pk.com.Taj.app.helper.UIHelper;
import pk.com.Taj.app.utils.BackgroundRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {


    ServiceManager serviceManager = new ServiceManager();

    private ShimmerFrameLayout shimmer_BookmarkPlace;
    private ListView list_Bookmark;

    private List<PlaceOverview> bookmarkList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        getSupportActionBar().setTitle("Bookmark");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        shimmer_BookmarkPlace = findViewById(R.id.shimmer_BookmarkPlace);

        list_Bookmark = findViewById(R.id.list_Bookmark);

        list_Bookmark.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), PlaceDetailActivity.class);
                intent.putExtra("PlaceId", bookmarkList.get(position).getPlaceId());
                startActivity(intent);
            }
        });

        BookmarkTask();
    }



    private void BookmarkTask() {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                shimmer_BookmarkPlace.startShimmer();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.UserCollection();

                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {

                try {
                    Type type;
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    final boolean status = jsonObject.getBoolean("Status");
                    final int statusCode = jsonObject.getInt("StatusCode");
                    if (status) {
                        JSONArray PlaceCollectionData = jsonObject.getJSONArray("PlaceCollectionData");

                        type = new TypeToken<List<PlaceOverview>>() {
                        }.getType();

                        bookmarkList = gson.fromJson(PlaceCollectionData.toString(), type);

                        SearchPlaceAdapter bookmarkAdapter = new SearchPlaceAdapter(BookmarkActivity.this, bookmarkList);
                        list_Bookmark.setAdapter(bookmarkAdapter);

                    } else {
                        UIHelper.showShortToast(BookmarkActivity.this, jsonObject.getString("Message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                shimmer_BookmarkPlace.stopShimmer();
                shimmer_BookmarkPlace.setVisibility(View.GONE);
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
