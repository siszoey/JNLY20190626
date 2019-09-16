package com.titan.jnly.patrol.api;

import com.lib.bandaid.data.remote.api.annotation.BaseUrl;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.titan.jnly.Config;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

@BaseUrl(Config.BASE_URL.BaseUrl_1)
public interface IPatrolCure {

    @GET("api/XHGL/PatrolRecord/GetByDZBQH")
    Observable<TTResult<Map>> httpGetPatrolList(@Query("DZBQH") String eleCode,
                                                @Query("pageNumber") Integer number,
                                                @Query("pageSize") Integer size);


    @GET("api/XHGL/MaintainRecord/GetByDZBQH")
    Observable<TTResult<Map>> httpGetCureList(@Query("DZBQH") String eleCode,
                                              @Query("pageNumber") Integer number,
                                              @Query("pageSize") Integer size);


}
