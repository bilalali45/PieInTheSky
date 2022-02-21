package pk.com.Taj.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.jaygoo.widget.VerticalRangeSeekBar;
import pk.com.Taj.app.R;
import pk.com.Taj.app.beans.FilterDetail;
import pk.com.Taj.app.widget.IconTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FilterDialog extends Dialog {
    Context context;


    private int tabCount = 0;
    private int activeTabIndex = 0;
    private int singleChoiceSelectedItemCount = 0;
    private int multipleChoiceSelectedItemCount = 0;
    private List<FilterDetail> filterDetails = new ArrayList<FilterDetail>();

    private IconTextView btnClose = null;
    private Button btnCLear = null;
    private Button btnApply = null;

    private LinearLayout lyFilterTab = null;
    private LinearLayout lyFilterTabContent = null;
    private LinearLayout ActiveTabButton = null;
    private LinearLayout ActiveTabContent = null;
    private TextView ActiveTabTitle = null;
    private TextView ActiveTabSubTitle = null;

    private OnApplyClickListener onApplyClickListener = null;
    private OnClearClickListener onClearClickListener = null;

    private HashMap<String, List<String>> filter = new HashMap<String, List<String>>();
    private HashMap<String, View> filterControl = new HashMap<String, View>();

    public FilterDialog(Context context, List<FilterDetail> filterDetails) {
        super(context);
        this.context = context;
        this.filterDetails = filterDetails;

        createDialog();
    }

    public FilterDialog(Context context, int themeResId, List<FilterDetail> filterDetails) {
        super(context, themeResId);
        this.context = context;
        this.filterDetails = filterDetails;

        createDialog();

        renderDialog();
    }

    private void createDialog() {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.filter_dialog);

        IconTextView btnClose = this.findViewById(R.id.btnClose);
        Button btnCLear = this.findViewById(R.id.btnClear);
        final Button btnApply = this.findViewById(R.id.btnApply);

        lyFilterTab = this.findViewById(R.id.lyFilterTab);
        lyFilterTabContent = this.findViewById(R.id.lyFilterTabContent);


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterDialog.this.dismiss();
            }
        });

        btnCLear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnClear_onClick(view);
            }
        });


        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnApply_onClick(view);
            }
        });


    }

    private void btnClear_onClick(View view) {
        filter.clear();
        singleChoiceSelectedItemCount=0;
        multipleChoiceSelectedItemCount=0;

        renderDialog();

        if (onClearClickListener != null) {
            onClearClickListener.onClick(view);
        }
    }

    private void btnApply_onClick(View view) {
        filter.clear();
        singleChoiceSelectedItemCount = 0;
        multipleChoiceSelectedItemCount = 0;

        for (final FilterDetail filterDetail : filterDetails) {
            List<String> values = new ArrayList<String>();

            if (filterDetail.getType().equals("SingleChoice") || filterDetail.getType().equals("MultipleChoice")) {
                ListView listFilter = (ListView) filterControl.get(filterDetail.getKey());

                SparseBooleanArray selectedItems = listFilter.getCheckedItemPositions();
                for (int position = 0; position < selectedItems.size(); position++) {
                    if (selectedItems.valueAt(position)) {
                        values.add(filterDetail.getData().get(selectedItems.keyAt(position)).getId());
                    }
                }

                if (filterDetail.getType().equals("SingleChoice")) {
                    singleChoiceSelectedItemCount += values.size();

                } else if (filterDetail.getType().equals("MultipleChoice")) {
                    multipleChoiceSelectedItemCount += values.size();
                }

                if (values.size() > 0) {
                    filter.put(filterDetail.getKey(), values);
                }
            } else if (filterDetail.getType().equals("SeekStep")) {
                VerticalRangeSeekBar sbSingle = (VerticalRangeSeekBar) filterControl.get(filterDetail.getKey());

                float rangeSelect = (filterDetail.getMax() / 100) * sbSingle.getLeftSeekBar().getProgress();
                values.add(String.valueOf(rangeSelect));

                if (values.size() > 0) {
                    filter.put(filterDetail.getKey(), values);
                }
            } else if (filterDetail.getType().equals("SeekRange")) {
                VerticalRangeSeekBar sbRange = (VerticalRangeSeekBar) filterControl.get(filterDetail.getKey());

                float minRangeSelect = (filterDetail.getMax() / 100) * sbRange.getLeftSeekBar().getProgress();
                float maxRangeSelect = (filterDetail.getMax() / 100) * sbRange.getRightSeekBar().getProgress();
                values.add(String.valueOf(minRangeSelect));
                values.add(String.valueOf(maxRangeSelect));

                if (values.size() > 0) {
                    filter.put(filterDetail.getKey(), values);
                }
            }
        }

        if (onApplyClickListener != null) {
            onApplyClickListener.onClick(view);
        }
    }


    private void renderDialog() {
        if (filterDetails.size() > 0) {

            tabCount = 0;
            activeTabIndex = 0;

            filterControl.clear();

            lyFilterTab.removeAllViews();
            lyFilterTabContent.removeAllViews();

            LayoutInflater layoutInflater = LayoutInflater.from(getContext());

            for (final FilterDetail filterDetail : filterDetails) {
                LinearLayout filter_tab_button = null;
                LinearLayout filter_tab_content = null;

                filter_tab_button = (LinearLayout) layoutInflater.inflate(R.layout.filter_tab_button, null);
                TextView tvTabTile = filter_tab_button.findViewById(R.id.tvTabTile);
                TextView tvTabSubTitle = filter_tab_button.findViewById(R.id.tvTabSubTitle);

                tvTabTile.setText(filterDetail.getTitle());
                tvTabSubTitle.setText("");

                filter_tab_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ActiveTabButton.setBackgroundResource(R.drawable.filter_tab);
                        ActiveTabContent.setVisibility(View.GONE);

                        activeTabIndex = view.getId();

                        ActiveTabButton = lyFilterTab.findViewById(activeTabIndex);
                        ActiveTabContent = lyFilterTabContent.findViewById(activeTabIndex);

                        ActiveTabButton.setBackgroundResource(R.drawable.filter_tab_selected);
                        ActiveTabTitle = ActiveTabButton.findViewById(R.id.tvTabTile);
                        ActiveTabSubTitle = ActiveTabButton.findViewById(R.id.tvTabSubTitle);

                        ActiveTabContent.setVisibility(View.VISIBLE);
                    }
                });

                if (filterDetail.getType().equals("SingleChoice") || filterDetail.getType().equals("MultipleChoice")) {

                    filter_tab_content = (LinearLayout) layoutInflater.inflate(R.layout.filter_tab_list_layout, null);
                    final ListView listFilter = filter_tab_content.findViewById(R.id.listFilter);

                    if (filterDetail.getType().equals("SingleChoice")) {
                        listFilter.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        ArrayAdapter filterDataArrayAdapter = new ArrayAdapter<FilterDetail.FilterData>(context, android.R.layout.simple_list_item_single_choice, filterDetail.getData());
                        listFilter.setAdapter(filterDataArrayAdapter);

                        listFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ActiveTabSubTitle.setText(filterDetail.getData().get(position).getText());
                            }
                        });


                        if (filterDetail.getDefaultValue().size() == 1) {
                            int valuePosition = filterDetail.getDataPosition(filterDetail.getDefaultValue().get(0));
                            if (valuePosition != -1) {
                                listFilter.setItemChecked(valuePosition, true);
                                tvTabSubTitle.setText(filterDetail.getData().get(valuePosition).getText());
                            }
                        }
                    } else {
                        listFilter.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                        ArrayAdapter filterDataArrayAdapter = new ArrayAdapter<FilterDetail.FilterData>(context, android.R.layout.simple_list_item_multiple_choice, filterDetail.getData());
                        listFilter.setAdapter(filterDataArrayAdapter);

                        listFilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (listFilter.getCheckedItemCount() != 0) {
                                    ActiveTabSubTitle.setText(listFilter.getCheckedItemCount() + " Selected");
                                } else {
                                    ActiveTabSubTitle.setText("");
                                }
                            }
                        });

                        for (String defaultValue : filterDetail.getDefaultValue()) {
                            int valuePosition = filterDetail.getDataPosition(defaultValue);
                            if (valuePosition != -1) {
                                listFilter.setItemChecked(valuePosition, true);
                            }
                        }

                        if (listFilter.getCheckedItemCount() != 0) {
                            tvTabSubTitle.setText(listFilter.getCheckedItemCount() + " Selected");
                        }
                    }

                    filterControl.put(filterDetail.getKey(), listFilter);
                } else if (filterDetail.getType().equals("SeekStep") || filterDetail.getType().equals("SeekRange")) {
                    filter_tab_content = (LinearLayout) layoutInflater.inflate(R.layout.filter_tab_range_layout, null);
                    TextView tvTittle = filter_tab_content.findViewById(R.id.tvTittle);
                    final TextView tvSelection = filter_tab_content.findViewById(R.id.tvSelection);
                    TextView tvMinText = filter_tab_content.findViewById(R.id.tvMinText);
                    TextView tvMaxText = filter_tab_content.findViewById(R.id.tvMaxText);
                    VerticalRangeSeekBar sbSingle = filter_tab_content.findViewById(R.id.sbSingle);
                    VerticalRangeSeekBar sbRange = filter_tab_content.findViewById(R.id.sbRange);

                    tvTittle.setText(filterDetail.getTitle());
                    tvSelection.setText("");
                    tvMinText.setText(filterDetail.getMinDisplayText());
                    tvMaxText.setText(filterDetail.getMaxDisplayText());

                    if (filterDetail.getType().equals("SeekStep")) {
                        sbRange.setVisibility(View.GONE);

                        sbSingle.setOnRangeChangedListener(new OnRangeChangedListener() {
                            @Override
                            public void onRangeChanged(RangeSeekBar rangeSeekBar, float leftValue, float rightValue, boolean isFromUser) {
                                float rangeSelect = (filterDetail.getMax() / 100) * leftValue;
                                String strRangeSelect;

                                if (rangeSelect == filterDetail.getMin()) {
                                    strRangeSelect = filterDetail.getMinText();
                                } else if (rangeSelect == filterDetail.getMax()) {
                                    strRangeSelect = filterDetail.getMaxText();
                                } else {
                                    strRangeSelect = String.format(filterDetail.getDecimalFormat(), rangeSelect);
                                }
                                strRangeSelect = filterDetail.getPrefixText() + strRangeSelect + filterDetail.getSuffixText();
                                tvSelection.setText(strRangeSelect);
                            }

                            @Override
                            public void onStartTrackingTouch(RangeSeekBar rangeSeekBar, boolean isLeft) {

                            }

                            @Override
                            public void onStopTrackingTouch(RangeSeekBar rangeSeekBar, boolean isLeft) {

                            }
                        });

                        if (filterDetail.getDefaultValue().size() == 1) {
                            float rangeSelect = (100 / filterDetail.getMax()) * Float.valueOf(filterDetail.getDefaultValue().get(0));
                            //  sbSingle.setProgress(rangeSelect);
                        }

                        filterControl.put(filterDetail.getKey(), sbSingle);
                    } else if (filterDetail.getType().equals("SeekRange")) {
                        sbSingle.setVisibility(View.GONE);

                        sbRange.setOnRangeChangedListener(new OnRangeChangedListener() {
                            @Override
                            public void onRangeChanged(RangeSeekBar rangeSeekBar, float leftValue, float rightValue, boolean isFromUser) {
                                float minRangeSelect = (filterDetail.getMax() / 100) * leftValue;
                                float maxRangeSelect = (filterDetail.getMax() / 100) * rightValue;
                                String strMinRangeSelect;
                                String strMaxRangeSelect;

                                if (minRangeSelect == filterDetail.getMin()) {
                                    strMinRangeSelect = filterDetail.getMinText();
                                } else {
                                    strMinRangeSelect = String.format(filterDetail.getDecimalFormat(), minRangeSelect);
                                }
                                if (maxRangeSelect == filterDetail.getMax()) {
                                    strMaxRangeSelect = filterDetail.getMaxText();
                                } else {
                                    strMaxRangeSelect = String.format(filterDetail.getDecimalFormat(), maxRangeSelect);
                                }

                                strMinRangeSelect = filterDetail.getPrefixText() + strMinRangeSelect + filterDetail.getSuffixText();
                                strMaxRangeSelect = filterDetail.getPrefixText() + strMaxRangeSelect + filterDetail.getSuffixText();

                                tvSelection.setText(strMinRangeSelect + " - " + strMaxRangeSelect);
                            }

                            @Override
                            public void onStartTrackingTouch(RangeSeekBar rangeSeekBar, boolean isLeft) {

                            }

                            @Override
                            public void onStopTrackingTouch(RangeSeekBar rangeSeekBar, boolean isLeft) {

                            }
                        });


                        if (filterDetail.getDefaultValue().size() == 2) {
                            float minRangeSelect = (100 / filterDetail.getMax()) * Float.valueOf(filterDetail.getDefaultValue().get(0));
                            float maxRangeSelect = (100 / filterDetail.getMax()) * Float.valueOf(filterDetail.getDefaultValue().get(1));
                            sbRange.setProgress(minRangeSelect, maxRangeSelect);
                        }

                        filterControl.put(filterDetail.getKey(), sbRange);
                    }
                }

                filter_tab_button.setId(tabCount);
                filter_tab_button.setTag(filterDetail.getKey());
                lyFilterTab.addView(filter_tab_button);

                filter_tab_content.setId(tabCount);
                filter_tab_content.setTag(filterDetail.getKey());
                filter_tab_content.setVisibility(View.GONE);
                lyFilterTabContent.addView(filter_tab_content);

                tabCount++;
            }


            ActiveTabButton = lyFilterTab.findViewById(activeTabIndex);
            ActiveTabContent = lyFilterTabContent.findViewById(activeTabIndex);

            ActiveTabButton.setBackgroundResource(R.drawable.filter_tab_selected);
            ActiveTabTitle = ActiveTabButton.findViewById(R.id.tvTabTile);
            ActiveTabSubTitle = ActiveTabButton.findViewById(R.id.tvTabSubTitle);

            ActiveTabContent.setVisibility(View.VISIBLE);
        }
    }


    public int getTabCount() {
        return tabCount;
    }

    public void setTabCount(int tabCount) {
        this.tabCount = tabCount;
    }

    public int getActiveTabIndex() {
        return activeTabIndex;
    }

    public void setActiveTabIndex(int activeTabIndex) {
        this.activeTabIndex = activeTabIndex;
    }

    public List<FilterDetail> getFilterDetails() {
        return filterDetails;
    }


    public LinearLayout getActiveTabButton() {
        return ActiveTabButton;
    }

    public void setActiveTabButton(LinearLayout activeTabButton) {
        ActiveTabButton = activeTabButton;
    }

    public LinearLayout getActiveTabContent() {
        return ActiveTabContent;
    }

    public void setActiveTabContent(LinearLayout activeTabContent) {
        ActiveTabContent = activeTabContent;
    }

    public TextView getActiveTabTitle() {
        return ActiveTabTitle;
    }

    public void setActiveTabTitle(TextView activeTabTitle) {
        ActiveTabTitle = activeTabTitle;
    }

    public TextView getActiveTabSubTitle() {
        return ActiveTabSubTitle;
    }

    public void setActiveTabSubTitle(TextView activeTabSubTitle) {
        ActiveTabSubTitle = activeTabSubTitle;
    }

    public void setOnApplyClickListener(OnApplyClickListener listener) {
        this.onApplyClickListener = listener;
    }

    public void setOnClearClickListener(OnClearClickListener listener) {
        this.onClearClickListener = listener;
    }

    public HashMap<String, List<String>> getFilter() {
        return filter;
    }

    public void setFilter(HashMap<String, List<String>> filter) {
        this.filter = filter;
    }

    public int getSingleChoiceSelectedItemCount() {
        return singleChoiceSelectedItemCount;
    }

    public void setSingleChoiceSelectedItemCount(int singleChoiceSelectedItemCount) {
        this.singleChoiceSelectedItemCount = singleChoiceSelectedItemCount;
    }

    public int getMultipleChoiceSelectedItemCount() {
        return multipleChoiceSelectedItemCount;
    }

    public void setMultipleChoiceSelectedItemCount(int multipleChoiceSelectedItemCount) {
        this.multipleChoiceSelectedItemCount = multipleChoiceSelectedItemCount;
    }


    public interface OnApplyClickListener {
        public void onClick(View view);
    }

    public interface OnClearClickListener {
        public void onClick(View view);
    }
}
