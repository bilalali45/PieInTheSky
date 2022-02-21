package pk.com.Taj.app;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import pk.com.Taj.app.beans.UserDetail;
import pk.com.Taj.app.beans.UserInfo;
import pk.com.Taj.app.connectivity.ServiceManager;
import pk.com.Taj.app.helper.UIHelper;
import pk.com.Taj.app.utils.BackgroundRequest;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FollowFollowingActivity extends AppCompatActivity {

    private ServiceManager serviceManager = new ServiceManager();

    private String userId;
    private int listType = 0;

    UserDetail userDetail = null;

    private ShimmerFrameLayout shimmer_UserLoading;
    private LinearLayout lyMainWindow;

    private TextView tvUserFullName;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_following);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userId = getIntent().getStringExtra("UserId");
        listType = getIntent().getIntExtra("ListType", 0);

        shimmer_UserLoading = findViewById(R.id.shimmer_UserLoading);
        lyMainWindow = findViewById(R.id.lyMainWindow);

        shimmer_UserLoading.setVisibility(View.VISIBLE);
        lyMainWindow.setVisibility(View.GONE);

        tvUserFullName = findViewById(R.id.tvUserFullName);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);


        PublicUserDataTask(userId);
    }


    private void PublicUserDataTask(final String UserId) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {

            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.PublicUserData(UserId);
                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                try {

                    Type type;
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {
                        type = new TypeToken<UserDetail>() {
                        }.getType();

                        userDetail = gson.fromJson(jsonObject.getJSONObject("PublicData").toString(), type);

                        type = new TypeToken<List<UserInfo>>() {
                        }.getType();
                        try {
                            List<UserInfo> followerList = null;
                            List<UserInfo> followingList = null;

                            if (!jsonObject.get("FollowerData").equals(null)) {
                                followerList = gson.fromJson(jsonObject.getJSONArray("FollowerData").toString(), type);
                            }

                            if (!jsonObject.get("FollowingData").equals(null)) {
                                followingList = gson.fromJson(jsonObject.getJSONArray("FollowingData").toString(), type);
                            }


                            userDetail.setFollowerData(followerList);
                            userDetail.setFollowingData(followingList);
                        } catch (Exception e) {
                        }
                        tvUserFullName.setText(userDetail.getFirstName() + " " + userDetail.getLastName());

                        setupViewPager(viewPager);

                        tabLayout.setupWithViewPager(viewPager);

                        tabLayout.getTabAt(listType).select();

                    } else {
                        UIHelper.showShortToast(FollowFollowingActivity.this, jsonObject.getString("Message"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                lyMainWindow.setVisibility(View.VISIBLE);

                shimmer_UserLoading.stopShimmer();
                shimmer_UserLoading.setVisibility(View.GONE);

            }
        }.execute();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        FollowFollowingFragment followerFragment = new FollowFollowingFragment();
        FollowFollowingFragment followingFragment = new FollowFollowingFragment();

        followerFragment.setUserInfoList(userDetail.getFollowerData());
        followingFragment.setUserInfoList(userDetail.getFollowingData());

        adapter.addFragment(followerFragment, "Followers (" + userDetail.getTotalFollower() + ")");
        adapter.addFragment(followingFragment, "Following (" + userDetail.getTotalFollowing() + ")");

        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void btnUserTimeline_onClick(View view) {
        Intent intent = new Intent(this, UserTimelineActivity.class);
        startActivity(intent);
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
