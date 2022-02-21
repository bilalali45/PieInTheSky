package pk.com.Taj.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import pk.com.Taj.app.adapter.UserInfoAdapter;
import pk.com.Taj.app.beans.UserInfo;
import pk.com.Taj.app.connectivity.ServiceManager;
import pk.com.Taj.app.utils.BackgroundRequest;

import org.json.JSONObject;

import java.util.List;


public class FollowFollowingFragment extends Fragment {

    private ServiceManager serviceManager = new ServiceManager();

    private ListView list_UserInfo;
    private List<UserInfo> userInfoList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follow_following, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        list_UserInfo = getView().findViewById(R.id.list_UserInfo);

        list_UserInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), UserTimelineActivity.class);
                intent.putExtra("UserId", userInfoList.get(position).getId());
                startActivity(intent);
            }
        });

        if (userInfoList != null) {
            UserInfoAdapter userInfoAdapter = new UserInfoAdapter(getActivity(), userInfoList);
            userInfoAdapter.setOnItemClickListener(onItemClickListener);
            list_UserInfo.setAdapter(userInfoAdapter);
        }
    }


    private UserInfoAdapter.OnItemClickListener onItemClickListener = new UserInfoAdapter.OnItemClickListener() {
        @Override
        public void onUserClick(View view, int position, long id, UserInfoAdapter.ViewHolder viewHolder) {
            Intent intent = new Intent(getActivity(), UserTimelineActivity.class);
            intent.putExtra("UserId", userInfoList.get(position).getId());
            startActivity(intent);
        }

        @Override
        public void onUserFollowClick(View view, int position, long id, UserInfoAdapter.ViewHolder viewHolder) {
            UserFollowTask(position,userInfoList.get(position).getId(),viewHolder);
        }


    };


    private void UserFollowTask(final int position, final String UserID, final UserInfoAdapter.ViewHolder viewHolder) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {

            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.UserFollow(UserID);
                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                try {

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {
                        if (userInfoList.get(position).isFollow() == false)
                        {
                            viewHolder.btnFollow.setText("followed");
                            viewHolder.btnFollow.setBackgroundResource(R.drawable.button_user_follow_fill);
                            viewHolder.btnFollow.setTextColor(getResources().getColor(R.color.colorWhite));
                            userInfoList.get(position).setFollow(true);
                        }
                        else
                        {
                            viewHolder.btnFollow.setText("follow");
                            viewHolder.btnFollow.setBackgroundResource(R.drawable.button_user_follow);
                            viewHolder.btnFollow.setTextColor(getResources().getColor(R.color.colorFollow));
                            userInfoList.get(position).setFollow(false);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }


    public List<UserInfo> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }
}
