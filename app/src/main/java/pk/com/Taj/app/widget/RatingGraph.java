package pk.com.Taj.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import pk.com.Taj.app.R;

public class RatingGraph extends LinearLayout {

    private TextView tvAvgRating;
    private TextView tvTotalReviews;
    private TextView tv5Star;
    private TextView tv4Star;
    private TextView tv3Star;
    private TextView tv2Star;
    private TextView tv1Star;
    private TextView tv5StarPercent;
    private TextView tv4StarPercent;
    private TextView tv3StarPercent;
    private TextView tv2StarPercent;
    private TextView tv1StarPercent;
    private View bar5Star;
    private View bar4Star;
    private View bar3Star;
    private View bar2Star;
    private View bar1Star;


    public RatingGraph(Context context) {
        super(context);
    }

    public RatingGraph(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createView();
    }

    public void setAvgRating(String avgRating) {
        tvAvgRating.setText(avgRating);
    }


    public void setTotalReviews(String reviews) {
        tvTotalReviews.setText(reviews);
    }


    public void setStar(int fiveStar, int fourStar, int threeStar, int twoStar, int oneStar) {
        int fiveStarPer = 0, fourStarPer = 0, threeStarPer = 0, twoStarPer = 0, oneStarPer = 0;
        float totalStar = fiveStar + fourStar + threeStar + twoStar + oneStar;

        if (totalStar > 0) {
            fiveStarPer = (int)((fiveStar / totalStar) * 100);
            fourStarPer = (int)((fourStar / totalStar) * 100);
            threeStarPer = (int)((threeStar / totalStar) * 100);
            twoStarPer = (int)((twoStar / totalStar) * 100);
            oneStarPer = (int)((oneStar / totalStar) * 100);
        }

        tv5StarPercent.setText(String.valueOf(fiveStarPer) + "%");
        tv4StarPercent.setText(String.valueOf(fourStarPer) + "%");
        tv3StarPercent.setText(String.valueOf(threeStarPer) + "%");
        tv2StarPercent.setText(String.valueOf(twoStarPer) + "%");
        tv1StarPercent.setText(String.valueOf(oneStarPer) + "%");

        ((LinearLayout.LayoutParams) bar5Star.getLayoutParams()).weight = fiveStarPer;
        ((LinearLayout.LayoutParams) bar4Star.getLayoutParams()).weight = fourStarPer;
        ((LinearLayout.LayoutParams) bar3Star.getLayoutParams()).weight = threeStarPer;
        ((LinearLayout.LayoutParams) bar2Star.getLayoutParams()).weight = twoStarPer;
        ((LinearLayout.LayoutParams) bar1Star.getLayoutParams()).weight = oneStarPer;
    }

    private void createView() {
        inflate(getContext(), R.layout.rating_graph, this);

        tvAvgRating = findViewById(R.id.tvAvgRating);
        tvTotalReviews = findViewById(R.id.tvTotalReviews);
        tv5Star = findViewById(R.id.tv5Star);
        tv4Star = findViewById(R.id.tv4Star);
        tv3Star = findViewById(R.id.tv3Star);
        tv2Star = findViewById(R.id.tv2Star);
        tv1Star = findViewById(R.id.tv1Star);
        tv5StarPercent = findViewById(R.id.tv5StarPercent);
        tv4StarPercent = findViewById(R.id.tv4StarPercent);
        tv3StarPercent = findViewById(R.id.tv3StarPercent);
        tv2StarPercent = findViewById(R.id.tv2StarPercent);
        tv1StarPercent = findViewById(R.id.tv1StarPercent);
        bar5Star = findViewById(R.id.bar5Star);
        bar4Star = findViewById(R.id.bar4Star);
        bar3Star = findViewById(R.id.bar3Star);
        bar2Star = findViewById(R.id.bar2Star);
        bar1Star = findViewById(R.id.bar1Star);

        setAvgRating("0");
        setTotalReviews("0");
        setStar(0, 0, 0, 0, 0);
    }
}
