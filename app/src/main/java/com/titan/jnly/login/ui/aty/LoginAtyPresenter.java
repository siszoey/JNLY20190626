package com.titan.jnly.login.ui.aty;

import javax.inject.Inject;

import com.lib.bandaid.data.remote.core.NetRequest;
import com.lib.bandaid.data.remote.entity.BaseResult;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.data.remote.listen.NetWorkListen;
import com.lib.bandaid.utils.CodeUtil;
import com.lib.bandaid.utils.SimpleMap;
import com.titan.jnly.login.api.ApiLogin;
import com.titan.jnly.login.bean.User;
import com.titan.jnly.login.bean.UserInfo;

import java.util.Map;

public class LoginAtyPresenter extends NetRequest<LoginAtyContract.View> implements LoginAtyContract.Presenter {
    @Inject
    public LoginAtyPresenter() {
    }

    @Override
    public void Login(User user) {
        request(ApiLogin.class, new NetWorkListen<TTResult<UserInfo>>() {
            @Override
            public void onSuccess(TTResult<UserInfo> data) {
                view.LoginSuccess(data.getContent());
            }

            @Override
            public void onError(int err, String errMsg, Throwable t) {
                System.out.println(errMsg);
            }
        }).httpLogin(user.getName(), user.getPwd());
    }
}