package com.work.unknown.sportifiy.Details;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.MapView;
import com.work.unknown.sportifiy.Adapter.LoadSearchedMatches;
import com.work.unknown.sportifiy.R;
import com.work.unknown.sportifiy.RecyclesModels.searchmodel;
import java.util.ArrayList;

public class AvailableMatch extends AppCompatActivity {
    ArrayList<searchmodel>getmatch=new ArrayList<>();
    RecyclerView cratedMatches;
    LoadSearchedMatches adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_match);
        cratedMatches=findViewById(R.id.avmatch);
         Bundle b=getIntent().getExtras();
        getmatch= (ArrayList<searchmodel>) b.getSerializable("data");
       // Toast.makeText(this,getmatch.get(0).getLocation(), Toast.LENGTH_SHORT).show();
        cratedMatches.setLayoutManager(new LinearLayoutManager(AvailableMatch.this));
         adapter = new  LoadSearchedMatches(getmatch,getApplicationContext());
        cratedMatches.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
        getmatch.clear();
        super.onBackPressed();
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
