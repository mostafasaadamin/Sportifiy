package com.work.unknown.sportifiy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.varunest.sparkbutton.SparkButton;
import com.work.unknown.sportifiy.Models.followersmodel;
import com.work.unknown.sportifiy.Profiles.AnotherUserProfile;
import com.work.unknown.sportifiy.R;
import com.work.unknown.sportifiy.RecyclesModels.searchmodel;

import java.util.ArrayList;
import java.util.HashSet;

public class followersAdapter extends RecyclerView.Adapter<followersAdapter.typeholder> {
    DatabaseReference remove_gust;
    FirebaseAuth mAuth;
    DatabaseReference notify;
    ArrayList<followersmodel> detail;
    Context con;
    String key;
    protected HashSet<MapView> mMapViews = new HashSet<>();

    public followersAdapter(ArrayList<followersmodel> detail, Context con) {
        this.detail = detail;
        this.con = con;
    }

    @Override
    public followersAdapter.typeholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View form = LayoutInflater.from(parent.getContext()).inflate(R.layout.folloersrequestscard, parent, false);
        followersAdapter.typeholder type = new followersAdapter.typeholder(form);
        mMapViews.add(type.mapView);
        return type;
    }

    @Override
    public void onBindViewHolder(final followersAdapter.typeholder holder, final int position) {
        final followersmodel po = detail.get(position);
        int index = po.getPlace().indexOf('&');
        String pl = po.getPlace().substring(0, index);
        holder.date.setText(po.getDate());
        holder.follower_key.setText(po.getFollower_key());
        holder.place.setText(pl);
        holder.time.setText(po.getTime());
        holder.sport.setText(po.getSport());
        ///////////////////////Get Location///////////////////////////
        String location = po.getPlace();
        String placeaddress = location.substring(index + 1, location.length() - 1);
        int in = placeaddress.indexOf(':');
        String latitude = placeaddress.substring(0, in);
        String longtitude = placeaddress.substring(in + 1, placeaddress.length());
        double lat = Double.parseDouble(latitude);
        double lng = Double.parseDouble(longtitude);
        LatLng coords = new LatLng(lat, lng);
        holder.setMapLocation(coords);

        //////////////////////////////////////////////////////////////
        mAuth = FirebaseAuth.getInstance();
        remove_gust = FirebaseDatabase.getInstance().getReference().child("Followers").child(mAuth.getCurrentUser().getUid());
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = FirebaseDatabase.getInstance().getReference().child("Notification").child(po.getFollower_key()).push();
                notify.child("status").setValue("Sorry your request had been  rejected");
                notify.child("rejectoracceptuserkey").setValue(mAuth.getCurrentUser().getUid());
                notify.child("Sport").setValue(po.getSport());
                notify.child("Place").setValue(po.getPlace());
                notify.child("Date").setValue(po.getDate());
                notify.child("Location").setValue(po.getLocation());
                notify.child("Time").setValue(po.getTime());
                followersmodel posit = detail.get(position);
                detail.remove(posit);
                notifyItemRemoved(position);
                notifyItemRangeRemoved(position, getItemCount());
                notify.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                 if (dataSnapshot.exists()) {
                     holder.reject.setChecked(true);
                     Toast.makeText(con, "player rejected ", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = FirebaseDatabase.getInstance().getReference().child("Notification").child(po.getFollower_key()).push();
                notify.child("status").setValue("congratulations your request had beenaccepted");
                notify.child("rejectoracceptuserkey").setValue(mAuth.getCurrentUser().getUid());
                notify.child("Sport").setValue(po.getSport());
                notify.child("Place").setValue(po.getPlace());
                notify.child("Date").setValue(po.getDate());
                notify.child("Location").setValue(po.getLocation());
                notify.child("Time").setValue(po.getTime());
                notify.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(con, "player accepted ", Toast.LENGTH_SHORT).show();
                            holder.accept.setChecked(true);
                         //   holder.cardView.setEnabled(false);
                            holder.accept.setOnClickListener(null);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        });
        holder.follower_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(con, AnotherUserProfile.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("userid", po.getFollower_key());
                con.startActivity(i);
            }
        });
    }

    public HashSet<MapView> getMapViews() {
        return mMapViews;
    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    class typeholder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        protected GoogleMap mGoogleMap;
        protected LatLng mMapLocation;
        public MapView mapView;
        SparkButton accept, reject;
        TextView date, place, follower_key, time, sport;
        CardView cardView;

        public typeholder(View itemView) {
            super(itemView);
            accept = itemView.findViewById(R.id.accept);
            reject = itemView.findViewById(R.id.cancel);
            date = itemView.findViewById(R.id.datevalue);
            place = itemView.findViewById(R.id.placevalue);
            follower_key = itemView.findViewById(R.id.creatormatchvalue);//Follower key dont look to id
            time = itemView.findViewById(R.id.locationvalue);// time dont look to id;
            sport = itemView.findViewById(R.id.sportvalue);
            mapView = (MapView) itemView.findViewById(R.id.img);
            mapView.onCreate(null);
            mapView.getMapAsync(this);
            cardView = itemView.findViewById(R.id.followers_card);
        }

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
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mMapLocation, 10f);
            mGoogleMap.moveCamera(cameraUpdate);
        }


    }
}
