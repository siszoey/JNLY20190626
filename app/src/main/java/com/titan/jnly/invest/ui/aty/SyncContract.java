package com.titan.jnly.invest.ui.aty;

import com.lib.bandaid.activity.i.ITipView;
import com.lib.bandaid.data.remote.core.INetRequest;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.titan.jnly.invest.bean.DataSync;

import java.util.List;
import java.util.Map;

public interface SyncContract {

    interface View extends ITipView {
        void syncSuccess(TTResult<Map> result);
    }

    interface Presenter extends INetRequest.BasePresenter<View> {

        void syncData(DataSync data);

        void syncSingle(DataSync dataSync);

        void syncMulti(List<DataSync> data);


    }

}