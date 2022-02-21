package pk.com.Taj.app.beans;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

public class QueueDetail implements Serializable {
    private String PlaceId;
    private String ProfileImageURL;
    private String PlaceName;
    private String PlaceSubTitle;
    private String PlaceAbout;
    private String PlaceLocation;
    private float Longitude;
    private float Latitude;
    private List<String> ContactNumber;
    private List<Cuisine> Cuisine;
    private List<MediaDetail> PlaceMenu;
    private String QueueId;
    private int TokenNo;
    private String EstimatedTime;
    private String Note;

    public String getPlaceId() {
        return PlaceId;
    }

    public void setPlaceId(String placeId) {
        PlaceId = placeId;
    }

    public String getProfileImageURL() {
        return ProfileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        ProfileImageURL = profileImageURL;
    }

    public String getPlaceName() {
        return PlaceName;
    }

    public void setPlaceName(String placeName) {
        PlaceName = placeName;
    }

    public String getPlaceSubTitle() {
        return PlaceSubTitle;
    }

    public void setPlaceSubTitle(String placeSubTitle) {
        PlaceSubTitle = placeSubTitle;
    }

    public String getPlaceAbout() {
        return PlaceAbout;
    }

    public void setPlaceAbout(String placeAbout) {
        PlaceAbout = placeAbout;
    }

    public String getPlaceLocation() {
        return PlaceLocation;
    }

    public void setPlaceLocation(String placeLocation) {
        PlaceLocation = placeLocation;
    }

    public float getLongitude() {
        return Longitude;
    }

    public void setLongitude(float longitude) {
        Longitude = longitude;
    }

    public float getLatitude() {
        return Latitude;
    }

    public void setLatitude(float latitude) {
        Latitude = latitude;
    }

    public List<String> getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(List<String> contactNumber) {
        ContactNumber = contactNumber;
    }

    public List<pk.com.Taj.app.beans.Cuisine> getCuisine() {
        return Cuisine;
    }

    public void setCuisine(List<pk.com.Taj.app.beans.Cuisine> cuisine) {
        Cuisine = cuisine;
    }

    public List<MediaDetail> getPlaceMenu() {
        return PlaceMenu;
    }

    public void setPlaceMenu(List<MediaDetail> placeMenu) {
        PlaceMenu = placeMenu;
    }

    public String getQueueId() {
        return QueueId;
    }

    public void setQueueId(String queueId) {
        QueueId = queueId;
    }

    public int getTokenNo() {
        return TokenNo;
    }

    public void setTokenNo(int tokenNo) {
        TokenNo = tokenNo;
    }

    public String getEstimatedTime() {
        return EstimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        EstimatedTime = estimatedTime;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }


    public String getCuisines() {
        String cuisines = "";
        if (Cuisine != null) {
            cuisines = TextUtils.join(", ", Cuisine);
        }
        return cuisines;
    }
}
