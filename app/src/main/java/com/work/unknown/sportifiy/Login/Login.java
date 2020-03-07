package com.work.unknown.sportifiy.Login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tapadoo.alerter.Alerter;
import com.work.unknown.sportifiy.Details.CreateOrSearch;
import com.work.unknown.sportifiy.MainActivity;
import com.work.unknown.sportifiy.R;
import com.work.unknown.sportifiy.info;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
public class Login extends AppCompatActivity {
FirebaseAuth Auth;
EditText Email,Password;
    AlertDialog dialog;
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
        setContentView(R.layout.activity_login);
        Auth=FirebaseAuth.getInstance();
        Email=findViewById(R.id.LoginuserName);
        Password=findViewById(R.id.LoginPassword);
        dialog = new SpotsDialog(this);
    }
public void Loginprocess(View view)
{
    if(check()) {
        dialog.show();
       Auth.signInWithEmailAndPassword(Email.getText().toString(), Password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
        @Override
        public void onSuccess(AuthResult authResult) {
            dialog.dismiss();
            Intent i = new Intent(Login.this, CreateOrSearch.class);
              startActivity(i);

        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            dialog.dismiss();
            alerter("Failed to login"+e.getMessage());
            // Snackbar.make(root,"Failed to Login :( "+e.getMessage(),Snackbar.LENGTH_LONG).show();
        }
    });
}else
    {
        alerter("Please Fill all fields");
    }
}
public void alerter(String v)
{
    Alerter.create(Login.this)
            .setTitle("error")
            .setText(v)
            .setBackgroundColorRes(R.color.darkDeepOrange)
            .setDuration(3000)
            .setIcon(R.drawable.alarm)
            .setTextTypeface(Typeface.createFromAsset(getAssets(), "Fonts/Arkhip_font.ttf"))
            .setTitleTypeface(Typeface.createFromAsset(getAssets(), "Fonts/Arkhip_font.ttf"))
            .show();

}
public boolean check()
{

if(TextUtils.isEmpty(Email.getText().toString()))
    {

        return false;
    }
    else if(TextUtils.isEmpty(Password.getText().toString()))
    {

        return false;
    }
    return true;
}

    public void gotoSignup(View view) {
        Intent i=new Intent(Login.this,SignUp.class);
        startActivity(i);
    }
   /* @Override
    protected void onStart() {
        super.onStart();
        if (Auth.getCurrentUser() != null) {
            Intent intent = new Intent(Login.this, CreateOrSearch.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }

    }*/
}
