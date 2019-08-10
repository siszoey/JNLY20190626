package com.titan.jnly.login.api;

import com.lib.bandaid.data.remote.api.annotation.BaseUrl;
import com.lib.bandaid.data.remote.entity.BaseResult;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.titan.jnly.Config;
import com.titan.jnly.login.bean.UserInfo;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

@BaseUrl(Config.BASE_URL.BaseUrl_1)
public interface ApiLogin {

    @GET("api/account/login")
    Observable<TTResult<UserInfo>> httpLogin(@Query("username") String name, @Query("password") String pwd);

}
