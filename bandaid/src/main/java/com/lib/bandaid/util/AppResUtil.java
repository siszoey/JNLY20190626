package com.lib.bandaid.util;

import android.content.Context;

import androidx.annotation.ColorRes;

public final class AppResUtil {

    public static int getColor(Context context, @ColorRes int color) {
        return context.getResources().getColor(color);
    }
}
