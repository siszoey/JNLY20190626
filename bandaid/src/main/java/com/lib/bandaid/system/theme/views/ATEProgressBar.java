package com.lib.bandaid.system.theme.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.lib.bandaid.system.theme.utils.ATE;


/**
 * @author Aidan Follestad (afollestad)
 */
public class ATEProgressBar extends ProgressBar {

    public ATEProgressBar(Context context) {
        super(context);
        init(context);
    }

    public ATEProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ATEProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ATEProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setTag("tint_accent_color");
        ATE.apply(context, this);
    }
}