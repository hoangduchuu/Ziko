package vn.zikoteam.ziko.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.zikoteam.ziko.R;
import vn.zikoteam.ziko.other.Constant;

public class ReviewShipperActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvNameToolbar)
    TextView tvNameToolbar;
    @BindView(R.id.editReview)
    EditText editReview;

    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_shipper);
        ButterKnife.bind(this);

        setUpToolbar();
        setUpFirebase();
    }

    private String getDataExtra() {
        Bundle extras = getIntent().getExtras();
        return extras.getString(Constant.KEY_DATA_ORDER);
    }

    private void setUpFirebase() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @OnClick(R.id.btnReview)
    public void eventReview(View view) {
        String review = editReview.getText().toString().trim();
        if (TextUtils.isEmpty(review)) {
            Toast.makeText(ReviewShipperActivity.this, "Review Content Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        mDatabaseReference.child(Constant.FB_KEY_USER_ORDER).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(getDataExtra()).child("connentReview").setValue(review);

        Intent mIntent = new Intent();
        mIntent.putExtra(Constant.KEY_DATA_REVIEW, review);
        setResult(Constant.RESULT_SELECT_REVIEW, mIntent);
        finish();
    }

    private void setUpToolbar() {
        tvNameToolbar.setText("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(3.0f);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
