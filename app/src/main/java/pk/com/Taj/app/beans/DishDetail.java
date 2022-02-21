package pk.com.Taj.app.beans;

import java.util.ArrayList;
import java.util.List;

public class DishDetail {
    private String DishId;
    private String DishName;
    private String ImageURL;
    private int TotalPrice;
    private List<DishVariant> Variants = new ArrayList<DishVariant>();
    private boolean selected = false;


    public DishDetail() {

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

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public int getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        TotalPrice = totalPrice;
    }

    public List<DishVariant> getVariants() {
        return Variants;
    }

    public void setVariants(List<DishVariant> variants) {
        Variants = variants;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
