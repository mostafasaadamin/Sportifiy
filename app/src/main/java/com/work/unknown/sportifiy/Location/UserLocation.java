package com.work.unknown.sportifiy.Location;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.SharedPreferencesCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.work.unknown.sportifiy.R;
import com.work.unknown.sportifiy.Retrofit.IGoCordinates;
import com.work.unknown.sportifiy.Retrofit.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLocation extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
  //  Address address;
    LatLng orderLocation;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int REQUEST_INTERNET = 200;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude = 0.0;
    private double currentLongitude = 0.0;
    private GoogleMap mMap;
    EditText searchdata;
    private IGoCordinates mService;
    ImageView search;
String addres;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location);
        mService= common.getGeoCodeService();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
       searchdata = findViewById(R.id.Search_input);
        if (ContextCompat.checkSelfPermission(UserLocation.this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UserLocation.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_INTERNET);

        }else
        {
            creategoogleapirequest();
        }

        search = findViewById(R.id.img);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        geoLocate();
            }
        });

    }
    private void creategoogleapirequest() {
        if (ContextCompat.checkSelfPermission(UserLocation.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UserLocation.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_INTERNET);
        } else {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            createLocationRequest();
        }
    }
    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }
    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }


    }
    private void geoLocate() {
        if (!TextUtils.isEmpty(searchdata.getText().toString())) {
            String searchString = searchdata.getText().toString();
//            Geocoder geocoder = new Geocoder(UserLocation.this);
//            List<Address> list = new ArrayList<>();
//            try {
//
//                list = geocoder.getFromLocationName(searchString, 1);
//            } catch (IOException ex) {
//                Log.e("errtot", "geoLocate: " + ex.getMessage().toString());
//            }
//            if (list.size() > 0) {
//                address = list.get(0);
//                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                mMap.clear();
//                LatLng sydney = new LatLng(address.getLatitude(), address.getLongitude());
//                mMap.addMarker(new MarkerOptions().position(sydney).title(address.getAddressLine(0)));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10.5f));
//            } else {
//                Toast.makeText(this, "No  Address Found", Toast.LENGTH_SHORT).show();
//                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//            }
//
//        } else {
//            Toast.makeText(this, "Enter Address", Toast.LENGTH_SHORT).show();
//        }

            mService.getGeocode(searchString).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try{
                        JSONObject jsonObject=new JSONObject(response.body().toString());
                        String lat=((JSONArray)jsonObject.get("results"))
                                .getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat").toString();
                        String lng=((JSONArray)jsonObject.get("results"))
                                .getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng").toString();
//addres=((JSONArray)jsonObject.get("results")).get("formatted_address");
                         orderLocation = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.map);
                        bitmap=common.scaleBitmap(bitmap,70,70);
                        //  Toast.makeText(trackcustomer.this, lat+":"+lng, Toast.LENGTH_SHORT).show();
                        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap)).position(orderLocation).title("match Location:"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(orderLocation,17.0f));

                    }catch(JSONException ex)
                    {
                        Log.i("err", ex.getMessage().toString());
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });


        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (currentLatitude != 0.0 && currentLongitude != 0.0) {
            LatLng sydney = new LatLng(currentLatitude, currentLongitude);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }else
            {
                //Toast.makeText(this, "cant get location"+currentLatitude+":"+currentLongitude, Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            LatLng sydney = new LatLng(currentLatitude, currentLongitude);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Your Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,12.2f));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        if(orderLocation!=null) {
            String loc = orderLocation.latitude + ":" + orderLocation.longitude;
            ArrayList<String> full = findaddress(orderLocation.latitude, orderLocation.longitude);
           // Toast.makeText(this, loc, Toast.LENGTH_SHORT).show();
            String address=full.get(0)+full.get(1)+full.get(2)+full.get(3);
            intent.putExtra("place", loc);
            intent.putExtra("address", address);
            setResult(2, intent);
            finish();//finishing activity
        }else
            {
                Toast.makeText(this, "You must select place", Toast.LENGTH_SHORT).show();
              finish();
            }
        super.onBackPressed();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_INTERNET) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                creategoogleapirequest();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(UserLocation.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "You can not know your location  please accept the permission", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(UserLocation.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_INTERNET);
                } else {
                    Toast.makeText(this, "Exit", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        }
    }
    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();

    }
    public ArrayList<String> findaddress(double lat, double lng) {
        ArrayList<String> fulladdress = new ArrayList<>();
        Geocoder geocoder;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null && addresses.size() > 0) {
                //   String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String knownName = addresses.get(0).getFeatureName();
                fulladdress.add(country);
                fulladdress.add(state);
                fulladdress.add(city);
                fulladdress.add(knownName);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fulladdress;
    }

}
