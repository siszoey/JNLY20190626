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
import android.widget.TextView;

import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.app.BaseApp;
import com.lib.bandaid.data.local.sqlite.proxy.transaction.DbManager;
import com.lib.bandaid.permission.Permission;
import com.lib.bandaid.permission.RxConsumer;
import com.lib.bandaid.permission.RxPermissionFactory;
import com.lib.bandaid.permission.SimplePermission;
import com.lib.bandaid.rw.file.utils.FileUtil;
import com.lib.bandaid.system.theme.views.ATECheckBox;
import com.lib.bandaid.system.theme.views.ATEEditText;
import com.lib.bandaid.utils.AppUtil;
import com.lib.bandaid.utils.ObjectUtil;
import com.lib.bandaid.utils.SPfUtil;
import com.lib.bandaid.utils.SimpleMap;
import com.lib.bandaid.utils.StringUtil;
import com.lib.bandaid.utils.ToastUtil;
import com.titan.jnly.Config;
import com.titan.jnly.R;
import com.titan.jnly.login.bean.User;
import com.titan.jnly.login.bean.UserInfo;
import com.titan.jnly.map.ui.aty.MapActivity;
import com.titan.jnly.system.Constant;

import java.io.File;
import java.util.Map;

public class LoginAty extends BaseMvpCompatAty<LoginAtyPresenter> implements LoginAtyContract.View, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private final static String CONSTANT_IS_REMEMBER = "CONSTANT_IS_REMEMBER";

    private TextView copyRight;
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
        copyRight = $(R.id.copyRight);
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
        boolean isDebug = AppUtil.isApkInDebug(this);
        copyRight.setText("北京航天泰坦:" + AppUtil.getApkVersionName(this));
    }

    @Override
    protected void initClass() {
        isRemember = SPfUtil.readT(_context, CONSTANT_IS_REMEMBER);
        isRemember = isRemember == null ? true : isRemember;
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
            if (ObjectUtil.isEmpty(cetPhoneNum.getText().toString()) || ObjectUtil.isEmpty(cetPwd.getText().toString())) {
                ToastUtil.showLong(_context, "请填写用户名和密码！");
                return;
            }
            //保存账号信息
            user = new User(cetPhoneNum.getText().toString(), cetPwd.getText().toString());
            if (isRemember) Constant.putUser(user);
            //用户验证
            Map condition = new SimpleMap().push("UserName", user.getName());
            UserInfo userInfo = (UserInfo) DbManager.createDefault().getTByMultiCondition(UserInfo.class, condition);
            if (userInfo != null) {
                LoginSuccess(userInfo);
                return;
            }
            presenter.Login(user);
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
    }

    @Override
    public void LoginSuccess(UserInfo info) {
        //保存用户信息到本地
        if (info != null) {
            DbManager.createDefault().saveOrUpdate(info);
            Constant.putUserInfo(info);
        }
        Constant.putUser(new User(cetPhoneNum.getText().toString(), cetPwd.getText().toString()));
        //清除其他activity
        BaseApp.baseApp.getAtyLifecycleCallback().removeOtherActivities(this);
        startActivity(new Intent(_context, MapActivity.class));
        finish();
    }

    //---------------------------------------------本地数据-----------------------------------------

    private void permissions() {
        RxPermissionFactory
                .getRxPermissions(_context)
                .requestEachCombined(SimplePermission.MANIFEST_EASY)
                .subscribe(new RxConsumer(_context) {
                    @Override
                    public void accept(Permission permission) {
                        super.accept(permission);
                        if (permission.granted) {
                            handlePermission();
                        } else {
                            finish();
                        }
                    }
                });
    }

    public void handlePermission() {
        //创建文集目录
        FileUtil.createFileSmart(
                Config.APP_DB_PATH,
                Config.APP_SDB_PATH,
                Config.APP_MAP_CACHE,
                Config.APP_PATH_CRASH,
                Config.APP_PHOTO_DIR,
                Config.APP_PATH_DIC
        );
        //创建系统字典表
        FileUtil.copyAssets(_context, Config.DIC_DB_MODULE, Config.APP_DIC_DB_PATH);
        //是否为app首次安装
        appFirstInstall();
        //是否为当前版本首次安装
        versionFirstInstall();

        //读取数据到内存里
        Constant.initialize(_context, Config.GEO_TB_MODULE);
    }

    private void appFirstInstall() {
        boolean flag = AppUtil.isAppFirstInstall(_context);
        //如果app是首次安装，则复制业务库模板到指定文件夹
        if (flag) {
            String sdbPath = Config.APP_SDB_PATH.concat(File.separator).concat(Config.GEO_DB_NAME[0]);
            boolean isExist = FileUtil.isExist(sdbPath);
            if (isExist) return;
            FileUtil.copyAssets(_context, Config.GEO_DB_MODULE[0], sdbPath);
        }
    }

    private void versionFirstInstall() {
        boolean flag = AppUtil.isVersionFirstInstall(_context);
        if (flag) {
            //如果版本更新，就更新字典数据库
            FileUtil.deleteFile(Config.APP_DIC_DB_PATH);
            FileUtil.copyAssets(_context, Config.DIC_DB_MODULE, Config.APP_DIC_DB_PATH);
        }
    }
}
