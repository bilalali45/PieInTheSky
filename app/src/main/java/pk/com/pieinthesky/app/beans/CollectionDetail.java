package pk.com.pieinthesky.app.beans;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

public class CollectionDetail  implements Serializable {
    private String CollectionID;
    private String CollectionTitle;
    private String Discription;
    private String CoverImageURL;
    private String CollectionPlaces;
    private List<PlaceOverview> ResturantProfileList;

    public String getCollectionID() {
        return CollectionID;
    }

    public void setCollectionID(String collectionID) {
        CollectionID = collectionID;
    }

    public String getCollectionTitle() {
        return CollectionTitle;
    }

    public void setCollectionTitle(String collectionTitle) {
        CollectionTitle = collectionTitle;
    }

    public String getDiscription() {
        return Discription;
    }

    public void setDiscription(String discription) {
        Discription = discription;
    }

    public String getCoverImageURL() {
        return CoverImageURL;
    }

    public void setCoverImageURL(String coverImageURL) {
        CoverImageURL = coverImageURL;
    }

    public String getCollectionPlaces() {
        return CollectionPlaces;
    }

    public void setCollectionPlaces(String collectionPlaces) {
        CollectionPlaces = collectionPlaces;
    }

    public List<PlaceOverview> getResturantProfileList() {
        return ResturantProfileList;
    }

    public void setResturantProfileList(List<PlaceOverview> resturantProfileList) {
        this.ResturantProfileList = resturantProfileList;
    }
}