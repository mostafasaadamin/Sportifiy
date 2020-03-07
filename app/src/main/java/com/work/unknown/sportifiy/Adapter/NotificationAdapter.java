package com.work.unknown.sportifiy.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.work.unknown.sportifiy.Models.NotificationModel;
import com.work.unknown.sportifiy.Models.followersmodel;
import com.work.unknown.sportifiy.R;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by unknown on 2/23/2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.notificationholder> {
    ArrayList<NotificationModel> notifyList = new ArrayList<>();
    Context context;
    FirebaseAuth mAuth;
    DatabaseReference remove_notification;
    protected HashSet<MapView> mMapViews = new HashSet<>();
    String reject_OR_accept_user_key;

    public NotificationAdapter(ArrayList<NotificationModel> notifyList, Context context) {
        this.notifyList = notifyList;
        this.context = context;
    }

    @Override
    public notificationholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View form = LayoutInflater.from(parent.getContext()).inflate(R.layout.notificationcardview, parent, false);
        NotificationAdapter.notificationholder notify = new NotificationAdapter.notificationholder(form);
        mMapViews.add(notify.mapView);
        return notify;
    }

    @Override
    public void onBindViewHolder(notificationholder holder, final int position) {
        final NotificationModel po = notifyList.get(position);
        holder.status.setText(po.getStatus());
        holder.Time.setText(po.getDate().concat("Time :").concat(po.getTime().substring(0, 5)));
        LatLng coords = getLatLng(po.getPlace());

        holder.setMapLocation(coords);
    }

    @Override
    public int getItemCount() {
        return notifyList.size();
    }

    public HashSet<MapView> getMapViews() {
        return mMapViews;
    }

    class notificationholder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        TextView status;
        TextView Time;
        protected GoogleMap mGoogleMap;
        protected LatLng mMapLocation;
        public MapView mapView;

        public notificationholder(View itemView) {
            super(itemView);
            Time = itemView.findViewById(R.id.curreentTime);
            status = itemView.findViewById(R.id.Matchresponse);
            mapView = (MapView) itemView.findViewById(R.id.notifacationmap);
            mapView.onCreate(null);
            mapView.getMapAsync(this);
        }

        public void setMapLocation(LatLng mapLocation) {
            mMapLocation = mapLocation;
            if (mGoogleMap != null) {
                updateMapContents();
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;
            MapsInitializer.initialize(context);
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
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mMapLocation, 12.5f);
            mGoogleMap.moveCamera(cameraUpdate);
        }


    }

    public LatLng getLatLng(String place) {
        int index = place.indexOf('&');
        String location = place;
        String placeaddress = location.substring(index + 1, location.length() - 1);
        int in = placeaddress.indexOf(':');
        String latitude = placeaddress.substring(0, in);
        String longtitude = placeaddress.substring(in + 1, placeaddress.length());
        double lat = Double.parseDouble(latitude);
        double lng = Double.parseDouble(longtitude);
        LatLng coords = new LatLng(lat, lng);
        return coords;
    }
}
