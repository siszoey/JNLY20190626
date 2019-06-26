package com.lib.bandaid.utils;

import android.view.View;
import android.widget.TextView;

/**
 * Created by zy on 2019/4/26.
 */

public final class FormUtil {
    FormUtil() {

    }

    private static boolean checkTextView(TextView view) {
        if (view == null) return false;
        return !view.getText().toString().equals("");
    }

   /* private static boolean checkButtonView(CompoundButton view) {
        if (view == null) return false;
        return !view.getText().toString().equals("");
    }*/

    public static boolean checkViewIsNoNull(View view) {
        if (view instanceof TextView) return checkTextView((TextView) view);
        return false;
    }

    public static boolean checkViewsIsNoNull(View... views) {
        if (views == null) return false;
        View view;
        boolean res = true;
        for (int i = 0; i < views.length; i++) {
            view = views[i];
            if (!(view instanceof TextView)) {
                throw new RuntimeException("待检查的控件必须是TextView的子类");
            }
            res = checkTextView((TextView) view);
            if (!res) break;
        }
        return res;
    }
}
