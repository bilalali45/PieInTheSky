package pk.com.Taj.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import pk.com.Taj.app.R;
import pk.com.Taj.app.helper.FontTypeface;


public class IconButton extends LinearLayout {
    private TextView textViewIcon;
    private TextView textView;
    private boolean isBrandingIcon, isSolidIcon;
    private String text, icon_name;
    private int textColor;
    private float textSize;

    public IconButton(Context context) {
        super(context);
        createView();
    }

    public IconButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IconButton,
                0, 0);
        isSolidIcon = typedArray.getBoolean(R.styleable.IconButton_button_solidIcon, false);
        isBrandingIcon = typedArray.getBoolean(R.styleable.IconButton_button_brandIcon, false);
        text = typedArray.getString(R.styleable.IconButton_button_text);
        icon_name = typedArray.getString(R.styleable.IconButton_button_iconName);
        textColor = typedArray.getColor(R.styleable.IconButton_button_textColor, Color.BLACK);
        textSize = getPixelSize(typedArray.getDimensionPixelSize(R.styleable.IconButton_button_textSize, 15));
        createView();
    }

    private float getPixelSize(float dimens) {
        float scaleRatio = getResources().getDisplayMetrics().density;
        return dimens / scaleRatio;
    }

    private void createView() {
        textViewIcon = new TextView(getContext());
        textView = new TextView(getContext());


        textViewIcon.setGravity(Gravity.CENTER);
        if (isBrandingIcon)
            textViewIcon.setTypeface(FontTypeface.get(getContext(), "fa-brands-400.ttf"));
        else if (isSolidIcon)
            textViewIcon.setTypeface(FontTypeface.get(getContext(), "fa-solid-900.ttf"));
        else
            textViewIcon.setTypeface(FontTypeface.get(getContext(), "fa-regular-400.ttf"));

        setIcon(icon_name);
        setText(text);

        textViewIcon.setTextColor(textColor);
        textView.setTextColor(textColor);

        textViewIcon.setTextSize(textSize);
        textView.setTextSize(textSize);

        this.addView(textViewIcon);
        this.addView(textView);
    }

    public void setIcon(String icon_name)
    {
        textViewIcon.setText(icon_name);
    }
    public void setText(String text) {
        textView.setText(" " + text);
    }

    public void setTextColor(int color) {
        textColor = color;
        textViewIcon.setTextColor(textColor);
        textView.setTextColor(textColor);
    }
}