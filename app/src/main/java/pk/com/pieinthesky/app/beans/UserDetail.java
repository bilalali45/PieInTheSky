package pk.com.pieinthesky.app.beans;

import java.util.List;

public class UserDetail {

    private String ProfileImageURL;
    private String FirstName;
    private String LastName;
    private String Email;
    private String IsMale = null;
    private String MobileNumber;
    private String Address;
    private String UserDescription;
    private String TotalFollower;
    private String TotalFollowing;
    private List<UserInfo> FollowerData;
    private List<UserInfo> FollowingData;


    public String getProfileImageURL() {
        return ProfileImageURL;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getEmail() {
        return Email;
    }

    public String getIsMale() {
        return IsMale;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public String getAddress() {
        return Address;
    }

    public String getUserDescription() {
        return UserDescription;
    }

    public String getTotalFollower() {
        return TotalFollower;
    }

    public String getTotalFollowing() {
        return TotalFollowing;
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

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setIsMale(String IsMale) {
        this.IsMale = IsMale;
    }

    public void setMobileNumber(String MobileNumber) {
        this.MobileNumber = MobileNumber;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public void setUserDescription(String UserDescription) {
        this.UserDescription = UserDescription;
    }

    public void setTotalFollower(String TotalFollower) {
        this.TotalFollower = TotalFollower;
    }

    public void setTotalFollowing(String TotalFollowing) {
        this.TotalFollowing = TotalFollowing;
    }

    public List<UserInfo> getFollowerData() {
        return FollowerData;
    }

    public void setFollowerData(List<UserInfo> followerData) {
        FollowerData = followerData;
    }

    public List<UserInfo> getFollowingData() {
        return FollowingData;
    }

    public void setFollowingData(List<UserInfo> followingData) {
        FollowingData = followingData;
    }
}
