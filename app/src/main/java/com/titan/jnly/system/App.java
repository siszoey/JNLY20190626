package com.titan.jnly.system;


import android.content.Context;

import com.lib.bandaid.app.BaseApp;
import com.lib.bandaid.data.local.sqlite.proxy.transaction.DbManager;
import com.tencent.bugly.beta.Beta;
import com.titan.jnly.dbase.DbVersion;
import com.titan.jnly.system.version.bugly.BuglySetting;

public class App extends BaseApp {

    final String appId = "dccc720c39";

    @Override
    public void onCreate() {
        super.onCreate();
        BuglySetting.init(this, appId);
        DbManager.dbConfig = new DbVersion(baseApp);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Beta.installTinker();
    }

}
