package com.titan.jnly.login.ui.aty;

import javax.inject.Inject;

import com.lib.bandaid.data.remote.core.NetRequest;
import com.lib.bandaid.data.remote.entity.BaseResult;
import com.lib.bandaid.data.remote.listen.NetWorkListen;
import com.lib.bandaid.utils.CodeUtil;
import com.lib.bandaid.utils.SimpleMap;
import com.titan.jnly.login.api.ApiLogin;
import com.titan.jnly.login.bean.User;

import java.util.Map;

public class LoginAtyPresenter extends NetRequest<LoginAtyContract.View> implements LoginAtyContract.Presenter {
    @Inject
    public LoginAtyPresenter() {
    }

    @Override
    public void Login(User user) {
        SimpleMap loginParam = new SimpleMap().push("username", user.getName()).push("pwd", user.getPwd()).push("loginType", "app");
        String info = loginParam.toJson();
        info = CodeUtil.toBase64(info);
        String _64Encode = new SimpleMap().push("logindto", info).toJson();
        request(ApiLogin.class, new NetWorkListen<BaseResult<Map>>() {
            @Override
            public void onSuccess(BaseResult<Map> data) {
                view.LoginSuccess();
            }

            @Override
            public void onError(int err, String errMsg, Throwable t) {
                System.out.println(errMsg);
            }
        }).httpLogin(_64Encode);
    }
}