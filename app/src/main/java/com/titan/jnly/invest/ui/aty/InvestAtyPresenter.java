package com.titan.jnly.invest.ui.aty;

import com.lib.bandaid.data.remote.core.NetRequest;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.data.remote.listen.NetWorkListen;
import com.titan.jnly.login.api.ApiLogin;
import com.titan.jnly.login.bean.User;
import com.titan.jnly.login.bean.UserInfo;

import javax.inject.Inject;

public class InvestAtyPresenter extends NetRequest<InvestAtyContract.View> implements InvestAtyContract.Presenter {
    @Inject
    public InvestAtyPresenter() {
    }

    @Override
    public void requestInfo(User user) {
        request(ApiLogin.class, new NetWorkListen<TTResult<UserInfo>>() {
            @Override
            public void onSuccess(TTResult<UserInfo> data) {
                view.reqSuccess(data.getContent());
            }

            @Override
            public void onError(int err, String errMsg, Throwable t) {
                System.out.println(errMsg);
            }
        }).httpLogin(user.getName(), user.getPwd());
    }
}