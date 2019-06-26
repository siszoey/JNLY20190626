package com.lib.bandaid.data.remote.utils;

import com.lib.bandaid.data.remote.intercept.AuthInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

/**
 * Created by zy on 2018/9/9.
 */

public final class OkHttp3Util {

    private static int connectTime = 20;
    public static OkHttpClient okHttpClient = initBuilder().dispatcher(getDispatcher()).build();

    public static OkHttpClient.Builder initBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(connectTime, TimeUnit.SECONDS);
        builder.readTimeout(connectTime, TimeUnit.SECONDS);
        builder.writeTimeout(connectTime * 2, TimeUnit.SECONDS);
        builder.addInterceptor(AuthInterceptor.createAuthInterceptor());
        return builder;
    }

    private static Dispatcher getDispatcher() {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequestsPerHost(15);
        return dispatcher;
    }
}
