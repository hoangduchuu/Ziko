package vn.zikoteam.ziko.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.zikoteam.ziko.R;
import vn.zikoteam.ziko.customui.CircleTransform;
import vn.zikoteam.ziko.model.Shipper;

/**
 * Created by dk-darkness on 30/11/2016.
 */

public class ShipperViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imgAvatarShipper)
    ImageView imgAvatarShipper;
    @BindView(R.id.tvNameShipper)
    TextView tvNameShipper;
    @BindView(R.id.imgShowDetails)
    ImageView imgShowDetails;

    public ShipperViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void bindToPost(Shipper shipper, View.OnClickListener starClickListener) {
        Picasso.with(itemView.getContext()).load(shipper.getAvatar())
                .transform(new CircleTransform()).into(imgAvatarShipper);
        tvNameShipper.setText(shipper.getName());

        imgShowDetails.setOnClickListener(starClickListener);
    }
}
