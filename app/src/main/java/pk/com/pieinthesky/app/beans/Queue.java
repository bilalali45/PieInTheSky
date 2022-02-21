package pk.com.pieinthesky.app.beans;

public class Queue {
    private String QueueId;
    private int TokenNo;
    private String FullName;
    private int NoOfPerson;
    private String QueueDate;
    private String PlaceId;
    private String ProfileImageURL;
    private String PlaceName;

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

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public int getNoOfPerson() {
        return NoOfPerson;
    }

    public void setNoOfPerson(int noOfPerson) {
        NoOfPerson = noOfPerson;
    }

    public String getQueueDate() {
        return QueueDate;
    }

    public void setQueueDate(String queueDate) {
        QueueDate = queueDate;
    }

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
}
