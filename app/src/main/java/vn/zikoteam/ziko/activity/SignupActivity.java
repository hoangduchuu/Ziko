package vn.zikoteam.ziko.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.zikoteam.ziko.R;
import vn.zikoteam.ziko.customui.ScaleImage;
import vn.zikoteam.ziko.model.User;

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

    private ScaleImage mScaleImage;

    private User mUser;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        setImageBg();
        setUpEditText();

        mFirebaseAuth = FirebaseAuth.getInstance();
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
        showProgressDialog();
        createNewAccount(editEmail.getText().toString(),
                editPass.getText().toString());
    }

    @OnClick(R.id.tvIntentLogin)
    public void eventIntentLogin(View v) {
        intentLogin();
    }

    private void intentLogin() {
        Intent mIntent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(mIntent);
    }

    private void setUpUser() {
        mUser = new User();
        mUser.setName(editName.getText().toString());
        mUser.setPhoneNumber(editPhoneNumber.getText().toString());
        mUser.setEmail(editEmail.getText().toString());
        mUser.setPassword(editPass.getText().toString());
    }

    private void createNewAccount(String email, String password) {
        setUpUser();
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();

                        if (!task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, R.string.mess_authen_error,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            onAuthenticationSucess(task.getResult().getUser());
                        }
                    }
                });
    }

    private void onAuthenticationSucess(FirebaseUser firebaseUser) {
        saveNewUser(firebaseUser.getUid(), mUser.getName(),
                mUser.getPhoneNumber(), mUser.getEmail());
        intentLogin();
    }

    private void saveNewUser(String userId, String name, String phoneNumber, String email) {
        User user = new User(userId, name, phoneNumber, email);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mDatabaseReference.child("users").child(userId).setValue(user);
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


}
