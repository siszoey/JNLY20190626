package com.lib.bandaid.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import com.android.tony.defenselib.DefenseCrash;
import com.android.tony.defenselib.handler.IExceptionHandler;


/**
 * Created by zy on 2018/1/2.
 * BaseApp
 */

public class BaseApplication extends Application implements IExceptionHandler {

    public static BaseApplication baseApp;

    private ActivityLifeCycle atyLifecycleCallback = new ActivityLifeCycle();

    @Override
    public void onCreate() {
        super.onCreate();
        baseApp = this;
        registerActivityLifecycleCallbacks(atyLifecycleCallback);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        DefenseCrash.initialize();
        DefenseCrash.install(this);
    }

    public ActivityLifeCycle getAtyLifecycleCallback() {
        return atyLifecycleCallback;
    }

    /**
     * **************************************************
     */
    @Override
    public void onCaughtException(Thread thread, Throwable throwable, boolean b) {
        throwable.printStackTrace();
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onEnterSafeMode() {

    }

    @Override
    public void onMayBeBlackScreen(Throwable throwable) {

    }
}
