package vn.zikoteam.ziko.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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
    @BindView(R.id.pgLoading)
    ProgressBar pgLoading;

    private ScaleImage mScaleImage;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startIntent(MainActivity.class);
        }

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
        doLogin();
    }

    @OnClick(R.id.tvSignUp)
    public void eventSignUp() {
        startIntent(SignupActivity.class);
    }

    private void doLogin() {
        String email = editEmail.getText().toString().trim();
        final String password = editPass.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Email Error", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Password Error", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8){
            Toast.makeText(getApplicationContext(), "Password Error > 8", Toast.LENGTH_SHORT).show();
            return;
        }

        pgLoading.setVisibility(View.VISIBLE);

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pgLoading.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, R.string.mess_authen_error,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

    }

    private void startIntent(Class<?> cls) {
        Intent mIntent = new Intent(LoginActivity.this, cls);
        startActivity(mIntent);
        finish();
    }
}

