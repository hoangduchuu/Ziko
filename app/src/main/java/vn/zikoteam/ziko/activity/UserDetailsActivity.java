package vn.zikoteam.ziko.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.zikoteam.ziko.R;
import vn.zikoteam.ziko.customui.CircleTransform;
import vn.zikoteam.ziko.fragment.FoodFriendFragment;
import vn.zikoteam.ziko.model.User;
import vn.zikoteam.ziko.other.Constant;

public class UserDetailsActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvNameToolbar)
    TextView tvNameToolbar;
    @BindView(R.id.imgAvatar)
    ImageView imgAvatar;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvLocation)
    TextView tvLocation;


    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);

        setUpToolbar();
        setUpFragment();
        setUpFirebase();
        setUpData();
    }

    private void setUpData(){
        mDatabaseReference.child(Constant.FB_KEY_USER).child(getKeyUser())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User mUser = dataSnapshot.getValue(User.class);

                tvNameToolbar.setText(mUser.getName());
                Picasso.with(UserDetailsActivity.this).load(mUser.getAvatar())
                        .transform(new CircleTransform()).into(imgAvatar);
                tvName.setText(mUser.getName());
                tvLocation.setText("Hồ Chí Minh");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUpFirebase() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void setUpFragment() {
        FoodFriendFragment foodFriendFragment = new FoodFriendFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.EXTRA_FRAGMENT_KEY, getKeyUser());
        foodFriendFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.vShowList, foodFriendFragment).commit();
    }

    private String getKeyUser() {
        Bundle extras = getIntent().getExtras();
        return extras.getString(Constant.EXTRA_USER_KEY);
    }

    private void setUpToolbar() {
        tvNameToolbar.setText("User Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(3.0f);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}
