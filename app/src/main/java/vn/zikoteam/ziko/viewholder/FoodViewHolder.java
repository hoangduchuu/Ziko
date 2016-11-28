package vn.zikoteam.ziko.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.zikoteam.ziko.R;
import vn.zikoteam.ziko.model.Food;

/**
 * Created by dk-darkness on 27/11/2016.
 */

public class FoodViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imgFood)
    ImageView imgFood;
    @BindView(R.id.tvNameFood)
    TextView tvNameFood;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvFavorite)
    TextView tvFavorite;
    @BindView(R.id.imgAvatar)
    ImageView imgAvatar;
    @BindView(R.id.tvNameUser)
    TextView tvNameUser;

    public FoodViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindToPost(Food food, View.OnClickListener starClickListener) {
        Picasso.with(itemView.getContext()).load(food.getImageFood()).into(imgFood);
        Picasso.with(itemView.getContext()).load(food.getUserAvatar()).into(imgAvatar);
        tvNameFood.setText(food.getNameFood());
        tvAddress.setText(food.getAddressFood());
        if (food.getFavoriteFood() == 0) {
            tvFavorite.setVisibility(View.GONE);
        } else {
            tvFavorite.setText(food.getFavoriteFood());
        }
        tvNameUser.setText(food.getUserName());

        imgAvatar.setOnClickListener(starClickListener);
        tvNameUser.setOnClickListener(starClickListener);
    }
}
