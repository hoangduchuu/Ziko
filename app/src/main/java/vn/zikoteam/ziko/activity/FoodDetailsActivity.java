package vn.zikoteam.ziko.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.zikoteam.ziko.R;
import vn.zikoteam.ziko.customui.CircleTransform;
import vn.zikoteam.ziko.customui.SimpleDividerItemDecoration;
import vn.zikoteam.ziko.model.Comment;
import vn.zikoteam.ziko.model.Food;
import vn.zikoteam.ziko.model.User;
import vn.zikoteam.ziko.other.Constant;

public class FoodDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvNameToolbar)
    TextView tvNameToolbar;
    @BindView(R.id.imgFood)
    ImageView imgFood;
    @BindView(R.id.tvNameFood)
    TextView tvNameFood;
    @BindView(R.id.priceFood)
    TextView priceFood;
    @BindView(R.id.tvTalkAbout)
    TextView tvTalkAbout;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.listComment)
    RecyclerView listComment;
    @BindView(R.id.editComment)
    EditText editComment;
    @BindView(R.id.btnSend)
    View btnSend;


    private DatabaseReference mFoodReference;
    private DatabaseReference mCommentsReference;
    private ValueEventListener mPostListener;
    private FirebaseUser mFirebaseUser;
    private CommentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        ButterKnife.bind(this);
        setUpToolbar();

        setUpFirebase();

        listComment.setLayoutManager(new LinearLayoutManager(this));
        listComment.addItemDecoration(new SimpleDividerItemDecoration(this));

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postComment();
            }
        });
    }

    private void setUpFirebase() {
        mFoodReference = FirebaseDatabase.getInstance().getReference()
                .child(Constant.FB_KEY_FOOD).child(getKeyFood());
        mCommentsReference = FirebaseDatabase.getInstance().getReference()
                .child(Constant.FB_KEY_COMMENT_FOOD).child(getKeyFood());
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

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

    @Override
    protected void onStart() {
        super.onStart();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Food mFood = dataSnapshot.getValue(Food.class);

                tvNameToolbar.setText(mFood.getNameFood());
                Picasso.with(FoodDetailsActivity.this).load(mFood.getImageFood()).into(imgFood);
                tvNameFood.setText(mFood.getNameFood());
                priceFood.setText(mFood.getPriceFood()+" VNĐ");
                tvTalkAbout.setText(mFood.getTalkAboutFood());
                tvAddress.setText(mFood.getAddressFood());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FoodDetailsActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        mFoodReference.addValueEventListener(postListener);
        mPostListener = postListener;

        mAdapter = new CommentAdapter(this, mCommentsReference);
        listComment.setAdapter(mAdapter);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPostListener != null) {
            mFoodReference.removeEventListener(mPostListener);
        }
        mAdapter.cleanupListener();
    }

    private String getUserID() {
        return mFirebaseUser.getUid();
    }

    private void postComment() {
        final String content = editComment.getText().toString().trim();
        if(TextUtils.isEmpty(content)){
            Toast.makeText(FoodDetailsActivity.this, "Comment is Empty, please input", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseDatabase.getInstance().getReference().child(Constant.FB_KEY_USER).child(getUserID())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User mUser = dataSnapshot.getValue(User.class);
                        String userName = mUser.getName();
                        String userAvatar = mUser.getAvatar();

                        Comment mComment = new Comment(getUserID(),userName, userAvatar, content);
                        mCommentsReference.push().setValue(mComment);
                        editComment.setText("");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private static class CommentViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgAvatar;
        private TextView tvNameUser;
        private TextView tvComment;

        public CommentViewHolder(View itemView) {
            super(itemView);

            imgAvatar = (ImageView) itemView.findViewById(R.id.imgAvatar);
            tvComment = (TextView) itemView.findViewById(R.id.tvComment);
            tvNameUser = (TextView) itemView.findViewById(R.id.tvNameUser);
        }
    }

    private static class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

        private Context mContext;
        private DatabaseReference mDatabaseReference;
        private ChildEventListener mChildEventListener;

        private List<String> mCommentIds = new ArrayList<>();
        private List<Comment> mComments = new ArrayList<>();

        public CommentAdapter(final Context context, DatabaseReference ref) {
            mContext = context;
            mDatabaseReference = ref;

            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    Comment comment = dataSnapshot.getValue(Comment.class);

                    mCommentIds.add(dataSnapshot.getKey());
                    mComments.add(comment);
                    notifyItemInserted(mComments.size() - 1);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    Comment newComment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();

                    int commentIndex = mCommentIds.indexOf(commentKey);
                    if (commentIndex > -1) {
                        mComments.set(commentIndex, newComment);
                        notifyItemChanged(commentIndex);
                    } else {
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    String commentKey = dataSnapshot.getKey();

                    int commentIndex = mCommentIds.indexOf(commentKey);
                    if (commentIndex > -1) {
                        mCommentIds.remove(commentIndex);
                        mComments.remove(commentIndex);
                        notifyItemRemoved(commentIndex);
                    } else {
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                    Comment movedComment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(mContext, "Failed to load comments.",
                            Toast.LENGTH_SHORT).show();
                }
            };
            ref.addChildEventListener(childEventListener);
            mChildEventListener = childEventListener;
        }

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_comment, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, int position) {
            Comment comment = mComments.get(position);

            Picasso.with(mContext).load(comment.getUserAvatar().trim()).transform(new CircleTransform()).into(holder.imgAvatar);
            holder.tvNameUser.setText(comment.getUserName());
            holder.tvComment.setText(comment.getContent());
        }

        @Override
        public int getItemCount() {
            return mComments.size();
        }

        public void cleanupListener() {
            if (mChildEventListener != null) {
                mDatabaseReference.removeEventListener(mChildEventListener);
            }
        }

    }
}
