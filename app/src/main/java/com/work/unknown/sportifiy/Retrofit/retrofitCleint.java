package com.work.unknown.sportifiy.Retrofit;


import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class retrofitCleint {
    public static Retrofit retrofit = null;

    public static Retrofit getClient(String baseurl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(baseurl).addConverterFactory(ScalarsConverterFactory.create()).build();


        }
        return retrofit;
    }
}
