package com.titan.jnly.patrolv1.api;

import com.lib.bandaid.data.remote.api.annotation.BaseUrl;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.titan.jnly.Config;
import com.titan.jnly.patrolv1.bean.ConserveTask;
import com.titan.jnly.patrolv1.bean.PatrolTask;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

@BaseUrl(Config.BASE_URL.BaseUrl_Mock)
public interface IPatrolApi {

    @GET("api/v1/patrol/task/list")
    Observable<TTResult<List<PatrolTask>>> httpGetPatrolTaskList(@Query("pageNumber") Integer number, @Query("pageSize") Integer size);

    @GET("api/v1/conserve/task/list")
    Observable<TTResult<List<ConserveTask>>> httpGetConserveTaskList(@Query("pageNumber") Integer number, @Query("pageSize") Integer size);

}
