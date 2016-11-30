package vn.zikoteam.ziko.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

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

public class OrderActivity extends AppCompatActivity {
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

    @BindView(R.id.editNameKh)
    EditText editNameKh;
    @BindView(R.id.editPhoneKh)
    EditText editPhoneKh;
    @BindView(R.id.editLocationKh)
    EditText editLocationKh;

    @BindView(R.id.vShipper)
    LinearLayout vShipper;
    @BindView(R.id.imgAvatarShipper)
    ImageView imgAvatarShipper;
    @BindView(R.id.tvNameShipper)
    TextView tvNameShipper;

    @BindView(R.id.pgLoading)
    ProgressBar pgLoading;

    private DatabaseReference mDatabaseReference;
    private DatabaseReference mOrderDatabaseReference;
    private DatabaseReference mUserDatabaseReference;
    private FirebaseUser mFirebaseUser;
    private ValueEventListener mPostListener;
    private ValueEventListener mUserListener;
    private int tax = 15000;
    private Shipper mShipper;
    private String strImgFood;
    private String strIdFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);

        setUpToolbar();
        setUpFirebase();
    }

    private void setUpFirebase() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child(Constant.FB_KEY_FOOD).child(getKeyFood());
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mOrderDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void setUpToolbar() {
        tvNameToolbar.setText("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(3.0f);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @OnClick(R.id.btnAddOrder)
    public void eventAddOrder(View view) {
        doCreateOrder();
    }

    private void doCreateOrder() {

        String nameFood = editNameFood.getText().toString().trim();
        String addressFood = editLocation.getText().toString().trim();
        String priceFood = editPrice.getText().toString().trim();

        String idKh = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String nameKh = editNameKh.getText().toString().trim();
        String phoneKh = editPhoneKh.getText().toString().trim();
        String locationKh = editLocationKh.getText().toString().trim();

        if (TextUtils.isEmpty(nameKh)) {
            Toast.makeText(OrderActivity.this, "Name Customer Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(phoneKh)) {
            Toast.makeText(OrderActivity.this, "Phone Customer Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(locationKh)) {
            Toast.makeText(OrderActivity.this, "Location Customer Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (vShipper.getVisibility() == View.GONE) {
            Toast.makeText(OrderActivity.this, "Select Shipper", Toast.LENGTH_SHORT).show();
            return;
        }
        pgLoading.setVisibility(View.VISIBLE);
        String key = mOrderDatabaseReference.child(Constant.FB_KEY_ORDER).push().getKey();
        Order mOrder = new Order(key, strIdFood, strImgFood, nameFood, addressFood
                , priceFood, idKh, nameKh, phoneKh, locationKh, 0, "",
                mShipper.getShipperID(), mShipper.getAvatar(), mShipper.getName(), 1);

        Map<String, Object> orderValue = mOrder.toMap();

        Map<String, Object> childUpdate = new HashMap<>();
        childUpdate.put("/" + Constant.FB_KEY_ORDER + "/" + key, orderValue);
        childUpdate.put("/" + Constant.FB_KEY_USER_ORDER + "/" +
                FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + key, orderValue);
        childUpdate.put("/" + Constant.FB_KEY_SHIPPER_ORDER_NEW + "/" + mShipper.getShipperID() + "/" + key, orderValue);

        mOrderDatabaseReference.updateChildren(childUpdate);
        pgLoading.setVisibility(View.GONE);
        Toast.makeText(OrderActivity.this, "Order Susscess", Toast.LENGTH_SHORT).show();
        redirectMain();

    }

    private void redirectMain() {
        Intent mIntent = new Intent(OrderActivity.this, MainActivity.class);
        startActivity(mIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Food mFood = dataSnapshot.getValue(Food.class);

                tvNameToolbar.setText(mFood.getNameFood());
                strImgFood = mFood.getImageFood();
                Picasso.with(OrderActivity.this).load(mFood.getImageFood()).into(imgFood);
                editNameFood.setText(mFood.getNameFood());
                editPrice.setText(mFood.getPriceFood());
                editLocation.setText(mFood.getAddressFood());
                editTax.setText("Price Tax: " + tax);
                int total = Integer.parseInt(mFood.getPriceFood()) + tax;
                editTotal.setText("Price Total: " + total);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(OrderActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User mUser = dataSnapshot.getValue(User.class);

                editNameKh.setText(mUser.getName());
                editPhoneKh.setText(mUser.getPhoneNumber());
                editLocationKh.setText(mUser.getAddress());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabaseReference.addValueEventListener(postListener);
        mUserDatabaseReference.child(Constant.FB_KEY_USER).
                child(mFirebaseUser.getUid()).addValueEventListener(userListener);

        mPostListener = postListener;
        mUserListener = userListener;

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPostListener != null) {
            mDatabaseReference.removeEventListener(mPostListener);
        }
        if (mUserListener != null) {
            mUserDatabaseReference.removeEventListener(mUserListener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private String getKeyFood() {
        Bundle extras = getIntent().getExtras();
        return extras.getString(Constant.EXTRA_FOOD_ORDER_KEY);
    }

    @OnClick(R.id.btnAddShipper)
    public void eventAddShiper() {
        Intent mIntent = new Intent(OrderActivity.this, ShowShipperActivity.class);
        startActivityForResult(mIntent, Constant.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.RESULT_SELECT_SHIPPER && requestCode == Constant.REQUEST_CODE) {
            mShipper = (Shipper) data.getSerializableExtra(Constant.KEY_DATA);
            Picasso.with(OrderActivity.this).load(mShipper.getAvatar())
                    .transform(new CircleTransform()).into(imgAvatarShipper);
            tvNameShipper.setText(mShipper.getName());
            vShipper.setVisibility(View.VISIBLE);
        }
    }
}
