package pk.com.pieinthesky.app.beans;

import java.io.Serializable;

public class VariantDetail implements Serializable {
    private String Id;
    private String Text;
    private float Price;
    private transient boolean selected;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
