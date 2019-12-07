package com.titan.jnly.patrolv1.api;

import com.lib.bandaid.data.remote.api.annotation.BaseUrl;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.titan.jnly.Config;
import com.titan.jnly.patrolv1.bean.PatrolTask;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

@BaseUrl(Config.BASE_URL.BaseUrl_Mock)
public interface IPatrolApi {

    @GET("api/v1/patrol/list")
    Observable<TTResult<List<PatrolTask>>> httpGetPatrolList(@Query("pageNumber") Integer number, @Query("pageSize") Integer size);

}
