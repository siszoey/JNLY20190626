package com.titan.jnly.examine.mvp;

import com.lib.bandaid.data.remote.core.NetRequest;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.data.remote.listen.NetWorkListen;
import com.titan.jnly.examine.api.ExamineApi;

import java.util.Map;

import javax.inject.Inject;

public class P extends NetRequest<Contract.ExamMapView> {


    public void postDataList(Map map, NetWorkListen listen) {
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

    public void postDataList1(Map map, NetWorkListen listen) {
        request(ExamineApi.class, listen).httpPostList(map);
    }
}
