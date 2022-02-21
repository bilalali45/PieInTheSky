package pk.com.pieinthesky.app.beans;

import android.text.TextUtils;

import java.util.List;

public class PlaceReview {
    private String ReviewsId;
    private String PlaceId;
    private String PlaceName;
    private String PlaceLocation;
    private String PlaceImageURL;
    private String BusinessAvgRating;
    private String TotalReview;
    private String ProfileImageURL;
    private String FullName;
    private String SubDetail;
    private String ReviewText;
    private String ReviewDate;
    private String ReviewDays;
    private int TotalComments;
    private int TotalLike;
    private boolean IsLike;
    private boolean IsFollow;
    private String UserId;
    private List<Cuisine> Cuisine;
    private List<ReviewComment> CommentOnReview;

    public String getReviewsId() {
        return ReviewsId;
    }

    public void setReviewsId(String reviewsId) {
        ReviewsId = reviewsId;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getSubDetail() {
        return SubDetail;
    }

    public void setSubDetail(String subDetail) {
        SubDetail = subDetail;
    }

    public String getReviewText() {
        return ReviewText;
    }

    public void setReviewText(String reviewText) {
        ReviewText = reviewText;
    }

    public String getReviewDate() {
        return ReviewDate;
    }

    public void setReviewDate(String reviewDate) {
        ReviewDate = reviewDate;
    }

    public int getTotalComments() {
        return TotalComments;
    }

    public void setTotalComments(int totalComments) {
        TotalComments = totalComments;
    }

    public int getTotalLike() {
        return TotalLike;
    }

    public void setTotalLike(int totalLike) {
        TotalLike = totalLike;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public List<ReviewComment> getCommentOnReview() {
        return CommentOnReview;
    }

    public void setCommentOnReview(List<ReviewComment> commentOnReview) {
        CommentOnReview = commentOnReview;
    }

    public boolean isLike() {
        return IsLike;
    }

    public void setLike(boolean like) {
        IsLike = like;
    }


    public String getProfileImageURL() {
        return ProfileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        ProfileImageURL = profileImageURL;
    }

    public String getReviewDays() {
        return ReviewDays;
    }

    public void setReviewDays(String reviewDays) {
        ReviewDays = reviewDays;
    }

    public boolean isFollow() {
        return IsFollow;
    }

    public void setFollow(boolean follow) {
        IsFollow = follow;
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

    public List<Cuisine> getCuisine() {
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
}
