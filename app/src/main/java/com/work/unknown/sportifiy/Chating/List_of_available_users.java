package com.work.unknown.sportifiy.Chating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;
import com.work.unknown.sportifiy.Adapter.AllusersAdapter;
import com.work.unknown.sportifiy.Adapter.LoadSearchedMatches;
import com.work.unknown.sportifiy.Details.AvailableMatch;
import com.work.unknown.sportifiy.Models.AlluserModel;
import com.work.unknown.sportifiy.Models.userinfo;
import com.work.unknown.sportifiy.R;

import java.util.ArrayList;
import java.util.List;

public class List_of_available_users extends AppCompatActivity {
   RecyclerView recyclerView;
   DatabaseReference get_users;
   FirebaseAuth mAuth;
    ArrayList<AlluserModel>datauser=new ArrayList<>();
    String username;
    String imageurl;
    AVLoadingIndicatorView avi;
    String key;
    String Current_user="";
    DatabaseReference getfollowers,getAdmains;
    List<String> followersKey=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_available_users);
        mAuth=FirebaseAuth.getInstance();
        Current_user=mAuth.getCurrentUser().getUid();
        getfollowers = FirebaseDatabase.getInstance().getReference().child("Followers").child(Current_user);
        getAdmains=FirebaseDatabase.getInstance().getReference().child("Followers");
        avi=findViewById(R.id.showme);
        avi.show();
        recyclerView=findViewById(R.id.avrecycleview);
        getFollowersData();
        getAdmainsData();
        getPlayersdata();

    }
    private void getAdmainsData() {
        getAdmains.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                     for(DataSnapshot c:d.getChildren()) {
                         String Follower_key = c.child("FollowerKey").getValue().toString();
                      //   Toast.makeText(List_of_available_users.this, Follower_key, Toast.LENGTH_SHORT).show();
                         if (Follower_key.equals(Current_user)) {
                             followersKey.add(d.getKey());
                         }
                     }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public void getPlayersdata()
    {
        get_users= FirebaseDatabase.getInstance().getReference().child("UserInfo");
        get_users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren())
                {
                    key=d.getKey().toString();
                    if(followersKey.contains(key)) {
                        username = d.child("data").child("username").getValue().toString();
                        imageurl = d.child("data").child("imageurl").getValue().toString();
                        datauser.add(new AlluserModel(username, key, imageurl));
                    }

                }
                if(datauser.size()>0)
                {
                    avi.hide();
                    recyclerView.setLayoutManager(new LinearLayoutManager(List_of_available_users.this));
                    AllusersAdapter adapter = new  AllusersAdapter(datauser,getApplicationContext());
                    recyclerView.setAdapter(adapter);
                }
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
                 String Follower_key=d.child("FollowerKey").getValue().toString();
                    followersKey.add(Follower_key);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
