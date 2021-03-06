package vn.zikoteam.ziko.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.zikoteam.ziko.R;
import vn.zikoteam.ziko.adapter.MainAdapter;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.viewPagerMain)
    ViewPager viewPagerMain;
    @BindView(R.id.tabMain)
    TabLayout tabMain;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvNameToolbar)
    TextView tvNameToolbar;
    @BindView(R.id.viewToolbar)
    LinearLayout viewToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setUpToolbar();
        setUpTabLayout();
    }

    private void setUpToolbar() {
        viewToolbar.setGravity(Gravity.CENTER);
        tvNameToolbar.setText(R.string.app_name);
        tvNameToolbar.setTextSize(25.0f);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setUpTabLayout() {
        viewPagerMain.setAdapter(new MainAdapter(getSupportFragmentManager(),
                MainActivity.this));
        tabMain.setupWithViewPager(viewPagerMain);
    }
}
