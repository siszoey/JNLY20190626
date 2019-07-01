package com.lib.bandaid.utils;

import android.content.Context;

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

    public WaitingDialog register(Context context) {
        if (!concurrentHashMap.containsKey(context.hashCode())) {
            SoftReference<WaitingDialog> dialogSoftReference = new SoftReference<>(new WaitingDialog(context, 100, 100));
            concurrentHashMap.put(context.hashCode(), dialogSoftReference);
        }
        return get(context);
    }

    public void unRegister(Context context) {
        int hashCode = context.hashCode();
        if (concurrentHashMap.containsKey(hashCode)) {
            WaitingDialog dialog = concurrentHashMap.get(hashCode).get();
            if (dialog != null) dialog.dismiss();
            concurrentHashMap.remove(hashCode);
        }
    }

    public void show(Context context) {
        if (concurrentHashMap.containsKey(context.hashCode())) {
            WaitingDialog dialog = concurrentHashMap.get(context.hashCode()).get();
            if (dialog != null && !dialog.isShowing()) {
                dialog.show();
            }
        }
    }

    private WaitingDialog get(Context context) {
        return concurrentHashMap.get(context.hashCode()).get();
    }


    public void dismiss(Context context) {
        if (concurrentHashMap.containsKey(context.hashCode())) {
            WaitingDialog dialog = concurrentHashMap.get(context.hashCode()).get();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
