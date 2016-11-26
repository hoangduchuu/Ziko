package vn.zikoteam.ziko.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.zikoteam.ziko.R;
import vn.zikoteam.ziko.customui.ScaleImage;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.editEmail)
    EditText editEmail;
    @BindView(R.id.editPass)
    EditText editPass;
    @BindView(R.id.tvForgotPass)
    TextView tvForgotPass;
    @BindView(R.id.imgBgHome)
    ImageView imgBgHome;

    private ScaleImage mScaleImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setImageBg();
        setUpEditText();
    }

    private void setImageBg() {
        mScaleImage = new ScaleImage(LoginActivity.this, R.drawable.bg);
        imgBgHome.setImageBitmap(mScaleImage.getBitmap());
    }

    private void setUpEditText() {
        editPass.setTransformationMethod(new PasswordTransformationMethod());
    }

    @OnClick(R.id.btnLogin)
    public void eventLogin() {
        startIntent(MainActivity.class);
    }

    @OnClick(R.id.tvSignUp)
    public void eventSignUp() {
        startIntent(SignupActivity.class);
    }

    private void startIntent(Class<?> cls) {
        Intent mIntent = new Intent(LoginActivity.this,cls);
        startActivity(mIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

