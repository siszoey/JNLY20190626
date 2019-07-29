package com.lib.bandaid.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;

import com.lib.bandaid.R;
import com.lib.bandaid.widget.easyui.ui_v1.ComplexTextView;

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

    public static <T> T setRightDrawable(View view, @DrawableRes int id) {
        Context context = view.getContext();
        if (view instanceof TextView) {
            Drawable drawable = context.getResources().getDrawable(id);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            ((TextView) view).setCompoundDrawables(null, null, drawable, null);
        }
        if (view instanceof ComplexTextView) {
            Drawable drawable = context.getResources().getDrawable(id);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            (((ComplexTextView) view).getEditText()).setCompoundDrawables(null, null, drawable, null);
        }
        return (T) view;
    }

    public static <T> T setLeftDrawable(View view, @DrawableRes int id) {
        Context context = view.getContext();
        if (view instanceof TextView) {
            Drawable drawable = context.getResources().getDrawable(id);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            ((TextView) view).setCompoundDrawables(drawable, null, null, null);
        }
        return (T) view;
    }
}
