package pk.com.pieinthesky.app.beans;

import java.util.List;

public class MenuDetail {

    private String CategoryId;
    private String CategoryName;
    private String Type;
    private String ImageURL;
    private List<DishDetail> DishList;

    public MenuDetail() {

    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public List<DishDetail> getDishList() {
        return DishList;
    }

    public void setDishList(List<DishDetail> dishList) {
        DishList = dishList;
    }
}
