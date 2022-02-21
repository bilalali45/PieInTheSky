package pk.com.pieinthesky.app.beans;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OrderMaster {
    private String OrderId;
    private Date OrderDeviceDate;
    private String PlaceId;
    private String PlaceName;
    private String PlaceLocation;
    private String PlaceImageURL;
    private String BusinessAvgRating;
    private String TotalReview;
    private List<Cuisine> Cuisine;
    private String OrderNo;
    private Date OrderDate;
    private String FullName;
    private String ContactNo;
    private int DiscountTypeId = 1;
    private String DiscountPercentage = "0";
    private String DiscountAmount = "0";
    private String SalesTaxPercent = "0";
    private String SalesTaxAmount = "0";
    private String DeliveryFee = "0";
    private String SubTotalAmount = "0";
    private String TotalAmount = "0";
    private String DeliveryAddress;
    private Date CheckoutDeviceDatetime = null;
    private String OrderText;
    private String Status;
    private String StatusDetail;
    private List<OrderChild> OrderDetails = new ArrayList<OrderChild>();

    public OrderMaster() {

    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public Date getOrderDeviceDate() {
        return OrderDeviceDate;
    }

    public void setOrderDeviceDate(Date orderDeviceDate) {
        OrderDeviceDate = orderDeviceDate;
    }

    public String getPlaceId() {
        return PlaceId;
    }

    public void setPlaceId(String placeId) {
        PlaceId = placeId;
    }

    public String getPlaceName() {
        return PlaceName;
    }

    public void setPlaceName(String placeName) {
        PlaceName = placeName;
    }

    public String getPlaceLocation() {
        return PlaceLocation;
    }

    public void setPlaceLocation(String placeLocation) {
        PlaceLocation = placeLocation;
    }

    public String getPlaceImageURL() {
        return PlaceImageURL;
    }

    public void setPlaceImageURL(String placeImageURL) {
        PlaceImageURL = placeImageURL;
    }

    public String getBusinessAvgRating() {
        return BusinessAvgRating;
    }

    public void setBusinessAvgRating(String businessAvgRating) {
        BusinessAvgRating = businessAvgRating;
    }

    public String getTotalReview() {
        return TotalReview;
    }

    public void setTotalReview(String totalReview) {
        TotalReview = totalReview;
    }

    public List<pk.com.pieinthesky.app.beans.Cuisine> getCuisine() {
        return Cuisine;
    }

    public void setCuisine(List<pk.com.pieinthesky.app.beans.Cuisine> cuisine) {
        Cuisine = cuisine;
    }

    public String getCuisines() {
        String cuisines="";
        if(Cuisine!=null) {
            cuisines = TextUtils.join(", ", Cuisine);
        }
        return cuisines;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public Date getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(Date orderDate) {
        OrderDate = orderDate;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public int getDiscountTypeId() {
        return DiscountTypeId;
    }

    public void setDiscountTypeId(int discountTypeId) {
        DiscountTypeId = discountTypeId;
    }

    public String getDiscountPercentage() {
        return DiscountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        DiscountPercentage = discountPercentage;
    }

    public String getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        DiscountAmount = discountAmount;
    }

    public String getSalesTaxPercent() {
        return SalesTaxPercent;
    }

    public void setSalesTaxPercent(String salesTaxPercent) {
        SalesTaxPercent = salesTaxPercent;
    }

    public String getSalesTaxAmount() {
        return SalesTaxAmount;
    }

    public void setSalesTaxAmount(String salesTaxAmount) {
        SalesTaxAmount = salesTaxAmount;
    }

    public String getDeliveryFee() {
        return DeliveryFee;
    }

    public void setDeliveryFee(String deliveryFee) {
        DeliveryFee = deliveryFee;
    }

    public String getSubTotalAmount() {
        return SubTotalAmount;
    }

    public void setSubTotalAmount(String subTotalAmount) {
        SubTotalAmount = subTotalAmount;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getDeliveryAddress() {
        return DeliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        DeliveryAddress = deliveryAddress;
    }

    public Date getCheckoutDeviceDatetime() {
        return CheckoutDeviceDatetime;
    }

    public void setCheckoutDeviceDatetime(Date checkoutDeviceDatetime) {
        CheckoutDeviceDatetime = checkoutDeviceDatetime;
    }

    public String getOrderText() {
        return OrderText;
    }

    public void setOrderText(String orderText) {
        OrderText = orderText;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getStatusDetail() {
        return StatusDetail;
    }

    public void setStatusDetail(String statusDetail) {
        StatusDetail = statusDetail;
    }


    public List<OrderChild> getOrderDetails() {
        return OrderDetails;
    }

    public void setOrderDetails(List<OrderChild> orderDetails) {
        OrderDetails = orderDetails;
    }

    public float getNumberOfItems() {
        int numOfItems = 0;
        for (OrderChild orderChild : OrderDetails) {
            numOfItems += Float.valueOf(orderChild.getQuantity());

        }

        return numOfItems;
    }

    public void calculateValues() {

        double subTotalAmount = 0;
        double totalAmount = 0;
        double beforeSaleTaxAmount = 0;
        double discountAmount = Float.valueOf("0" + DiscountAmount);
        double discountPercentage = Float.valueOf("0" + DiscountPercentage);
        double saleTaxPercent = Float.valueOf(SalesTaxPercent);
        double saleTaxAmount = 0;
        double deliveryFee = Float.valueOf("0" + DeliveryFee);

        for (OrderChild orderChild : OrderDetails) {
            subTotalAmount += Float.valueOf(orderChild.getTotalAmount());
        }

        if (subTotalAmount > 0) {
            if (DiscountTypeId == 1 && discountAmount > 0) {
                discountPercentage = (discountAmount / subTotalAmount) * 100;
            } else if (DiscountTypeId == 2 && discountPercentage > 0) {
                discountAmount = (subTotalAmount / 100) * discountPercentage;
            }
        } else {
            discountAmount = 0;
            discountPercentage = 0;
        }

        beforeSaleTaxAmount = subTotalAmount - discountAmount;

        if (saleTaxPercent > 0 && beforeSaleTaxAmount > 0) {
            saleTaxAmount = (beforeSaleTaxAmount / 100) * saleTaxPercent;
        }

        totalAmount = beforeSaleTaxAmount + saleTaxAmount + deliveryFee;

        setDiscountAmount(String.format("%.2f", discountAmount).replaceFirst("\\.?0*$", ""));
        setDiscountPercentage(String.format("%.2f", discountPercentage).replaceFirst("\\.?0*$", ""));
        setSalesTaxAmount(String.format("%.2f", saleTaxAmount).replaceFirst("\\.?0*$", ""));
        setSubTotalAmount(String.format("%.2f", subTotalAmount).replaceFirst("\\.?0*$", ""));
        setTotalAmount(String.format("%.2f", totalAmount).replaceFirst("\\.?0*$", ""));

    }


}
