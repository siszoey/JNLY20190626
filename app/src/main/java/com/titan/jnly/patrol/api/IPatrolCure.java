package com.titan.jnly.patrol.api;

import com.lib.bandaid.data.remote.api.annotation.BaseUrl;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.titan.jnly.Config;
import com.titan.jnly.patrol.bean.CureModel;
import com.titan.jnly.patrol.bean.PatrolModel;

import java.util.List;
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

    @POST("api/XHGL/PatrolRecord/Upload")
    Observable<TTResult<Map>> httpPostPatrolItem(@Body List<PatrolModel> model);


    @POST("api/XHGL/MaintainRecord/Upload")
    Observable<TTResult<Map>> httpPostCureItem(@Body List<CureModel> model);


    //巡查信息删除
    @GET("api/XHGL/PatrolRecord/Remove")
    Observable<TTResult<Map>> httpDelPatrolItem(@Query("UserId") String userId, @Query("Id") String id);

    //养护信息删除
    @GET("api/XHGL/MaintainRecord/Remove")
    Observable<TTResult<Map>> httpDelCureItem(@Query("UserId") String userId, @Query("Id") String id);
}
