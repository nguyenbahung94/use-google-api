package Model;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 5/29/2016.
 */
public class DerictionJson {
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyAXvwKE-unZw2jU9veeWvUexBVe7x35Sts";
    private final String UTF_8 = "utf-8";
    private DerectionListener mListener;
    private String mDestination;
    private String mOrigin;
    private String mroutes="routes";
    private String moverview_polyline="overview_polyline";
    private String mlegs="legs";
    private String mdistance="distance";
    private String mduration="duration";
    private String mend_location="end_location";
    private String mstart_location="start_location";
    private String mend_address="end_address";
    private String mstart_address="start_address";
    private String mpoints="points";
    private Double mlat;
    private Double mlng;
    private Boolean mFlag;

    public DerictionJson(DerectionListener listener,Double mlat,Double mlng,boolean mFlag, String destination) {
        this.mListener = listener;
        this.mlat=mlat;
        this.mlng=mlng;
        this.mFlag=mFlag;
        this.mDestination = destination;
    }
    public DerictionJson(DerectionListener listener,boolean flag,String origin,String mDestination){
        this.mListener=listener;
        mFlag=flag;
        mOrigin=origin;
        this.mDestination=mDestination;

    }

    public void execute() throws UnsupportedEncodingException {
        mListener.onDirecterStart();
        new DowloadDataJson().execute(createUrl());
        Log.e("Create url",createUrl());
    }
    public void execute2() throws UnsupportedEncodingException {
        mListener.onDirecterStart();
        new DowloadDataJson().execute(createUrl2());
        Log.e("Create url",createUrl2());
    }
    private String createUrl2() throws UnsupportedEncodingException {
        if(mFlag){
            String origin=URLEncoder.encode(mOrigin, "utf8");
            return DIRECTION_URL_API + "origin=" +origin+ "&destination=place_id:" + mDestination +"&alternatives=true" +"&key=" + GOOGLE_API_KEY;
        }
        String newdes=URLEncoder.encode(mDestination, "utf8");
        String origin=URLEncoder.encode(mOrigin, "utf8");
        return DIRECTION_URL_API + "origin=" +origin+ "&destination=" + newdes +"&alternatives=true" +"&key=" + GOOGLE_API_KEY;
    }

    private String createUrl() throws UnsupportedEncodingException {
        if (mFlag){
            return DIRECTION_URL_API + "origin=" +mlat+","+mlng + "&destination=place_id:" + mDestination +"&alternatives=true" +"&key=" + GOOGLE_API_KEY;
        }
        String newdes=URLEncoder.encode(mDestination, "utf8");
        return DIRECTION_URL_API + "origin=" +mlat+","+mlng + "&destination=" + newdes +"&alternatives=true" +"&key=" + GOOGLE_API_KEY;
    }

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
        List<Route> routes = new ArrayList<>();
        JSONObject jsonData = new JSONObject(data);
        JSONArray ArrayRoute = jsonData.getJSONArray(mroutes);
        if (ArrayRoute.length() == 0) {
            return;
        }
        for (int i = 0; i < ArrayRoute.length(); i++) {
            JSONObject jsonRoute = ArrayRoute.getJSONObject(i);
            Route route = new Route();
            JSONObject overview_Polyline = jsonRoute.getJSONObject(moverview_polyline);
            JSONArray jsonlegs = jsonRoute.getJSONArray(mlegs);
            JSONObject jsonleg = jsonlegs.getJSONObject(0);
            JSONObject jsonDistance = jsonleg.getJSONObject(mdistance);
            JSONObject jsonDuration = jsonleg.getJSONObject(mduration);
            JSONObject jsonEndlocation = jsonleg.getJSONObject(mend_location);
            JSONObject jsonStartlocation = jsonleg.getJSONObject(mstart_location);
            //add
            route.distance = new Distance(jsonDistance.getString("text"), jsonDistance.getInt("value"));
            route.duration = new DurationJ(jsonDuration.getString("text"), jsonDuration.getInt("value"));
            route.endAddress = jsonleg.getString(mend_address);
            route.startAddress = jsonleg.getString(mstart_address);
            route.endLocation = new LatLng(jsonEndlocation.getDouble("lat"), jsonEndlocation.getDouble("lng"));
            route.startLocation = new LatLng(jsonStartlocation.getDouble("lat"), jsonStartlocation.getDouble("lng"));
            route.points = decodePollyLine(overview_Polyline.getString(mpoints));
            routes.add(route);

        }
        mListener.onDirecterSuccess(routes);

    }

    private List<LatLng> decodePollyLine(String encoded) {
        ArrayList<LatLng> polyline = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
            polyline.add(p);
        }
//        for (int i = 0; i < polyline.size(); i++) {
//            Log.i("Location", "Point sent: Latitude: " + polyline.get(i).latitude + " Longitude: " + polyline.get(i).longitude);
//        }
        return polyline;

    }
}
