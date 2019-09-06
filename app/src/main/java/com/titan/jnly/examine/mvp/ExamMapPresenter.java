package com.titan.jnly.examine.mvp;

import com.lib.bandaid.data.remote.core.NetRequest;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.data.remote.listen.NetWorkListen;
import com.lib.bandaid.util.ObjectUtil;
import com.titan.jnly.examine.api.ExamineApi;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class ExamMapPresenter extends NetRequest<Contract.ExamMapView> implements Contract.ExamMapPresenter {

    @Inject
    public ExamMapPresenter() {
    }

    @Override
    public void postDataList(Map map) {
        request(ExamineApi.class, new NetWorkListen<TTResult<Map>>() {

            @Override
            public void onSuccess(TTResult<Map> data) {
                System.out.println(data);
            }

            @Override
            public void onError(int err, String errMsg, Throwable t) {
                System.out.println(errMsg);
            }
        }).httpPostList(map);
    }
}
