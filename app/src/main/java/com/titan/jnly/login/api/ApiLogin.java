package com.titan.jnly.login.api;

import com.lib.bandaid.data.remote.api.annotation.BaseUrl;
import com.lib.bandaid.data.remote.entity.BaseResult;
import com.titan.jnly.Config;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

@BaseUrl(Config.BASE_URL.MON)
public interface ApiLogin {

    @POST("api-admin/login")
    @Headers({"Content-Type:application/json"})
    Observable<BaseResult<Map>> httpLogin(@Body String map);

}
