package pk.com.pieinthesky.app.beans;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

public class PlaceDetail implements Serializable {
    private String PlaceId;
    private String ProfileImageURL;
    private String PlaceName;
    private String PlaceSubTitle;
    private String PlaceAbout;
    private String VistorText;
    private String Rating;
    private int ReviewCount;
    private int LikeCount;
    private String PlaceLocation;
    private String ContactNo;
    private boolean IsBookmark;
    private boolean IsLike;
    private String MyRatingAvg;
    private float Latitude;
    private float Longitude;
    private String UserID;
    private String TrustworthyText;
    private List<Cuisine> Cuisine;
    private List<Tag> Tags;
    private MyRatingDetail MyRatingDetail;
    private List<RatingDetail> RatingDetailOverall;
    private List<String> PlaceTimings;
    private List<String> ContactNumber;
    private List<MediaDetail> BannerPictures;
    private List<MediaDetail> PlacePictures;
    private List<MediaDetail> PlaceVideos;
    private List<MediaDetail> PlaceMenu;
    private List<PlaceReview> PlaceReviews;
    private boolean IsTableReservation;
    private boolean IsOrderOnline;
    private boolean IsTableReservationAvailable;
    private boolean IsOrderOnlineAvailable;
    private String GSTPercentage;
    private String DeliveryCharges;
    private boolean AllowCheckIn;

    public MyRatingDetail getMyRatingDetail() {
        return MyRatingDetail;
    }

    public void setMyRatingDetail(MyRatingDetail MyRatingDetail) {
        this.MyRatingDetail = MyRatingDetail;
    }

    public String getPlaceId() {
        return PlaceId;
    }

    public String getProfileImageURL() {
        return ProfileImageURL;
    }

    public String getPlaceName() {
        return PlaceName;
    }

    public String getPlaceSubTitle() {
        return PlaceSubTitle;
    }

    public String getPlaceAbout() {
        return PlaceAbout;
    }

    public String getVistorText() {
        return VistorText;
    }

    public String getRating() {
        return Rating;
    }

    public int getReviewCount() {
        return ReviewCount;
    }

    public int getLikeCount() {
        return LikeCount;
    }

