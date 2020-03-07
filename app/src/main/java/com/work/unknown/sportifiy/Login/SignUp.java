package com.work.unknown.sportifiy.Login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tapadoo.alerter.Alerter;
import com.work.unknown.sportifiy.Details.CreateOrSearch;
import com.work.unknown.sportifiy.R;
import com.work.unknown.sportifiy.info;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignUp extends AppCompatActivity {
TextView username,Email,Password;
FirebaseAuth Auth;
SpotsDialog alert;
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
        setContentView(R.layout.activity_sign_up);
        username=findViewById(R.id.SIGNupuserName);
        Email=findViewById(R.id.SignupEmail);
        Password=findViewById(R.id.SignupPassword);
       Auth=FirebaseAuth.getInstance();
    alert=new SpotsDialog(this);
    }

    public void register(View view) {
        if (check()) {
            alert.show();

            Auth.createUserWithEmailAndPassword(Email.getText().toString(), Password.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                        Intent i=new Intent(SignUp.this,info.class);
                        startActivity(i);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    alert.dismiss();
                    Alerter.create(SignUp.this)
                            .setTitle("error")
                            .setText(R.string.failed+e.getMessage())
                            .setBackgroundColorRes(R.color.darkDeepOrange)
                            .setDuration(3000)
                            .setIcon(R.drawable.alarm)
                            .show();
                }
            });

        } else {
            Alerter.create(SignUp.this)
                    .setTitle("error")
                    .setText(R.string.erro)
                    .setBackgroundColorRes(R.color.darkDeepOrange)
                    .setDuration(3000)
                    .setIcon(R.drawable.alarm)
                    .show();
        }
    }

    public boolean check()
    {
        if(TextUtils.isEmpty(username.getText().toString()))
        {

            return false;
        }
        else if(TextUtils.isEmpty(Email.getText().toString()))
        {

            return false;
        }
        else if(TextUtils.isEmpty(Password.getText().toString()))
        {

            return false;
        }
        return true;
    }
}
