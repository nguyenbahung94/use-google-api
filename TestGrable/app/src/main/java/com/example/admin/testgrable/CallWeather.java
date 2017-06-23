package com.example.admin.testgrable;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Admin on 23/6/2017.
 */

public class CallWeather {
    private static final String BASE_URL = "http://api.openweathermap.org";
    private static final String KEY = "1613ff23344f6d14f06caf356786d566";
    private LatLng mylng;
    private DerectionListener mdDerectionListener;

    public CallWeather(DerectionListener mdDerectionListener, LatLng mylng) {
        this.mdDerectionListener = mdDerectionListener;
        this.mylng = mylng;
    }


    public void createRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitWeather retrofitWeather = retrofit.create(RetrofitWeather.class);
        Call<InforWeather> call = retrofitWeather.getWeather(String.valueOf(mylng.latitude),String.valueOf(mylng.longitude),"metric", KEY);
        call.enqueue(new Callback<InforWeather>() {
            @Override
            public void onResponse(Call<InforWeather> call, Response<InforWeather> response) {
                InforWeather inforWeather = response.body();
                mdDerectionListener.getWeartherSucces(inforWeather);
            }

            @Override
            public void onFailure(Call<InforWeather> call, Throwable t) {
                Log.e("respon2", t.getMessage());
            }
        });

    }
}
