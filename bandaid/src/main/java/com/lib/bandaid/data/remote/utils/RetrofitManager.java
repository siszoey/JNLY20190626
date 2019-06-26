package com.lib.bandaid.data.remote.utils;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public final class RetrofitManager {

    private static Map<String, Retrofit> retrofitMap = new HashMap<>();
    static Gson gsonFormat = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    private static Retrofit createRetrofit(String baseUrl) {
        if (retrofitMap.containsKey(baseUrl)) {
            return retrofitMap.get(baseUrl);
        } else {
            /*创建retrofit对象*/
            Retrofit retrofit = new Retrofit.Builder()
                    .client(OkHttp3Util.okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gsonFormat))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(baseUrl)
                    .build();
            retrofitMap.put(baseUrl, retrofit);
            return retrofitMap.get(baseUrl);
        }
    }


    public static <T> T create(@NonNull Class clazz) {
        String baseUrl = "";
        Retrofit createRetrofit = createRetrofit(baseUrl);
        return (T) createRetrofit.create(clazz);
    }

}
