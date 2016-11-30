package vn.zikoteam.ziko.activity;

import android.content.Intent;
import android.os.RecoverySystem;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.zikoteam.ziko.R;
import vn.zikoteam.ziko.customui.CircleTransform;
import vn.zikoteam.ziko.fragment.FoodFriendFragment;
import vn.zikoteam.ziko.model.Food;
import vn.zikoteam.ziko.model.User;
import vn.zikoteam.ziko.other.Constant;
import vn.zikoteam.ziko.viewholder.FoodViewHolder;

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
    private RecyclerView listFoodFriend;


    private DatabaseReference mDatabaseReference;
    private DatabaseReference mFoodDatabaseReference;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter<Food, FoodViewHolder> mAdapter;
    private String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);

        setIdUser();
        setUpToolbar();
        setUpFirebase();
        setUpData();
        setUpListFood();
    }

    private void setUpData() {
        mDatabaseReference.child(Constant.FB_KEY_USER).child(idUser)
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

    private void setUpListFood() {
        listFoodFriend = (RecyclerView) findViewById(R.id.listFoodFriend);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        listFoodFriend.setLayoutManager(layoutManager);
        listFoodFriend.setHasFixedSize(true);

        Query foodQuery = mFoodDatabaseReference;
        mAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.item_food,
                FoodViewHolder.class, foodQuery) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, final Food model, int position) {
                final DatabaseReference postRef = getRef(position);
                final String postKey = postRef.getKey();

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserDetailsActivity.this, FoodDetailsActivity.class);
                        intent.putExtra(Constant.EXTRA_FOOD_KEY, postKey);
                        startActivity(intent);
                    }
                });

                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        
                    }
                });
            }

            @Override
            protected void onCancelled(DatabaseError error) {
                super.onCancelled(error);
            }
        };
        listFoodFriend.setAdapter(mAdapter);
    }

    private void setUpFirebase() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFoodDatabaseReference = FirebaseDatabase.getInstance().getReference()
                .child(Constant.FB_KEY_USER_FOOD).child(idUser);
    }

    private void setIdUser() {
        Bundle extras = getIntent().getExtras();
        idUser = extras.getString(Constant.EXTRA_USER_KEY);
    }

    private void setUpToolbar() {
        tvNameToolbar.setText("");
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
