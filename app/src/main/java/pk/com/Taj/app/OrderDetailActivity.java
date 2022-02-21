package pk.com.Taj.app;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pk.com.Taj.app.beans.OrderChild;
import pk.com.Taj.app.beans.OrderMaster;
import pk.com.Taj.app.connectivity.ServiceManager;
import pk.com.Taj.app.helper.CommonUtils;
import pk.com.Taj.app.helper.UIHelper;
import pk.com.Taj.app.utils.BackgroundRequest;
import pk.com.Taj.app.widget.NonScrollListView;
import pk.com.Taj.app.widget.RoundCornerImageView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

    ServiceManager serviceManager = new ServiceManager();

    private String orderId = null;

    private LinearLayout lyLoading;
    private RelativeLayout containerOrderDetail;
    private RoundCornerImageView ivPlaceImage;
    private TextView tvPlaceName;
    private TextView tvTags;
    private TextView tvAvgRating;
    private TextView tvTotalReview;
    private TextView tvStatus;
    private TextView tvOrderNo;
    private TextView tvDeliveryAddress;
    private NonScrollListView list_OrderDetail;
    private TextView tvSubTotalAmount;
    private  LinearLayout lySalesTaxRow;
    private LinearLayout lyDeliveryRow;
    private TextView tvSalesTax;
    private TextView tvDeliveryFee;
    private TextView tvTotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        getSupportActionBar().setTitle("Order Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        orderId = getIntent().getStringExtra("OrderId");


        lyLoading = findViewById(R.id.lyLoading);
        containerOrderDetail = findViewById(R.id.containerOrderDetail);
        ivPlaceImage = findViewById(R.id.ivPlaceImage);
        tvPlaceName = findViewById(R.id.tvPlaceName);
        tvTags = findViewById(R.id.tvTags);
        tvAvgRating = findViewById(R.id.tvAvgRating);
        tvTotalReview = findViewById(R.id.tvTotalReview);
        tvStatus = findViewById(R.id.tvStatus);
        tvOrderNo = findViewById(R.id.tvOrderNo);
        tvDeliveryAddress = findViewById(R.id.tvDeliveryAddress);
        list_OrderDetail = findViewById(R.id.list_OrderDetail);
        tvSubTotalAmount = findViewById(R.id.tvSubTotalAmount);
        lySalesTaxRow = findViewById(R.id.lySalesTaxRow);
        lyDeliveryRow = findViewById(R.id.lyDeliveryRow);
        tvSalesTax = findViewById(R.id.tvSalesTax);
        tvDeliveryFee = findViewById(R.id.tvDeliveryFee);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);


        lyLoading.setVisibility(View.VISIBLE);
        containerOrderDetail.setVisibility(View.GONE);

        OrderDetailTask(orderId);
    }

    public void btnReorder_onClick(View view) {

    }

    private void OrderDetailTask(final String OrderId) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                lyLoading.setVisibility(View.VISIBLE);
                containerOrderDetail.setVisibility(View.GONE);
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.OrderDetail(Configuration.getUserId(), OrderId);

                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {

                lyLoading.setVisibility(View.GONE);
                containerOrderDetail.setVisibility(View.VISIBLE);
                try {
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {

                        OrderMaster orderMaster = gson.fromJson(jsonObject.getJSONObject("OrderDetail").toString(), OrderMaster.class);

                        tvPlaceName.setText(orderMaster.getPlaceName());
                        tvTags.setText(orderMaster.getCuisines());
                        Glide.with(OrderDetailActivity.this)
                                .load(orderMaster.getPlaceImageURL())
                                .centerCrop()
                                .placeholder(R.drawable.image_place_loading)
                                .into(ivPlaceImage);

                        //tvAvgRating.setText(orderMaster.getBusinessAvgRating());
                        //tvTotalReview.setText(orderMaster.getTotalReview() + "+ Reviews");
                        tvStatus.setText(orderMaster.getStatus());
                        tvOrderNo.setText("Order no. : " + orderMaster.getOrderNo());
                        tvDeliveryAddress.setText("Delivery Address : " + orderMaster.getDeliveryAddress());
                        tvSubTotalAmount.setText("Rs." + CommonUtils.formatTwoDecimal(orderMaster.getSubTotalAmount()));
                        if (orderMaster.getSalesTaxAmount() != null && orderMaster.getSalesTaxAmount().length() > 0 && orderMaster.getSalesTaxAmount().equals("0") == false) {
                            tvSalesTax.setText("Rs." + CommonUtils.formatTwoDecimal(orderMaster.getSalesTaxAmount()));
                        } else {
                            lySalesTaxRow.setVisibility(View.GONE);
                        }
                        if (orderMaster.getDeliveryFee() != null && orderMaster.getDeliveryFee().length() > 0 && orderMaster.getDeliveryFee().equals("0") == false) {
                            tvDeliveryFee.setText("Rs." + CommonUtils.formatTwoDecimal(orderMaster.getDeliveryFee()));
                        } else {
                            lyDeliveryRow.setVisibility(View.GONE);
                        }
                        tvTotalAmount.setText("Rs." + CommonUtils.formatTwoDecimal(orderMaster.getTotalAmount()));

                        OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(OrderDetailActivity.this, orderMaster.getOrderDetails());

                        list_OrderDetail.setAdapter(orderDetailAdapter);

                    } else {
                        UIHelper.showShortToast(OrderDetailActivity.this, jsonObject.getString("Message"));
                    }
                } catch (Exception e) {
                    finish();
                    e.printStackTrace();
                }


            }
        }.execute();
    }


    class OrderDetailAdapter extends BaseAdapter {
        Context context;
        private List<OrderChild> orderDetailList;

        public OrderDetailAdapter(Context context, List<OrderChild> orderDetailList) {
            this.context = context;
            this.orderDetailList = orderDetailList;
        }

        @Override
        public int getCount() {
            return orderDetailList.size();
        }

        @Override
        public Object getItem(int position) {
            return orderDetailList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            OrderDetailAdapter.ViewHolder viewHolder;
            if (convertView == null) {
                final LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(R.layout.order_detail_row, null);

                TextView tvQuantity = convertView.findViewById(R.id.tvQuantity);
                TextView tvDishName = convertView.findViewById(R.id.tvDishName);
                TextView tvAmount = convertView.findViewById(R.id.tvAmount);

                viewHolder = new ViewHolder(tvQuantity, tvDishName, tvAmount);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tvQuantity.setText(orderDetailList.get(position).getQuantity() + "x");
            viewHolder.tvDishName.setText(orderDetailList.get(position).getDishName());
            viewHolder.tvAmount.setText("Rs." + CommonUtils.formatTwoDecimal(orderDetailList.get(position).getAmount()));

            return convertView;
        }


        private class ViewHolder {
            TextView tvQuantity;
            TextView tvDishName;
            TextView tvAmount;

            public ViewHolder(TextView tvQuantity, TextView tvDishName, TextView tvAmount) {
                this.tvQuantity = tvQuantity;
                this.tvDishName = tvDishName;
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
