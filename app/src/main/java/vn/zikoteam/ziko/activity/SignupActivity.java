package vn.zikoteam.ziko.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.zikoteam.ziko.R;
import vn.zikoteam.ziko.customui.ScaleImage;
import vn.zikoteam.ziko.model.User;
import vn.zikoteam.ziko.other.Constant;

public class SignupActivity extends AppCompatActivity {
    @BindView(R.id.editName)
    EditText editName;
    @BindView(R.id.editPhoneNumber)
    EditText editPhoneNumber;
    @BindView(R.id.editEmail)
    EditText editEmail;
    @BindView(R.id.editPass)
    EditText editPass;
    @BindView(R.id.editRePass)
    EditText editRePass;
    @BindView(R.id.imgBgHome)
    ImageView imgBgHome;
    @BindView(R.id.pgLoading)
    ProgressBar pgLoading;

    private ScaleImage mScaleImage;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        setUpFirebase();
        setImageBg();
        setUpEditText();
    }

    private void setUpFirebase() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void setImageBg() {
        mScaleImage = new ScaleImage(SignupActivity.this, R.drawable.bg);
        imgBgHome.setImageBitmap(mScaleImage.getBitmap());
    }

    private void setUpEditText() {
        editPass.setTransformationMethod(new PasswordTransformationMethod());
        editRePass.setTransformationMethod(new PasswordTransformationMethod());
    }

    @OnClick(R.id.btnSignIn)
    public void eventSignIn(View v) {
        createNewAccount();
    }

    @OnClick(R.id.tvIntentLogin)
    public void eventIntentLogin(View v) {
        intentLogin();
    }

    private void intentLogin() {
        Intent mIntent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(mIntent);
        finish();
    }

    private void showMessageValidData(String message) {
        Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void createNewAccount() {
        final String name = editName.getText().toString().trim();
        final String email = editEmail.getText().toString().trim();
        final String phoneNumber = editPhoneNumber.getText().toString().trim();
        String password = editPass.getText().toString().trim();
        String confirmPass = editRePass.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            showMessageValidData("Name Error");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            showMessageValidData("Email Error");
            return;
        }
        if (TextUtils.isEmpty(phoneNumber)) {
            showMessageValidData("PhoneNumber Error");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showMessageValidData("Password Error");
            return;
        }
        if (TextUtils.isEmpty(confirmPass)) {
            showMessageValidData("Confirm Password Error");
            return;
        }
        if (!password.equals(confirmPass)) {
            showMessageValidData("Confirm Password Error");
            return;
        }

        pgLoading.setVisibility(View.VISIBLE);
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pgLoading.setVisibility(View.GONE);

                        if (!task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, R.string.mess_authen_error,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            saveNewUser(firebaseUser.getUid(), name, phoneNumber, email);
                            intentLogin();
                        }
                    }
                });
    }

    private void saveNewUser(String userId, String name, String phoneNumber, String email) {
        User mUser = new User(userId, name, phoneNumber, email, Constant.BASE_URL_IMAGE, "");
        Map<String, Object> userValue = mUser.toMap();

        Map<String, Object> childUpdate = new HashMap<>();
        childUpdate.put("/" + Constant.FB_KEY_USER + "/" +userId , userValue);
        mDatabaseReference.updateChildren(childUpdate);
    }


}
