package vn.zikoteam.ziko.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import vn.zikoteam.ziko.fragment.HomeFragment;
import vn.zikoteam.ziko.fragment.OrderFragment;
import vn.zikoteam.ziko.fragment.ProfileFragment;
import vn.zikoteam.ziko.fragment.StoreFragment;

/**
 * Created by dk-darkness on 23/11/2016.
 */

public class MainAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[]{"List Foods", "My Foods", "My Order", "My Profile"};
    private Context context;

    public MainAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.context = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                StoreFragment storeFragment = new StoreFragment();
                return storeFragment;
            case 2:
                OrderFragment orderFragment = new OrderFragment();
                return orderFragment;
            case 3:
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
