package pk.com.Taj.app;


import static pk.com.Taj.app.Constant.jsonObject_Tajhotel;
import static pk.com.Taj.app.Constant.jsonObject_mart;
import static pk.com.Taj.app.Constant.jsonObject_paittoohotel;
import static pk.com.Taj.app.Constant.jsonObject_yelohotel;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dk.animation.circle.CircleAnimationUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pk.com.Taj.app.adapter.DishListAdapter;
import pk.com.Taj.app.beans.AddressDetail;
import pk.com.Taj.app.beans.DishDetail;
import pk.com.Taj.app.beans.DishVariant;
import pk.com.Taj.app.beans.LocationDetail;
import pk.com.Taj.app.beans.MenuDetail;
import pk.com.Taj.app.beans.OrderChild;
import pk.com.Taj.app.beans.OrderChildVariant;
import pk.com.Taj.app.beans.OrderMaster;
import pk.com.Taj.app.beans.User;
import pk.com.Taj.app.connectivity.ServiceManager;
import pk.com.Taj.app.helper.ActivityRequest;
import pk.com.Taj.app.helper.CommonUtils;
import pk.com.Taj.app.helper.UIHelper;
import pk.com.Taj.app.widget.RoundCornerImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class OrderOnlineFragment extends Fragment {

    private ServiceManager serviceManager = new ServiceManager();

    private String placeId = null;
    private String GSTPercentage = "0";
    private String DeliveryCharges = "0";

    private LinearLayout container_back_button;
    private ProgressBar loading_progress;
    private RoundCornerImageView ivPlaceImage;
    private TextView tvPlaceName;
    private TextView tvTags;
    private TextView tvAvgRating;
    private TextView tvTotalReview;
    private TextView tvDeliveringAddress;
    private Button btnChangeAddress;

    private SearchView svDishes;
    private TabLayout tabCategoryList;
    private ListView listDish;
    private Button btnProceed;
    private Button btnOptions;
    private TextView tvDropDish;
    private LinearLayout lyFooter;
    private DishListAdapter dishListAdapter;

    private int selectedCategoryIndex = 0;
    private String selectedDeliveryAddress = "";

    private List<MenuDetail> menuDetails;
    private List<DishDetail> dishDetailList = null;

    OrderMaster orderMaster = null;

    public OrderOnlineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_online, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        container_back_button = getView().findViewById(R.id.container_back_button);
        loading_progress = getView().findViewById(R.id.loading_progress);
        ivPlaceImage = getView().findViewById(R.id.ivPlaceImage);
        tvPlaceName = getView().findViewById(R.id.tvPlaceName);
        tvTags = getView().findViewById(R.id.tvTags);
        tvAvgRating = getView().findViewById(R.id.tvAvgRating);
        tvTotalReview = getView().findViewById(R.id.tvTotalReview);
        tvDeliveringAddress = getView().findViewById(R.id.tvDeliveringAddress);
        btnChangeAddress = getView().findViewById(R.id.btnChangeAddress);
        svDishes = getView().findViewById(R.id.svDishes);
        tabCategoryList = getView().findViewById(R.id.tabCategoryList);
        listDish = getView().findViewById(R.id.listDish);
        btnProceed = getView().findViewById(R.id.btnProceed);
        btnOptions = getView().findViewById(R.id.btnOptions);
        tvDropDish = getView().findViewById(R.id.tvDropDish);
        lyFooter = getView().findViewById(R.id.lyFooter);

        loading_progress.setVisibility(View.GONE);
        placeId = getArguments().getString("PlaceId");
        GSTPercentage = getArguments().getString("GSTPercentage");
        DeliveryCharges = getArguments().getString("DeliveryCharges");



        tvPlaceName.setText(getArguments().getString("PlaceName"));
        tvTags.setText(getArguments().getString("Tags"));
      //  tvAvgRating.setText(getArguments().getString("AvgRating"));
       // tvTotalReview.setText(getArguments().getString("TotalReview"));


        orderMaster = Configuration.getOrderMaster();

//        dishDetailList = new ArrayList<>();
//        DishDetail p1 = new DishDetail();
//        p1.setDishName("Buttery Garlic Bread");
//        p1.setImageURL("Royal Taj Restaurant");
//        p1.setTotalPrice(135);
//        p1.setDishId("57970f6c-e17e-4b8f-ae11-1ed21d970a6a");
//        dishDetailList.add(p1);
//
//
//        DishDetail p3 = new DishDetail();
//        p1.setDishName("Mexican Fries");
//        p1.setImageURL("Royal Taj Restaurant");
//        p1.setTotalPrice(335);
//        p1.setDishId("2f570d26-0adc-4f20-bcdb-9320a7bdf55e");
//        dishDetailList.add(p1);
       // DishVariant v1 = new DishVariant();
       // v1.set();





        if (orderMaster != null) {
            if (orderMaster.getPlaceId().equals(placeId) == false) {
                UIHelper.showConfirmDialog(getActivity(), "", "Your previous cart will be cleared if you proceed with this restaurant.", "Ok", "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clearCart();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().getSupportFragmentManager().beginTransaction().remove(OrderOnlineFragment.this).commit();
                    }
                });
            } else {
                loadOrder();
            }
        }


        if (Configuration.getCurrentLocation() != null) {
            selectedDeliveryAddress = Configuration.getCurrentLocation().getAddress();
            tvDeliveringAddress.setText("Delivering to " + selectedDeliveryAddress);
        } else {
            tvDeliveringAddress.setText("Delivering address not select");
        }

        container_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack("Main", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        btnChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressSelectionDialog();
            }
        });

        tabCategoryList.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedCategoryIndex = tab.getPosition();
                loadDishes(selectedCategoryIndex, "");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        svDishes.setActivated(true);
        svDishes.setQueryHint("Search here");
        svDishes.onActionViewExpanded();
        svDishes.setIconified(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                svDishes.clearFocus();
            }
        }, 200);


        View closeButton = svDishes.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                svDishes.setQuery("", false);
                svDishes.clearFocus();

                loadDishes(selectedCategoryIndex, "");
            }
        });


        svDishes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                loadDishes(selectedCategoryIndex, newText.trim().toLowerCase());

                return false;
            }
        });


        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderMaster != null) {
                    if (Configuration.isLogin()) {
                        User user = Configuration.getUser();
                        orderMaster.setFullName(user.getFirstName() + " " + user.getLastName());
                        orderMaster.setContactNo(user.getMobileNo());

                        orderMaster.setDeliveryAddress(selectedDeliveryAddress);

                        Intent intent = new Intent(getActivity(), CartActivity.class);
                        startActivityForResult(intent, ActivityRequest.REQUEST_CART);
                    } else {
                        Intent intent = new Intent(getActivity(), LoginSelectionActivity.class);
                        intent.putExtra("IsUserLoginRequest", true);
                        startActivity(intent);
                    }
                } else {
                    UIHelper.showErrorDialog(getActivity(), "", "Cart is empty");
                }
            }
        });


        btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderMaster != null) {
                    showBottomSheetDialogFragment();
                } else {
                    UIHelper.showErrorDialog(getActivity(), "", "Cart is empty");
                }
            }
        });


        listDish.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView ivDishImage = view.findViewById(R.id.ivDishImage);
                selectItem(ivDishImage, position);
            }
        });

        if(tvPlaceName.getText().toString().equals("Royal Taj Restaurant")){
            GetMenuList(jsonObject_Tajhotel);
            Glide.with(this)
                    .load(R.drawable.one)
                    .centerCrop()
                    .placeholder(R.drawable.image_place_loading)
                    .into(ivPlaceImage);
        }else if (tvPlaceName.getText().toString().equals("Piatto")){
            GetMenuList(jsonObject_paittoohotel);
            Glide.with(this)
                    .load(R.drawable.two)
                    .centerCrop()
                    .placeholder(R.drawable.image_place_loading)
                    .into(ivPlaceImage);
        }else if (tvPlaceName.getText().toString().equals("YELO")){
            GetMenuList(jsonObject_yelohotel);
            Glide.with(this)
                    .load(R.drawable.three)
                    .centerCrop()
                    .placeholder(R.drawable.image_place_loading)
                    .into(ivPlaceImage);
          //  GetMenuList(placeId);
        }else {
            GetMenuList(jsonObject_Tajhotel);
            Glide.with(this)
                    .load(R.drawable.four)
                    .centerCrop()
                    .placeholder(R.drawable.image_place_loading)
                    .into(ivPlaceImage);
        }



    }



    private void selectItem(ImageView ivDishImage, int position) {
        List<DishVariant> dishVariants = dishDetailList.get(position).getVariants();
        if (dishVariants.size() > 0) {

            Intent intent = new Intent(getActivity(), CartCustomizeActivity.class);
            intent.putExtra("ModeId", 1);
            intent.putExtra("Position", position);
            intent.putExtra("DishName", dishDetailList.get(position).getDishName());
            intent.putExtra("DishPrice", dishDetailList.get(position).getTotalPrice());
            intent.putExtra("Quantity", 1);
            intent.putExtra("SpecialInstruction", "");
            intent.putExtra("DishVariants", (Serializable) dishVariants);
            intent.putExtra("SelectedVariants", (Serializable) new ArrayList<OrderChildVariant>());

            startActivityForResult(intent, ActivityRequest.REQUEST_CUSTOMIZE_CART);

        } else {
            addToCart(ivDishImage, null, "", 1, position);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequest.REQUEST_CUSTOMIZE_CART && resultCode == Activity.RESULT_OK) {

            List<OrderChildVariant> variants = (List<OrderChildVariant>) data.getSerializableExtra("Variants");
            String specialInstruction = data.getStringExtra("SpecialInstruction");
            int quantity = data.getIntExtra("Quantity", 1);
            int position = data.getIntExtra("Position", 0);

            addToCart(null, variants, specialInstruction, quantity, position);
        } else if (requestCode == ActivityRequest.REQUEST_CART) {
            if (resultCode == Activity.RESULT_OK) {
                orderMaster = Configuration.getOrderMaster();
                if (orderMaster != null) {
                    loadOrder();
                } else {
                    clearCart();
                }
            } else {
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
        }

    }


    private void showAddressSelectionDialog() {
        AddressSelectionFragment addressSelectionFragment = new AddressSelectionFragment();
        addressSelectionFragment.setOnItemClickListener(new AddressSelectionFragment.OnOptionItemClickListener() {
            @Override
            public void onSelectLocation(LocationDetail locationDetail) {
                selectedDeliveryAddress = locationDetail.getAddress();
                tvDeliveringAddress.setText("Delivering to " + selectedDeliveryAddress);
            }

            @Override
            public void onSelectAddress(AddressDetail addressDetail) {
                selectedDeliveryAddress = addressDetail.getAddress();
                tvDeliveringAddress.setText("Delivering to " + selectedDeliveryAddress);
            }
        });
        addressSelectionFragment.show(getActivity().getSupportFragmentManager(), addressSelectionFragment.getTag());
    }

    public void showBottomSheetDialogFragment() {
        CartOptionsBottomSheetFragment cartOptionsBottomSheetFragment = new CartOptionsBottomSheetFragment();
        cartOptionsBottomSheetFragment.setOnItemClickListener(new CartOptionsBottomSheetFragment.OnOptionItemClickListener() {

            @Override
            public void onRemoveOrder(View view) {
                UIHelper.showConfirmDialog(getActivity(), "Empty Cart", "Do you want to empty the cart?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clearCart();
                    }
                });
            }
        });
        cartOptionsBottomSheetFragment.show(getActivity().getSupportFragmentManager(), cartOptionsBottomSheetFragment.getTag());
    }

    HashMap<String, Integer> hashMapOrderItem = new HashMap<String, Integer>();

    private void loadOrder() {

        hashMapOrderItem.clear();
        float numOfItems = 0;
        float totalAmount = 0;

        if (orderMaster != null) {
            if (orderMaster.getDeliveryAddress() != null && orderMaster.getDeliveryAddress().length() > 0) {
                selectedDeliveryAddress = orderMaster.getDeliveryAddress();
                tvDeliveringAddress.setText("Delivering to " + selectedDeliveryAddress);
            }
            int index = 0;
            for (OrderChild orderChild : orderMaster.getOrderDetails()) {
                hashMapOrderItem.put(orderChild.getDishId(), index);

                numOfItems += Float.valueOf(orderChild.getQuantity());
                index++;
            }
            totalAmount = Float.valueOf(orderMaster.getSubTotalAmount());

            lyFooter.setVisibility(View.VISIBLE);
        }

        btnProceed.setText(CommonUtils.formatTwoDecimal(numOfItems) + " items = Rs. " + CommonUtils.formatTwoDecimal(totalAmount));
    }

    private void clearCart() {
        hashMapOrderItem.clear();
        orderMaster = null;
        Configuration.setOrderMaster(orderMaster);
        btnProceed.setText("0 items = Rs. 0");
        lyFooter.setVisibility(View.GONE);
    }

    private void addToCart(View view, List<OrderChildVariant> variants, String specialInstruction, int cartQuantity, int position) {

        if (view != null) {
            new CircleAnimationUtil().attachActivity(getActivity()).
                    setTargetView(view).
                    setDestView(tvDropDish).
                    setCircleDuration(1).
                    setMoveDuration(280).
                    startAnimation();

            view.setVisibility(View.VISIBLE);
        }
        if (orderMaster == null) {

            orderMaster = new OrderMaster();
            orderMaster.setPlaceId(placeId);
            orderMaster.setPlaceName(tvPlaceName.getText().toString());
            orderMaster.setDiscountTypeId(1);
            orderMaster.setOrderDeviceDate(CommonUtils.getDate());
            orderMaster.setSalesTaxPercent(GSTPercentage);
            orderMaster.setCheckoutDeviceDatetime(new Date());
            orderMaster.setDeliveryFee(DeliveryCharges);

            Configuration.setOrderMaster(orderMaster);

            lyFooter.setVisibility(View.VISIBLE);
        }

        String dishId = dishDetailList.get(position).getDishId();
        String price = CommonUtils.parseTwoDecimal(dishDetailList.get(position).getTotalPrice());
        String dishKey = dishId;
        if (variants != null && variants.size() > 0) {
            dishKey += UUID.randomUUID().toString();
        }

        OrderChild orderChild = new OrderChild();

        if (hashMapOrderItem.containsKey(dishKey)) {
            int orderChildIndex = hashMapOrderItem.get(dishKey);
            orderChild = orderMaster.getOrderDetails().get(orderChildIndex);
        }

        String quantity = CommonUtils.parseTwoDecimal(Float.valueOf(orderChild.getQuantity()) + cartQuantity);

        orderChild.setDishId(dishId);
        orderChild.setDishVariants(dishDetailList.get(position).getVariants());
        orderChild.setDishName(dishDetailList.get(position).getDishName());
        orderChild.setQuantity(quantity);
        orderChild.setPrice(price);
        if (specialInstruction.equals("") == false) {
            orderChild.setSpecialInstruction(specialInstruction);
        }
        if (variants != null && variants.size() > 0) {
            orderChild.setVariants(variants);
        }

        if (hashMapOrderItem.containsKey(dishKey) == false) {
            orderMaster.getOrderDetails().add(orderChild);
            hashMapOrderItem.put(dishKey, orderMaster.getOrderDetails().size() - 1);
        }

        orderChild.calculateValues();
        orderMaster.calculateValues();


        btnProceed.setText(CommonUtils.formatTwoDecimal(orderMaster.getNumberOfItems()) + " items = Rs. " + CommonUtils.formatTwoDecimal(orderMaster.getSubTotalAmount()));
    }


    private void loadDishes(int categoryIndex, String dishName) {

        if (dishName.length() == 0) {
            dishDetailList = new ArrayList<DishDetail>(menuDetails.get(categoryIndex).getDishList());
        } else {
            dishDetailList.clear();
            for (DishDetail dishDetail : menuDetails.get(categoryIndex).getDishList()) {
                try{
                    if (dishDetail.getDishName().toLowerCase().contains(dishName)) {
                        dishDetailList.add(dishDetail);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }

        dishListAdapter = new DishListAdapter(getActivity(), dishDetailList);
        listDish.setAdapter(dishListAdapter);

    }

    private void GetMenuList(final JSONObject jsonObject) {

        Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Type type;
        try {
            type = new TypeToken<List<MenuDetail>>() {
            }.getType();
            menuDetails = gson.fromJson(jsonObject.getJSONArray("MenuList").toString(),type);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        MenuDetail allMenu = new MenuDetail();
        allMenu.setCategoryId("0");
        allMenu.setCategoryName("All");

        List<DishDetail> allDishDetail = new ArrayList<DishDetail>();

        for (MenuDetail menuDetail : menuDetails) {
            allDishDetail.addAll(menuDetail.getDishList());
        }
        allMenu.setDishList(allDishDetail);
        menuDetails.add(0, allMenu);

        for (MenuDetail menuDetail : menuDetails) {
            TabLayout.Tab tab = tabCategoryList.newTab();
            tab.setText(menuDetail.getCategoryName());
            tab.setTag(menuDetail.getCategoryId());
            tabCategoryList.addTab(tab);
        }




//        new BackgroundRequest<String, Void, JSONObject>() {
//
//            protected void onPreExecute() {
//                loading_progress.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            protected JSONObject doInBackground(String... params) {
//                JSONObject jobject;
//                jobject = serviceManager.MenuList(PlaceId);
//
//                return jobject;
//            }
//
//            protected void onPostExecute(JSONObject jsonObject) {
//                loading_progress.setVisibility(View.GONE);
//                try {
//                    Type type;
//                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//
//                    final boolean status = jsonObject.getBoolean("Status");
//                    if (status) {
//                        type = new TypeToken<List<MenuDetail>>() {
//                        }.getType();
//
//                        menuDetails = gson.fromJson(jsonObject.getJSONArray("MenuList").toString(), type);
//
//                        MenuDetail allMenu = new MenuDetail();
//                        allMenu.setCategoryId("0");
//                        allMenu.setCategoryName("All");
//
//                        List<DishDetail> allDishDetail = new ArrayList<DishDetail>();
//
//                        for (MenuDetail menuDetail : menuDetails) {
//                            allDishDetail.addAll(menuDetail.getDishList());
//                        }
//                        allMenu.setDishList(allDishDetail);
//                        menuDetails.add(0, allMenu);
//
//                        for (MenuDetail menuDetail : menuDetails) {
//                            TabLayout.Tab tab = tabCategoryList.newTab();
//                            tab.setText(menuDetail.getCategoryName());
//                            tab.setTag(menuDetail.getCategoryId());
//                            tabCategoryList.addTab(tab);
//                        }
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.execute();
    }



}
