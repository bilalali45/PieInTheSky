package pk.com.Taj.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import pk.com.Taj.app.beans.DishVariant;
import pk.com.Taj.app.beans.OrderChildVariant;
import pk.com.Taj.app.beans.VariantDetail;
import pk.com.Taj.app.helper.CommonUtils;
import pk.com.Taj.app.widget.IconTextView;
import pk.com.Taj.app.widget.NonScrollListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartCustomizeActivity extends AppCompatActivity {

    private LinearLayout container_back_button;
    private TextView tvDishName;
    private TextView tvDishPrice;
    private LinearLayout lyVariantContainer;
    private LinearLayout lyVariants;
    private EditText etSpecialInstruction;
    private IconTextView btnPlus;
    private IconTextView btnMinus;
    private TextView tvQuantity;
    private Button btnAddToCart;

    private int modeId;
    private int position;
    private String dishName;
    private int dishPrice;
    private int quantity = 1;
    private String specialInstruction;
    private List<DishVariant> dishVariants;
    private List<OrderChildVariant> SelectedVariants = new ArrayList<OrderChildVariant>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_customize);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();

            window.setStatusBarColor(Color.parseColor("#333333"));
        }

        container_back_button = findViewById(R.id.container_back_button);
        tvDishName = findViewById(R.id.tvDishName);
        tvDishPrice = findViewById(R.id.tvDishPrice);
        lyVariantContainer = findViewById(R.id.lyVariantContainer);
        lyVariants = findViewById(R.id.lyVariants);
        etSpecialInstruction = findViewById(R.id.etSpecialInstruction);
        btnPlus = findViewById(R.id.btnPlus);
        btnMinus = findViewById(R.id.btnMinus);
        tvQuantity = findViewById(R.id.tvQuantity);
        btnAddToCart = findViewById(R.id.btnAddToCart);


        modeId = getIntent().getIntExtra("ModeId", 0);
        position = getIntent().getIntExtra("Position", 0);
        dishName = getIntent().getStringExtra("DishName");
        dishPrice = getIntent().getIntExtra("DishPrice", 0);
        quantity = getIntent().getIntExtra("Quantity", 1);
        specialInstruction = getIntent().getStringExtra("SpecialInstruction");
        List<DishVariant> originalDishVariants = (List<DishVariant>) getIntent().getSerializableExtra("DishVariants");
        List<OrderChildVariant> originalSelectedVariants = (List<OrderChildVariant>) getIntent().getSerializableExtra("SelectedVariants");

        dishVariants = new ArrayList<DishVariant>();

        for (DishVariant dv : originalDishVariants) {
            DishVariant dishVariant = new DishVariant();

            dishVariant.setId(dv.getId());
            dishVariant.setKey(dv.getKey());
            dishVariant.setTitle(dv.getTitle());
            dishVariant.setDescription(dv.getDescription());
            dishVariant.setType(dv.getType());
            dishVariant.setRequired(dv.isRequired());

            List<VariantDetail> variantDetails = new ArrayList<VariantDetail>();

            for (VariantDetail vd : dv.getData()) {
                VariantDetail variantDetail = new VariantDetail();

                variantDetail.setId(vd.getId());
                variantDetail.setText(vd.getText());
                variantDetail.setPrice(vd.getPrice());

                for (OrderChildVariant orderChildVariant : originalSelectedVariants) {
                    if (orderChildVariant.getId().equals(dv.getId())) {
                        for (VariantDetail ocvd : orderChildVariant.getData()) {
                            if (ocvd.getId().equals(vd.getId())) {
                                variantDetail.setSelected(true);
                                break;
                            }
                        }
                        break;
                    }
                }


                variantDetails.add(variantDetail);
            }
            dishVariant.setData(variantDetails);
            dishVariants.add(dishVariant);
        }

        if (modeId == 2) {
            btnAddToCart.setText("Update Cart");
        }

        tvDishName.setText(dishName);
        tvDishPrice.setText("Rs. " + CommonUtils.formatTwoDecimal(dishPrice));
        tvQuantity.setText(String.valueOf(quantity));
        etSpecialInstruction.setText(specialInstruction);

        if (dishVariants.size() > 0) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);

            for (DishVariant dishVariant : dishVariants) {
                LinearLayout cartList = (LinearLayout) layoutInflater.inflate(R.layout.cart_customize_list_layout, null);
                TextView tvTitle = cartList.findViewById(R.id.tvTitle);
                TextView tvDescription = cartList.findViewById(R.id.tvDescription);
                TextView tvMessage = cartList.findViewById(R.id.tvMessage);
                NonScrollListView listVariant = cartList.findViewById(R.id.listVariant);

                tvTitle.setText(dishVariant.getTitle());
                tvDescription.setText(dishVariant.getDescription());
                tvMessage.setText(dishVariant.getTitle() + " is required.");

                DishVariantDetailAdapter dishVariantDetailAdapter = new DishVariantDetailAdapter(this, dishVariant.getData(), dishVariant.getType());
                listVariant.setAdapter(dishVariantDetailAdapter);

                lyVariants.addView(cartList);
            }
        } else {
            lyVariantContainer.setVisibility(View.GONE);
        }

        container_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity++;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });


        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity > 1) {
                    quantity--;
                    tvQuantity.setText(String.valueOf(quantity));
                }
            }
        });


        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectedVariants.clear();
                int dishVariantIndex = 0;
                int dishRequiredCount = 0;
                for (DishVariant dishVariant : dishVariants) {

                    List<VariantDetail> variantDetails = new ArrayList<VariantDetail>();

                    for (VariantDetail variantDetail : dishVariant.getData()) {
                        if (variantDetail.isSelected()) {
                            variantDetails.add(variantDetail);
                        }
                    }
                    TextView tvMessage = lyVariants.getChildAt(dishVariantIndex).findViewById(R.id.tvMessage);
                    if (dishVariant.isRequired() && variantDetails.size() == 0) {
                        tvMessage.setVisibility(View.VISIBLE);
                        dishRequiredCount++;
                    } else {
                        tvMessage.setVisibility(View.GONE);
                        if (variantDetails.size() > 0) {
                            OrderChildVariant orderChildVariant = new OrderChildVariant();
                            orderChildVariant.setId(dishVariant.getId());
                            orderChildVariant.setKey(dishVariant.getKey());
                            orderChildVariant.setData(variantDetails);
                            SelectedVariants.add(orderChildVariant);
                        }
                    }

                    dishVariantIndex++;
                }
                if (dishRequiredCount == 0) {
                    Intent intent = new Intent();
                    intent.putExtra("Variants", (Serializable) SelectedVariants);
                    intent.putExtra("SpecialInstruction", etSpecialInstruction.getText().toString());
                    intent.putExtra("Quantity", quantity);
                    intent.putExtra("Position", position);

                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    class DishVariantDetailAdapter extends BaseAdapter {
        Context context;
        private List<VariantDetail> variantDetails;
        private String listType;
        private int selectedRadioItemPosition = -1;
        private RadioButton selectedRadioButton;

        public DishVariantDetailAdapter(Context context, List<VariantDetail> variantDetails, String listType) {
            this.context = context;
            this.variantDetails = variantDetails;
            this.listType = listType;
        }

        @Override
        public int getCount() {
            return variantDetails.size();
        }

        @Override
        public Object getItem(int position) {
            return variantDetails.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if (convertView == null) {
                final LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(R.layout.cart_customize_list_row, null);

                CheckBox cbSelect = convertView.findViewById(R.id.cbSelect);
                RadioButton rbSelect = convertView.findViewById(R.id.rbSelect);
                TextView tvAmount = convertView.findViewById(R.id.tvAmount);

                viewHolder = new ViewHolder(cbSelect, rbSelect, tvAmount);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    variantDetails.get(position).setSelected(isChecked);
                }
            });

            viewHolder.rbSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedRadioButton != null) {
                        variantDetails.get(selectedRadioItemPosition).setSelected(false);
                        selectedRadioButton.setChecked(false);
                    }
                    selectedRadioItemPosition = position;
                    selectedRadioButton = (RadioButton) view;
                    variantDetails.get(selectedRadioItemPosition).setSelected(true);
                    selectedRadioButton.setChecked(true);
                }
            });

            viewHolder.cbSelect.setText(variantDetails.get(position).getText());
            viewHolder.rbSelect.setText(variantDetails.get(position).getText());
            viewHolder.tvAmount.setText("Rs. " + CommonUtils.formatTwoDecimal(variantDetails.get(position).getPrice()));

            if (listType.equals("SingleChoice")) {
                viewHolder.rbSelect.setVisibility(View.VISIBLE);
                viewHolder.cbSelect.setVisibility(View.GONE);
                viewHolder.rbSelect.setChecked(variantDetails.get(position).isSelected());
            } else if (listType.equals("MultipleChoice")) {
                viewHolder.cbSelect.setVisibility(View.VISIBLE);
                viewHolder.rbSelect.setVisibility(View.GONE);
                viewHolder.cbSelect.setChecked(variantDetails.get(position).isSelected());
            }
            return convertView;
        }


        private class ViewHolder {
            CheckBox cbSelect;
            RadioButton rbSelect;
            TextView tvAmount;

            public ViewHolder(CheckBox cbSelect, RadioButton rbSelect, TextView tvAmount) {
                this.cbSelect = cbSelect;
                this.rbSelect = rbSelect;
                this.tvAmount = tvAmount;
            }
        }
    }
}
