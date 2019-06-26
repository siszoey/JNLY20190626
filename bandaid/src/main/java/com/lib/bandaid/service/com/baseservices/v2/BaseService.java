package com.lib.bandaid.service.com.baseservices.v2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.lib.bandaid.service.com.baseservices.v2.binder.BaseServiceBinder;
import com.lib.bandaid.service.com.baseservices.v2.i.IBaseServiceLifeCycle;


/**
 * Created by zy on 2017/11/11.
 */

public abstract class BaseService extends Service {

    protected IBaseServiceLifeCycle iBaseServiceLifeCycle;

    protected BaseServiceBinder baseServiceBinder;
    protected boolean isServiceRunning = false;
    protected boolean serviceRunningInCreate = false;
    protected boolean serviceRunningInCommand = false;
    /**
     * 是否创建前台服务
     */
    protected boolean isFrontService = false;


    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("返回Binder");
        baseServiceBinder = new BaseServiceBinder();
        baseServiceBinder.setBaseService(this);
        return baseServiceBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("创建");
        isServiceRunning = true;
        doInBackGroundingInitialize();
        if (iBaseServiceLifeCycle != null) {
            iBaseServiceLifeCycle.serviceCreate();
        }
        if (serviceRunningInCreate) {
            new Thread() {
                @Override
                public void run() {
                    while (serviceRunningInCreate) {
                        doInBackGrounding();
                    }
                    doInBackGrounded();
                }
            }.start();
        }
    }

    protected abstract void doInBackGroundingInitialize();

    protected abstract void doInBackGrounding();

    protected abstract void doCommandInBackGroundingInitialize();

    protected abstract void doCommandInBackGrounding();

    protected void startNotify() {
    }

    protected void stopNotify() {
    }

    protected void doInBackGrounded() {
        serviceRunningInCreate = false;
        serviceRunningInCommand = false;
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (iBaseServiceLifeCycle != null) {
            iBaseServiceLifeCycle.serviceStartCommand();
        }
        //System.out.println("onStartCommand");
        doCommandInBackGroundingInitialize();

        if (serviceRunningInCommand) {
            new Thread() {
                @Override
                public void run() {
                    while (serviceRunningInCommand) {
                        //System.out.println("doCommandInBackGrounding");
                        doCommandInBackGrounding();
                    }
                    doInBackGrounded();
                }
            }.start();
        }
        if (isFrontService) {
            super.onStartCommand(intent, flags, startId);
            startNotify();
            return START_STICKY;
        } else {
            return super.onStartCommand(intent, flags, startId);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceRunningInCreate = false;
        serviceRunningInCommand = false;
        isServiceRunning = false;
        if (iBaseServiceLifeCycle != null) {
            iBaseServiceLifeCycle.serviceDestroy();
        }
        if (isFrontService) {
            stopNotify();
        }
        System.out.println("销毁");
    }

    public void setIBaseServiceLifeCycle(IBaseServiceLifeCycle iBaseServiceLifeCycle) {
        this.iBaseServiceLifeCycle = iBaseServiceLifeCycle;
    }
}
