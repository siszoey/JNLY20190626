package com.lib.bandaid.data.remote.api;

import com.lib.bandaid.data.remote.api.annotation.BaseUrl;
import com.lib.bandaid.data.remote.entity.BaseResult;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

@BaseUrl("http://127.0.0.1:8080/")
public interface ExampleApi {

    @POST("api/v1/cure/task/add")
    Observable<BaseResult<Boolean>> httpTaskAdd(@Body Map map);

}
