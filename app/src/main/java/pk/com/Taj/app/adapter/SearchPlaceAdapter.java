package pk.com.Taj.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import pk.com.Taj.app.R;
import pk.com.Taj.app.beans.PlaceOverview;

import java.util.List;

public class SearchPlaceAdapter extends BaseAdapter {
    Context context;
    private List<PlaceOverview> searchPlaceList;

    public SearchPlaceAdapter(Context context, List<PlaceOverview> searchPlaceList) {
        this.context = context;
        this.searchPlaceList = searchPlaceList;
    }

    @Override
    public int getCount() {
        return searchPlaceList.size();
    }

    @Override
    public Object getItem(int position) {
        return searchPlaceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final SearchPlaceAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.place_search_row, null);

            ImageView ivPlaceImage = convertView.findViewById(R.id.ivPlaceImage);
            TextView tvPlaceName = convertView.findViewById(R.id.tvPlaceName);
            TextView tvTags = convertView.findViewById(R.id.tvTags);
            TextView tvLocation = convertView.findViewById(R.id.tvLocation);
            TextView tvTotalReview = convertView.findViewById(R.id.tvTotalReview);
            TextView tvAvgRating = convertView.findViewById(R.id.tvAvgRating);

            viewHolder = new SearchPlaceAdapter.ViewHolder(ivPlaceImage, tvPlaceName, tvTags, tvLocation, tvTotalReview, tvAvgRating);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SearchPlaceAdapter.ViewHolder) convertView.getTag();
        }

        Glide.with(context)
                .load(searchPlaceList.get(position).getProfileImageURL())
                .centerCrop()
                .placeholder(R.drawable.image_place_loading)
                .into(viewHolder.ivPlaceImage);

        viewHolder.tvPlaceName.setText(searchPlaceList.get(position).getPlaceName());
        viewHolder.tvTags.setText(searchPlaceList.get(position).getCuisines());
        viewHolder.tvLocation.setText(searchPlaceList.get(position).getPlaceLocation());
        viewHolder.tvTotalReview.setText("" + searchPlaceList.get(position).getTotalReview() + "+ Reviews");
        viewHolder.tvAvgRating.setText(searchPlaceList.get(position).getBusinessAvgRating());

        return convertView;
    }


    private class ViewHolder {
        ImageView ivPlaceImage;
        TextView tvPlaceName;
        TextView tvTags;
        TextView tvLocation;
        TextView tvTotalReview;
        TextView tvAvgRating;

        public ViewHolder(ImageView ivPlaceImage, TextView tvPlaceName, TextView tvTags, TextView tvLocation, TextView tvTotalReview, TextView tvAvgRating) {
            this.ivPlaceImage = ivPlaceImage;
            this.tvPlaceName = tvPlaceName;
            this.tvTags = tvTags;
            this.tvLocation = tvLocation;
            this.tvTotalReview = tvTotalReview;
            this.tvAvgRating = tvAvgRating;
        }
    }

}