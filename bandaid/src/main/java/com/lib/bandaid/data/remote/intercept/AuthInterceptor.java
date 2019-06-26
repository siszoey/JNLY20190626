package com.lib.bandaid.data.remote.intercept;

import com.lib.bandaid.app.BaseApplication;
import com.lib.bandaid.data.remote.header.ComHeader;
import com.lib.bandaid.utils.CacheUtil;
import com.lib.bandaid.utils.SimpleMap;
import com.lib.bandaid.utils.SystemUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zy on 2018/5/28.
 * 权限拦截器
 */

public class AuthInterceptor implements Interceptor {

    private final String AUTH_INFO = "AUTH_INFO";
    /**
     * 检查版本和登录免验证
     */
    private final String[] unInterceptor = {"checkAppVersion", "login", "MobeleLoginAllConfig"};

    private static AuthInterceptor authInterceptor;

    private static CacheUtil httpCache;

    private AuthInterceptor() {

    }

    public static AuthInterceptor createAuthInterceptor() {
        if (authInterceptor == null) {
            authInterceptor = new AuthInterceptor();
        }
        if (httpCache == null) {
            httpCache = CacheUtil.get(BaseApplication.baseApp);
        }
        return authInterceptor;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        String requestUrl = chain.request().url().toString();
        SimpleMap simpleMap = (SimpleMap) httpCache.getAsObject(AUTH_INFO);
        /**
         * token
         */
        String token = simpleMap == null ? "" : simpleMap.get("token") == null ? "" : simpleMap.get("token").toString();
        /**
         * 公共头
         */
        ComHeader comHeader = httpCache.getAsT(ComHeader.HEAD_FLAG);
        boolean flag = interceptor(requestUrl);
        System.out.println(flag);
        if (comHeader == null && flag) {
            httpCache.remove(AUTH_INFO);
            token = "unknow_token!";
        }
        String commonParameters = ComHeader.encode(comHeader) == null ? "" : ComHeader.encode(comHeader);
        String agent = SystemUtil.getPhoneInfo().toJson();
        Request original = chain.request();
        Request request = original.newBuilder()
                .header("User-Agent", agent)
                .header("Authorization", token)
                .header(ComHeader.HEAD_FLAG, commonParameters)
                .method(original.method(), original.body())
                .build();

        Response response = chain.proceed(request);
        return response;
    }

    private boolean interceptor(String url) {
        String method;
        for (int i = 0; i < unInterceptor.length; i++) {
            method = unInterceptor[i];
            if (url.contains(method)) {
                return false;
            }
        }
        return true;
    }
}
