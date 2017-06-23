package com.example.admin.testgrable;

import android.Manifest;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;

import android.widget.ImageButton;

import android.widget.TextView;
import android.widget.Toast;


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



public class MainActivity extends FragmentActivity implements OnMapReadyCallback, DerectionListener {
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private boolean gps, isOnline;
    private String provider;
    private BroadcastReceiver broadcastReceiver;
    private Location mlocation;
    private TextView edtDestination, edtOrigin;
    private Button btn_find;
    private ImageButton imgButton, imggetweather;
    private LatLng myLatlng;
    private LatLng latLngor, latLnged;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 100;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODES = 200;
    private AlertDialog dialog;
    private LocationListener listener;
    private boolean booleanOrigin = false, booleanDestination = false;

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
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocation(mlocation);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                updateLocation(mlocation);
            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

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
                Log.i("place", "canceled");
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
                Log.i("place", "canceled");
            }
        }
    }

    public void init() {
        imggetweather = (ImageButton) findViewById(R.id.btngetwearther);
        imggetweather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myLocationNow(mlocation);
                new CallWeather(MainActivity.this, myLatlng).createRetrofit();
            }
        });
        edtDestination = (TextView) findViewById(R.id.edtDestination);
        edtDestination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                booleanDestination = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
        edtOrigin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                booleanDestination = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    booleanOrigin = true;
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(MainActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
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
        if (booleanOrigin && booleanDestination) {
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
        checkConnectActivity();
        if (isOnline) {
            updateLocation(mlocation);
        }
        checkPermissions();
        checkGps();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                checkPermissions();
                break;
        }
    }

    public void myLocationNow(Location location) {
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addressList.size() > 0) {
                Double lat = addressList.get(0).getLatitude();
                Double longt = addressList.get(0).getLongitude();
                myLatlng = new LatLng(lat, longt);
                updateLocation(mlocation);

            }
        } catch (IOException e) {
            e.printStackTrace();
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
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, listener);
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
    public void getWeartherSucces(InforWeather inforWeather) {
        Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
        Log.e("sssss", inforWeather.toString());
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.customdialog, null);
        alertdialog.setView(dialogView);
        final TextView temp = (TextView) dialogView.findViewById(R.id.temp);
        temp.setText(inforWeather.getObmain().getTemp() + " °C");
        //set dialog message
        alertdialog.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }
        );
        //create alerDialog
        AlertDialog createDialog = alertdialog.create();
        //show
        createDialog.show();
    }
}
