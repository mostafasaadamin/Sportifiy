package com.work.unknown.sportifiy.Details;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
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

import com.tapadoo.alerter.Alerter;

import com.work.unknown.sportifiy.Location.UserLocation;
import com.work.unknown.sportifiy.Models.MatchIfo;
import com.work.unknown.sportifiy.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

public class CreateMatch extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    String place;
    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int REQUEST_INTERNET =200 ;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    String Location="";
    DatabaseReference CreateMatchdata;
    FirebaseAuth mAuth;
    Button create;
    @Bind(R.id.Sport)
    Spinner Sport;
    @Bind(R.id.pickdate)
    EditText Pick_date;
    @Bind(R.id.pickTime)
    EditText Pick_Time;
    @Bind(R.id.Createflex)
    CheckBox Create_Flex;
    @Bind(R.id.pickPlace)
    EditText Pick_Place;
    @Bind(R.id.pickNote)
    EditText Note;
    DateFormat formatDateTime = DateFormat.getDateTimeInstance();
    java.util.Calendar dateTime = java.util.Calendar.getInstance();
    android.app.AlertDialog dialog;
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
        setContentView(R.layout.activity_create_match);
        ///////////////////////////////////////////////
        if (ContextCompat.checkSelfPermission(CreateMatch.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CreateMatch.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_INTERNET);

        }
        else  {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            createLocationRequest();
        }


        //////////////////////////////////////
        ButterKnife.bind(this);
        Pick_Place.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                   showMapDialog();

                }
            }
        });

        Pick_Time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showtimedialog();
                }

            }
        });
        Pick_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showDATEdialog();
                }

            }
        });
       create = findViewById(R.id.mn);
        mAuth = FirebaseAuth.getInstance();
        dialog = new SpotsDialog(this);
        CreateMatchdata = FirebaseDatabase.getInstance().getReference().child("CreatedMatchDataDetail").child(mAuth.getCurrentUser().getUid());
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                ch();
            }
        });
    }

    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }

    private void showDATEdialog() {
        new DatePickerDialog(this, d, dateTime.get(java.util.Calendar.YEAR), dateTime.get(java.util.Calendar.MONTH), dateTime.get(java.util.Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateTime.set(java.util.Calendar.YEAR, year);
            dateTime.set(java.util.Calendar.MONTH, monthOfYear);
            dateTime.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);
            //  Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateOnly = new SimpleDateFormat("MM/dd/yyyy");
            Pick_date.setText(dateOnly.format(dateTime.getTime()).toString());
        }
    };

    private void showtimedialog() {
        new TimePickerDialog(this, t, dateTime.get(java.util.Calendar.HOUR_OF_DAY), dateTime.get(java.util.Calendar.MINUTE), true).show();
    }

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateTime.set(java.util.Calendar.HOUR_OF_DAY, hourOfDay);
            dateTime.set(java.util.Calendar.MINUTE, minute);
            String timeComp = dateTime.get(java.util.Calendar.HOUR_OF_DAY) + ":" + dateTime.get(java.util.Calendar.MINUTE) + ":" + dateTime.get(java.util.Calendar.SECOND);
            Pick_Time.setText(timeComp);
        }
    };

    public void alerter(String v) {
        Alerter.create(CreateMatch.this)
                .setTitle("warning")
                .setText(v)
                .setBackgroundColorRes(R.color.darkDeepOrange)
                .setDuration(3000)
                .setIcon(R.drawable.alarm)
                .setTextTypeface(Typeface.createFromAsset(getAssets(), "Fonts/Arkhip_font.ttf"))
                .setTitleTypeface(Typeface.createFromAsset(getAssets(), "Fonts/Arkhip_font.ttf"))
                .show();
    }

    public boolean check() {
        if (TextUtils.isEmpty(Sport.getSelectedItem().toString())) {
            return false;
        } else if (TextUtils.isEmpty(Pick_date.getText().toString())) {
            return false;
        } else if (TextUtils.isEmpty(Pick_Time.getText().toString())) {
            return false;
        } else if (TextUtils.isEmpty(Pick_Place.getText().toString())) {
            return false;
        }
     else if(Location==null)
     {
         Toast.makeText(this, "Cant get your location", Toast.LENGTH_SHORT).show();
         return false;
     }

        else if (TextUtils.isEmpty(Note.getText().toString())) {
            return false;
        } else {
            return true;
        }
    }

    public void ch() {
        if (check()&&place!=null) {

            String pl =Pick_Place.getText().toString().concat("&").concat(place);
            MatchIfo Match = new MatchIfo(
                    Sport.getSelectedItem().toString()
                    , Pick_date.getText().toString()
                    , Pick_Time.getText().toString(),
                    pl,
                    Location
                    , Note.getText().toString());
            CreateMatchdata.push().setValue(Match);
            CreateMatchdata.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        dialog.dismiss();
                        Alerter.create(CreateMatch.this)
                                .setTitle("sucess")
                                .setText("info has been registered successfully")
                                .setBackgroundColorRes(R.color.darkGreen)
                                .setDuration(4000)
                                .setIcon(R.drawable.alarm)
                                .show();
                        Intent i = new Intent(CreateMatch.this, CreateOrSearch.class);
                        startActivity(i);
                    } else {
                        dialog.dismiss();
                        alerter("Error when trying to save data");

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            alerter("Please Fill All required Filleds");
        }

    }

    private void showMapDialog() {
//        final Dialog dialog = new Dialog(CreateMatch.this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        /////make map clear
//        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        dialog.setContentView(R.layout.picklocation);////your custom content
//        MapView mMapView = (MapView) dialog.findViewById(R.id.mapView);
//        MapsInitializer.initialize(CreateMatch.this);
//        mMapView.onCreate(dialog.onSaveInstanceState());
//        mMapView.onResume();
//        mMapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(final GoogleMap googleMap) {
//                LatLng posisiabsen = new LatLng(-34, 151); ////your lat lng
//                googleMap.addMarker(new MarkerOptions().position(posisiabsen).title("Yout title"));
//                googleMap.moveCamera(CameraUpdateFactory.newLatLng(posisiabsen));
//                googleMap.getUiSettings().setZoomControlsEnabled(true);
//                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
//            }
//        });
//        dialog.setTitle("Please Pic match Place");
//        dialog.show();
//        Button cancelButton = (Button) dialog.findViewById(R.id.cancel_map);
//// if button is clicked, close the custom dialog
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        Button okButton = (Button) dialog.findViewById(R.id.ok);
//// if button is clicked, close the custom dialog
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Pick_Place.setText();
//                Toast.makeText(CreateMatch.this, "ok clicked", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//            }
//        });
     Intent i=new Intent(CreateMatch.this, UserLocation.class);
     startActivityForResult(i,2);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2&&data!=null)
        {
             place=data.getStringExtra("place");
            String address=data.getStringExtra("address");
           Pick_Place.setText(address);

        }else
            {
            create.setEnabled(false);

        }
    }
    @Override
    protected void onDestroy() {
        Runtime.getRuntime().gc();
        super.onDestroy();
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

    @Override
    public void onConnected(Bundle bundle) {
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
            Location=currentLatitude+":"+currentLongitude;
                }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        Location=currentLatitude+":"+currentLongitude;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_INTERNET) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();
                createLocationRequest();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(CreateMatch.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(this, "Please agree permission:(", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(CreateMatch.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_INTERNET);
                } else {
                    Toast.makeText(this, "Exit :(", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        }
    }

}
