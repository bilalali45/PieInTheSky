package pk.com.Taj.app.beans;

import android.text.TextUtils;

import java.util.List;

public class PlaceOverview {
    private String PlaceId;
    private String PlaceName;
    private String ProfileImageURL;
    private String PlaceLocation;
    private String BusinessAvgRating;
    private String TotalReview;
    private String Tags;
    private List<Cuisine> Cuisine;

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

    public String getProfileImageURL() {
        return ProfileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        ProfileImageURL = profileImageURL;
    }

    public String getPlaceLocation() {
        return PlaceLocation;
    }

    public void setPlaceLocation(String placeLocation) {
        PlaceLocation = placeLocation;
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

    public List<Cuisine> getCuisine() {
        return Cuisine;
    }

    public String getCuisines() {
        String cuisines="";
        if(Cuisine!=null) {
            cuisines = TextUtils.join(", ", Cuisine);
        }
        return cuisines;
    }

    public void setCuisine(List<Cuisine> cuisine) {
        Cuisine = cuisine;
    }

    public String getTags() {
        return Tags;
    }

    public void setTags(String tags) {
        Tags = tags;
    }

}