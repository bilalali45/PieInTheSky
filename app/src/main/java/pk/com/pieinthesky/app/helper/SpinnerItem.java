package pk.com.pieinthesky.app.helper;

public class SpinnerItem {

    public SpinnerItem() {
    }

    public SpinnerItem(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public SpinnerItem(String value, String text, String tag) {
        this.value = value;
        this.text = text;
        this.tag = tag;
    }

    private String value;
    private String text;
    private String tag;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return text;
    }
}
