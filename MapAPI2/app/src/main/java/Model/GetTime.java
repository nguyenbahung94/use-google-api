package Model;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 8/22/2016.
 */
public class GetTime   {
    private String uRL="";
    private Double mlat;
    private Double mlng;
    private String nameTam;
    private DerectionListener mListener;;
    private List<onePlace> listplace = new ArrayList<onePlace>();
    private ArrayList<Time> listtime = new ArrayList<Time>();
    public static final String URL_API = "https://maps.googleapis.com/maps/api/distancematrix/json?";
    public static final String GOOGLE_API = "AIzaSyAXvwKE-unZw2jU9veeWvUexBVe7x35Sts";

    public GetTime(DerectionListener mListener,Double mlat,Double mlng, List<onePlace> listplace) {
        this.mListener = mListener;
        this.listplace = listplace;
        this.mlat=mlat;
        this.mlng=mlng;
    }

    public void execute() throws UnsupportedEncodingException {
       mListener.onStartTime();
        for (onePlace place:listplace){
            nameTam=place.getName();
            String placeid=place.getMplaceId();
         uRL= URL_API + "origin=" +mlat+","+mlng +"&destination=place_id:"+placeid+"&key=" + GOOGLE_API;
            new DowloadDataJson().execute(uRL);
            Log.e("Create url",uRL);
        }
         //  mListener.onSuccessTime(listtime);

    }

//    private String createUrl() throws UnsupportedEncodingException {
//           // return URL_API + "origin=" +mlat+ "&destination=place_id:" + mDestination +"&alternatives=true" +"&key=" + GOOGLE_API;
//        return "https://maps.googleapis.com/maps/api/distancematrix/json?origins=Washington,DC&destinations=New+York+City,NY&key=AIzaSyAXvwKE-unZw2jU9veeWvUexBVe7x35Sts";
//
//    }

    private class DowloadDataJson extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String data) {
            try {
                parseJson(data);

            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(data);
        }
    }

    private void parseJson(String data) throws JSONException {
        if (data == null) {
            return;
        }
        JSONObject jsonData = new JSONObject(data);
        JSONArray ArrayRoute = jsonData.getJSONArray("rows");
        if (ArrayRoute.length() == 0) {
            return;
        }
        for (int i = 0; i < ArrayRoute.length(); i++) {
            JSONObject jsonRoute = ArrayRoute.getJSONObject(i);
            JSONArray ArrayRoute1 = jsonRoute.getJSONArray("elements");
             Log.e("Arrrrrrrr",ArrayRoute1.toString());
            for (int j=0;j<ArrayRoute1.length();j++){
                JSONObject object = ArrayRoute1.getJSONObject(j);
                Log.e("object",object.toString());
                JSONObject object1 = object.getJSONObject("distance");
                String distance=object1.getString("text");
                Log.e("text",distance.toString());
                JSONObject object2 = object.getJSONObject("duration");
                String duration=object2.getString("text");
                Log.e("text",duration.toString());
                Time tamtime=new Time(nameTam,distance,duration);
                listtime.add(tamtime);
            }
        }


    }
}
