 package com.example.adam.mapapi2;

 import android.app.AlertDialog;
 import android.app.ProgressDialog;
 import android.content.Context;
 import android.content.DialogInterface;
 import android.content.Intent;
 import android.content.pm.PackageManager;
 import android.database.sqlite.SQLiteDatabase;
 import android.graphics.Color;
 import android.location.Geocoder;
 import android.location.Location;
 import android.location.LocationListener;
 import android.location.LocationManager;
 import android.net.ConnectivityManager;
 import android.os.Build;
 import android.os.Bundle;
 import android.support.annotation.NonNull;
 import android.support.v4.app.ActivityCompat;
 import android.support.v4.app.FragmentManager;
 import android.support.v4.widget.DrawerLayout;
 import android.support.v7.app.ActionBarDrawerToggle;
 import android.support.v7.app.AppCompatActivity;
 import android.support.v7.widget.Toolbar;
 import android.util.Log;
 import android.util.TypedValue;
 import android.view.Gravity;
 import android.view.LayoutInflater;
 import android.view.Menu;
 import android.view.MenuItem;
 import android.view.View;
 import android.widget.AdapterView;
 import android.widget.ArrayAdapter;
 import android.widget.AutoCompleteTextView;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.ImageButton;
 import android.widget.LinearLayout;
 import android.widget.ListView;
 import android.widget.Spinner;
 import android.widget.TextView;
 import android.widget.Toast;

 import com.google.android.gms.common.ConnectionResult;
 import com.google.android.gms.common.GooglePlayServicesUtil;
 import com.google.android.gms.common.api.GoogleApiClient;
 import com.google.android.gms.maps.CameraUpdateFactory;
 import com.google.android.gms.maps.GoogleMap;
 import com.google.android.gms.maps.OnMapReadyCallback;
 import com.google.android.gms.maps.SupportMapFragment;
 import com.google.android.gms.maps.model.BitmapDescriptorFactory;
 import com.google.android.gms.maps.model.LatLng;
 import com.google.android.gms.maps.model.Marker;
 import com.google.android.gms.maps.model.MarkerOptions;
 import com.google.android.gms.maps.model.Polyline;
 import com.google.android.gms.maps.model.PolylineOptions;

 import java.io.UnsupportedEncodingException;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Locale;

 import Model.BenhViens;
 import Model.ConnectSQL;
 import Model.DerectionListener;
 import Model.DerictionJson;
 import Model.GetListBvKhoa;
 import Model.GetListKhoa;
 import Model.MyDialogFragment;
 import Model.Route;
 import Model.Time;
 import Model.onePlace;
 import PlacesAPI.passJson;
 import SlidingMenu.NavItems;
 import SlidingMenu.NavListAdapter;

 public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener,MyDialogFragment.ReturnOneplace, DerectionListener {

    private  Double mlat;
    private  Double mlng;
    private GoogleMap mMap;
    private LocationManager mlocationManager;
    private Button btnFindpath;
     private Button btngetlocation;
    private EditText edtOrigrin;
    private List<Marker> morigrinMakers = new ArrayList<>();
    private List<Marker> mdestinationMarker = new ArrayList<>();
    private List<Polyline> mpolylinePath = new ArrayList<>();
    public List<BenhViens> listTam=new ArrayList<>();
    public List<onePlace> newitem=new ArrayList<>();
    private ProgressDialog mprogressDialog;
    private boolean isOnline = false;
    private boolean gpsenable = false;
    private GoogleApiClient mgGoogleApiClient;
    private Location network_loc = null;
    private DrawerLayout mdrawer;
    private ListView mlvNav;
    private List<NavItems> mlistNavItems;
    private NavListAdapter mnavListAdapter;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private boolean flagsearch = false;
    private LinearLayout mlinearLayout;
    private SupportMapFragment mmapFragment;
    private List<String> nameHospital;
    private List<onePlace> listname= new ArrayList<>();
    private AutoCompleteTextView autoCompleteTextView;
    private   String origin;
    private   String destination;
    private ConnectSQL mDBHelper;
    private SQLiteDatabase mDatabase;
    public  String choseKhoa;
    public String selected;
    public int i=-1;
    public  MyDialogFragment pl;
    private boolean flag=false;
    private List<Route> mRoute=new ArrayList<>();
    private List<Route> ListMin=new ArrayList<>();
    private List<String> danhSachKhoa=new ArrayList<>();
     private List<onePlace> onePlaceList1;
    private ImageButton myimagebutton;
    private DialogInterface.OnClickListener mDialogOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mprogressDialog = ProgressDialog.show(this, "Please wait.", "...", true);
        mDBHelper = new ConnectSQL(getApplicationContext());

        mmapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mmapFragment.getMapAsync(this);
        addToolbar();
        anhXa();

        GetListKhoa tam=new GetListKhoa(this);
        tam.CreateUrltam();

        //Check exists database
//        File database = getApplicationContext().getDatabasePath(ConnectSQL.DBNAME);
//        if( !database.exists()) {
//            mDBHelper.getReadableDatabase();
//
//            if(copyDatabase(this)) {
//                Toast.makeText(this, "Copy database succes", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Copy data error", Toast.LENGTH_SHORT).show();
//                return;
//            }
//        }
        //end check
       // int a =mDBHelper.testSQL();
        mnavListAdapter = new NavListAdapter(getApplicationContext(), R.layout.nav_list, mlistNavItems);
        mlvNav.setAdapter(mnavListAdapter);
        mlvNav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplication(),String.valueOf(position),Toast.LENGTH_LONG).show();
                if (position==0){
                    showDialogEdittext();
                }
                if (position==1){
                    flagsearch = true;
                    mlinearLayout.setVisibility(View.VISIBLE);
                }
                mdrawer.closeDrawer(Gravity.LEFT);

            }
        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mdrawer,
                R.string.dropen, R.string.drclose) {

            @Override
            public void onDrawerOpened(View drawerView) {
                // TODO Auto-generated method stub
                invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // TODO Auto-generated method stub
                invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
            }

        };
        mdrawer.addDrawerListener(actionBarDrawerToggle);
