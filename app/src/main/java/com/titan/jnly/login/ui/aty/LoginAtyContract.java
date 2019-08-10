package com.titan.jnly.login.ui.aty;

import com.lib.bandaid.data.remote.core.INetRequest;
import com.titan.jnly.login.bean.User;
import com.titan.jnly.login.bean.UserInfo;

public interface LoginAtyContract {

    interface View extends INetRequest.BaseView {
        void LoginSuccess(UserInfo info);
    }

    interface Presenter extends INetRequest.BasePresenter<View> {

        void Login(User user);

    }

}