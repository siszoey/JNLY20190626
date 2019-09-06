package com.titan.jnly.examine.mvp;

import com.lib.bandaid.data.remote.core.NetRequest;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.data.remote.listen.NetWorkListen;
import com.lib.bandaid.util.ObjectUtil;
import com.titan.jnly.examine.api.ExamineApi;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class DataListPresenter extends NetRequest<Contract.DataListView> implements Contract.DataListPresenter {

    @Inject
    public DataListPresenter() {
    }

    @Override
    public void getDataList(Integer num, Integer size, String userId) {
        request(ExamineApi.class, new NetWorkListen<TTResult<Map>>() {

            @Override
            public void onSuccess(TTResult<Map> data) {
                List<Map> list = ObjectUtil.convert(data.getContent().get("rows"), Map.class);
                view.getListSuccess(list);
            }

            @Override
            public void onError(int err, String errMsg, Throwable t) {
                view.getListFail(errMsg);
            }
        }).httpGetList(num, size, userId);
    }

    @Override
    public void postDataList(Map map) {
        request(ExamineApi.class, new NetWorkListen<TTResult<Map>>() {

            @Override
            public void onSuccess(TTResult<Map> data) {
                List<Map> list = ObjectUtil.convert(data.getContent().get("rows"), Map.class);
                view.getListSuccess(list);
            }

            @Override
            public void onError(int err, String errMsg, Throwable t) {
                view.getListFail(errMsg);
            }
        }).httpPostList(map);
    }
}
