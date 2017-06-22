package com.example.admin.maplocation;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.admin.maplocation.Model.leg;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DerectionListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private boolean gps, isOnline;
    private ProgressDialog mProgressDialog;
    private String provider;
    private BroadcastReceiver broadcastReceiver;
    private Location mlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mlocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        checkGps();
        checkConnectActivity();
        mMap = googleMap;
        checkPermissions();
        updateLocation(mlocation);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        new Directions(this, "benthanh", "quan9").createRitrofit();


//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                checkPermissions();
                break;
        }
    }

    public void getCityLocation(double lat, double longt) {
        String cityName = "";
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocation(lat, longt, 1);
            if (addressList.size() > 0) {
                cityName = addressList.get(0).getLocality();
                Log.e("cityName", cityName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkGps() {
        gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gps) {
            showDialog("Gps not enable", "you need to enable gps");
        }
        Log.e("gps", String.valueOf(gps));
    }

    public void updateLocation(Location location) {
        if (location!=null){
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
        }

    }

    public void showDialog(String title, String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle(title);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void checkConnectActivity() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            isOnline = false;
            Log.e("isOnline", "false");

        } else {
            if (info.getType() == ConnectivityManager.TYPE_WIFI || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                isOnline = true;
                Log.e("isOnline", "true");
            } else {
                isOnline = false;
                Log.e("isOnline", "false");

            }
        }
        if (!isOnline) {
            showDialog("not connect", "you need to enable network");
        }
    }

    public void checkPermissions() {
        if (gps) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                            , 10);
                }
                return;
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();



    }

    @Override
    protected void onResume() {
        super.onResume();
        checkConnectActivity();
    }

    @Override
    public void onDirecterStart() {

    }

    @Override
    public void onDirecterSuccess(leg leg, List<LatLng> listPoint) {

    }

}
