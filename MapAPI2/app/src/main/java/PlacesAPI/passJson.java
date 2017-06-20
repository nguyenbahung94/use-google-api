package PlacesAPI;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import Model.DerectionListener;
import Model.onePlace;

/**
 * Created by Adam on 6/12/2016.
 */
public class passJson {
    private ArrayList<onePlace> listplace = new ArrayList<onePlace>();
    public static final String URL_API = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    public static final String GOOGLE_API = "AIzaSyAXvwKE-unZw2jU9veeWvUexBVe7x35Sts";
    private Double lat;
    private Double lng;
    private int radius;
    private DerectionListener mListener;

    public passJson(DerectionListener listener,Double lat, Double lng, int radius) {
        this.mListener=listener;
        this.lat = lat;
        this.lng = lng;
        this.radius = radius;
    }
    public String createUrl(){
        return URL_API+"location="+lat+","+lng+"&radius="+radius+"&type=hospital&name=benhvien&key="+GOOGLE_API;
    }
    public void excutePass(){
        mListener.onSearchStart();
        new DowloadJson().execute(createUrl());
        Log.i("create url",createUrl());
    }
    private class  DowloadJson extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            StringBuilder content = new StringBuilder();
            try{
                // create a url object
                URL url = new URL(params[0]);

                // create a urlconnection object
                URLConnection urlConnection = url.openConnection();

                // wrap the urlconnection in a bufferedreader
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String line;

                // read from the urlconnection via the bufferedreader
                while ((line = bufferedReader.readLine()) != null)
                {
                    content.append(line + "\n");
                }

                return content.toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String datajson) {
            try
            {
                 jsonToPlace(datajson);
            }catch (Exception e){
                e.printStackTrace();
            }
            super.onPostExecute(datajson);
        }
    }
    public void jsonToPlace(String data) throws JSONException {
        if (data==null){
            return;
        }
        JSONObject jsonObject=new JSONObject(data);

        JSONArray arrresults=jsonObject.getJSONArray("results");

        for (int i=0;i<arrresults.length();i++){
            JSONObject objectPlace=arrresults.getJSONObject(i);

            //get id
            String placeid=objectPlace.getString("place_id");
            //get name
            String name=objectPlace.getString("name");

            //get address
            String Address=objectPlace.getString("vicinity");

            //get location
            JSONObject objectgeometry=objectPlace.getJSONObject("geometry");

            JSONObject objectlocation=objectgeometry.getJSONObject("location");


            onePlace moneplace=new onePlace();
            moneplace.setAddress(Address);
            moneplace.setName(name);
            moneplace.setMplaceId(placeid);
            moneplace.setCurrentLocation(new LatLng(Double.parseDouble(objectlocation.getString("lat")),Double.parseDouble(objectlocation.getString("lng"))));
            listplace.add(moneplace);

        }
       mListener.onSearchSuccess(listplace);
    }
}
