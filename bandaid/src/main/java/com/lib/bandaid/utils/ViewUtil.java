package com.lib.bandaid.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by zy on 2018/1/8.
 */

public class ViewUtil {
    public static <T extends View> T findViewById(Activity activity, int resId) {
        return (T) activity.findViewById(resId);
    }

    public static <T extends View> T findViewById(View view, int resId) {
        return (T) view.findViewById(resId);
    }


    public static int getViewColorDrawable(View view) {
        int color = -1;
        Drawable background = view.getBackground();
        if (background instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) background;
            color = colorDrawable.getColor();
        }
        return color;
    }

    public static Bitmap getViewBitmapDrawable(View view) {
        Drawable background = view.getBackground();
        Bitmap bgBitmap = null;
        if (background instanceof BitmapDrawable) {
            bgBitmap = ((BitmapDrawable) background).getBitmap();
        }
        return bgBitmap;
    }
}
