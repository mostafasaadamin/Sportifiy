package com.work.unknown.sportifiy.Followers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;
import com.work.unknown.sportifiy.Adapter.Loadcreatedmatchandfollowing;
import com.work.unknown.sportifiy.Adapter.followersAdapter;
import com.work.unknown.sportifiy.Models.userinfo;
import com.work.unknown.sportifiy.Profiles.CurrentUserProfile;
import com.work.unknown.sportifiy.R;
import com.work.unknown.sportifiy.Models.followersmodel;
import com.work.unknown.sportifiy.Followers.followermatchs;
import com.work.unknown.sportifiy.RecyclesModels.searchmodel;

import java.util.ArrayList;
import java.util.Map;

public class followermatchs extends AppCompatActivity {
    RecyclerView Followers_Matches;
    DatabaseReference getCurrentuser;
    FirebaseAuth mAuth;
    DatabaseReference userKey;
    String follower_key;
    followersAdapter adapter;
    AVLoadingIndicatorView avi;
    ArrayList<followersmodel> c_followerlist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followingmatchs);
        mAuth = FirebaseAuth.getInstance();
        avi = findViewById(R.id.cir);
        avi.show();
        Followers_Matches = findViewById(R.id.FollowersMatches);
        String current_key = mAuth.getCurrentUser().getUid();
        userKey = FirebaseDatabase.getInstance().getReference().child("Followers").child(current_key);
        userKey.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        String Place = d.child("Place").getValue().toString();
                        String Location = d.child("Location").getValue().toString();
                        String FollowerKey = d.child("FollowerKey").getValue().toString();
                        String Date = d.child("Date").getValue().toString();
                        String Time = d.child("Time").getValue().toString();
                        String Sport = d.child("Sport").getValue().toString();
                        followersmodel model = new followersmodel(Place, Time, Date, Sport, FollowerKey, Location);
                        c_followerlist.add(model);
                    }
                    if (c_followerlist.size() > 0) {
                        avi.hide();
                        Followers_Matches.setLayoutManager(new LinearLayoutManager(followermatchs.this));
                         adapter = new followersAdapter(c_followerlist, getApplicationContext());
                        Followers_Matches.setAdapter(adapter);
                    } else {
                        avi.hide();
                        Toast.makeText(getBaseContext(), "No data to show", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(followermatchs.this, "No players for your match ", Toast.LENGTH_SHORT).show();
                    avi.hide();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                avi.hide();
            }
        });
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (adapter != null) {
            for (MapView m : adapter.getMapViews()) {
                m.onLowMemory();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (adapter != null) {
            for (MapView m : adapter.getMapViews()) {
                m.onPause();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            for (MapView m : adapter.getMapViews()) {
                m.onResume();
            }
        }
    }
    @Override
    protected void onDestroy() {
        if (adapter != null) {
            for (MapView m : adapter.getMapViews()) {
                m.onDestroy();
            }
        }
        super.onDestroy();
    }

}
