package com.wzes.vmovie.service;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit.Builder;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRetrofit {

    private static MyRetrofit myRetrofit;

    private MyRetrofit() {

    }

    /**
     *
     * @return
     */
    public static MyRetrofit getInstance() {
        if(myRetrofit == null) {
            myRetrofit = new MyRetrofit();
        }
        return myRetrofit;
    }

    /**
     *
     * @param baseurl
     * @return
     */
    public RetrofitService getGsonRetrofit(String baseurl) {
        return new Builder().baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RetrofitService.class);
    }

    /**
     *
     * @param baseurl
     * @return
     */
    public RetrofitService getNormalRetrofit(String baseurl) {
        return new Builder().baseUrl(baseurl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RetrofitService.class);
    }
}
