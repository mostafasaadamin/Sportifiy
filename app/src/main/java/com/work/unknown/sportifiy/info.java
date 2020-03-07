package com.work.unknown.sportifiy;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tapadoo.alerter.Alerter;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.work.unknown.sportifiy.Details.CreateOrSearch;
import com.work.unknown.sportifiy.Models.userinfo;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class info extends AppCompatActivity {
     DatabaseReference DAta;
     final static int REQUEST_Storage=111;
    final static int Gallary_result=222;
    FirebaseAuth mAuth;
    Uri imageUri;
    String Downloadurl=" ";
    StorageReference mStorageRef;
    android.app.AlertDialog dialog;
    android.app.AlertDialog dialo;
    @Bind(R.id.username)
    EditText user_name;
    @Bind(R.id.firstname)
    EditText first_name;
    @Bind(R.id.lastname)
    EditText last_name;
    @Bind(R.id.phonenumber)
    EditText phone_number;
    @Bind(R.id.Day)
    EditText D_ay;
    @Bind(R.id.Month)
    EditText M_onth;
    @Bind(R.id.Year)
    EditText Y_ear;
    @Bind(R.id.gender)
    EditText g_ender;
    @Bind(R.id.address)
    EditText a_ddress;
    @Bind(R.id.favouritlist)
    EditText f_avouritlist;
    @Bind(R.id.Bio)
    EditText B_io;
    @Bind(R.id.image)
    CircleImageView userimage;
    @Bind(R.id.SAVE)
    Button SAve;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("Fonts/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);
        mAuth=FirebaseAuth.getInstance();
        DAta= FirebaseDatabase.getInstance().getReference().child("UserInfo").child(mAuth.getCurrentUser().getUid());
        mStorageRef = FirebaseStorage.getInstance().getReference().child("profile_images");
        dialog = new SpotsDialog(this);
        dialo=new SpotsDialog(this);
        dialo.setTitle("uplode image");
        dialo.setCancelable(false);
        SAve.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           adddata();
       }
   });
    }
    private void adddata() {

        if(check()&&!Downloadurl.equals(" "))
        {
            dialog.show();
        userinfo user=new userinfo(
        user_name.getText().toString()
        ,first_name.getText().toString()
        ,last_name.getText().toString(),
        g_ender.getText().toString(),
        a_ddress.getText().toString()
        ,phone_number.getText().toString()
        ,B_io.getText().toString(),
        D_ay.getText().toString()
        ,M_onth.getText().toString(),
        Y_ear.getText().toString(),
        f_avouritlist.getText().toString()
               ,Downloadurl);
           DAta.child("data").setValue(user);
            DAta.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                    {
                        dialog.dismiss();
                        Alerter.create(info.this)
                                .setTitle("sucess")
                                .setText("info has been registered successfully")
                                .setBackgroundColorRes(R.color.darkGreen)
                                .setDuration(4000)
                                .setIcon(R.drawable.alarm)
                                .show();
                        Intent i=new Intent(info.this, CreateOrSearch.class);
                        startActivity(i);
                    }
                    else
                    {
                        dialog.dismiss();
                        alerter("Error when trying to save data");

                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else
            {
            alerter("Please Fill All Values");
            }
    }
    public boolean check()
    {
        if(TextUtils.isEmpty(user_name.getText().toString()))
        {
            return false;
        }
        if(TextUtils.isEmpty(first_name.getText().toString()))
        {
            return false;
        }
        if(TextUtils.isEmpty(phone_number.getText().toString()))
    {
        return false;
    }
        if(TextUtils.isEmpty(D_ay.getText().toString()))
        {
            return false;
        }
        if(TextUtils.isEmpty(M_onth.getText().toString()))
    {
        return false;
    }
    if(TextUtils.isEmpty(Y_ear.getText().toString()))
    {
        return false;
    }if(TextUtils.isEmpty(g_ender.getText().toString()))
    {
        return false;
    }if(TextUtils.isEmpty(a_ddress.getText().toString()))
    {
        return false;
    }if(TextUtils.isEmpty(f_avouritlist.getText().toString()))
    {
        return false;
    }
        if(TextUtils.isEmpty(B_io.getText().toString()))
        {
            return false;
        }
        return  true;
    }
    public void alerter(String v)
    {

        Alerter.create(info.this)
                .setTitle("warning")
                .setText(v)
                .setBackgroundColorRes(R.color.darkDeepOrange)
                .setDuration(3000)
                .setIcon(R.drawable.alarm)
                .show();
    }

    public void selectImage(View view) {
        if (ContextCompat.checkSelfPermission(info.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(info.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_Storage);
        } else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, Gallary_result);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_Storage) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, Gallary_result);
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(info.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "You cant select imagge please accept the permission", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(info.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_Storage);
                } else {
                    Toast.makeText(this, "Exit", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == Gallary_result && resultCode == RESULT_OK) {
                imageUri = data.getData();
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);
              }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    imageUri = result.getUri();
                    userimage.setImageURI(imageUri);
                   saveImage();
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Toast.makeText(this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    Log.i("crop", error.getMessage().toString());
                }

            }
        }catch (Exception ex)
        {
            Log.i("crop", ex.getMessage().toString());
        }
    }
    public void saveImage()
    {
        dialo.show();
        StorageReference filepath = mStorageRef.child(imageUri.getLastPathSegment());
        filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
              Downloadurl = taskSnapshot.getDownloadUrl().toString();
              //  Toast.makeText(info.this, Downloadurl, Toast.LENGTH_SHORT).show();
                dialo.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        Runtime.getRuntime().gc();
        super.onDestroy();
    }
}
