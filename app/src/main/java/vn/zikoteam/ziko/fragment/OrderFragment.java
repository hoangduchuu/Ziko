package vn.zikoteam.ziko.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import vn.zikoteam.ziko.R;
import vn.zikoteam.ziko.activity.FoodDetailsActivity;
import vn.zikoteam.ziko.activity.OrderDetailsActivity;
import vn.zikoteam.ziko.model.Order;
import vn.zikoteam.ziko.other.Constant;
import vn.zikoteam.ziko.viewholder.OrderViewHolder;

/**
 * Created by dk-darkness on 23/11/2016.
 */

public class OrderFragment extends Fragment {
    private RecyclerView listHome;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference mDatabaseReference;
    private FirebaseRecyclerAdapter<Order, OrderViewHolder> mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, parent, false);

        setUpFirebase();

        listHome = (RecyclerView) view.findViewById(R.id.listOrder);
        listHome.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpData();
    }

    private void setUpData() {
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        listHome.setLayoutManager(layoutManager);

        Query orderQuery = mDatabaseReference.child(Constant.FB_KEY_USER_ORDER).
                child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mAdapter = new FirebaseRecyclerAdapter<Order, OrderViewHolder>(Order.class, R.layout.item_order,
                OrderViewHolder.class, orderQuery) {


            @Override
            protected void onCancelled(DatabaseError error) {
                super.onCancelled(error);
            }

            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Order model, int position) {
                final DatabaseReference postRef = getRef(position);
                final String postKey = postRef.getKey();

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
                        intent.putExtra(Constant.EXTRA_ORDER_KEY, postKey);
                        startActivity(intent);
                    }
                });

                viewHolder.bindToPost(model);
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
        if(mAdapter!=null){
            mAdapter.cleanup();
        }
    }
}
