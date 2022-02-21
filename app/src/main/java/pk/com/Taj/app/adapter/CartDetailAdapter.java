package pk.com.Taj.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import pk.com.Taj.app.R;
import pk.com.Taj.app.beans.OrderChild;
import pk.com.Taj.app.helper.CommonUtils;
import pk.com.Taj.app.widget.IconTextView;

import java.util.List;

public class CartDetailAdapter extends BaseAdapter {
    Context context;
    private List<OrderChild> orderChildList;
    private OnItemClickListener onItemClickListener;

    public CartDetailAdapter(Context context, List<OrderChild> orderChildList) {
        this.context = context;
        this.orderChildList = orderChildList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public int getCount() {
        return orderChildList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderChildList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.cart_detail_row, null);

            TextView tvQuantity = convertView.findViewById(R.id.tvQuantity);
            TextView tvDishName = convertView.findViewById(R.id.tvDishName);
            TextView tvDishPrice = convertView.findViewById(R.id.tvDishPrice);
            TextView tvAmount = convertView.findViewById(R.id.tvAmount);
            IconTextView btnPlus = convertView.findViewById(R.id.btnPlus);
            IconTextView btnMinus = convertView.findViewById(R.id.btnMinus);

            if (onItemClickListener != null) {
                btnPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onPlusClick(view, position, view.getId());


                    }
                });

                btnMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onMinusClick(view, position, view.getId());


                    }
                });


            }


            viewHolder = new ViewHolder(tvQuantity, tvDishName, tvDishPrice, tvAmount, btnPlus, btnMinus);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.tvQuantity.setText(CommonUtils.formatTwoDecimal(orderChildList.get(position).getQuantity()));
        viewHolder.tvDishName.setText(orderChildList.get(position).getDishName());
        viewHolder.tvDishPrice.setText("Rs. " + CommonUtils.formatTwoDecimal(orderChildList.get(position).getPrice()));
        viewHolder.tvAmount.setText("Rs. " + CommonUtils.formatTwoDecimal(orderChildList.get(position).getTotalAmount()));

        return convertView;
    }


    public class ViewHolder {
        TextView tvQuantity;
        TextView tvDishName;
        TextView tvDishPrice;
        TextView tvAmount;
        IconTextView btnPlus;
        IconTextView btnMinus;


        public ViewHolder(TextView tvQuantity, TextView tvDishName, TextView tvDishPrice, TextView tvAmount, IconTextView btnPlus, IconTextView btnMinus) {
            this.tvQuantity = tvQuantity;
            this.tvDishName = tvDishName;
            this.tvDishPrice = tvDishPrice;
            this.tvAmount = tvAmount;
            this.btnPlus = btnPlus;
            this.btnMinus = btnMinus;
        }
    }

    public interface OnItemClickListener {

        public void onPlusClick(View view, int position, long id);

        public void onMinusClick(View view, int position, long id);


    }
}
