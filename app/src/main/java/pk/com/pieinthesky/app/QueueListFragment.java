package pk.com.pieinthesky.app;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pk.com.pieinthesky.app.beans.PlaceOverview;
import pk.com.pieinthesky.app.beans.Queue;
import pk.com.pieinthesky.app.connectivity.ServiceManager;
import pk.com.pieinthesky.app.helper.UIHelper;
import pk.com.pieinthesky.app.utils.BackgroundRequest;
import pk.com.pieinthesky.app.widget.NonScrollListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class QueueListFragment extends Fragment {

    private ServiceManager serviceManager = new ServiceManager();

    private List<Queue> queueList = null;
    private QueueAdapter queueAdapter = null;

    private NestedScrollView main_window;
    private ShimmerFrameLayout shimmer_Queue;
    private NonScrollListView list_Queue;

    private LinearLayout lyLoadQueues;

    int pageSize = 10;
    boolean isSearching = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_queue_list, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        main_window = getView().findViewById(R.id.main_window);
        shimmer_Queue = getView().findViewById(R.id.shimmer_Queue);
        list_Queue = getView().findViewById(R.id.list_Queue);
        lyLoadQueues = getView().findViewById(R.id.lyLoadQueues);


        queueList = new ArrayList<Queue>();
        queueAdapter = new QueueAdapter(getActivity(), queueList);
        list_Queue.setAdapter(queueAdapter);

        main_window.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int x, int y, int oldx, int oldy) {

                View view = (View) main_window.getChildAt(main_window.getChildCount() - 1);
                int diff = (view.getBottom() - (main_window.getHeight() + main_window.getScrollY()));

                if (diff == 0 && isSearching == false) {

                    QueueListTask(queueList.size(), pageSize);
                }
            }
        });

        QueueListTask(queueList.size(), pageSize);
    }


    private void QueueListTask(final int Skip, final int PageSize) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                isSearching = true;
                if (queueList.size() == 0) {
                    shimmer_Queue.startShimmer();
                    shimmer_Queue.setVisibility(View.VISIBLE);
                } else {
                    lyLoadQueues.setVisibility(View.VISIBLE);
                }

            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.QueueDetails(Configuration.getUserId(),Skip,PageSize);

                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                try {
                    Type type;
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {
                        type = new TypeToken<List<Queue>>() {
                        }.getType();

                        List<Queue> currentQueueList = gson.fromJson(jsonObject.getJSONArray("QueueDetails").toString(), type);

                        queueList.addAll(currentQueueList);

                        //list_Queue.setAdapter(queueAdapter);

                        queueAdapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                isSearching = false;
                shimmer_Queue.stopShimmer();
                shimmer_Queue.setVisibility(View.GONE);

                lyLoadQueues.setVisibility(View.GONE);

            }
        }.execute();
    }

    class QueueAdapter extends BaseAdapter {
        Context context;
        private List<Queue> queueList;

        public QueueAdapter(Context context, List<Queue> queueList) {
            this.context = context;
            this.queueList = queueList;
        }

        @Override
        public int getCount() {
            return queueList.size();
        }

        @Override
        public Object getItem(int position) {
            return queueList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            QueueAdapter.ViewHolder viewHolder;
            if (convertView == null) {
                final LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(R.layout.queue_row, null);

                TextView tvPlaceName = convertView.findViewById(R.id.tvPlaceName);
                TextView tvFullName = convertView.findViewById(R.id.tvFullName);
                TextView tvQueueDate = convertView.findViewById(R.id.tvQueueDate);
                TextView tvTokenNo = convertView.findViewById(R.id.tvTokenNo);
                TextView tvNoOfPerson = convertView.findViewById(R.id.tvNoOfPerson);

                viewHolder = new ViewHolder(tvPlaceName, tvFullName, tvQueueDate, tvTokenNo, tvNoOfPerson);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tvPlaceName.setText(queueList.get(position).getPlaceName());
            viewHolder.tvFullName.setText(queueList.get(position).getFullName());
            viewHolder.tvQueueDate.setText(queueList.get(position).getQueueDate());
            viewHolder.tvTokenNo.setText(String.valueOf(queueList.get(position).getTokenNo()));
            viewHolder.tvNoOfPerson.setText(queueList.get(position).getNoOfPerson() + " persons");

            return convertView;
        }


        private class ViewHolder {
            TextView tvPlaceName;
            TextView tvFullName;
            TextView tvQueueDate;
            TextView tvTokenNo;
            TextView tvNoOfPerson;

            public ViewHolder(TextView tvPlaceName, TextView tvFullName, TextView tvQueueDate, TextView tvTokenNo, TextView tvNoOfPerson) {
                this.tvPlaceName = tvPlaceName;
                this.tvFullName = tvFullName;
                this.tvQueueDate = tvQueueDate;
                this.tvTokenNo = tvTokenNo;
                this.tvNoOfPerson = tvNoOfPerson;
            }
        }
    }
}
