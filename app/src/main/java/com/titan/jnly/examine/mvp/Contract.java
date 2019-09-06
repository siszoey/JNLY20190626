package com.titan.jnly.examine.mvp;

import com.lib.bandaid.data.remote.core.INetRequest;
import com.titan.jnly.login.bean.UserInfo;

import java.util.List;
import java.util.Map;

public interface Contract {

    interface DataListView extends INetRequest.BaseView {
        void getListSuccess(List<Map> list);

        void getListFail(String msg);
    }

    interface DataListPresenter extends INetRequest.BasePresenter<DataListView> {
        void getDataList(Integer num, Integer size, String userId);
    }


    interface ExamMapView extends INetRequest.BaseView {
        void getListSuccess(List<Map> list);

        void getListFail(String msg);
    }

    interface ExamMapPresenter extends INetRequest.BasePresenter<ExamMapView> {
        void postDataList(Map map);
    }

}
