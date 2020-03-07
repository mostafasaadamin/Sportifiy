package com.work.unknown.sportifiy.Notification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;
import com.work.unknown.sportifiy.Adapter.NotificationAdapter;
import com.work.unknown.sportifiy.Adapter.followersAdapter;
import com.work.unknown.sportifiy.Followers.followermatchs;
import com.work.unknown.sportifiy.Models.NotificationModel;
import com.work.unknown.sportifiy.Models.followersmodel;
import com.work.unknown.sportifiy.R;

import java.util.ArrayList;
import java.util.Map;

public class notification extends AppCompatActivity {
    ArrayList<NotificationModel> notify = new ArrayList<>();
    DatabaseReference getnotify;
    FirebaseAuth mAuth;
    NotificationAdapter adapter;
    RecyclerView notification_recycle;
    AVLoadingIndicatorView avi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        notification_recycle = findViewById(R.id.notifcation_recy);
        avi = findViewById(R.id.notyfme);
        avi.show();
        mAuth = FirebaseAuth.getInstance();
        getnotify = FirebaseDatabase.getInstance().getReference().child("Notification").child(mAuth.getCurrentUser().getUid());
        getnotify.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                 //   Toast.makeText(notification.this, d.toString(), Toast.LENGTH_SHORT).show();
                    Log.i("asdya", "onDataChange: "+d.toString());
                    String status = d.child("status").getValue().toString();
                   String key = d.child("rejectoracceptuserkey").getValue().toString();
                    String Date= d.child("Date").getValue().toString();
                    String time=d.child("Time").getValue().toString();
                    String place=d.child("Place").getValue().toString();
                    String sport=d.child("Sport").getValue().toString();
                    if (status != null && key != null) {
                        notify.add(new NotificationModel(status,Date,place,sport,time,key));
                   }
                 //   Toast.makeText(notification.this,status, Toast.LENGTH_SHORT).show();
                }
                if (notify.size() > 0) {
                    avi.hide();
                    notification_recycle.setLayoutManager(new LinearLayoutManager(notification.this));
                    adapter = new NotificationAdapter(notify, getApplicationContext());
                    notification_recycle.setAdapter(adapter);
                } else {
                    avi.hide();
                    Toast.makeText(notification.this, "No Notification to show", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

//    @Override
//    protected void onDestroy() {
//        Runtime.getRuntime().gc();
//        super.onDestroy();
//    }
//


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

//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//
//        if (resultCode == ConnectionResult.SUCCESS) {
//            mRecyclerView.setAdapter(mListAdapter);
//        } else {
//            GooglePlayServicesUtil.getErrorDialog(resultCode, this, 1).show();
//        }

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
