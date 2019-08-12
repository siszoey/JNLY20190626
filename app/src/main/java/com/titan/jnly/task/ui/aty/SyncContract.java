package com.titan.jnly.task.ui.aty;

import com.lib.bandaid.data.remote.core.INetRequest;
import com.titan.jnly.login.bean.User;
import com.titan.jnly.login.bean.UserInfo;
import com.titan.jnly.task.bean.DataSync;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface SyncContract {

    interface View extends INetRequest.BaseView {
        void syncSuccess();
    }

    interface Presenter extends INetRequest.BasePresenter<View> {

        void syncData(DataSync data);

        void syncData(List<DataSync> data);

        void syncFile(List<File> files);

        void syncSingle(List<File> files, DataSync dataSync);

    }

}