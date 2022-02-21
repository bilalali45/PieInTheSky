package pk.com.pieinthesky.app.beans;

import java.io.Serializable;

public class MediaDetail  implements Serializable {
    private String Title;
    private String Url;
    private String Description;


    public String getTitle() {
        return Title;
    }

    public String getUrl() {
        return Url;
    }

    public String getDescription() {
        return Description;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

}
