package pk.com.Taj.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import pk.com.Taj.app.R;

public class RoundCornerImageView extends android.support.v7.widget.AppCompatImageView  {

    private float radius = 18.0f;
    private Path path;
    private RectF rect;

    public RoundCornerImageView(Context context) {
        super(context);
        init();
    }

    public RoundCornerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundCornerImageView,
                0, 0);
        radius = getPixelSize(typedArray.getDimensionPixelSize(R.styleable.RoundCornerImageView_image_radius, 18));

        init();
    }

    private void init() {
        path = new Path();

    }

    private float getPixelSize(float dimens) {
        float scaleRatio = getResources().getDisplayMetrics().density;
        return dimens / scaleRatio;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        path.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}