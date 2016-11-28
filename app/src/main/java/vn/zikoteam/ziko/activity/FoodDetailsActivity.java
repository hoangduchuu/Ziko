package vn.zikoteam.ziko.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.zikoteam.ziko.R;
import vn.zikoteam.ziko.other.Constant;

public class FoodDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvNameToolbar)
    TextView tvNameToolbar;

    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        ButterKnife.bind(this);
        setUpToolbar();

        setUpFirebase();
        setUpData();
    }

    private void setUpFirebase() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void setUpData() {

    }

    private String getKeyFood() {
        Bundle extras = getIntent().getExtras();
        return extras.getString(Constant.EXTRA_FOOD_KEY);
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
