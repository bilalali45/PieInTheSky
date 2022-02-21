package pk.com.pieinthesky.app.beans;


import java.io.Serializable;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class OrderChild {
    private String DishId;
    private String DishName;
    private String Quantity = "0";
    private String Price = "0";
    private String Amount = "0";
    private String SpecialInstruction;
    private String TotalAmount = "0";
    private List<OrderChildVariant> Variants = new ArrayList<OrderChildVariant>();
    private transient List<DishVariant> DishVariants = new ArrayList<DishVariant>();

    public OrderChild() {

    }

    public String getDishId() {
        return DishId;
    }

    public void setDishId(String dishId) {
        DishId = dishId;
    }

    public String getDishName() {
        return DishName;
    }

    public void setDishName(String dishName) {
        DishName = dishName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getSpecialInstruction() {
        return SpecialInstruction;
    }

    public void setSpecialInstruction(String specialInstruction) {
        SpecialInstruction = specialInstruction;
    }


    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public List<OrderChildVariant> getVariants() {
        return Variants;
    }

    public void setVariants(List<OrderChildVariant> variants) {
        Variants = variants;
    }

    public List<DishVariant> getDishVariants() {
        return DishVariants;
    }

    public void setDishVariants(List<DishVariant> dishVariants) {
        DishVariants = dishVariants;
    }

    public void calculateValues() {
        double quantity = Float.valueOf("0" + Quantity);
        double price = Float.valueOf("0" + Price);
        double amount = quantity * price;
        double variantAmount = 0;
        double totalAmount = 0;

        for (OrderChildVariant orderChildVariant : Variants) {
            for (VariantDetail variantDetail : orderChildVariant.getData()) {
                variantAmount += (quantity * variantDetail.getPrice());
            }
        }

        totalAmount = amount + variantAmount;

        setAmount(String.format("%.2f", amount).replaceFirst("\\.?0*$", ""));
        setTotalAmount(String.format("%.2f", totalAmount).replaceFirst("\\.?0*$", ""));
    }



}
