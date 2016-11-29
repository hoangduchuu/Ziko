package vn.zikoteam.ziko.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.zikoteam.ziko.R;
import vn.zikoteam.ziko.other.Constant;

/**
 * Created by dk-darkness on 29/11/2016.
 */

public class FoodFriendFragment extends MainFragment{
    @BindView(R.id.pgLoading)
    ProgressBar pgLoading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child(Constant.FB_KEY_USER_FOOD).child(getArguments().getString(Constant.EXTRA_FRAGMENT_KEY));
    }

    @Override
    public int getLayoutFragment() {
        return R.layout.fragment_food_friend;
    }

    @Override
    public View loadDataSusscess() {
        return pgLoading;
    }
}
