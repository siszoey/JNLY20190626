package com.titan.jnly.login.ui.aty;

import android.content.Intent;
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
import com.lib.bandaid.permission.Permission;
import com.lib.bandaid.permission.RxConsumer;
import com.lib.bandaid.permission.RxPermissionFactory;
import com.lib.bandaid.permission.SimplePermission;
import com.lib.bandaid.rw.file.utils.FileUtil;
import com.lib.bandaid.system.theme.views.ATECheckBox;
import com.lib.bandaid.system.theme.views.ATEEditText;
import com.lib.bandaid.utils.SPfUtil;
import com.lib.bandaid.utils.StringUtil;
import com.titan.jnly.Config;
import com.titan.jnly.R;
import com.titan.jnly.login.bean.User;
import com.titan.jnly.main.ui.aty.MainActivity;
import com.titan.jnly.map.ui.aty.MapActivity;
import com.titan.jnly.system.Constant;

import javax.inject.Inject;

public class LoginAty extends BaseMvpCompatAty<LoginAtyPresenter> implements LoginAtyContract.View, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private final static String CONSTANT_IS_REMEMBER = "CONSTANT_IS_REMEMBER";

    private Boolean isRemember;
    private ATEEditText cetPhoneNum, cetPwd;
    private ATECheckBox ckRemember;
    private ImageView ivShowPwd;
    private Button btnLogin;
    private boolean showPwd = false;

    private User user = new User();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        presenter = new LoginAtyPresenter();
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_back, "登录", Gravity.CENTER);
        setContentView(R.layout.login_ui_aty_login_layout);
        permissions();
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
        isRemember = SPfUtil.readT(_context, CONSTANT_IS_REMEMBER);
        isRemember = isRemember == null ? false : isRemember;
        ckRemember.setChecked(isRemember);
        if (isRemember) {
            user = Constant.getUser();
            if (user == null) user = new User();
            cetPhoneNum.setText(StringUtil.removeNull(user.getName()));
            cetPwd.setText(StringUtil.removeNull(user.getPwd()));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin) {
            //保存账号信息
            user = new User(cetPhoneNum.getText().toString(), cetPwd.getText().toString());
            if (isRemember) {
                Constant.putUser(user);
            }
            // presenter.Login(user);
            LoginSuccess();
        }
        if (v.getId() == R.id.ivShowPwd) {
            showPwd = !showPwd;
            cetPwd.setTransformationMethod((showPwd) ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
            ivShowPwd.setImageResource((showPwd) ? R.mipmap.ic_pass_open : R.mipmap.ic_pass_gone);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SPfUtil.putT(_context, CONSTANT_IS_REMEMBER, isChecked);
        isRemember = SPfUtil.readT(_context, CONSTANT_IS_REMEMBER);
        if (!isChecked) {
            Constant.delUser();
        }
    }

    @Override
    public void LoginSuccess() {
        Constant.putUser(new User(cetPhoneNum.getText().toString(), cetPwd.getText().toString()));
        //startActivity(new Intent(_context, MainActivity.class));
        startActivity(new Intent(_context, MapActivity.class));
        finish();
    }

    private void permissions() {
        RxPermissionFactory
                .getRxPermissions(_context)
                .requestEachCombined(SimplePermission.MANIFEST_STORAGE)
                .subscribe(new RxConsumer(_context) {
                    @Override
                    public void accept(Permission permission) {
                        super.accept(permission);
                        if (permission.granted) {
                            FileUtil.createFileSmart(Config.APP_DB_PATH, Config.APP_SDB_PATH, Config.APP_MAP_CACHE, Config.APP_PATH_CRASH);
                        } else {
                            finish();
                        }
                    }
                });
    }
}
