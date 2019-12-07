package com.lib.bandaid.data.remote.util;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lib.bandaid.data.remote.api.annotation.BaseUrl;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
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
                    //.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(baseUrl)
                    .build();
            retrofitMap.put(baseUrl, retrofit);
            return retrofitMap.get(baseUrl);
        }
    }


    public static <T> T create(@NonNull Class<?> clazz) {
        boolean hasBaseUrl = clazz.isAnnotationPresent(BaseUrl.class);
        String baseUrl = "http://127.0.0.1:8080/";
        if (hasBaseUrl) {
            BaseUrl _baseUrl = clazz.getAnnotation(BaseUrl.class);
            baseUrl = _baseUrl.value();
        }
        Retrofit createRetrofit = createRetrofit(baseUrl);
        return (T) createRetrofit.create(clazz);
    }

}
