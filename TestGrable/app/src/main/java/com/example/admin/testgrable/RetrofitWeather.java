package com.example.admin.testgrable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Admin on 23/6/2017.
 */

public interface RetrofitWeather {
    @GET("/data/2.5/weather")
    Call<InforWeather> getWeather(@Query("lat") String lat,@Query("lon") String longt,@Query("units") String units,@Query("appid") String tam);

}
