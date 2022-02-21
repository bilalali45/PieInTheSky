package pk.com.Taj.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pk.com.Taj.app.adapter.CartDetailAdapter;
import pk.com.Taj.app.beans.AddressDetail;
import pk.com.Taj.app.beans.DishVariant;
import pk.com.Taj.app.beans.LocationDetail;
import pk.com.Taj.app.beans.OrderChild;
import pk.com.Taj.app.beans.OrderChildVariant;
import pk.com.Taj.app.beans.OrderMaster;
import pk.com.Taj.app.connectivity.ServiceManager;
import pk.com.Taj.app.helper.ActivityRequest;
import pk.com.Taj.app.helper.CommonUtils;
import pk.com.Taj.app.helper.UIHelper;
import pk.com.Taj.app.utils.BackgroundRequest;
import pk.com.Taj.app.widget.NonScrollListView;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    ProgressDialog dialog;

    OrderMaster orderMaster = null;

    private ServiceManager serviceManager = new ServiceManager();

    private RelativeLayout rlCartCompleted;
    private RelativeLayout rlCart;
    private NonScrollListView listCart;
    private TextView tvSubtotal;
    private LinearLayout lySaleTax;
    private TextView tvSaleTaxPercent;
    private TextView tvSaleTaxAmount;
    private TextView tvTotal;
    private TextView tvDeliveryFee;
    private TextView tvDeliveringAddress;
    private Button btnChangeAddress;
    private Button btnCheckout;
    private List<OrderChild> orderChildList;
    private CartDetailAdapter cartDetailAdapter;
    private Button btnOptions;
    private TextView tvOrderNo;
    private LinearLayout btnViewOrder;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getSupportActionBar().setTitle("Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rlCartCompleted = findViewById(R.id.rlCartCompleted);
        rlCart = findViewById(R.id.rlCart);
        listCart = findViewById(R.id.listCart);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        lySaleTax = findViewById(R.id.lySaleTax);
        tvSaleTaxPercent = findViewById(R.id.tvSaleTaxPercent);
        tvSaleTaxAmount = findViewById(R.id.tvSaleTaxAmount);
        tvTotal = findViewById(R.id.tvTotal);
        tvDeliveryFee = findViewById(R.id.tvDeliveryFee);
        tvDeliveringAddress = findViewById(R.id.tvDeliveringAddress);
        btnChangeAddress = findViewById(R.id.btnChangeAddress);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnOptions = findViewById(R.id.btnOptions);
        tvOrderNo = findViewById(R.id.tvOrderNo);
        btnViewOrder = findViewById(R.id.btnViewOrder);
        btnBack = findViewById(R.id.btnBack);

        rlCartCompleted.setVisibility(View.GONE);
        rlCart.setVisibility(View.VISIBLE);

        btnChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressSelectionDialog();
            }
        });


        orderMaster = Configuration.getOrderMaster();

        tvDeliveringAddress.setText(orderMaster.getDeliveryAddress());

        orderChildList = orderMaster.getOrderDetails();

        cartDetailAdapter = new CartDetailAdapter(this, orderChildList);
        cartDetailAdapter.setOnItemClickListener(onCartItemClickListener);
        listCart.setAdapter(cartDetailAdapter);

        listCart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<DishVariant> dishVariants = orderChildList.get(position).getDishVariants();

                Intent intent = new Intent(CartActivity.this, CartCustomizeActivity.class);
                intent.putExtra("ModeId", 2);
                intent.putExtra("Position", position);
                intent.putExtra("DishName", orderChildList.get(position).getDishName());
                intent.putExtra("DishPrice", CommonUtils.parseInt(orderChildList.get(position).getPrice()));
                intent.putExtra("Quantity", CommonUtils.parseInt(orderChildList.get(position).getQuantity()));
                intent.putExtra("SpecialInstruction", orderChildList.get(position).getSpecialInstruction());
                intent.putExtra("DishVariants", (Serializable) dishVariants);
                intent.putExtra("SelectedVariants", (Serializable) orderChildList.get(position).getVariants());

                startActivityForResult(intent, ActivityRequest.REQUEST_CUSTOMIZE_CART);
            }
        });

        calculateAmounts();


        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                String orderData = gson.toJson(orderMaster);

                CreateOrderTask(orderData);
            }
        });

        btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialogFragment();
            }
        });

        btnViewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, OrderDetailActivity.class);
                intent.putExtra("OrderId", tvOrderNo.getTag().toString());
                startActivity(intent);
                finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequest.REQUEST_CUSTOMIZE_CART && resultCode == Activity.RESULT_OK) {

            List<OrderChildVariant> variants = (List<OrderChildVariant>) data.getSerializableExtra("Variants");
            String specialInstruction = data.getStringExtra("SpecialInstruction");
            int quantity = data.getIntExtra("Quantity", 1);
            int position = data.getIntExtra("Position", 0);

            if (variants != null && variants.size() > 0) {
                orderChildList.get(position).setVariants(variants);
            }
            orderChildList.get(position).setSpecialInstruction(specialInstruction);
            orderChildList.get(position).setQuantity(CommonUtils.parseTwoDecimal(quantity));
            orderChildList.get(position).calculateValues();
            orderMaster.calculateValues();

            cartDetailAdapter.notifyDataSetChanged();

            calculateAmounts();
        }
    }

    private void CreateOrderTask(final String OrderData) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                dialog = new ProgressDialog(CartActivity.this);
                dialog.setMessage("Please wait...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.CreateOrder(Configuration.getUserId(), OrderData);
                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                dialog.dismiss();
                try {

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {

                        tvOrderNo.setText("#[" + jsonObject.getString("OrderNo") + "]");
                        tvOrderNo.setTag(jsonObject.getString("OrderId"));

                        orderMaster = null;
                        Configuration.setOrderMaster(orderMaster);
                        rlCart.setVisibility(View.GONE);
                        rlCartCompleted.setVisibility(View.VISIBLE);
                    } else {
                        UIHelper.showErrorDialog(CartActivity.this, "", jsonObject.getString("Message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }


    private void showAddressSelectionDialog() {
        AddressSelectionFragment addressSelectionFragment = new AddressSelectionFragment();
        addressSelectionFragment.setOnItemClickListener(new AddressSelectionFragment.OnOptionItemClickListener() {
            @Override
            public void onSelectLocation(LocationDetail locationDetail) {
                tvDeliveringAddress.setText(locationDetail.getAddress());
                orderMaster.setDeliveryAddress(locationDetail.getAddress());
            }

            @Override
            public void onSelectAddress(AddressDetail addressDetail) {
                tvDeliveringAddress.setText(addressDetail.getAddress());
                orderMaster.setDeliveryAddress(addressDetail.getAddress());
            }
        });
        addressSelectionFragment.show(getSupportFragmentManager(), addressSelectionFragment.getTag());
    }

    public void showBottomSheetDialogFragment() {
        CartOptionsBottomSheetFragment cartOptionsBottomSheetFragment = new CartOptionsBottomSheetFragment();
        cartOptionsBottomSheetFragment.setOnItemClickListener(new CartOptionsBottomSheetFragment.OnOptionItemClickListener() {

            @Override
            public void onRemoveOrder(View view) {
                UIHelper.showConfirmDialog(CartActivity.this, "Empty Cart", "Do you want to empty the cart?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        orderMaster = null;
                        Configuration.setOrderMaster(orderMaster);
                        setResult(RESULT_OK);
                        finish();
                    }
                });


            }
        });
        cartOptionsBottomSheetFragment.show(getSupportFragmentManager(), cartOptionsBottomSheetFragment.getTag());
    }


    private CartDetailAdapter.OnItemClickListener onCartItemClickListener = new CartDetailAdapter.OnItemClickListener() {


        @Override
        public void onPlusClick(View view, int position, long id) {

            float quantity = Float.valueOf(orderChildList.get(position).getQuantity()) + 1;

            orderChildList.get(position).setQuantity(CommonUtils.parseTwoDecimal(quantity));
            orderChildList.get(position).calculateValues();
            orderMaster.calculateValues();

            cartDetailAdapter.notifyDataSetChanged();

            calculateAmounts();
        }

        @Override
        public void onMinusClick(View view, int position, long id) {

            float quantity = Float.valueOf(orderChildList.get(position).getQuantity()) - 1;

            if (quantity > 0) {
                orderChildList.get(position).setQuantity(CommonUtils.parseTwoDecimal(quantity));
                orderChildList.get(position).calculateValues();
                orderMaster.calculateValues();

                cartDetailAdapter.notifyDataSetChanged();

                calculateAmounts();
            } else {
                orderChildList.remove(position);
                orderMaster.calculateValues();

                cartDetailAdapter.notifyDataSetChanged();

                calculateAmounts();
                if (orderChildList.size() == 0) {
                    orderMaster = null;
                    Configuration.setOrderMaster(orderMaster);
                    setResult(RESULT_OK);
                    finish();
                }
            }
        }
    };


    private void calculateAmounts() {
        float saleTaxAmount = Float.valueOf(orderMaster.getSalesTaxAmount());
        float deliveryFeeAmount = Float.valueOf(orderMaster.getDeliveryFee());

        tvSubtotal.setText("Subtotal: Rs. " + CommonUtils.formatTwoDecimal(orderMaster.getSubTotalAmount()));
        if (saleTaxAmount > 0) {
            lySaleTax.setVisibility(View.VISIBLE);
            tvSaleTaxPercent.setText("Sale tax " + CommonUtils.formatTwoDecimal(orderMaster.getSalesTaxPercent()) + "%: ");
            tvSaleTaxAmount.setText("Rs. " + CommonUtils.formatTwoDecimal(saleTaxAmount));
        } else {
            lySaleTax.setVisibility(View.GONE);
            tvSaleTaxPercent.setText("");
            tvSaleTaxAmount.setText("");
        }

        if (deliveryFeeAmount > 0) {
            tvDeliveryFee.setText("Delivery Fee: Rs. " + CommonUtils.formatTwoDecimal(deliveryFeeAmount));
            tvDeliveryFee.setVisibility(View.VISIBLE);
        } else {
            tvDeliveryFee.setText("");
            tvDeliveryFee.setVisibility(View.GONE);
        }

        tvTotal.setText("Total: Rs. " + CommonUtils.formatTwoDecimal(orderMaster.getTotalAmount()));

        //btnCheckout.setText(CommonUtils.formatTwoDecimal(orderMaster.getNumberOfItems()) + " items = Rs. " + CommonUtils.formatTwoDecimal(orderMaster.getTotalAmount()));
    }

    @Override
    public void onBackPressed() {
        Configuration.setOrderMaster(orderMaster);
        setResult(RESULT_OK);
        finish();
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
