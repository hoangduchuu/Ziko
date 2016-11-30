package vn.zikoteam.ziko.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.zikoteam.ziko.R;
import vn.zikoteam.ziko.activity.AddFoodActivity;
import vn.zikoteam.ziko.other.Constant;

/**
 * Created by dk-darkness on 23/11/2016.
 */

public class StoreFragment extends MainFragment {
    @BindView(R.id.pgLoading)
    ProgressBar pgLoading;

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child(Constant.FB_KEY_USER_FOOD)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    @Override
    public int getLayoutFragment() {
        return R.layout.fragment_store;
    }

    @Override
    public View loadDataSusscess() {
        return pgLoading;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.fabAddFood)
    public void fabEventAddFood(View view) {
        replaceFragment();
    }

    public void replaceFragment() {
        Intent mIntent = new Intent(getContext(), AddFoodActivity.class);
        startActivity(mIntent);
    }

}
