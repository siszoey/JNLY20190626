package com.titan.jnly.system;


import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.lib.bandaid.app.BaseApp;
import com.lib.bandaid.data.local.sqlite.proxy.transaction.DbManager;
import com.lib.bandaid.data.remote.util.MappingMethod;
import com.lib.bandaid.service.imp.ServiceLocation;
import com.tencent.bugly.beta.Beta;
import com.tencent.smtt.sdk.QbSdk;
import com.titan.jnly.dbase.DbVersion;
import com.titan.jnly.system.version.bugly.BuglySetting;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class App extends BaseApp {

    final String TAG = "App";
    final String appId = "dccc720c39";

    @Override
    public void onCreate() {
        super.onCreate();
        BuglySetting.init(this, appId);
        DbManager.dbConfig = new DbVersion(baseApp);
        MappingMethod.getInstance(this);

        initX5Web();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Beta.installTinker();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    void initX5Web() {
        Log.i(TAG, "QbSdk.initX5Environment");
        QbSdk.initX5Environment(getApplicationContext(), new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                Log.d(TAG, "onCoreInitFinished");
            }

            @Override
            public void onViewInitFinished(boolean initResult) {
                Log.e(TAG, "onViewInitFinished" + initResult);
            }
        });
    }
}
