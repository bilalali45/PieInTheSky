package pk.com.Taj.app.beans;

public class ServiceDetail {
    private String ServiceName;
    private String PictureUrl;


    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getPictureUrl() {
        return PictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        PictureUrl = pictureUrl;
    }


    @Override
    public String toString() {
        return getServiceName();
    }
}
