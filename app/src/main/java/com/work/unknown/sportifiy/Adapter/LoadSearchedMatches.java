package com.work.unknown.sportifiy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;
import com.work.unknown.sportifiy.Profiles.AnotherUserProfile;
import com.work.unknown.sportifiy.R;
import com.work.unknown.sportifiy.RecyclesModels.CreatedMatchModel;
import com.work.unknown.sportifiy.RecyclesModels.searchmodel;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by unknown on 2/6/2018.
 */

public class LoadSearchedMatches extends RecyclerView.Adapter<LoadSearchedMatches.typeholder>{
    ArrayList<searchmodel> detail;
    DatabaseReference add_follower;
    FirebaseAuth mAuth;
    Context con;
    String key;
    searchmodel po;
    protected HashSet<MapView> mMapViews = new HashSet<>();
    public LoadSearchedMatches(ArrayList<searchmodel>detail,Context con)
    {
        this.detail=detail;
        this.con=con;
    }
    @Override
    public LoadSearchedMatches.typeholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View form= LayoutInflater.from(parent.getContext()).inflate(R.layout.searchmatchcard,parent,false);
        typeholder type=new typeholder(form);
        mMapViews.add(type.mapView);
        return type;
    }
    @Override
    public void onBindViewHolder(typeholder holder, int position) {
         po=detail.get(position);
        String location=po.getPlace();
        int index=location.indexOf('&');
        String placeaddress=location.substring(index+1,location.length()-1);
        int in=placeaddress.indexOf(':');
        String latitude=placeaddress.substring(0,in);
        String longtitude=placeaddress.substring(in+1,placeaddress.length());
        double lat=Double.parseDouble(latitude);
        double lng=Double.parseDouble(longtitude);
        LatLng coords = new LatLng(lat, lng);
        holder.Sport.setText(po.getSport());
        holder.Date.setText(po.getDate());
        holder.Location.setText(po.getLocation());
        holder.Place.setText(po.getPlace());
        key=po.getKey();
        holder.creatorid.setText(key);
        holder.setMapLocation(coords);
    }
    @Override
    public int getItemCount() {
        return detail.size();
    }
    public HashSet<MapView> getMapViews() {
        return mMapViews;
    }
    class typeholder extends RecyclerView.ViewHolder implements OnMapReadyCallback
    {
        SparkButton AddMatch;
        SparkButton LoveMatch;
        TextView Sport,Date,Location,Place,CREAT;
        TextView creatorid;
        ////////////////////////////////////////////
        protected GoogleMap mGoogleMap;
        protected LatLng mMapLocation;
        public MapView mapView;
        /////////////////////////////////////////////////
        public typeholder(View itemView) {
            super(itemView);
            ///////////////////////////try/ map view/////////////////////////////////////////
            mapView = (MapView) itemView.findViewById(R.id.img);
            mapView.onCreate(null);
            mapView.getMapAsync(this);
            /////////////////////////////////////////////////////////////////////
            Sport=itemView.findViewById(R.id.sportvalue);
            Date=itemView.findViewById(R.id.datevalue);
            Location=itemView.findViewById(R.id.locationvalue);
            Place=itemView.findViewById(R.id.placevalue);
          //  CREAT=itemView.findViewById(R.id.creatormatchvalue);
            AddMatch=itemView.findViewById(R.id.add_Match);
            LoveMatch=itemView.findViewById(R.id.Love_Match);
            creatorid=itemView.findViewById(R.id.creatormatchvalue);
            creatorid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(con, AnotherUserProfile.class);
                    i.putExtra("userid",creatorid.getText().toString());
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    con.startActivity(i);
                }
            });
//            AddMatch.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mAuth=FirebaseAuth.getInstance();
//                    String keey=mAuth.getCurrentUser().getUid();
//                    add_follower= FirebaseDatabase.getInstance().getReference().child("Followers").child(key).push();
//                    add_follower.child("FollowerKey").setValue(keey);
//                    add_follower.child("Sport").setValue(po.getSport());
//                    add_follower.child("Place").setValue(po.getPlace());
//                    add_follower.child("Date").setValue(po.getDate());
//                    add_follower.child("Location").setValue(po.getLocation());
//                    add_follower.child("Time").setValue(po.getTime());
//                    add_follower.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            if(dataSnapshot.exists())
//                            {
//                                Toast.makeText(con, "You joined now", Toast.LENGTH_SHORT).show();
//
//                            }
//                            else
//                             {
//                                    Toast.makeText(con, "error occured", Toast.LENGTH_SHORT).show();
//                             }
//
//                        }
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                        }
//                    });
//                }
//            });

//            LoveMatch.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(con, "Loveclicked", Toast.LENGTH_SHORT).show();
//
//                }
//            });
            LoveMatch.setEventListener(new SparkEventListener(){
                @Override
                public void onEvent(ImageView button, boolean buttonState) {
                    if (buttonState) {
                        LoveMatch.setChecked(true);
                    } else {
                        // Button is inactive
                    }
                }

                @Override
                public void onEventAnimationEnd(ImageView button, boolean buttonState) {

                }

                @Override
                public void onEventAnimationStart(ImageView button, boolean buttonState) {

                }
            });
            AddMatch.setEventListener(new SparkEventListener(){
                @Override
                public void onEvent(ImageView button, boolean buttonState) {
                    if (buttonState) {
                        mAuth=FirebaseAuth.getInstance();
                        String keey=mAuth.getCurrentUser().getUid();
                        add_follower= FirebaseDatabase.getInstance().getReference().child("Followers").child(key).push();
                        add_follower.child("FollowerKey").setValue(keey);
                        add_follower.child("Sport").setValue(po.getSport());
                        add_follower.child("Place").setValue(po.getPlace());
                        add_follower.child("Date").setValue(po.getDate());
                        add_follower.child("Location").setValue(po.getLocation());
                        add_follower.child("Time").setValue(po.getTime());
                        add_follower.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists())
                                {
                                    Toast.makeText(con, "You joined now", Toast.LENGTH_SHORT).show();
                                    AddMatch.setEnabled(false);
                                }
                                else
                                {
                                    Toast.makeText(con, "error occured", Toast.LENGTH_SHORT).show();
                                }

                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                        AddMatch.setChecked(true);
                    } else {
                        // Button is inactive
                    }
                }

                @Override
                public void onEventAnimationEnd(ImageView button, boolean buttonState) {

                }

                @Override
                public void onEventAnimationStart(ImageView button, boolean buttonState) {

                }
            });
        }
        /////////////////////////try map view//////////////////////////////
        public void setMapLocation(LatLng mapLocation) {
            mMapLocation = mapLocation;
            // If the map is ready, update its content.
            if (mGoogleMap != null) {
                updateMapContents();
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;
            MapsInitializer.initialize(con);
            googleMap.getUiSettings().setMapToolbarEnabled(false);
            if (mMapLocation != null) {
                updateMapContents();
            }
        }
        protected void updateMapContents() {
            // Since the mapView is re-used, need to remove pre-existing mapView features.
            mGoogleMap.clear();
            // Update the mapView feature data and camera position.
            mGoogleMap.addMarker(new MarkerOptions().position(mMapLocation));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mMapLocation, 17.2f);
            mGoogleMap.moveCamera(cameraUpdate);
        }



        //////////////////////////////////////////////////////////

    }

}
