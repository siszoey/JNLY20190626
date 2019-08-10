package com.lib.bandaid.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zy on 2019/4/23.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ActivityLifeCycle implements Application.ActivityLifecycleCallbacks {


    private List<Activity> activities = new LinkedList<>();

    public static int sAnimationId = 0;


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        addActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {


    }

    @Override
    public void onActivityResumed(Activity activity) {

    }


    @Override
    public void onActivityPaused(Activity activity) {

    }


    @Override
    public void onActivityStopped(Activity activity) {

    }


    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }


    @Override
    public void onActivityDestroyed(Activity activity) {
        removeActivity(activity);
    }


    /**
     * 添加Activity
     */
    public void addActivity(Activity activity) {
        if (activities == null) activities = new LinkedList<>();
        if (!activities.contains(activity)) {
            activities.add(activity);
        }
    }


    /**
     * 移除Activity
     */

    public void removeActivity(Activity activity) {
        if (activities.contains(activity)) activities.remove(activity);
        if (activities.size() == 0) activities = null;
    }


    /**
     * 销毁所有activity
     */
    public void removeAllActivities() {
        for (Activity activity : activities) {
            if (null != activity) {
                activity.finish();
                activity.overridePendingTransition(0, sAnimationId);
            }
        }
    }

    public void removeOtherActivities(Activity activity) {
        for (Activity aty : activities) {
            if (null != aty && aty != activity) {
                aty.finish();
                aty.overridePendingTransition(0, sAnimationId);
            }
        }
    }

}
