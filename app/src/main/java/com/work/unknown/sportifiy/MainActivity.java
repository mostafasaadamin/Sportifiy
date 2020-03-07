package com.work.unknown.sportifiy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.work.unknown.sportifiy.Login.Login;
import com.work.unknown.sportifiy.Login.SignUp;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_main);
    }

    public void SignIN(View view) {
        Intent i=new Intent(MainActivity.this, Login.class);
        startActivity(i);
    }

    public void Signup(View view) {
        Intent i=new Intent(MainActivity.this, SignUp.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
       System.exit(0);
        finishAffinity();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        Runtime.getRuntime().gc();
        super.onDestroy();
    }
}
