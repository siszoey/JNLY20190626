package com.titan.jnly.patrol.api;

import com.lib.bandaid.data.remote.api.annotation.BaseUrl;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.titan.jnly.Config;

import java.util.Map;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

@BaseUrl(Config.BASE_URL.BaseUrl_1)
public interface IPatrol {

    @POST("api/SDS/GSMM/GetGSMM")
    Observable<TTResult<Map>> httpQueryList(@Body Map map);

    @POST("api/SDS/GSMM/GetGSMM")
    Observable<TTResult<Map>> httpList(@Body Map map);

}
