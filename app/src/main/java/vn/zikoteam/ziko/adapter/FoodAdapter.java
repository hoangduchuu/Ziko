package vn.zikoteam.ziko.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.zikoteam.ziko.R;
import vn.zikoteam.ziko.model.Food;

/**
 * Created by dk-darkness on 28/11/2016.
 */

public class FoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Food> mFoodList;

    public FoodAdapter() {
        this.mFoodList = new ArrayList<>();
    }

    public void setFood(List<Food> mFoodList) {
        mFoodList.clear();
        mFoodList.addAll(mFoodList);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Food food = mFoodList.get(position);
        ViewHolder holder = (ViewHolder) viewHolder;

        Picasso.with(holder.itemView.getContext()).load(food.getImageFood()).into(holder.imgFood);
        holder.tvNameFood.setText(food.getNameFood());
        holder.tvAddress.setText(food.getAddressFood());
        holder.tvNameUser.setText(food.getUserName());
        Picasso.with(holder.itemView.getContext()).load(food.getUserAvatar()).into(holder.imgAvatar);
        if (food.getFavoriteFood() == 0) {
            holder.tvFavorite.setVisibility(View.GONE);
        } else {
            holder.tvFavorite.setText(String.valueOf(food.getFavoriteFood()));
        }
    }

    @Override
    public int getItemCount() {
        return mFoodList == null ? 0 : mFoodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
