package com.lib.bandaid.utils;

import android.app.Activity;
import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lib.bandaid.widget.dialog.WaitingDialog;

import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zy on 2019/4/24.
 */

public final class DialogFactory {

    private ConcurrentHashMap<Integer, SoftReference<WaitingDialog>> concurrentHashMap;

    private static volatile DialogFactory singleton;

    private DialogFactory() {
        this.concurrentHashMap = new ConcurrentHashMap<>();
    }

    public static DialogFactory getFactory() {
        if (singleton == null) {
            synchronized (DialogFactory.class) {
                if (singleton == null) {
                    singleton = new DialogFactory();
                }
            }
        }
        return singleton;
    }

    public WaitingDialog register(Activity activity) {
        if (!concurrentHashMap.containsKey(activity.getTaskId())) {
            SoftReference<WaitingDialog> dialogSoftReference = new SoftReference<>(new WaitingDialog(activity, 100, 100));
            concurrentHashMap.put(activity.getTaskId(), dialogSoftReference);
        }
        return get(activity);
    }

    public void unRegister(Activity activity) {
        int taskId = activity.getTaskId();
        if (concurrentHashMap.containsKey(taskId)) {
            WaitingDialog dialog = concurrentHashMap.get(taskId).get();
            if (dialog != null) dialog.dismiss();
            concurrentHashMap.remove(taskId);
        }
    }

    public void show(Activity activity) {
        if (concurrentHashMap.containsKey(activity.getTaskId())) {
            WaitingDialog dialog = concurrentHashMap.get(activity.getTaskId()).get();
            if (dialog != null && !dialog.isShowing()) {
                dialog.show();
            }
        }
    }

    private WaitingDialog get(Activity activity) {
        return concurrentHashMap.get(activity.getTaskId()).get();
    }


    public void dismiss(Activity activity) {
        if (concurrentHashMap.containsKey(activity.getTaskId())) {
            WaitingDialog dialog = concurrentHashMap.get(activity.getTaskId()).get();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }


    public static MaterialDialog createDialogUnCancel(Context context) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.progress(true, 0);
        builder.cancelable(false);
        builder.canceledOnTouchOutside(false);
        return builder.build();
    }

    public static MaterialDialog showLoadProgress(Context context, String msg, boolean isCancel) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.content(msg);
        builder.progress(true, 0);
        builder.cancelable(isCancel);
        builder.canceledOnTouchOutside(isCancel);
        return builder.build();
    }
}
