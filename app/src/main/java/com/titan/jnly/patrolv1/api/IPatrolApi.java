package com.titan.jnly.patrolv1.api;

import com.lib.bandaid.data.remote.api.annotation.BaseUrl;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.titan.jnly.Config;
import com.titan.jnly.common.bean.PageRes;
import com.titan.jnly.patrolv1.bean.PatrolTask;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

@BaseUrl(Config.BASE_URL.BaseUrl_1)
public interface IPatrolApi {


    /**
     * 巡查任务分页列表
     *
     * @param id     id
     * @param number 页码
     * @param size   每页显示条数
     * @param userId userId
     * @param eNum   电子标签号
     * @return
     */
    @GET("api/XHGL/PatrolRecord/PatrolTask")
    Observable<TTResult<PageRes<PatrolTask>>> httpGetPatrolTaskList(
            @Query("Id") String id,
            @Query("PageNumber") Integer number,
            @Query("PageSize") Integer size,
            @Query("UserId") String userId,
            @Query("DZBQH") String eNum);


}
