package com.titan.jnly.task.ui.aty;

import com.lib.bandaid.data.remote.core.NetRequest;
import com.lib.bandaid.data.remote.entity.TTFileResult;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.data.remote.listen.NetWorkListen;
import com.lib.bandaid.data.remote.utils.OkHttp3Util;
import com.lib.bandaid.util.SimpleList;
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
        request(ApiDataSync.class, new NetWorkListen<TTResult<Map>>() {
            @Override
            public void onSuccess(TTResult<Map> data) {
                view.syncSuccess(data);
            }

        }).httpFileSync(new SimpleList<>().push(data));
    }

    @Override
    public void syncSingle(DataSync dataSync) {
        List<File> files = dataSync.getFiles();
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
            }).httpFileSync(body);
        }
    }

    @Override
    public void syncMulti(List<DataSync> data) {
        DataSync dataSync;
        for (int i = 0; i < data.size(); i++) {
            dataSync = data.get(i);

        }
        request(ApiDataSync.class, new NetWorkListen<TTResult>() {
            @Override
            public void onSuccess(TTResult data) {
                //view.LoginSuccess(data.getContent());
            }
        }).httpFileSync(data);
    }
}