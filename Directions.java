package com.example.nbhung.mymap;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nbhung on 6/21/2017.
 */

public class Directions {
    private static final String URL = "https://maps.googleapis.com/maps/";
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyAEJ_LbDl4yv9YiFH84rI7uSqbzkBZcrD4";
    private String mDestination;
    private String mOrigin;
    private DerectionListener derectionListener;

    public Directions(DerectionListener derectionListener, String mOrigin, String mDestination) {
        this.mDestination = mDestination;
        this.mOrigin = mOrigin;
        this.derectionListener = derectionListener;
        derectionListener.onDirecterStart();
    }

    public void createRitrofit() {
        try {
            String newdes = URLEncoder.encode(mDestination, "utf8");
            String origin = URLEncoder.encode(mOrigin, "utf8");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RetrofitMap retrofitMap = retrofit.create(RetrofitMap.class);
            Call<Routes> call = retrofitMap.getDirections(origin, newdes);
            call.enqueue(new Callback<Routes>() {
                @Override
                public void onResponse(Call<Routes> call, Response<Routes> response) {
                    MyRouter myRouter = response.body().getRoutes().get(0);
//                    Log.e("thanh cong", String.valueOf(response.isSuccessful()));
                    List<String> listpolyline = myRouter.getListpolyline().get(0).getPoints();
                    for (String ss : listpolyline) {
                        Log.e("listLeg", ss.toString());
                    }
                    List<leg> listleg = myRouter.getLegs();
                    for (leg ss : listleg) {
                        Log.e("listLeg", ss.toString());
                    }
                    Log.e("size", "polyline" + listpolyline.size() + "listleg" + listleg.size());

//                    List<LatLng> listResult=decodePollyLine(points);
//                    derectionListener.onDirecterSuccess(rerultLeg,listResult);
                }

                @Override
                public void onFailure(Call<Routes> call, Throwable throwable) {
                    Log.e("that bai", "that bai");
                    Log.e("that bai",throwable.getMessage());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


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
