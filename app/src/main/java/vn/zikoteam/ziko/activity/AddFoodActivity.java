package vn.zikoteam.ziko.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.zikoteam.ziko.R;
import vn.zikoteam.ziko.model.Food;
import vn.zikoteam.ziko.model.User;
import vn.zikoteam.ziko.other.Constant;
import vn.zikoteam.ziko.other.IPermission;

public class AddFoodActivity extends AppCompatActivity {
    @BindView(R.id.imgFood)
    ImageView imgFood;
    @BindView(R.id.editNameFood)
    EditText editNameFood;
    @BindView(R.id.editAbout)
    EditText editAbout;
    @BindView(R.id.editPrice)
    EditText editPrice;
    @BindView(R.id.editLocation)
    EditText editLocation;
    @BindView(R.id.btnAddFood)
    Button btnAddFood;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvNameToolbar)
    TextView tvNameToolbar;
    @BindView(R.id.pgLoading)
    ProgressBar pgLoading;

    private static int RESULT_LOAD_IMG = 1;
    private String imgDecodableString;
    private StorageReference mStorageReference;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReference;
    protected String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    protected IPermission ipms;
    protected static final int REQUEST_ID_PERMISSION = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        ButterKnife.bind(this);

        setUpToolbar();
        setUpFirebase();
    }

    private void setUpFirebase() {
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void setUpToolbar() {
        tvNameToolbar.setText(R.string.add_food_in_store);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(3.0f);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btnAddFood)
    public void eventAddFood(View view) {
        doPost();
    }

    private String createFileName() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date now = new Date();
        return Constant.FIREBASE_STOGARE_IMGPOST + mFirebaseUser.getUid() + formatter.format(now) + ".jpg";
    }

    private void doPost() {
        final String nameFood = editNameFood.getText().toString().trim();
        final String talkAbout = editAbout.getText().toString().trim();
        final String price = editPrice.getText().toString().trim();
        final String location = editLocation.getText().toString().trim();
        if (TextUtils.isEmpty(nameFood)) {
            editNameFood.setError("Name Food Error");
            return;
        }
        if (TextUtils.isEmpty(talkAbout)) {
            editAbout.setError("Talk About Error");
            return;
        }
        if (TextUtils.isEmpty(price)) {
            editPrice.setError("Price Error");
            return;
        }
        if (TextUtils.isEmpty(location)) {
            editLocation.setError("Location Error");
            return;
        }

        if (imgDecodableString == null) {
            Toast.makeText(AddFoodActivity.this, "Set Image", Toast.LENGTH_SHORT).show();
            return;
        }

        pgLoading.setVisibility(View.VISIBLE);
        Uri file = Uri.fromFile(new File(imgDecodableString));
        StorageReference riversRef = mStorageReference.child(createFileName());

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String downloadUrl = String.valueOf(taskSnapshot.getDownloadUrl());
                        createPost(nameFood, price, location, downloadUrl, talkAbout);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });

    }

    private void createPost(final String nameFood, final String priceFood,
                            final String addressFood, final String imageFood,
                            final String talkAboutFood) {

        mDatabaseReference.child(Constant.FB_KEY_USER).child(mFirebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User mUser = dataSnapshot.getValue(User.class);

                        if (mUser != null) {
                            post(nameFood, priceFood, addressFood, imageFood,
                                    talkAboutFood, mUser.getId(),
                                    mUser.getName(), mUser.getAvatar());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void post(String nameFood, String priceFood,
                      String addressFood, String imageFood,
                      String talkAboutFood, String idUser,
                      String userName, String userAvatar) {
        Food mFood = new Food(nameFood, priceFood, addressFood, imageFood,
                talkAboutFood, idUser, userName, userAvatar, 0);
        Map<String, Object> foodValue = mFood.toMap();

        String key = mDatabaseReference.child(Constant.FB_KEY_FOOD).push().getKey();

        Map<String, Object> childUpdate = new HashMap<>();
        childUpdate.put("/" + Constant.FB_KEY_FOOD + "/" + key, foodValue);
        childUpdate.put("/" + Constant.FB_KEY_USER_FOOD + "/" + idUser + "/" + key, foodValue);

        mDatabaseReference.updateChildren(childUpdate);
        pgLoading.setVisibility(View.GONE);
        resetForm();
        Toast.makeText(AddFoodActivity.this, "UpLoad Susscess", Toast.LENGTH_SHORT).show();
    }

    private void resetForm() {
        imgDecodableString = "";
        imgFood.setImageResource(R.drawable.ic_image);
        editNameFood.setText("");
        editAbout.setText("");
        editPrice.setText("");
        editLocation.setText("");
    }

    @OnClick(R.id.imgFood)
    public void eventGetImage(View view) {
        boolean checkPermission = isAskPermission(new IPermission() {
            @Override
            public void onComplete() {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });
        if(!checkPermission){
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                imgFood.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public boolean isAskPermission(IPermission iPermission) {
        this.ipms = iPermission;
        if (Build.VERSION.SDK_INT >= 23) {
            List requestPermission = new ArrayList<>();
            for (int i = 0; i < PERMISSIONS.length; i++) {
                int permission = ActivityCompat.checkSelfPermission(this, PERMISSIONS[i]);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    requestPermission.add(PERMISSIONS[i]);
                }
            }
            if (requestPermission.size() > 0) {
                String[] permissions = new String[requestPermission.size()];
                permissions = (String[]) requestPermission.toArray(permissions);
                this.requestPermissions(permissions, REQUEST_ID_PERMISSION);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && Build.VERSION.SDK_INT >= 23) {
            int permissionEnable = 0;
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    boolean showRationale = shouldShowRequestPermissionRationale(permissions[i]);
                    if (showRationale) {
                        finish();
                    } else {
                        showDialogSetting();
                    }
                } else {
                    permissionEnable++;
                }
            }
            if (permissionEnable == permissions.length) {
                ipms.onComplete();
            }
        }
    }

    public void showDialogSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enable permission").setMessage("Enable permission Storage")
                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startInstalledAppDetailsActivity();
                    }
                });
        builder.show();
    }

    public void startInstalledAppDetailsActivity() {
        final Intent settingActivity = new Intent();
        settingActivity.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        settingActivity.addCategory(Intent.CATEGORY_DEFAULT);
        settingActivity.setData(Uri.parse("package:" + getPackageName()));
        settingActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        settingActivity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        settingActivity.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(settingActivity);
        finish();
    }
}
