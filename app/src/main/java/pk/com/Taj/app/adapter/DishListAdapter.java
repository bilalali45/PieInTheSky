package pk.com.Taj.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import pk.com.Taj.app.beans.DishDetail;
import pk.com.Taj.app.R;
import pk.com.Taj.app.helper.CommonUtils;
import pk.com.Taj.app.widget.RoundCornerImageView;

import java.util.List;

public class DishListAdapter extends BaseAdapter {
    Context context;
    private List<DishDetail> dishDetailList;

    public DishListAdapter(Context context, List<DishDetail> dishDetailList) {
        this.context = context;
        this.dishDetailList = dishDetailList;
    }

    @Override
    public int getCount() {
        return dishDetailList.size();
    }

    @Override
    public Object getItem(int position) {
        return dishDetailList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.dish_detail_row, null);

            RoundCornerImageView ivDishImage = convertView.findViewById(R.id.ivDishImage);
            TextView tvDishName = convertView.findViewById(R.id.tvDishName);
            TextView tvDishPrice = convertView.findViewById(R.id.tvDishPrice);
            ImageView ivAdd = convertView.findViewById(R.id.ivAdd);

            viewHolder = new ViewHolder(ivDishImage, tvDishName, tvDishPrice, ivAdd);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(context)
                .load(dishDetailList.get(position).getImageURL())
                .centerCrop()
                .placeholder(R.drawable.image_dish_loading)
                .into(viewHolder.ivDishImage);

        viewHolder.tvDishName.setText(dishDetailList.get(position).getDishName());
        viewHolder.tvDishPrice.setText("Rs. " + CommonUtils.formatTwoDecimal(dishDetailList.get(position).getTotalPrice()));

        if(dishDetailList.get(position).isSelected())
        {
            viewHolder.ivAdd.setImageResource(R.drawable.add_cart_fill);
        }
        else
        {
            viewHolder.ivAdd.setImageResource(R.drawable.add_cart);
        }

        return convertView;
    }


    public class ViewHolder {
        RoundCornerImageView ivDishImage;
        TextView tvDishName;
        TextView tvDishPrice;
        ImageView ivAdd;

        public ViewHolder(RoundCornerImageView ivDishImage, TextView tvDishName, TextView tvDishPrice, ImageView ivAdd) {
            this.ivDishImage = ivDishImage;
            this.tvDishName = tvDishName;
            this.tvDishPrice = tvDishPrice;
            this.ivAdd = ivAdd;

        }
    }
}