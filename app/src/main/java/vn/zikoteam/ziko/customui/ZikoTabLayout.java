package vn.zikoteam.ziko.customui;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by dk-darkness on 26/11/2016.
 */

public class ZikoTabLayout extends TabLayout {
    private Typeface typeface;

    public ZikoTabLayout(Context context) {
        super(context);
    }

    public ZikoTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZikoTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setupWithViewPager(ViewPager viewPager) {
        super.setupWithViewPager(viewPager);
        typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/extrabold.ttf");

        if (typeface != null) {
            this.removeAllTabs();

            ViewGroup slidingTabStrip = (ViewGroup) getChildAt(0);

            PagerAdapter adapter = viewPager.getAdapter();

            for (int i = 0, count = adapter.getCount(); i < count; i++) {
                Tab tab = this.newTab();
                this.addTab(tab.setText(adapter.getPageTitle(i)));
                AppCompatTextView view = (AppCompatTextView) ((ViewGroup) slidingTabStrip.getChildAt(i)).getChildAt(1);
                view.setTypeface(typeface, Typeface.NORMAL);
            }
        }
    }
}
