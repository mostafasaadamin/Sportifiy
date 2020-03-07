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
import com.work.unknown.sportifiy.Models.followersmodel;
import com.work.unknown.sportifiy.Models.followingModel;
import com.work.unknown.sportifiy.Profiles.AnotherUserProfile;
import com.work.unknown.sportifiy.R;
import com.work.unknown.sportifiy.RecyclesModels.searchmodel;

import java.util.ArrayList;
import java.util.HashSet;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.typeholder> {
    DatabaseReference remove_gust;
    FirebaseAuth mAuth;
    ArrayList<followingModel> detail;
    Context con;
    protected HashSet<MapView> mMapViews = new HashSet<>();
    public FollowingAdapter(ArrayList<followingModel> detail, Context con) {
        this.detail = detail;
        this.con = con;
    }
    @Override
    public FollowingAdapter.typeholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View form = LayoutInflater.from(parent.getContext()).inflate(R.layout.followingcardview, parent, false);
        FollowingAdapter.typeholder type = new FollowingAdapter.typeholder(form);
        mMapViews.add(type.mapView);
        return type;
    }
    @Override
    public void onBindViewHolder(final FollowingAdapter.typeholder holder, final int position) {
        final followingModel po = detail.get(position);
        int index = po.getPlace().indexOf('&');
        String pl = po.getPlace().substring(0, index);
        holder.date.setText(po.getDate());
        holder.creator_key.setText(po.getCreator_key());
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

        holder.creator_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(con, AnotherUserProfile.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("userid", po.getCreator_key());
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
        ImageView accept, reject;
        TextView date, place, creator_key, time, sport;
        CardView cardView;

        public typeholder(View itemView) {
            super(itemView);
            accept = itemView.findViewById(R.id.accept);
            reject = itemView.findViewById(R.id.cancel);
            date = itemView.findViewById(R.id.datevalue);
            place = itemView.findViewById(R.id.placevalue);
            creator_key = itemView.findViewById(R.id.creatormatchvalue);//Follower key dont look to id
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
