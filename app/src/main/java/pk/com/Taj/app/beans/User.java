package pk.com.Taj.app.beans;

public class User {
    private String userId;
    private String loginWith;
    private String uniqueId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNo;
    private String photoURL;
    private String coverPhotoURL;
    private int userModeId;

    public User(){

    }

    public User(String userId, String uniqueId, String firstName,String lastName, String mobileNo,  String email) {
        this.userId = userId;
        this.setUniqueId(uniqueId);
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNo = mobileNo;
        this.email = email;

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setProfileId(String uniqueId) {
        this.setUniqueId(uniqueId);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getCoverPhotoURL() {
        return coverPhotoURL;
    }

    public void setCoverPhotoURL(String coverPhotoURL) {
        this.coverPhotoURL = coverPhotoURL;
    }

    public String getLoginWith() {
        return loginWith;
    }

    public void setLoginWith(String loginWith) {
        this.loginWith = loginWith;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserModeId() {
        return userModeId;
    }

    public void setUserModeId(int userModeId) {
        this.userModeId = userModeId;
    }
}
