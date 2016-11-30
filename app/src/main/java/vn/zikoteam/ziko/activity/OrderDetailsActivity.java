package vn.zikoteam.ziko.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.zikoteam.ziko.R;
import vn.zikoteam.ziko.customui.CircleTransform;
import vn.zikoteam.ziko.model.Food;
import vn.zikoteam.ziko.model.Order;
import vn.zikoteam.ziko.model.Shipper;
import vn.zikoteam.ziko.model.User;
import vn.zikoteam.ziko.other.Constant;

public class OrderDetailsActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvNameToolbar)
    TextView tvNameToolbar;

    @BindView(R.id.imgFood)
    ImageView imgFood;
    @BindView(R.id.editNameFood)
    EditText editNameFood;
    @BindView(R.id.editPrice)
    EditText editPrice;
    @BindView(R.id.editLocation)
    EditText editLocation;
    @BindView(R.id.editTax)
    EditText editTax;
    @BindView(R.id.editTotal)
    EditText editTotal;

    @BindView(R.id.tvNameShipper)
    TextView tvNameShipper;
    @BindView(R.id.editStatus)
    EditText editStatus;
    @BindView(R.id.imgAvatarShipper)
    ImageView imgAvatarShipper;

    @BindView(R.id.vComment)
    LinearLayout vComment;
    @BindView(R.id.editComment)
    EditText editComment;

    private DatabaseReference mDatabaseReference;
    private ValueEventListener mPostListener;
    private String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);

        setUpToolbar();
        setUpFirebase();
        loadData();
    }

    private void setUpFirebase() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Constant.FB_KEY_USER_ORDER)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getKeyOrder());
    }

    private void setUpToolbar() {
        tvNameToolbar.setText("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(3.0f);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void loadData() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Order mOrder = dataSnapshot.getValue(Order.class);

                tvNameToolbar.setText(mOrder.getNamFood());
                Picasso.with(OrderDetailsActivity.this).load(mOrder.getImgFood()).into(imgFood);
                editNameFood.setText(mOrder.getNamFood());
                editPrice.setText(mOrder.getPriceFood());
                editLocation.setText(mOrder.getAddressFood());
                editTax.setText("Price Tax: " + 15000);
                int total = Integer.parseInt(mOrder.getPriceFood()) + 15000;
                editTotal.setText("Price Total: " + total);
                tvNameShipper.setText(mOrder.getNameShipper());
                Picasso.with(OrderDetailsActivity.this).load(mOrder.getAvatarShipper())
                        .transform(new CircleTransform()).into(imgAvatarShipper);
                if(mOrder.getStatus() == 1){
                    editStatus.setText("Dang van chuyen");
                }else {
                    editStatus.setText("Giao dich thanh cong");
                }

                if(mOrder.getConnentReview() == "" || mOrder.getConnentReview() == null){
                    vComment.setVisibility(View.GONE);
                }else {
                    vComment.setVisibility(View.VISIBLE);
                    editComment.setText(mOrder.getConnentReview());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(OrderDetailsActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        mDatabaseReference.addValueEventListener(postListener);

        mPostListener = postListener;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.RESULT_SELECT_REVIEW &&
                requestCode == Constant.REQUEST_CODE_REVIEWSHIPPER) {
            content = (String) data.getSerializableExtra(Constant.KEY_DATA_REVIEW);
            vComment.setVisibility(View.VISIBLE);
            editComment.setText(content);
        }
    }

    @OnClick(R.id.btnReviewShipper)
    public void eventReviewShiper(View view) {
        Intent mIntent = new Intent(OrderDetailsActivity.this, ReviewShipperActivity.class);
        mIntent.putExtra(Constant.KEY_DATA_ORDER, getKeyOrder());
        startActivityForResult(mIntent, Constant.REQUEST_CODE_REVIEWSHIPPER);
    }

    private String getKeyOrder() {
        Bundle extras = getIntent().getExtras();
        return extras.getString(Constant.EXTRA_ORDER_KEY);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPostListener != null) {
            mDatabaseReference.removeEventListener(mPostListener);
        }
    }


}
