package com.lib.bandaid.data.remote.util;

import com.lib.bandaid.data.remote.intercept.AuthInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

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


    public static MultipartBody fileBody(File file) {
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        MultipartBody multipartbody = requestBody.build();
        return multipartbody;
    }
}
