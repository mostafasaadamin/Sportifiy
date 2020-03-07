package com.work.unknown.sportifiy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;
import com.work.unknown.sportifiy.Adapter.FollowingAdapter;
import com.work.unknown.sportifiy.Adapter.followersAdapter;
import com.work.unknown.sportifiy.Followers.followermatchs;
import com.work.unknown.sportifiy.Models.followersmodel;
import com.work.unknown.sportifiy.Models.followingModel;

import java.util.ArrayList;

public class Following extends AppCompatActivity {
    RecyclerView Followeing_Matches;
    AVLoadingIndicatorView avi;
    FollowingAdapter adapter;
    FirebaseAuth mAuth;
    String current_key;
    DatabaseReference getfollowingmatch;
    ArrayList<followingModel> c_followinglist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
       mAuth=FirebaseAuth.getInstance();
        Followeing_Matches =findViewById(R.id.FollowingMatches);
        avi=findViewById(R.id.cir);
        get_following_match();
        current_key=mAuth.getCurrentUser().getUid();
    }
    public void get_following_match()
    {

        getfollowingmatch = FirebaseDatabase.getInstance().getReference().child("Followers");
        getfollowingmatch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        for (DataSnapshot s : d.getChildren()) {
                            String Place = s.child("Place").getValue().toString();
                            String Location = s.child("Location").getValue().toString();
                            String FollowerKey = s.child("FollowerKey").getValue().toString();
                            String Date = s.child("Date").getValue().toString();
                            String Time = s.child("Time").getValue().toString();
                            String Sport = s.child("Sport").getValue().toString();
                             if(current_key.equals(FollowerKey)) {
                                 followingModel model = new followingModel(Place, Time, Date, Sport, FollowerKey, Location);
                                 c_followinglist.add(model);
                             }
                        }
                    }
                    if (c_followinglist.size() > 0) {
                        avi.hide();
                        Followeing_Matches.setLayoutManager(new LinearLayoutManager(Following.this));
                         adapter = new FollowingAdapter(c_followinglist, getApplicationContext());
                        Followeing_Matches.setAdapter(adapter);
                    } else {
                        avi.hide();
                        Toast.makeText(getBaseContext(), "No data to show :(", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Following.this, "you never joined to any matches :(", Toast.LENGTH_SHORT).show();
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
