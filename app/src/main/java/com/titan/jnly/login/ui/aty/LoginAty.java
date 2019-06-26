package com.titan.jnly.login.ui.aty;

import android.os.Bundle;
import android.view.Gravity;

import com.lib.bandaid.activity.BaseAppCompatAty;
import com.titan.jnly.R;

public class LoginAty extends BaseAppCompatAty {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_back, "登录", Gravity.CENTER);

    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void registerEvent() {

    }

    @Override
    protected void initClass() {

    }
}
