package vn.zikoteam.ziko.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import vn.zikoteam.ziko.R;
import vn.zikoteam.ziko.activity.FoodDetailsActivity;
import vn.zikoteam.ziko.activity.UserDetailsActivity;
import vn.zikoteam.ziko.model.Food;
import vn.zikoteam.ziko.other.Constant;
import vn.zikoteam.ziko.viewholder.FoodViewHolder;

/**
 * Created by dk-darkness on 23/11/2016.
 */

public abstract class MainFragment extends Fragment {
    private RecyclerView listHome;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference mDatabaseReference;
    private FirebaseRecyclerAdapter<Food, FoodViewHolder> mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutFragment(), parent, false);

        setUpFirebase();

        listHome = (RecyclerView) view.findViewById(R.id.listHome);
        listHome.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        listHome.setLayoutManager(layoutManager);

        Query foodQuery = getQuery(mDatabaseReference);
        mAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.item_food,
                FoodViewHolder.class, foodQuery) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, final Food model, int position) {
                final DatabaseReference postRef = getRef(position);
                final String postKey = postRef.getKey();

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), FoodDetailsActivity.class);
                        intent.putExtra(Constant.EXTRA_FOOD_KEY, postKey);
                        startActivity(intent);
                    }
                });

                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {
                        Intent intent = new Intent(getActivity(), UserDetailsActivity.class);
                        intent.putExtra(Constant.EXTRA_USER_KEY, model.getIdUser());
                        startActivity(intent);
                    }
                });
                loadDataSusscess().setVisibility(View.GONE);
            }

            @Override
            protected void onCancelled(DatabaseError error) {
                super.onCancelled(error);
            }
        };
        listHome.setAdapter(mAdapter);
    }

    private void setUpFirebase() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);

    public abstract int getLayoutFragment();

    public abstract View loadDataSusscess();
}