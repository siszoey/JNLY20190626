package com.titan.jnly.invest.ui.aty;

import com.lib.bandaid.data.remote.core.INetRequest;
import com.titan.jnly.login.bean.User;
import com.titan.jnly.login.bean.UserInfo;

public interface InvestAtyContract {

    interface View extends INetRequest.BaseView {
        void reqSuccess(UserInfo info);
    }

    interface Presenter extends INetRequest.BasePresenter<View> {

        void requestInfo(User user);

    }
}
