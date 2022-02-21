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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pk.com.pieinthesky.app.beans.OrderMaster;
import pk.com.pieinthesky.app.connectivity.ServiceManager;
import pk.com.pieinthesky.app.helper.CommonUtils;
import pk.com.pieinthesky.app.helper.UIHelper;
import pk.com.pieinthesky.app.utils.BackgroundRequest;
import pk.com.pieinthesky.app.widget.NonScrollListView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends AppCompatActivity {
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

    ServiceManager serviceManager = new ServiceManager();

    private List<OrderMaster> orderList = null;
    private OrderAdapter orderAdapter = null;

    private NestedScrollView main_window;
    private ShimmerFrameLayout shimmer_Order;
    private NonScrollListView list_Order;

    private LinearLayout lyLoadOrders;

    int pageSize = 10;
    boolean isSearching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        getSupportActionBar().setTitle("My Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        main_window = findViewById(R.id.main_window);
        shimmer_Order = findViewById(R.id.shimmer_Order);
        list_Order = findViewById(R.id.list_Order);
        lyLoadOrders = findViewById(R.id.lyLoadOrders);

        orderList = new ArrayList<OrderMaster>();
        orderAdapter = new OrderAdapter(this, orderList);
        list_Order.setAdapter(orderAdapter);

       /* main_window.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int x, int y, int oldx, int oldy) {

                View view = (View) main_window.getChildAt(main_window.getChildCount() - 1);
                int diff = (view.getBottom() - (main_window.getHeight() + main_window.getScrollY()));

                if (diff == 0 && isSearching == false) {

                    OrderListTask(orderList.size(), pageSize);
                }
            }
        });*/


        list_Order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(OrderListActivity.this, OrderDetailActivity.class);
                intent.putExtra("OrderId", orderList.get(position).getOrderId());
                startActivity(intent);
            }
        });

        OrderListTask(orderList.size(), pageSize);
    }


    private void OrderListTask(final int Skip, final int PageSize) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                shimmer_Order.startShimmer();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.OrderList(Configuration.getUserId(), Skip, PageSize);

                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {

                try {
                    Type type;
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {

                        type = new TypeToken<List<OrderMaster>>() {
                        }.getType();

                        List<OrderMaster> currentOrderList = gson.fromJson(jsonObject.getJSONArray("OrderList").toString(), type);

                        orderList.addAll(currentOrderList);

                        //list_Order.setAdapter(orderAdapter);

                        orderAdapter.notifyDataSetChanged();

                    } else {
                        UIHelper.showShortToast(OrderListActivity.this, jsonObject.getString("Message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                shimmer_Order.stopShimmer();
                shimmer_Order.setVisibility(View.GONE);
            }
        }.execute();
    }

    class OrderAdapter extends BaseAdapter {
        Context context;
        private List<OrderMaster> orderList;

        public OrderAdapter(Context context, List<OrderMaster> orderList) {
            this.context = context;
            this.orderList = orderList;
        }

        @Override
        public int getCount() {
            return orderList.size();
        }

        @Override
        public Object getItem(int position) {
            return orderList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            OrderAdapter.ViewHolder viewHolder;
            if (convertView == null) {
                final LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(R.layout.order_row, null);

                TextView tvPlaceName = convertView.findViewById(R.id.tvPlaceName);
                TextView tvOrderDetail = convertView.findViewById(R.id.tvOrderDetail);
                TextView tvOrderDate = convertView.findViewById(R.id.tvOrderDate);
                TextView tvAmount = convertView.findViewById(R.id.tvAmount);

                viewHolder = new ViewHolder(tvPlaceName, tvOrderDetail, tvOrderDate, tvAmount);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tvPlaceName.setText(orderList.get(position).getPlaceName());
            viewHolder.tvOrderDetail.setText(orderList.get(position).getOrderText());
            viewHolder.tvOrderDate.setText(sdf.format(orderList.get(position).getOrderDate()));
            viewHolder.tvAmount.setText("Rs." + CommonUtils.formatTwoDecimal(orderList.get(position).getTotalAmount()));

            return convertView;
        }


        private class ViewHolder {
            TextView tvPlaceName;
            TextView tvOrderDetail;
            TextView tvOrderDate;
            TextView tvAmount;

            public ViewHolder(TextView tvPlaceName, TextView tvOrderDetail, TextView tvOrderDate, TextView tvAmount) {
                this.tvPlaceName = tvPlaceName;
                this.tvOrderDetail = tvOrderDetail;
                this.tvOrderDate = tvOrderDate;
                this.tvAmount = tvAmount;
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
