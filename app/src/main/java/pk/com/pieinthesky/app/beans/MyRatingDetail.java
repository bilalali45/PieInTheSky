package pk.com.pieinthesky.app.beans;

import java.io.Serializable;

public class MyRatingDetail implements Serializable {

    private String ReviewsId;
    private String Reviewstext;
    private Float Rating;
    private String TotalLike;
    private String TotalComments;
    private String ReviewDays;
    private String PictureDetail;
    private String CreatedDate;

    public String getReviewsId() {
        return ReviewsId;
    }

    public void setReviewsId(String reviewsId) {
        ReviewsId = reviewsId;
    }

    public String getReviewstext() {
        return Reviewstext;
    }

    public void setReviewstext(String reviewstext) {
        Reviewstext = reviewstext;
    }

    public Float getRating() {
        return Rating;
    }

    public void setRating(Float rating) {
        Rating = rating;
    }

    public String getTotalLike() {
        return TotalLike;
    }

    public void setTotalLike(String totalLike) {
        TotalLike = totalLike;
    }

    public String getTotalComments() {
        return TotalComments;
    }

    public void setTotalComments(String totalComments) {
        TotalComments = totalComments;
    }

    public String getReviewDays() {
        return ReviewDays;
    }

    public void setReviewDays(String reviewDays) {
        ReviewDays = reviewDays;
    }

    public String getPictureDetail() {
        return PictureDetail;
    }

    public void setPictureDetail(String pictureDetail) {
        PictureDetail = pictureDetail;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }
}
