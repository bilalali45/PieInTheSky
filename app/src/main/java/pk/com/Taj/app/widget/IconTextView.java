package pk.com.Taj.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

import pk.com.Taj.app.R;
import pk.com.Taj.app.helper.FontTypeface;

public class IconTextView extends AppCompatTextView {
    private boolean isBrandingIcon, isSolidIcon;

    public IconTextView(Context context) {
        super(context);
    }

    public IconTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IconTextView,
                0, 0);
        isSolidIcon = typedArray.getBoolean(R.styleable.IconTextView_solid_icon, false);
        isBrandingIcon = typedArray.getBoolean(R.styleable.IconTextView_brand_icon, false);
        createView();
    }


    private void createView(){
        setGravity(Gravity.CENTER);
        if (isBrandingIcon)
            setTypeface(FontTypeface.get(getContext(), "fa-brands-400.ttf"));
        else if (isSolidIcon)
            setTypeface(FontTypeface.get(getContext(), "fa-solid-900.ttf"));
        else
            setTypeface(FontTypeface.get(getContext(), "fa-regular-400.ttf"));
    }
}