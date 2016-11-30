package vn.zikoteam.ziko.viewholder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.zikoteam.ziko.R;
import vn.zikoteam.ziko.customui.CircleTransform;
import vn.zikoteam.ziko.model.Food;
import vn.zikoteam.ziko.model.Order;

/**
 * Created by dk-darkness on 30/11/2016.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imgFood)
    ImageView imgFood;
    @BindView(R.id.tvNameFood)
    TextView tvNameFood;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvStatus)
    TextView tvStatus;

    public OrderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindToPost(Order order) {
        Picasso.with(itemView.getContext()).load(order.getImgFood()).into(imgFood);
        Log.d("ImageFood", order.getImgFood());
        tvNameFood.setText(order.getNamFood());
        int price = Integer.parseInt(order.getPriceFood())+ 15000;
        tvPrice.setText(price+"");
        if(order.getStatus() == 1) {
            tvStatus.setText("Đang vận chuyển.");
        }else {
            tvStatus.setText("Giao dịch thành công.");
        }
    }

}
