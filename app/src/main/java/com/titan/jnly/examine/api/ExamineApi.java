package com.titan.jnly.examine.api;

import com.lib.bandaid.data.remote.api.annotation.BaseUrl;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.titan.jnly.Config;
import com.titan.jnly.login.bean.UserInfo;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

@BaseUrl(Config.BASE_URL.BaseUrl_1)
public interface ExamineApi {

    @GET("api/SDS/GSMM/GetGSMMCom")
    Observable<TTResult<Map>> httpGetList(@Query("pageNumber") Integer pageNumber, @Query("pageSize") Integer pageSize, @Query("userId") String userId);

    @POST("api/SDS/GSMM/GetGSMM")
    Observable<TTResult<Map>> httpPostList(@Body Map map);

}
