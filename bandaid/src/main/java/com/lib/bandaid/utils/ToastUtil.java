package com.lib.bandaid.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zy on 2019/5/10.
 */

public final class ToastUtil {
    private ToastUtil() {

    }

    public static void showLong(Context context, Object msg) {
        if (context == null || msg == null) return;
        Toast.makeText(context, msg.toString(), Toast.LENGTH_LONG).show();
    }
}
