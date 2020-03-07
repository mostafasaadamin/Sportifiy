package com.work.unknown.sportifiy.Profiles;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.work.unknown.sportifiy.Models.userinfo;
import com.work.unknown.sportifiy.R;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnotherUserProfile extends AppCompatActivity {
    DatabaseReference getCurrentuser;
    ImageView imageView;
    TextView user_name, followerstext, followingtext;
    RatingBar ratingBar;
    TextView ratingnum1, ratingnum2, ratingnum3;
    Button submit;
    FirebaseAuth mAuth;
    DatabaseReference getRates, getfollowing, getfollowers;
    int num1 = 0, num2 = 0, num3 = 0, followers = 0, following = 0;
    DatabaseReference save_user_Rates;
    String Key="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_user_profile);
         Key = getIntent().getExtras().getString("userid");
        user_name = findViewById(R.id.ausername);
        imageView = findViewById(R.id.anothermanid);
        submit = findViewById(R.id.submitrate);
        ratingBar = findViewById(R.id.yourRate);
        ratingnum1 = findViewById(R.id.one);
        ratingnum2 = findViewById(R.id.two);
        ratingnum3 = findViewById(R.id.three);
        followerstext = findViewById(R.id.Followers);
        followingtext = findViewById(R.id.following);
        save_user_Rates = FirebaseDatabase.getInstance().getReference().child("userRates");
        mAuth = FirebaseAuth.getInstance();
        if (Key != null) {
            getCurrentuser = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(Key).child("data");
            getRates = FirebaseDatabase.getInstance().getReference().child("userRates").child(Key);
            getfollowers = FirebaseDatabase.getInstance().getReference().child("Followers").child(Key);
            getfollowing = FirebaseDatabase.getInstance().getReference().child("Followers");
            getRatesData();
            getFollowersData();
            getFollowingData();
        }

        getCurrentuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userinfo in = dataSnapshot.getValue(userinfo.class);
                //    Toast.makeText(AnotherUserProfile.this, in.getUsername(), Toast.LENGTH_SHORT).show();
                user_name.setText(in.getUsername());
                try {
                    String url = in.getImageurl();
                    if (url != null) {
                        Picasso.with(AnotherUserProfile.this)
                                .load(url)
                                .placeholder(R.drawable.man)
                                .error(R.drawable.man)
                                .centerCrop()
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        Bitmap imagebitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                        RoundedBitmapDrawable imgdrawable = RoundedBitmapDrawableFactory.create(getResources(), imagebitmap);
                                        imgdrawable.setCircular(true);
                                        imgdrawable.setCornerRadius(Math.max(imagebitmap.getWidth(), imagebitmap.getHeight()) / 2.0f);
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
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rate = String.valueOf(ratingBar.getRating());
                save_user_Rates.child(Key).push().child("rate").setValue(rate);
                save_user_Rates.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(AnotherUserProfile.this, " your feedback had been submitted", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(AnotherUserProfile.this, " Error occured", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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

    @Override
    protected void onDestroy() {
        Runtime.getRuntime().gc();
        super.onDestroy();
    }
}
