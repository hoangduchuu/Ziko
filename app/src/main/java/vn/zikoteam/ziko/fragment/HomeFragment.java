package vn.zikoteam.ziko.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.zikoteam.ziko.R;
import vn.zikoteam.ziko.other.Constant;

/**
 * Created by dk-darkness on 28/11/2016.
 */

public class HomeFragment extends MainFragment {
    @BindView(R.id.pgLoading)
    ProgressBar pgLoading;

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child(Constant.FB_KEY_FOOD);
    }

    @Override
    public int getLayoutFragment() {
        return R.layout.fragmen_home;
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

}
