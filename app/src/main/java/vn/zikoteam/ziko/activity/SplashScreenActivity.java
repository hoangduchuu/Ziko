package vn.zikoteam.ziko.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import vn.zikoteam.ziko.R;
import vn.zikoteam.ziko.other.Constant;

public class SplashScreenActivity extends AppCompatActivity {
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            startMain();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mHandler.postDelayed(mRunnable, Constant.TIME_START_MAIN);
    }

    private void startMain() {
        Intent myIntent = new Intent(SplashScreenActivity.this, LoginActivity.class);
        startActivity(myIntent);
        finish();
    }
}
