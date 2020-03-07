package com.work.unknown.sportifiy.Details;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.work.unknown.sportifiy.Following;
import com.work.unknown.sportifiy.Login.Login;
import com.work.unknown.sportifiy.MainActivity;
import com.work.unknown.sportifiy.Models.userinfo;
import com.work.unknown.sportifiy.Notification.notification;
import com.work.unknown.sportifiy.Profiles.CurrentUserProfile;
import com.work.unknown.sportifiy.Followers.followermatchs;
import com.work.unknown.sportifiy.R;
import com.work.unknown.sportifiy.info;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CreateOrSearch extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth mAuth;
    KenBurnsView backimage;
    public static final String MyPREFERENCES = "imageback";
    Uri imageUri;
    SharedPreferences sharedpreferences;
    public static Picasso picassoWithCache;
    final static int Gallary_result = 223;
    final static int REQUEST_Storage = 112;
    DatabaseReference get_current_data;
    String curent_user_id = "";
    String current_user_Email = "";
    String current_user_name = "";
    String current_user_image_url = "";
    TextView username, Email;
    ImageView current_user_image;

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
        setContentView(R.layout.activity_create_or_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        //////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////
        NavigationView navigationView = findViewById(R.id.nav_view);
        View navView = navigationView.inflateHeaderView(R.layout.nav_header_create_or_search);
        //current_user_image=navView.findViewById(R.id.userimagefornavheader);
        username = navView.findViewById(R.id.usernamefornav);
        Email = navView.findViewById(R.id.useremailfornav);
        backimage = navView.findViewById(R.id.imagefornav);
        sharedpreferences = getApplication().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        String imagestring = sharedpreferences.getString("imageString", "null");
        if (!imagestring.equals("null")) {
            Bitmap image = decodeBase64(imagestring);
            backimage.setImageBitmap(image);
        }

        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        curent_user_id = mAuth.getCurrentUser().getUid();
        current_user_Email = mAuth.getCurrentUser().getEmail();
        get_current_data = FirebaseDatabase.getInstance().getReference().child("UserInfo")
                .child(curent_user_id).child("data");
        get_current_data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userinfo info = dataSnapshot.getValue(userinfo.class);
                current_user_name = info.getUsername();
                username.setText(current_user_name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        Email.setText(current_user_Email);
        ////////////////////////////////////////////////////////
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_or_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.Profile) {
            Intent i = new Intent(CreateOrSearch.this, CurrentUserProfile.class);
            i.putExtra("currentuserid", mAuth.getCurrentUser().getUid());
            startActivity(i);

        } else if (id == R.id.Followers) {
            Intent i = new Intent(CreateOrSearch.this, followermatchs.class);
            startActivity(i);
        } else if (id == R.id.Following) {
            Intent i = new Intent(CreateOrSearch.this, Following.class);
            startActivity(i);
        } else if (id == R.id.setting) {

        } else if (id == R.id.Notification) {
            Intent i = new Intent(CreateOrSearch.this, notification.class);
            startActivity(i);
        } else if (id == R.id.logout) {
            mAuth.signOut();
            Intent i = new Intent(CreateOrSearch.this, Login.class);
            // i.putExtra("currentuserid",mAuth.getCurrentUser().getUid());
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void CreateMAtchbyuser(View view) {
        Intent i = new Intent(CreateOrSearch.this, CreateMatch.class);
        startActivity(i);
    }

    public void SearchMatch(View view) {
        Intent i = new Intent(CreateOrSearch.this, SearchMatch.class);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        Runtime.getRuntime().gc();
        super.onDestroy();
    }

    public void selectImage() {
        if (ContextCompat.checkSelfPermission(CreateOrSearch.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CreateOrSearch.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_Storage);
        } else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, Gallary_result);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == Gallary_result && resultCode == RESULT_OK) {
                imageUri = data.getData();
                backimage.setImageURI(imageUri);

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                String bitmapencoded = encodeTobase64(bitmap);
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor = sharedpreferences.edit();
                editor.putString("imageString", bitmapencoded);
                editor.commit();


                //Picasso.with(CreateOrSearch.this).load(imageUri).placeholder(R.drawable.bar).error(R.drawable.bar).into(backimage);

                //   picassoWithCache.load(imageUri).placeholder(R.drawable.bar).error(R.drawable.bar).into(backimage);
            }
        } catch (Exception ex) {
            Log.i("crop", ex.getMessage().toString() + " ");
        }
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 50, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    // method for base64 to bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
