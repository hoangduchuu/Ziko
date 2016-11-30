package vn.zikoteam.ziko.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.zikoteam.ziko.R;
import vn.zikoteam.ziko.customui.SimpleDividerItemDecoration;
import vn.zikoteam.ziko.model.Shipper;
import vn.zikoteam.ziko.other.Constant;
import vn.zikoteam.ziko.viewholder.ShipperViewHolder;

public class ShowShipperActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvNameToolbar)
    TextView tvNameToolbar;
    @BindView(R.id.pgLoading)
    ProgressBar pgLoading;
    @BindView(R.id.listShipper)
    RecyclerView listShipper;

    private DatabaseReference mDatabaseReference;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter<Shipper, ShipperViewHolder> mAdapter;
    private Shipper mShipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_shipper);
        ButterKnife.bind(this);

        setUpToolbar();
        setUpFireBase();
        setUpData();
    }

    private void setUpToolbar() {
        tvNameToolbar.setText("Select Shipper");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(3.0f);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setUpData() {
        layoutManager = new LinearLayoutManager(this);
        listShipper.setLayoutManager(layoutManager);
        listShipper.addItemDecoration(new SimpleDividerItemDecoration(this));

        Query shipperQuery = mDatabaseReference.child(Constant.FB_KEY_SHIPPER);
        mAdapter = new FirebaseRecyclerAdapter<Shipper, ShipperViewHolder>(Shipper.class, R.layout.item_shipper,
                ShipperViewHolder.class, shipperQuery) {

            @Override
            protected void onCancelled(DatabaseError error) {
                super.onCancelled(error);
            }

            @Override
            protected void populateViewHolder(ShipperViewHolder viewHolder, final Shipper model, final int position) {
                final DatabaseReference postRef = getRef(position);
                final String postKey = postRef.getKey();

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("IDSHipper", postKey);
                        mShipper  = new Shipper(postKey, model.getName(), model.getEmail(),
                                model.getDateOfBirth(), model.getAddress(), model.getCountLike()
                                , model.getCountDisLike(), model.getPhoneNumber(), model.getAvatar());
                        Intent mIntent = new Intent();
                        mIntent.putExtra(Constant.KEY_DATA, mShipper);
                        setResult(Constant.RESULT_SELECT_SHIPPER, mIntent);
                        finish();
                    }
                });

                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {
                        Intent intent = new Intent(ShowShipperActivity.this, UserDetailsActivity.class);
                        intent.putExtra(Constant.EXTRA_USER_KEY, postKey);
                        startActivity(intent);
                    }
                });
                pgLoading.setVisibility(View.GONE);
            }
        };
        listShipper.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpFireBase() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }
}
