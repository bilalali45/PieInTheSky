package pk.com.Taj.app.beans;

import java.util.List;

public class PlaceCategory {
    private String CategoryId;
    private String CategoryName;
    private List<PlaceOverview> placeOverviews;

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

    public List<PlaceOverview> getPlaceOverviews() {
        return placeOverviews;
    }

    public void setPlaceOverviews(List<PlaceOverview> placeOverviews) {
        this.placeOverviews = placeOverviews;
    }
}