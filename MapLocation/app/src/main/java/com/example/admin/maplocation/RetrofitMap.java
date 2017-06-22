package com.example.admin.maplocation;

import com.example.admin.maplocation.Model.Routes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by nbhung on 6/21/2017.
 */

public interface RetrofitMap {
    @GET("api/directions/json?alternatives=true&key=AIzaSyAEJ_LbDl4yv9YiFH84rI7uSqbzkBZcrD4")
    Call<Routes> getDirections(@Query("origin") String origin, @Query("destination") String destination);

}
