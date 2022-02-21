package pk.com.Taj.app.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderChildVariant  implements Serializable {
    private String Id;
    private String Key;
    private List<VariantDetail> Data = new ArrayList<VariantDetail>();

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public List<VariantDetail> getData() {
        return Data;
    }

    public void setData(List<VariantDetail> data) {
        Data = data;
    }
}