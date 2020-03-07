package com.work.unknown.sportifiy.Retrofit;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by unknown on 6/18/2018.
 */

public interface IGoCordinates {
    @GET("maps/api/geocode/json")
    Call<String> getGeocode(@Query("address") String address);
    @GET("/maps/api/directions/json?")
    Call<String>getdriections(@Query("orgin") String orgin, @Query("destination") String destination);


}
