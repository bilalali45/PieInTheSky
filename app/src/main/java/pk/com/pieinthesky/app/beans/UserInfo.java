package pk.com.pieinthesky.app.beans;

public class UserInfo {

    private String Id;
    private String ProfileImageURL;
    private String FirstName;
    private String LastName;
    private String TotalFollower;
    private String TotalReview;
    private boolean IsFollow;

    public String getId() {
        return Id;
    }

    public String getProfileImageURL() {
        return ProfileImageURL;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getTotalFollower() {
        return TotalFollower;
    }

    public String getTotalReview() {
        return TotalReview;
    }


    public void setId(String Id) {
        this.Id = Id;
    }

    public void setProfileImageURL(String ProfileImageURL) {
        this.ProfileImageURL = ProfileImageURL;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public void setTotalFollower(String TotalFollower) {
        this.TotalFollower = TotalFollower;
    }

    public void setTotalReview(String TotalReview) {
        this.TotalReview = TotalReview;
    }

    public boolean isFollow() {
        return IsFollow;
    }

    public void setFollow(boolean follow) {
        IsFollow = follow;
    }
}