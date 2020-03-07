package com.work.unknown.sportifiy.Details;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tapadoo.alerter.Alerter;
import com.work.unknown.sportifiy.Adapter.Loadcreatedmatchandfollowing;
import com.work.unknown.sportifiy.Models.SearchModel;
import com.work.unknown.sportifiy.Profiles.CurrentUserProfile;
import com.work.unknown.sportifiy.R;
import com.work.unknown.sportifiy.RecyclesModels.CreatedMatchModel;
import com.work.unknown.sportifiy.RecyclesModels.searchmodel;
import com.work.unknown.sportifiy.info;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
public class SearchMatch extends AppCompatActivity {
   DatabaseReference getmatch;
   ArrayList<searchmodel>search_model_list=new ArrayList<>();
    List<String> spinnerDateArray =  new ArrayList<String>();
    List<String> spinnerAreaArray =  new ArrayList<String>();
    @Bind(R.id.selectdate)
    Spinner Select_date;
    @Bind(R.id.selectArea)
    Spinner Select_Area;
    @Bind(R.id.selectGame)
    Spinner Select_Game;
    @Bind(R.id.flecheck)
    CheckBox Flexiable;
    @Bind(R.id.Malecheck)
    CheckBox Male;
    @Bind(R.id.Femalecheck)
    CheckBox Female;
    @Bind(R.id.searchformatches)
    Button SearchFORMatches;
    android.app.AlertDialog dialog;
    DatabaseReference getAllMatchsData;
    ArrayAdapter<String> Areayadapter;
    ArrayAdapter<String> dateadapter;
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
        setContentView(R.layout.activity_search_match);
        ButterKnife.bind(this);
        getAllMatchsData = FirebaseDatabase.getInstance().getReference().child("CreatedMatchDataDetail");
        getMatchsDetails();
         Areayadapter = new ArrayAdapter<>(this, R.layout.spiner_items, spinnerAreaArray);
         dateadapter = new ArrayAdapter<>(this, R.layout.spiner_items, spinnerDateArray);
        dialog=new SpotsDialog(this);
        getmatch= FirebaseDatabase.getInstance().getReference().child("CreatedMatchDataDetail");
        SearchFORMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                getmatch.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            dialog.dismiss();
                            String key="";
                            for(DataSnapshot d:dataSnapshot.getChildren()) {
                                key=d.getKey().toString();
                                for(DataSnapshot singleUser:d.getChildren()) {
                                    ////////////////////////getDAta///////////////////////////////
                                    String Sport,Date,Time,Place,Location;
                                    Sport=singleUser.child("sport").getValue().toString();
                                    Date=singleUser.child("date").getValue().toString();
                                    Time=singleUser.child("time").getValue().toString();
                                    Place=singleUser.child("place").getValue().toString();
                                    Location=singleUser.child("location").getValue().toString();
                                    int index=Place.indexOf('&');
                                    String placeaddress=Place.substring(0,index);
                                    if(Sport!=null&&Date!=null&&Time!=null&&Place!=null&&Location!=null) {
                                        if (Select_date.getSelectedItem().toString().equals(Date) && Select_Game.getSelectedItem().toString().equals(Sport) && Select_Area.getSelectedItem().toString().equals(placeaddress)) {
                                            search_model_list.add(new searchmodel(Sport, Date, Time, Place, Location, key));
                                        }
                                    }
                                    ///////////////////////////////////////////////////////
                                    //    getMatchdata((Map<String, Object>)d.getValue(),d.getKey());
                                }
                            }
                        if(search_model_list.size()>0) {
                          Intent i = new Intent(SearchMatch.this, AvailableMatch.class);
                          Bundle b=new Bundle();
                          b.putSerializable("data",search_model_list);
                          i.putExtras(b);
                            startActivity(i);
                            search_model_list.clear();
                        }else
                        {
                            Toast.makeText(SearchMatch.this, "No Data to show", Toast.LENGTH_SHORT).show();
                        }


                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void getMatchsDetails() {
        getAllMatchsData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                    for(DataSnapshot d:dataSnapshot.getChildren()) {
                        for(DataSnapshot s:d.getChildren()) {
                            String Sport, Date, Time, Place, Location;
                            Sport = s.child("sport").getValue().toString();
                            Date = s.child("date").getValue().toString();
                            Time = s.child("time").getValue().toString();
                            Place = s.child("place").getValue().toString();
                            Location = s.child("location").getValue().toString();
                            int index=Place.indexOf('&');
                            String placeaddress=Place.substring(0,index);
                            spinnerAreaArray.add(placeaddress);
                            spinnerDateArray.add(Date);
                       //     Toast.makeText(SearchMatch.this, Sport + ":" + Date + ":" + Time + ":" + Place + ":" + Location, Toast.LENGTH_SHORT).show();

                        }}


                }
                //Areayadapter.addAll(spinnerAreaArray);
                //dateadapter.addAll(spinnerDateArray);
                Select_date.setAdapter(dateadapter);
                Select_Area.setAdapter(Areayadapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

    }
}

