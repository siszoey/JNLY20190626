package com.titan.jnly.login.ui.aty;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.system.theme.views.ATECheckBox;
import com.lib.bandaid.system.theme.views.ATEEditText;
import com.titan.jnly.R;

public class LoginAty extends BaseMvpCompatAty<LoginAtyPresenter> implements LoginAtyContract.View, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ATEEditText cetPhoneNum, cetPwd;
    private ATECheckBox ckRemember;
    private ImageView ivShowPwd;
    private Button btnLogin;
    private boolean showPwd = false;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        presenter = new LoginAtyPresenter();
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_back, "登录", Gravity.CENTER);
        setContentView(R.layout.login_ui_aty_login_layout);
    }

    @Override
    protected void initialize() {
        cetPhoneNum = $(R.id.cetPhoneNum);
        cetPwd = $(R.id.cetPwd);
        ivShowPwd = $(R.id.ivShowPwd);
        btnLogin = $(R.id.btnLogin);
        ckRemember = $(R.id.ckRemember);
    }

    @Override
    protected void registerEvent() {
        btnLogin.setOnClickListener(this);
        ivShowPwd.setOnClickListener(this);
        ckRemember.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initClass() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin) {
            presenter.Login(cetPhoneNum.getText().toString(), cetPhoneNum.getText().toString());
        }
        if (v.getId() == R.id.ivShowPwd) {
            showPwd = !showPwd;
            cetPwd.setTransformationMethod((showPwd) ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
            ivShowPwd.setImageResource((showPwd) ? R.mipmap.ic_pass_open : R.mipmap.ic_pass_gone);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void LoginSuccess() {
        ToastUtils.showLong("页面跳转");
    }
}
