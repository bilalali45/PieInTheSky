package pk.com.Taj.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import pk.com.Taj.app.R;
import pk.com.Taj.app.beans.ReservationDetail;
import pk.com.Taj.app.widget.RoundCornerImageView;

import java.util.List;


public class ReservationAdapter extends BaseAdapter {
    Context context;
    List<ReservationDetail> reservationDetailList;

    public ReservationAdapter(Context context, List<ReservationDetail> reservationDetailList) {
        this.context = context;
        this.reservationDetailList = reservationDetailList;
    }

    @Override
    public int getCount() {
        return reservationDetailList.size();
    }

    @Override
    public Object getItem(int position) {
        return reservationDetailList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.reservation_row, null);

            RoundCornerImageView ivPlaceImage = convertView.findViewById(R.id.ivPlaceImage);
            TextView tvPlaceName = convertView.findViewById(R.id.tvPlaceName);
            TextView tvPlaceLocation = convertView.findViewById(R.id.tvPlaceLocation);
            TextView tvReservationNo = convertView.findViewById(R.id.tvReservationNo);
            TextView tvReservationDate = convertView.findViewById(R.id.tvReservationDate);
            TextView tvByName = convertView.findViewById(R.id.tvByName);
            TextView tvStatus = convertView.findViewById(R.id.tvStatus);

            viewHolder = new ViewHolder(ivPlaceImage, tvPlaceName, tvPlaceLocation, tvReservationNo, tvReservationDate, tvByName, tvStatus);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(context)
                .load(reservationDetailList.get(position).getPlaceImageURL())
                .centerCrop()
                .placeholder(R.drawable.image_place_loading)
                .into(viewHolder.ivPlaceImage);

        viewHolder.tvPlaceName.setText(reservationDetailList.get(position).getPlaceName());
        viewHolder.tvPlaceLocation.setText(reservationDetailList.get(position).getPlaceLocation());
        viewHolder.tvReservationNo.setText(String.valueOf(reservationDetailList.get(position).getReservationNo()));
        viewHolder.tvReservationDate.setText(reservationDetailList.get(position).getReservationDate());
        viewHolder.tvByName.setText(reservationDetailList.get(position).getByName());
        viewHolder.tvStatus.setText(reservationDetailList.get(position).getReservationStatus());

        return convertView;
    }


    private class ViewHolder {
        RoundCornerImageView ivPlaceImage;
        TextView tvPlaceName;
        TextView tvPlaceLocation;
        TextView tvReservationNo;
        TextView tvReservationDate;
        TextView tvByName;
        TextView tvStatus;

        public ViewHolder(RoundCornerImageView ivPlaceImage, TextView tvPlaceName, TextView tvPlaceLocation, TextView tvReservationNo, TextView tvReservationDate, TextView tvByName, TextView tvStatus) {
            this.ivPlaceImage = ivPlaceImage;
            this.tvPlaceName = tvPlaceName;
            this.tvPlaceLocation = tvPlaceLocation;
            this.tvReservationNo = tvReservationNo;
            this.tvReservationDate = tvReservationDate;
            this.tvByName = tvByName;
            this.tvStatus = tvStatus;
        }
    }

}