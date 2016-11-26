package vn.zikoteam.ziko.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.zikoteam.ziko.R;

/**
 * Created by dk-darkness on 23/11/2016.
 */

public class StoreFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
    }

    @OnClick(R.id.fabAddFood)
    public void fabEventAddFood(View view){
        replaceFragment();
    }

    public void replaceFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        AddFoodFragment addFoodFragment = new AddFoodFragment();
        ft.add(R.id.fragment_container, addFoodFragment, "HELLO");
        ft.commit();
    }

}
