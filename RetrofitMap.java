package com.example.nbhung.mymap.View;

import com.example.nbhung.mymap.Model.Routes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by nbhung on 6/21/2017.
 */

public interface RetrofitMap {
    @GET("/maps/api/directions/json")
    Call<Routes> getDirections(@Query("origin") String origin, @Query("destination") String destination,@Query("key") String key);

}
