package com.lib.bandaid.system.crash.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.bandaid.R;
import com.lib.bandaid.activity.BaseAppCompatAty;
import com.lib.bandaid.system.crash.CrashHandler;
import com.lib.bandaid.system.crash.api.IHttpCrash;
import com.lib.bandaid.system.crash.bean.CrashInfo;
import com.lib.bandaid.utils.AppUtil;
import com.lib.bandaid.utils.DialogFactory;
import com.lib.bandaid.utils.ProgressDialogCUtil;


/**
 * Created by zy on 2019/4/23.
 */

public class CrashActivity extends BaseAppCompatAty implements View.OnClickListener {

    CrashInfo crashInfo;

    TextView tvCrashInfo;
    TextView tvCancel;
    TextView tvSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) crashInfo = (CrashInfo) intent.getSerializableExtra(CrashHandler.TAG);
        super.onCreate(savedInstanceState);
        initTitle(null, AppUtil.getAppName(this) + "错误信息", Gravity.CENTER);
        setContentView(R.layout.system_crash_ui_layout);
        adjustActivitySize(0.85f, 0.6f);
    }

    @Override
    protected void initialize() {
        tvCrashInfo = $(R.id.tvCrashInfo);
        tvCancel = $(R.id.tvCancel);
        tvSubmit = $(R.id.tvSubmit);
        DialogFactory.getFactory().show(this);
    }

    @Override
    protected void registerEvent() {
        tvCancel.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

    @Override
    protected void initClass() {
        if (crashInfo != null) tvCrashInfo.setText(crashInfo.getCrashInfo());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvCancel) {
            finish();
        }
        if (v.getId() == R.id.tvSubmit) {
            submitError(crashInfo);
        }
    }

    private void submitError(CrashInfo crashInfo) {
        /*Observable observable = BaseApi.createDefaultApi(IHttpCrash.class).httpCrash(crashInfo.reportUrl, crashInfo);
        if (observable == null) return;
        BaseApi.doRequest(observable, new IMvpHttpListener() {
            @Override
            public void before() {
                ProgressDialogCUtil.showProgressDialog(_activity);
            }

            @Override
            public void onNext(Object result) {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(_context, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void complete() {
                ProgressDialogCUtil.cancelProgressDialog(_activity);
            }
        });*/
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
