package com.titan.jnly.examine.mvp;

import com.lib.bandaid.data.remote.core.INetRequest;
import com.titan.jnly.login.bean.UserInfo;

import java.util.List;
import java.util.Map;

public interface DataListContract {

    interface View extends INetRequest.BaseView {
        void getListSuccess(List<Map> list);

        void getListFail(String msg);
    }

    interface Presenter extends INetRequest.BasePresenter<View> {

        void getDataList(Integer num, Integer size, String userId);

    }

}
