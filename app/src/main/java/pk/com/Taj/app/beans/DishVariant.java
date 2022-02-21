package pk.com.Taj.app.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DishVariant implements Serializable {
    private String Id;
    private String Key;
    private String Title;
    private String Description;
    private String Type;
    private boolean Required;
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

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public boolean isRequired() {
        return Required;
    }

    public void setRequired(boolean required) {
        Required = required;
    }

    public List<VariantDetail> getData() {
        return Data;
    }

    public void setData(List<VariantDetail> data) {
        Data = data;
    }


}
