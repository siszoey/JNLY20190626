package com.titan.jnly.task.ui.aty;

import com.lib.bandaid.data.remote.core.NetRequest;
import com.lib.bandaid.data.remote.entity.TTFileResult;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.data.remote.listen.NetWorkListen;
import com.lib.bandaid.data.remote.utils.OkHttp3Util;
import com.lib.bandaid.utils.SimpleList;
import com.titan.jnly.login.api.ApiLogin;
import com.titan.jnly.login.bean.User;
import com.titan.jnly.login.bean.UserInfo;
import com.titan.jnly.login.ui.aty.LoginAtyContract;
import com.titan.jnly.task.api.ApiDataSync;
import com.titan.jnly.task.api.ApiFileSync;
import com.titan.jnly.task.bean.DataSync;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.MultipartBody;


public class SyncPresenter extends NetRequest<SyncContract.View> implements SyncContract.Presenter {
    @Inject
    public SyncPresenter() {
    }


    @Override
    public void syncData(DataSync data) {
        request(ApiDataSync.class, new NetWorkListen<TTResult>() {
            @Override
            public void onSuccess(TTResult data) {
                view.syncSuccess();
            }

            @Override
            public void onError(int err, String errMsg, Throwable t) {
                //System.out.println(errMsg);
            }
        }).httpFileSync(new SimpleList<>().push(data));
    }

    @Override
    public void syncData(List<DataSync> data) {
        request(ApiDataSync.class, new NetWorkListen<TTResult>() {
            @Override
            public void onSuccess(TTResult data) {
                //view.LoginSuccess(data.getContent());
            }

            @Override
            public void onError(int err, String errMsg, Throwable t) {
                //System.out.println(errMsg);
            }
        }).httpFileSync(data);
    }

    @Override
    public void syncFile(List<File> files) {
        if (files == null || files.size() == 0) return;
        MultipartBody body;
        for (File file : files) {
            body = OkHttp3Util.fileBody(file);
            request(ApiFileSync.class, new NetWorkListen<Object>() {
                @Override
                public void onSuccess(Object data) {

                }
            }).httpFileSync(body);
        }
    }

    @Override
    public void syncSingle(List<File> files, DataSync dataSync) {
        if (files == null || files.size() == 0) {
            syncData(dataSync);
            return;
        }
        MultipartBody body;
        List<TTFileResult> results = new ArrayList<>();
        for (File file : files) {
            body = OkHttp3Util.fileBody(file);
            request(ApiFileSync.class, new NetWorkListen<TTFileResult>() {
                @Override
                public void onSuccess(TTFileResult data) {
                    results.add(data);
                    if (results.size() == files.size()) {
                        dataSync.setImages(results);
                        syncData(dataSync);
                    }
                }

                @Override
                public void onError(int err, String errMsg, Throwable t) {
                    System.err.println(errMsg);
                }
            }).httpFileSync(body);
        }
    }
}