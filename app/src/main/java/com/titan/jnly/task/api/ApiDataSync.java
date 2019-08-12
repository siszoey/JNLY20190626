package com.titan.jnly.task.api;

import com.lib.bandaid.data.remote.api.annotation.BaseUrl;
import com.lib.bandaid.data.remote.entity.TTFileResult;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.titan.jnly.Config;
import com.titan.jnly.task.bean.DataSync;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

@BaseUrl(Config.BASE_URL.BaseUrl_1)
public interface ApiDataSync {

    @POST("api/SDS/GSMM/GSMMUpload")
    Observable<TTResult> httpFileSync(@Body List<DataSync> list);

}
