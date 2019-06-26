package com.lib.bandaid.system.crash.api;

import com.lib.bandaid.data.remote.entity.BaseResult;
import com.lib.bandaid.system.crash.bean.CrashInfo;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by zy on 2019/4/24.
 */

public interface IHttpCrash {

    @POST
    Observable<BaseResult<Map<String, Object>>> httpCrash(@Url String url, @Body CrashInfo crashInfo);

}
