package com.lib.bandaid.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public final class IntentUtil {

    public static <T> T getTData(Context context, String key) {
        Activity activity = null;
        if (context instanceof Activity) activity = (Activity) context;
        if (activity == null) return null;
        Intent intent = activity.getIntent();
        if (intent == null) return null;
        T t = (T) intent.getSerializableExtra(key);
        return t;
    }

    public static Boolean getBooleanData(Context context, String key) {
        Activity activity = null;
        if (context instanceof Activity) activity = (Activity) context;
        if (activity == null) return null;
        Intent intent = activity.getIntent();
        if (intent == null) return null;
        return intent.getBooleanExtra(key, false);
    }
}
