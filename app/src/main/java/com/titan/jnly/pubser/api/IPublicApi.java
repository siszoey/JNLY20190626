package com.titan.jnly.pubser.api;

import com.lib.bandaid.data.remote.api.annotation.BaseUrl;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.titan.jnly.Config;
import com.titan.jnly.pubser.bean.ItemContent;
import com.titan.jnly.pubser.bean.Publish;
import com.titan.jnly.pubser.bean.PublishPage;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

@BaseUrl(Config.BASE_URL.BaseUrl_1)
public interface IPublicApi {

    /**
     * 查询公共服务
     *
     * @param type
     * @param title
     * @param pageSize
     * @param pageNum
     * @return
     */
    @GET("api/GZFW/PoliciesRecord/GetList")
    Observable<TTResult<PublishPage>> httpGetList(@Query("pagetype") Integer type, @Query("Title") String title, @Query("PageSize") Integer pageSize, @Query("PageNumber") Integer pageNum);


    @GET("api/GZFW/PoliciesRecord/PoliciesRegutions_Detail")
    Observable<TTResult<Object>> httpGetDetail(@Query("Id") String id, @Query("pagetype") Integer type);
}
