package com.titan.jnly.invest.api;

import com.lib.bandaid.data.remote.api.annotation.BaseUrl;
import com.lib.bandaid.data.remote.entity.TTFileResult;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.titan.jnly.Config;
import com.titan.jnly.Config_pro;
import com.titan.jnly.login.bean.UserInfo;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 图片上传服务
 */
@BaseUrl(Config.BASE_URL.BaseFileService)
public interface ApiFileSync {

    @POST("api/FileServer/upload")
    Observable<TTFileResult> httpFileSync(@Body MultipartBody file);

}