//        if(checkConnectActivity()){
//            // is connected
//        }else{
//            // is not connected
//        }
        //end navigation
        checkConnectActivity();
        if (!isOnline) {
            String error="Please connect to a network and try again";
            showDialog(error);
        } else {
            //check permission
            String[] PERMISSIONS = {
                    android.Manifest.permission.CHANGE_NETWORK_STATE,
                    android.Manifest.permission.CHANGE_WIFI_STATE,
                    android.Manifest.permission.ACCESS_WIFI_STATE,
                    android.Manifest.permission.INTERNET,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.ACCESS_NETWORK_STATE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION};

            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
            }
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.

            mlocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
          //  gpsenable = mlocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
           // if (!gpsenable) {
                network_loc = mlocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
           // } else {
           //     network_loc = mlocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

           // }
            btngetlocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edtOrigrin.setText("your location!");
                }
            });
            btnFindpath.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   mMap.clear();
                                                  sendRequest();


                                               }
                                           }
            );
            myimagebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMap.clear();
                    addline();
                }
            });

        }
    }
//    private boolean copyDatabase(Context context) {
//        try {
//
//            InputStream inputStream = context.getAssets().open(ConnectSQL.DBNAME);
//            String outFileName = ConnectSQL.DBLOCATION + ConnectSQL.DBNAME;
//            OutputStream outputStream = new FileOutputStream(outFileName);
//            byte[]buff = new byte[1024];
//            int length;
//            while ((length = inputStream.read(buff)) > 0) {
//                outputStream.write(buff, 0, length);
//            }
//            outputStream.flush();
//            outputStream.close();
//            return true;
//        }catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    public void showDialog(String error) {
        AlertDialog builder = new AlertDialog.Builder(this).create();
        builder.setTitle(R.string.title_network);
        builder.setMessage(error);
        builder.setButton("ok", mDialogOnClickListener);
        builder.show();
    }

    public void anhXa() {
        mdrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mlvNav = (ListView) findViewById(R.id.nav_list);
        mlinearLayout = (LinearLayout) findViewById(R.id.layout_linear);
        mlinearLayout.setVisibility(View.GONE);
        btnFindpath = (Button) findViewById(R.id.btnFind);
        autoCompleteTextView=(AutoCompleteTextView)findViewById(R.id.edtDestination);
        edtOrigrin = (EditText) findViewById(R.id.edtOrigin);
        myimagebutton=(ImageButton)findViewById(R.id.img_button);
        btngetlocation=(Button)findViewById(R.id.btsetlocation);


        mlistNavItems = new ArrayList<>();
        mlistNavItems.add(new NavItems("Tìm Kiếm Theo Bán Kính", R.drawable.icon_place));
        mlistNavItems.add(new NavItems("Chỉ Đường", R.drawable.icon_search));
    }

    private void addToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void sendRequest() {
        flag=false;

        origin = edtOrigrin.getText().toString();
        destination = autoCompleteTextView.getText().toString();
        if (origin.isEmpty() | destination.isEmpty()) {
            String error="Please enter  destination address";
            showDialog(error);
            return;
        }
        if (origin.equals("your location!")){
            Log.e("trueeeeeeeeeeeeeeeeee","trueeeeeeeeeeeeeeeee");
            if (edtOrigrin!=null && destination!=null){
                if (listname.size()!=0) {

                    for (onePlace placeex : listname) {
                        if (destination.equals(placeex.getName())){
                            destination = placeex.getMplaceId();
                            flag=true;
                            Log.e("denstation",destination);
                        }
                    }
                }
            }
            try {
                new DerictionJson(this, mlat,mlng,flag, destination).execute();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            Log.e("falseeeeeeeeeeeeeeeeeee","falseeeeeeeeeeeeeeeeeee");
            try {
                if (listname.size()!=0) {

                    for (onePlace placeex : listname) {
                        if (destination.equals(placeex.getName())){
                            destination = placeex.getMplaceId();
                            flag=true;
                            Log.e("denstation",destination);
                        }
                    }
                }
                new DerictionJson(this,flag,origin, destination).execute2();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
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

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mprogressDialog.dismiss();
                if (ActivityCompat.checkSelfPermission(getApplication(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplication(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                updateLocation(network_loc);
                mMap.setMyLocationEnabled(true);
            }
        });

    }

    @Override
    public void onDirecterStart() {
        i=0;
        mprogressDialog = ProgressDialog.show(this, "Please wait.", "...", true);
        if (morigrinMakers != null) {
            for (Marker marker : morigrinMakers) {
                marker.remove();
            }
        }
        if (mdestinationMarker != null) {
            for (Marker marker : mdestinationMarker) {
                marker.remove();
            }
        }
        if (mpolylinePath != null) {
            for (Polyline polyline : mpolylinePath) {
                polyline.remove();
            }
        }
        if (mRoute!=null){
            mRoute.removeAll(mRoute);
        }
    }

    @Override
    public void onDirecterSuccess(List<Route> route) {
        mprogressDialog.dismiss();
        mpolylinePath = new ArrayList<>();
        morigrinMakers = new ArrayList<>();
        mdestinationMarker = new ArrayList<>();
        mRoute=new ArrayList<>();

        if (route.size() == 0) {
            String error="Lỗi Không Có Đường đi";
            showDialog(error);
        } else {
            Toast.makeText(getApplicationContext(),"Xong",Toast.LENGTH_LONG).show();
            mRoute=route;
            Log.i("size of route", String.valueOf(mRoute.size()));
            addline();

        }

    }
        public void addline(){
        i++;
        if (mRoute.size()!=0){
            for (onePlace place : newitem) {
                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.iconplacehospital)).title(place.getName()).snippet(place.getAddress()).position(place.getCurrentLocation()));
            }
            if (i<mRoute.size()){
                addsubline();
            }else
            {
                i=0;
                addsubline();
            }
        }else{
            String error="Bạn Hãy Tìm Đường Trước";
            showDialog(error);
        }

    }
    public void addsubline(){

        Route route1=mRoute.get(i);
        //   for (Route route1 : route) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route1.startLocation, 15));
        ((TextView) findViewById(R.id.tvDuration)).setText(route1.duration.text);
        ((TextView) findViewById(R.id.tvDistance)).setText(route1.distance.text);

        morigrinMakers.add(mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_place)).title(route1.startAddress).position(route1.startLocation)));
        mdestinationMarker.add(mMap.addMarker((new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_place)).title(route1.endAddress).position(route1.endLocation))));

        PolylineOptions polylineOptions = new PolylineOptions().geodesic(true).color(Color.YELLOW).width(10);
        for (int i = 0; i < route1.points.size(); i++) {
            polylineOptions.add(route1.points.get(i));
        }
        mMap.addPolyline(polylineOptions);
       // mpolylinePath.add(mMap.addPolyline(polylineOptions));
    }


    @Override
    public void onSearchStart() {
        mMap.clear();
        if(nameHospital!=null){
            nameHospital.removeAll(nameHospital);
        }
       if (listname!=null){
          listname.removeAll(listname);
       }
        if (listTam!=null){
            listTam.removeAll(listTam);

        }
        if (newitem!=null){
            newitem.removeAll(newitem);
        }

        mprogressDialog = ProgressDialog.show(this, "Vui lòng chờ.", "đang tải...", true);
    }

    @Override
    public void onSearchSuccess(List<onePlace> onePlaceList) {
        Log.e("oneplacelist",String.valueOf(onePlaceList.size()));
        mprogressDialog.dismiss();
        onePlaceList1=new ArrayList<>();
        nameHospital=new ArrayList<>();
        listname=new ArrayList<>();
        newitem=new ArrayList<>();
        listTam=new ArrayList<>();
        onePlaceList1=onePlaceList;
        Log.e("CHose Khoa",choseKhoa);
        new GetListBvKhoa(this,choseKhoa).CreateUrltam();



    }

    public List<onePlace> getMyList(){
        return newitem;
    }
    public void showlist_dialog(){
        FragmentManager fm=getSupportFragmentManager();
        pl=new MyDialogFragment();
        pl.show(fm,"");

    }

    @Override
    public void onStartTime() {

    }

    @Override
    public void onSuccessTime(List<Time> listTime) {
     //Log.e("listtime",listTime.toString());
    }



     @Override
     public void onSuccessGetListKhoa(List<String> listkhoa) {
     Log.i("OnSuccessGetListKhoa",listkhoa.toString());
         danhSachKhoa=listkhoa;

     }

     @Override
     public void onSuccessGetListBvKhoa(List<BenhViens> listBV) {
            Log.i("OnsucessGetListBVkhoa",String.valueOf(listBV.size()));

         if (choseKhoa.equals("none")){
             for (onePlace tam:onePlaceList1){
                 newitem.add(tam);
             }
         }else{
             listTam=listBV;
             Log.e("dữ liệu ",listTam.toString());
             Log.e("Dữ Liệu oneplace",onePlaceList1.toString());

             for (int j=0;j<onePlaceList1.size();j++){
                 onePlace tam = onePlaceList1.get(j);
                 for (int i=0;i<listTam.size();i++){
                     BenhViens bvTam=listTam.get(i);
                     if (tam.getName().equalsIgnoreCase(bvTam.getName())){
                         newitem.add(tam);
                         Log.e("thanh cong ",tam.toString());
                     }
                 }
             }
         }

         if (newitem.size()== 0) {

             String error="Không Có Bệnh Viện  ";
             showDialog(error);
         } else {
             showlist_dialog();
             for (onePlace place : newitem) {
                 Toast.makeText(getApplicationContext(),"Có "+newitem.size()+" bệnh viện",Toast.LENGTH_SHORT).show();
                 nameHospital.add(place.getName());
                 onePlace oneplacenew=new onePlace(place.getCurrentLocation(),place.getName(),place.getAddress(),place.getMplaceId());
                 listname.add(oneplacenew);
                 add();
                 mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.iconplacehospital)).title(place.getName()).snippet(place.getAddress()).position(place.getCurrentLocation()));
             }
         }
     }


     public void add(){
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,nameHospital);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);
    }


    public void Loi(String error) {
        AlertDialog builder = new AlertDialog.Builder(this).create();
        builder.setTitle(error);
        builder.setButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access location data.", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access location data.", Toast.LENGTH_LONG).show();

                }
                break;
            }
        }
    }

    //check permision ondroid 6.0
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    //check internet
    private void checkConnectActivity() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting() || cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting()) {
            isOnline = true;
//            return true;
        } else {
//            return false;
            isOnline = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mlocationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkGooglePlayServices();
        if (isOnline) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mlocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,600, 1, this);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mgGoogleApiClient.isConnecting() &&
                        !mgGoogleApiClient.isConnected()) {
                    mgGoogleApiClient.connect();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Google Play Services must be installed.",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        updateLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void updateLocation(Location l) {

        if (l != null) {
            mlat = l.getLatitude();
            mlng = l.getLongitude();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mlat, mlng), 15));
            edtOrigrin.setText("your location!");
        }
    }
    public void setText() {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<android.location.Address> listadress = geocoder.getFromLocation(mlat,mlng, 1);
            if (listadress != null && listadress.size() > 0) {
                    Log.i("Place info featurename", listadress.get(0).getAddressLine(0));
                edtOrigrin.setText(listadress.get(0).getAddressLine(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    //check google service
    private boolean checkGooglePlayServices() {
        int checkGooglePlayServices = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (checkGooglePlayServices != ConnectionResult.SUCCESS) {
            GooglePlayServicesUtil.getErrorDialog(checkGooglePlayServices,
                    this, 1000).show();
            return false;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        int id = item.getItemId();
        if (id == R.id.icon_search) {
            if (flagsearch) {
                mlinearLayout.setVisibility(View.GONE);
                flagsearch = false;
            } else {
               // setText();
                flagsearch = true;
                mlinearLayout.setVisibility(View.VISIBLE);
            }
            return true;
        }
        if (id == R.id.icon_hospital) {
            mlinearLayout.setVisibility(View.GONE);
            flagsearch = false;
            showDialogEdittext();
        }
        if (id==R.id.change_12sp){
            autoCompleteTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,getResources().getDimensionPixelSize(R.dimen.textview_12sp));
        }
        if (id==R.id.change_15sp){
            autoCompleteTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,getResources().getDimensionPixelSize(R.dimen.textview_15sp));
        }
        if (id==R.id.change_20sp){
            autoCompleteTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,getResources().getDimensionPixelSize(R.dimen.textview_20sp));
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDialogEdittext() {
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.customdialog,null);

        alertdialog.setView(dialogView);

        final EditText userInput = (EditText) dialogView.findViewById(R.id.edit_dialog);
        final Spinner autoCompletekhoa=(Spinner)dialogView.findViewById(R.id.autocplkhoa);
      //  danhSachKhoa.add("None");

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,danhSachKhoa);
        autoCompletekhoa.setAdapter(adapter);

        //set dialog message

        alertdialog.setTitle("Nhập Vào Khoảng Cách").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                searchHospitalAround(userInput.getText().toString());
                 selected=autoCompletekhoa.getSelectedItem().toString();
                Log.i("selected",selected);
                if (selected.equalsIgnoreCase("khoa chấn thương chỉnh hình")){
                    choseKhoa="kctch";
                }
                if (selected.equalsIgnoreCase("khoa dinh dưỡng")){
                    choseKhoa="kdd";
                }
                if (selected.equalsIgnoreCase("khoa hô hấp-tiêu hóa")){
                    choseKhoa="khh";
                }
                if (selected.equalsIgnoreCase("Khoa Lâm sàn")){
                    choseKhoa="kls";
                }
                if (selected.equalsIgnoreCase("khoa mắt")){
                    choseKhoa="km";
                }
                if (selected.equalsIgnoreCase("Khoa Y Học Cổ Truyền")){
                    choseKhoa="kyh";
                }
                if (selected.equalsIgnoreCase("Khoa Sản")){
                    choseKhoa="ks";
                }
                if (selected.equalsIgnoreCase("khoa nội nhi")){
                    choseKhoa="knn";
                }
                if (selected.equalsIgnoreCase("Khoa Ngoại Tổng Quát")){
                    choseKhoa="kntq";
                }
                if (selected.equalsIgnoreCase("khoa tai mũi họng")){
                    choseKhoa="ktmh";
                }
                if (selected.equalsIgnoreCase("Khoa Răng Hàm Mặt")){
                    choseKhoa="krhm";
                }
                if (selected.equalsIgnoreCase("Khoa Hô Hấp Tiêu Hóa")){
                    choseKhoa="khhth";
                }
                if (selected.equalsIgnoreCase("Khoa Tim")){
                    choseKhoa="kt";
                }
                if (selected.equalsIgnoreCase("Khoa Ngoại")){
                    choseKhoa="kn";
                }
                if (selected.equalsIgnoreCase("Khoa Nội")){
                    choseKhoa="kni";
                }
                if (selected.equalsIgnoreCase("Khoa Nhi")){
                    choseKhoa="knh";
                }
                if (selected.equalsIgnoreCase("Khoa Thần Kinh")){
                    choseKhoa="ktk";
                }
                if (selected.equalsIgnoreCase("khoa y học cổ truyền")){
                    choseKhoa="kyhct";
                }
                if (selected.equalsIgnoreCase("None")){
                    choseKhoa="none";
                }

            }
        }
        )
                .setNegativeButton("Cannel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        //create alerDialog
        AlertDialog createDialog = alertdialog.create();
        //show
        createDialog.show();


    }


    public void searchHospitalAround(String kc){
        if (kc.equals("")){
            String error="Bạn Nhập Khoảng Cách Không Hợp Lệ";
            Loi(error);
        }else{

            int intkc=Integer.parseInt(kc);
            if (0<=intkc && intkc<=50000){
                new passJson(this,mlat,mlng,intkc).excutePass();
            }else
            {
                AlertDialog builder = new AlertDialog.Builder(this).create();
                builder.setTitle("Khoảng Cách Nhập Không hợp lệ");
                builder.setMessage("Khoảng Cách Từ 0 Đến 50000");
                builder.setButton("ok", mDialogOnClickListener);
                builder.show();
            }
        }


    }


    @Override
    public void getOnePlace(onePlace onePlace) {
        try {
            pl.dismiss();
            new DerictionJson(this, mlat,mlng,true, onePlace.getMplaceId()).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
