package pk.com.Taj.app.beans;


import android.text.TextUtils;

import java.util.List;

public class ReservationDetail {
    private String ReservationId;
    private String UserId;
    private String PlaceID;
    private String ReservationNo;
    private String ReservationDate;
    private String TimeSlotId;
    private int NoOfGuest;
    private String AdditionalRequests;
    private String ByName;
    private String ContactNo;
    private String CreatedDate;
    private String PlaceName;
    private String PlaceLocation;
    private String PlaceImageURL;
    private List<String> PlaceContactNo;
    private String BusinessAvgRating;
    private String TotalReview;
    private String ReservationStatus;
    private String ReservationMessage;
    private List<Cuisine> Cuisine;

    public String getReservationId() {
        return ReservationId;
    }

    public void setReservationId(String reservationId) {
        ReservationId = reservationId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPlaceID() {
        return PlaceID;
    }

    public void setPlaceID(String placeID) {
        PlaceID = placeID;
    }

    public String getReservationNo() {
        return ReservationNo;
    }

    public void setReservationNo(String reservationNo) {
        ReservationNo = reservationNo;
    }

    public String getReservationDate() {
        return ReservationDate;
    }

    public void setReservationDate(String reservationDate) {
        ReservationDate = reservationDate;
    }

    public String getTimeSlotId() {
        return TimeSlotId;
    }

    public void setTimeSlotId(String timeSlotId) {
        TimeSlotId = timeSlotId;
    }

    public int getNoOfGuest() {
        return NoOfGuest;
    }

    public void setNoOfGuest(int noOfGuest) {
        NoOfGuest = noOfGuest;
    }

    public String getAdditionalRequests() {
        return AdditionalRequests;
    }

    public void setAdditionalRequests(String additionalRequests) {
        AdditionalRequests = additionalRequests;
    }

    public String getByName() {
        return ByName;
    }

    public void setByName(String byName) {
        ByName = byName;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getPlaceImageURL() {
        return PlaceImageURL;
    }

    public void setPlaceImageURL(String placeImageURL) {
        PlaceImageURL = placeImageURL;
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

    public List<String> getPlaceContactNo() {
        return PlaceContactNo;
    }

    public void setPlaceContactNo(List<String> placeContactNo) {
        PlaceContactNo = placeContactNo;
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

    public String getReservationStatus() {
        return ReservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        ReservationStatus = reservationStatus;
    }

    public String getReservationMessage() {
        return ReservationMessage;
    }

    public void setReservationMessage(String reservationMessage) {
        ReservationMessage = reservationMessage;
    }

    public List<Cuisine> getCuisine() {
        return Cuisine;
    }

    public void setCuisine(List<pk.com.Taj.app.beans.Cuisine> cuisine) {
        Cuisine = cuisine;
    }

    public String getCuisines() {
        String cuisines="";
        if(Cuisine!=null) {
            cuisines = TextUtils.join(", ", Cuisine);
        }
        return cuisines;
    }

}
