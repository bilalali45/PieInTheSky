package pk.com.Taj.app.beans;

import android.text.TextUtils;

import java.util.List;

public class RestaurantDetail {
    private String PlaceId;
    private String PlaceName;
    private String ProfileImageURL;
    private String PlaceLocation;
    private int RestaurantPlaces;
    private double Distance;
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

    public int getRestaurantPlaces() {
        return RestaurantPlaces;
    }

    public void setRestaurantPlaces(int restaurantPlaces) {
        RestaurantPlaces = restaurantPlaces;
    }

    public double getDistance() {
        return Distance;
    }

    public void setDistance(double distance) {
        Distance = distance;
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


}