    public String getPlaceLocation() {
        return PlaceLocation;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public boolean isBookmark() {
        return IsBookmark;
    }

    public boolean isLike() {
        return IsLike;
    }

    public String getMyRatingAvg() {
        return MyRatingAvg;
    }

    public String getUserID() {
        return UserID;
    }

    //public Object getMyRatingDetail() {
    //    return MyRatingDetail;
    //}


    public void setPlaceId(String PlaceId) {
        this.PlaceId = PlaceId;
    }

    public void setProfileImageURL(String ProfileImageURL) {
        this.ProfileImageURL = ProfileImageURL;
    }

    public void setPlaceName(String PlaceName) {
        this.PlaceName = PlaceName;
    }

    public void setPlaceSubTitle(String PlaceSubTitle) {
        this.PlaceSubTitle = PlaceSubTitle;
    }

    public void setPlaceAbout(String PlaceAbout) {
        this.PlaceAbout = PlaceAbout;
    }

    public void setVistorText(String VistorText) {
        this.VistorText = VistorText;
    }

    public void setRating(String Rating) {
        this.Rating = Rating;
    }

    public void setReviewCount(int ReviewCount) {
        this.ReviewCount = ReviewCount;
    }

    public void setLikeCount(int LikeCount) {
        this.LikeCount = LikeCount;
    }

    public void setPlaceLocation(String PlaceLocation) {
        this.PlaceLocation = PlaceLocation;
    }

    public void setContactNo(String ContactNo) {
        this.ContactNo = ContactNo;
    }

    public void setBookmark(boolean IsBookmark) {
        this.IsBookmark = IsBookmark;
    }

    public void setLike(boolean IsLike) {
        this.IsLike = IsLike;
    }

    public void setMyRatingAvg(String MyRatingAvg) {
        this.MyRatingAvg = MyRatingAvg;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    //public void setMyRatingDetail(Object MyRatingDetail) {
    //    this.MyRatingDetail = MyRatingDetail;
    //}


    public List<pk.com.pieinthesky.app.beans.Cuisine> getCuisine() {
        return Cuisine;
    }

    public void setCuisine(List<pk.com.pieinthesky.app.beans.Cuisine> cuisine) {
        Cuisine = cuisine;
    }

    public List<String> getPlaceTimings() {
        return PlaceTimings;
    }

    public void setPlaceTimings(List<String> placeTimings) {
        PlaceTimings = placeTimings;
    }

    public List<MediaDetail> getBannerPictures() {
        return BannerPictures;
    }

    public void setBannerPictures(List<MediaDetail> bannerPictures) {
        BannerPictures = bannerPictures;
    }

    public List<MediaDetail> getPlacePictures() {
        return PlacePictures;
    }

    public void setPlacePictures(List<MediaDetail> placePictures) {
        PlacePictures = placePictures;
    }

    public List<MediaDetail> getPlaceVideos() {
        return PlaceVideos;
    }

    public void setPlaceVideos(List<MediaDetail> placeVideos) {
        PlaceVideos = placeVideos;
    }

    public List<MediaDetail> getPlaceMenu() {
        return PlaceMenu;
    }

    public void setPlaceMenu(List<MediaDetail> placeMenu) {
        PlaceMenu = placeMenu;
    }

    public List<PlaceReview> getPlaceReviews() {
        return PlaceReviews;
    }

    public void setPlaceReviews(List<PlaceReview> placeReviews) {
        PlaceReviews = placeReviews;
    }

    public String getCuisines() {
        String cuisines = "";
        if (Cuisine != null) {
            cuisines = TextUtils.join(", ", Cuisine);
        }
        return cuisines;
    }

    public List<RatingDetail> getRatingDetailOverall() {
        return RatingDetailOverall;
    }

    public void setRatingDetailOverall(List<RatingDetail> ratingDetailOverall) {
        RatingDetailOverall = ratingDetailOverall;
    }

    public float getLatitude() {
        return Latitude;
    }

    public void setLatitude(float latitude) {
        Latitude = latitude;
    }

    public float getLongitude() {
        return Longitude;
    }

    public void setLongitude(float longitude) {
        Longitude = longitude;
    }

    public String getTrustworthyText() {
        return TrustworthyText;
    }

    public void setTrustworthyText(String trustworthyText) {
        TrustworthyText = trustworthyText;
    }

    public List<Tag> getTags() {
        return Tags;
    }

    public void setTags(List<Tag> tags) {
        Tags = tags;
    }

    public List<String> getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(List<String> contactNumber) {
        ContactNumber = contactNumber;
    }

    public boolean isTableReservation() {
        return IsTableReservation;
    }

    public void setTableReservation(boolean tableReservation) {
        IsTableReservation = tableReservation;
    }

    public boolean isOrderOnline() {
        return IsOrderOnline;
    }

    public void setOrderOnline(boolean orderOnline) {
        IsOrderOnline = orderOnline;
    }

    public boolean isTableReservationAvailable() {
        return IsTableReservationAvailable;
    }

    public void setTableReservationAvailable(boolean tableReservationAvailable) {
        IsTableReservationAvailable = tableReservationAvailable;
    }

    public boolean isOrderOnlineAvailable() {
        return IsOrderOnlineAvailable;
    }

    public void setOrderOnlineAvailable(boolean orderOnlineAvailable) {
        IsOrderOnlineAvailable = orderOnlineAvailable;
    }

    public String getGSTPercentage() {
        return GSTPercentage;
    }

    public void setGSTPercentage(String GSTPercentage) {
        this.GSTPercentage = GSTPercentage;
    }

    public String getDeliveryCharges() {
        return DeliveryCharges;
    }

    public void setDeliveryCharges(String deliveryCharges) {
        DeliveryCharges = deliveryCharges;
    }

    public boolean isAllowCheckIn() {
        return AllowCheckIn;
    }

    public void setAllowCheckIn(boolean allowCheckIn) {
        AllowCheckIn = allowCheckIn;
    }
}
