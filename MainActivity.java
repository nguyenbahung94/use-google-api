package com.example.nbhung.mymap;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nbhung.mymap.API.Directions;
import com.example.nbhung.mymap.Model.leg;
import com.example.nbhung.mymap.View.DerectionListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener, DerectionListener {
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private boolean gps, isOnline;
    private ProgressDialog mProgressDialog;
    private String provider;
    private BroadcastReceiver broadcastReceiver;
    private Location mlocation;
    private TextView edtDestination, edtOrigin;
    private Button btn_find;
    private ImageButton imgButton;
    private LatLng myLatlng;
    private LatLng latLngor, latLnged;
    private ProgressDialog mprogressDialog;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 100;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODES = 200;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createDialogWait();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mlocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        init();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                edtOrigin.setText(place.getAddress());
                latLngor = place.getLatLng();
                Log.e("placeId", String.valueOf(place.getLatLng()));
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i("place", status.getStatusMessage());
            } else if (requestCode == RESULT_CANCELED) {

            }
        }
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODES) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                edtDestination.setText(place.getAddress());
                latLnged = place.getLatLng();
                Log.e("placeId", String.valueOf(place.getLatLng()));
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i("place", status.getStatusMessage());
            } else if (requestCode == RESULT_CANCELED) {

            }
        }
    }

    public void init() {
        edtDestination = (TextView) findViewById(R.id.edtDestination);
        edtDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(MainActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODES);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
        edtOrigin = (TextView) findViewById(R.id.edtOrigin);
        edtOrigin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(MainActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
                return false;
            }
        });
        btn_find = (Button) findViewById(R.id.btnFind);
        imgButton = (ImageButton) findViewById(R.id.btn_mlocation);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyLocation();
            }
        });
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starFind();
            }
        });
    }


    public void getMyLocation() {
        getCityLocation(mlocation);
    }

    public void starFind() {
        if (edtDestination.getText() != null && edtOrigin.getText() != null) {
            if (edtOrigin.getText().toString().equalsIgnoreCase("your location!")) {
                new Directions(this, myLatlng, latLnged).createRitrofit();
            } else {
                new Directions(this, latLngor, latLnged).createRitrofit();
            }

        } else {
            showDialog("Error", "Destination and Origin not empty!");
        }
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

        mMap = googleMap;
        checkPermissions();
        checkConnectActivity();
        if (isOnline) {
            updateLocation(mlocation);
        }
        checkGps();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


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

    public void getCityLocation(Location location) {
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addressList.size() > 0) {
                edtOrigin.setText("Your Location!");
                Double lat = addressList.get(0).getLatitude();
                Double longt = addressList.get(0).getLongitude();
                myLatlng = new LatLng(lat, longt);
                updateLocation(mlocation);
                Log.e("ssss", String.valueOf(myLatlng));
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
        if (location != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.me)).position(new LatLng(location.getLatitude(), location.getLongitude())));
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
    }

    public void createDialogWait() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Loading.............");
        builder.setMessage("Please wait..........");
        AlertDialog dialog = builder.create();
    }

    @Override
    public void onDirecterStart() {
        mMap.clear();
        Toast.makeText(getApplicationContext(), "Please wait....", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDirecterSuccess(leg leg, List<LatLng> listPoint) {
        if (listPoint.size() == 0) {
            showDialog("Error", "Tai Du Lieu Khong Thanh Cong");
        } else {
            Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_LONG).show();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(leg.getStartLocation().getLat(), leg.getStartLocation().getLng()), 15));
            ((TextView) findViewById(R.id.tvDuration)).setText(leg.getDuration().getText());
            ((TextView) findViewById(R.id.tvDistance)).setText(leg.getDistance().getText());

            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.place)).title(leg.getStartAdd()).position(new LatLng(leg.getStartLocation().getLat(), leg.getStartLocation().getLng())));
            mMap.addMarker((new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.place)).title(leg.getEndAdd()).position(new LatLng(leg.getEndLocation().getLat(), leg.getEndLocation().getLng()))));

            PolylineOptions polylineOptions = new PolylineOptions().geodesic(true).color(Color.GREEN).width(5);
            for (int i = 0; i < listPoint.size(); i++) {
                polylineOptions.add(listPoint.get(i));
            }
            mMap.addPolyline(polylineOptions);
        }
    }

    @Override
    public void onMyLocationChange(Location location) {

    }
}
