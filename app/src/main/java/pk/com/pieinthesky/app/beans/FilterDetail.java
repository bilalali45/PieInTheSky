package pk.com.pieinthesky.app.beans;

import java.util.Date;
import java.util.List;

public class FilterDetail {
    private String Key;
    private String Title;
    private String Type;
    private float Min;
    private float Max;
    private float Step;
    private String DecimalFormat;
    private String PrefixText;
    private String SuffixText;
    private String MinText;
    private String MinDisplayText;
    private String MaxText;
    private String MaxDisplayText;
    private List<String> Steps;
    private List<String> DefaultValue;
    private List<FilterData> Data;

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

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public float getMin() {
        return Min;
    }

    public void setMin(float min) {
        Min = min;
    }

    public float getMax() {
        return Max;
    }

    public void setMax(float max) {
        Max = max;
    }

    public float getStep() {
        return Step;
    }

    public void setStep(float step) {
        Step = step;
    }

    public String getDecimalFormat() {
        return DecimalFormat;
    }

    public void setDecimalFormat(String decimalFormat) {
        DecimalFormat = decimalFormat;
    }

    public String getPrefixText() {
        return PrefixText;
    }

    public void setPrefixText(String prefixText) {
        PrefixText = prefixText;
    }

    public String getSuffixText() {
        return SuffixText;
    }

    public void setSuffixText(String suffixText) {
        SuffixText = suffixText;
    }

    public String getMinText() {
        return MinText;
    }

    public void setMinText(String minText) {
        MinText = minText;
    }

    public String getMinDisplayText() {
        return MinDisplayText;
    }

    public void setMinDisplayText(String minDisplayText) {
        MinDisplayText = minDisplayText;
    }

    public String getMaxText() {
        return MaxText;
    }

    public void setMaxText(String maxText) {
        MaxText = maxText;
    }

    public String getMaxDisplayText() {
        return MaxDisplayText;
    }

    public void setMaxDisplayText(String maxDisplayText) {
        MaxDisplayText = maxDisplayText;
    }

    public List<String> getSteps() {
        return Steps;
    }

    public void setSteps(List<String> steps) {
        Steps = steps;
    }

    public List<String> getDefaultValue() {
        return DefaultValue;
    }

    public void setDefaultValue(List<String> defaultValue) {
        DefaultValue = defaultValue;
    }

    public List<FilterData> getData() {
        return Data;
    }


    public void setData(List<FilterData> data) {
        Data = data;
    }

    public int getDataPosition(String id) {
        int position = -1;
        for (int i = 0; i < Data.size(); i++) {
            if (Data.get(i).Id.equals(id)) {
                position = i;
                break;
            }
        }
        return position;
    }


    public class FilterData {

        private String Id;
        private String Text;


        // Getter Methods

        public String getId() {
            return Id;
        }

        public String getText() {
            return Text;
        }

        // Setter Methods

        public void setId(String Id) {
            this.Id = Id;
        }

        public void setText(String Text) {
            this.Text = Text;
        }

        @Override
        public String toString() {
            return Text;
        }
    }
}
