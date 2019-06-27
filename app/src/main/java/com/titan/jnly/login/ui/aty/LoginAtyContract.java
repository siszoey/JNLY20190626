package com.titan.jnly.login.ui.aty;

import com.lib.bandaid.data.remote.core.INetRequest;

public interface LoginAtyContract {

    interface View extends INetRequest.BaseView {
        void LoginSuccess();
    }

    interface Presenter extends INetRequest.BasePresenter<View> {

        void Login(String account, String password);

    }

}