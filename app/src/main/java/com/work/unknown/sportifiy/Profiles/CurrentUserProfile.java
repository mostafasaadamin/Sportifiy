package com.work.unknown.sportifiy.Profiles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.tapadoo.alerter.Alerter;
import com.work.unknown.sportifiy.Adapter.Loadcreatedmatchandfollowing;
import com.work.unknown.sportifiy.Chating.List_of_available_users;
import com.work.unknown.sportifiy.Chating.chat;
import com.work.unknown.sportifiy.Details.CreateOrSearch;
import com.work.unknown.sportifiy.Login.Login;
import com.work.unknown.sportifiy.Models.userinfo;
import com.work.unknown.sportifiy.R;
import com.work.unknown.sportifiy.RecyclesModels.CreatedMatchModel;
import com.work.unknown.sportifiy.info;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.work.unknown.sportifiy.Details.CreateOrSearch.decodeBase64;

public class CurrentUserProfile extends AppCompatActivity {
    DatabaseReference getCurrentuser;
    DatabaseReference getCreatedMatchByUser;
    ImageView imageView;
    KenBurnsView backimage;
    final static int Gallary_result=226;
    final static int REQUEST_Storage=118;
    public static final String MyPREFERENCES = "imagebackforprofile" ;
    Uri imageUri;
    SharedPreferences sharedpreferences;
    RecyclerView cratedMatches;
    TextView user_name;
    int num1 = 0, num2 = 0, num3 = 0, followers = 0, following = 0;
    TextView ratingnum1, ratingnum2, ratingnum3;
    TextView followerstext, followingtext;
    DatabaseReference getRates, getfollowing, getfollowers;
    ArrayList<CreatedMatchModel>c_matchlist=new ArrayList<>();
    String Key="" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_user_profile);
         Key = getIntent().getExtras().getString("currentuserid");
        user_name = findViewById(R.id.UserName);
        imageView = findViewById(R.id.manid);
        ratingnum1 = findViewById(R.id.one);
        ratingnum2 = findViewById(R.id.two);
        ratingnum3 = findViewById(R.id.three);
        followerstext = findViewById(R.id.Followers);
        followingtext = findViewById(R.id.following);
        cratedMatches=findViewById(R.id.Cm);
        if (Key != null) {
            getCurrentuser = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(Key).child("data");
            getCreatedMatchByUser = FirebaseDatabase.getInstance().getReference().child("CreatedMatchDataDetail").child(Key);
            getRates = FirebaseDatabase.getInstance().getReference().child("userRates").child(Key);
            getfollowers = FirebaseDatabase.getInstance().getReference().child("Followers").child(Key);
            getfollowing = FirebaseDatabase.getInstance().getReference().child("Followers");
            getRatesData();
            getFollowersData();
            getFollowingData();
        }
        /////////////////////////this for background of header//////////////////////////

        backimage=findViewById(R.id.imagehead);
        sharedpreferences=getApplication().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        String imagestring=sharedpreferences.getString("imageString","null");
        if(!imagestring.equals("null"))
        {
            Bitmap image=decodeBase64(imagestring);
            backimage.setImageBitmap(image);
        }

        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });


        ///////////////////////////////////////////////////////////////////////////////
        getCurrentuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userinfo in = dataSnapshot.getValue(userinfo.class);
            //    getUserdata((Map<String, Object>) dataSnapshot.getValue());
                if (in.getUsername() != null) {
                    user_name.setText(in.getUsername());
                }
            try {
                String url = in.getImageurl();
                if (url != null) {
                  new Picasso.Builder(CurrentUserProfile.this)
                            .downloader(new OkHttpDownloader(CurrentUserProfile.this, Integer.MAX_VALUE))
                            .build()
                            .load(url)
                            .into(imageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Bitmap imagebitmap=((BitmapDrawable)imageView.getDrawable()).getBitmap();
                                    RoundedBitmapDrawable imgdrawable= RoundedBitmapDrawableFactory.create(getResources(),imagebitmap);
                                    imgdrawable.setCircular(true);
                                    imgdrawable.setCornerRadius(Math.max(imagebitmap.getWidth(),imagebitmap.getHeight())/2.0f);
                                    imageView.setImageDrawable(imgdrawable);
                                }

                                @Override
                                public void onError() {
                                    imageView.setImageResource(R.drawable.man);
                                }
                            });
                }

            } catch (Exception ex) {
            }



            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//////////////////////////////////////////////////////////////////////////////////
        getCreatedMatchByUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.getValue()!=null) {
          //      getMatchdata((Map<String, Object>) dataSnapshot.getValue());
                for(DataSnapshot d:dataSnapshot.getChildren()) {

                    String Sport,Date,Time,Place,Location;
                    Sport=d.child("sport").getValue().toString();
                    Date=d.child("date").getValue().toString();
                    Time=d.child("time").getValue().toString();
                    Place=d.child("place").getValue().toString();
                    Location=d.child("location").getValue().toString();
                    if(Sport!=null&&Date!=null&&Time!=null&&Place!=null&&Location!=null){
                        c_matchlist.add(new CreatedMatchModel(Sport, Date, Time, Place, Location));
                    }
//            Toast.makeText(CurrentUserProfile.this, Sport+":"+Date+":"+Time+":"+Place+":"+Location, Toast.LENGTH_SHORT).show();
                }
                if(c_matchlist.size()>0)
                {
                    //   Toast.makeText(this, " data", Toast.LENGTH_SHORT).show();
                    cratedMatches.setLayoutManager(new LinearLayoutManager(CurrentUserProfile.this));
                    Loadcreatedmatchandfollowing adapter = new  Loadcreatedmatchandfollowing(c_matchlist,getApplicationContext());
                    cratedMatches.setAdapter(adapter);
                }else
                {
                    Toast.makeText(CurrentUserProfile.this, "No data", Toast.LENGTH_SHORT).show();
                }
                }
                else
                {
                    Alerter.create(CurrentUserProfile.this)
                            .setTitle("sucess")
                            .setText("you hav not create any match till now")
                            .setBackgroundColorRes(R.color.darkRed)
                            .setDuration(4000)
                            .setIcon(R.drawable.alarm)
                            .show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

//////////////////////////////////////////////////////////////////////////////////////
    }

    private void getMatchdata(Map<String, Object> users) {
        for (Map.Entry<String, Object> entry : users.entrySet()) {
            Map singleUser = (Map) entry.getValue();
            String Sport,Date,Time,Place,Location;
            Sport=singleUser.get("sport").toString();
            Date=singleUser.get("date").toString();
            Time=singleUser.get("time").toString();
            Place=singleUser.get("place").toString();
            Location=singleUser.get("location").toString();
            if(Sport!=null&&Date!=null&&Time!=null&&Place!=null&&Location!=null){
                c_matchlist.add(new CreatedMatchModel(Sport, Date, Time, Place, Location));
            }
//            Toast.makeText(CurrentUserProfile.this, Sport+":"+Date+":"+Time+":"+Place+":"+Location, Toast.LENGTH_SHORT).show();
        }
        if(c_matchlist.size()>0)
        {
         //   Toast.makeText(this, " data", Toast.LENGTH_SHORT).show();
            cratedMatches.setLayoutManager(new LinearLayoutManager(CurrentUserProfile.this));
            Loadcreatedmatchandfollowing adapter = new  Loadcreatedmatchandfollowing(c_matchlist,getApplicationContext());
            cratedMatches.setAdapter(adapter);
        }else
        {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CurrentUserProfile.this, CreateOrSearch.class);
        startActivity(i);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        Runtime.getRuntime().gc();
        super.onDestroy();
    }

    public void chatme(View view) {

        Intent i=new Intent(CurrentUserProfile.this,List_of_available_users.class);
        startActivity(i);

    }

    public void selectImage() {
        if (ContextCompat.checkSelfPermission(CurrentUserProfile.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CurrentUserProfile.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_Storage);
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

                String bitmapencoded=encodeTobase64(bitmap);
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor = sharedpreferences.edit();
                editor.putString("imageString", bitmapencoded);
                editor.commit();
                
                //Picasso.with(CreateOrSearch.this).load(imageUri).placeholder(R.drawable.bar).error(R.drawable.bar).into(backimage);

                //   picassoWithCache.load(imageUri).placeholder(R.drawable.bar).error(R.drawable.bar).into(backimage);
            }
        }catch (Exception ex)
        {
            Log.i("crop", ex.getMessage().toString());
        }
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
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

    private void getFollowingData() {
        getfollowing.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    for (DataSnapshot s : d.getChildren()) {
                        String k = s.child("FollowerKey").getValue().toString();
                        if(k.equals(Key)) {
                            following++;
                        }
                    }
                }
                followingtext.setText(following + "");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getFollowersData() {
        getfollowers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    followers++;
                }
                followerstext.setText(followers + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getRatesData() {
        getRates.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    String rate = d.child("rate").getValue().toString();
                    if (rate.equals("3.0")) {
                        num3++;
                        //Toast.makeText(AnotherUserProfile.this, rate, Toast.LENGTH_SHORT).show();
                    } else if (rate.equals("2.0")) {
                        num2++;
                    } else if (rate.equals("1.0")) {
                        num1++;
                    }
                }
                ratingnum1.setText("(" + num1 + ")");
                ratingnum2.setText("(" + num2 + ")");
                ratingnum3.setText("(" + num3 + ")");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    }

