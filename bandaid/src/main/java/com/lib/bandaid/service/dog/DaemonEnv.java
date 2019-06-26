package com.lib.bandaid.service.dog;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lib.bandaid.service.imp.TraceService;

import java.util.HashMap;
import java.util.Map;

public final class DaemonEnv {

    private DaemonEnv() {
    }

    public static final int DEFAULT_WAKE_UP_INTERVAL = 6 * 60 * 1000;
    private static final int MINIMAL_WAKE_UP_INTERVAL = 3 * 60 * 1000;

    static Context app;
    static boolean initialized;
    static Class<? extends HomeService> sServiceClass;
    private static int sWakeUpInterval = DEFAULT_WAKE_UP_INTERVAL;

   private static final Map<Class<? extends Service>, ServiceConnection> BIND_STATE_MAP = new HashMap<>();

    public static void initialize(@NonNull Context app, @NonNull Class<? extends HomeService> serviceClass) {
        DaemonEnv.app = app;
        sServiceClass = serviceClass;
        initialized = true;
        TraceService.shouldStopService = false;
    }

    /**
     * @param app            Application Context.
     * @param wakeUpInterval 定时唤醒的时间间隔(ms).
     */
    public static void initialize(@NonNull Context app, @NonNull Class<? extends HomeService> serviceClass, @Nullable Integer wakeUpInterval) {
        DaemonEnv.app = app;
        sServiceClass = serviceClass;
        if (wakeUpInterval != null) sWakeUpInterval = wakeUpInterval;
        initialized = true;
    }

    public static void startServiceMayBind(@NonNull final Class<? extends Service> serviceClass) {
        if (!initialized) return;
        final Intent i = new Intent(app, serviceClass);
        startServiceSafely(i);
        ServiceConnection bound = BIND_STATE_MAP.get(serviceClass);
        if (bound == null) app.bindService(i, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                BIND_STATE_MAP.put(serviceClass, this);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                BIND_STATE_MAP.remove(serviceClass);
                startServiceSafely(i);
                if (!initialized) return;
                app.bindService(i, this, Context.BIND_AUTO_CREATE);
            }

            @Override
            public void onBindingDied(ComponentName name) {
                onServiceDisconnected(name);
            }
        }, Context.BIND_AUTO_CREATE);
    }

    static void startServiceSafely(Intent i) {
        if (!initialized) return;
        try {
            app.startService(i);
        } catch (Exception ignored) {
        }
    }

     static int getWakeUpInterval() {
        return Math.max(sWakeUpInterval, MINIMAL_WAKE_UP_INTERVAL);
    }
}
