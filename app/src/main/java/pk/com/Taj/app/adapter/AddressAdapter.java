package pk.com.Taj.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import pk.com.Taj.app.R;
import pk.com.Taj.app.beans.AddressDetail;
import pk.com.Taj.app.widget.IconTextView;

import java.util.List;

public class AddressAdapter extends BaseAdapter {
    Context context;
    private List<AddressDetail> addressList;
    private OnItemClickListener onItemClickListener;

    public AddressAdapter(Context context, List<AddressDetail> addressList) {
        this.context = context;
        this.addressList = addressList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public int getCount() {
        return addressList.size();
    }

    @Override
    public Object getItem(int position) {
        return addressList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.address_row, null);

            TextView tvLabel = convertView.findViewById(R.id.tvLabel);
            IconTextView btnDelete = convertView.findViewById(R.id.btnDelete);
            TextView tvCompleteAddress = convertView.findViewById(R.id.tvAddress);

            viewHolder = new ViewHolder(tvLabel, btnDelete, tvCompleteAddress);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (onItemClickListener != null) {

            viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onItemClickListener.onDeleteClick(view, position, view.getId());
                }
            });
        } else {
            viewHolder.btnDelete.setVisibility(View.GONE);
        }
        viewHolder.tvLabel.setText(addressList.get(position).getLabel());
        viewHolder.tvAddress.setText(addressList.get(position).getAddress());

        return convertView;
    }


    private class ViewHolder {
        TextView tvLabel;
        IconTextView btnDelete;
        TextView tvAddress;

        public ViewHolder(TextView tvLabel, IconTextView btnDelete, TextView tvAddress) {
            this.tvLabel = tvLabel;
            this.btnDelete = btnDelete;
            this.tvAddress = tvAddress;
        }
    }


    public interface OnItemClickListener {
        public void onDeleteClick(View view, int position, long id);
    }

}
