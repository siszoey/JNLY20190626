package com.lib.bandaid.system.theme.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.lib.bandaid.system.theme.utils.ATE;


/**
 * @author Aidan Follestad (afollestad)
 */
public class ATEPrimaryTextView extends AppCompatEditText {

    public ATEPrimaryTextView(Context context) {
        super(context);
        init(context);
    }

    public ATEPrimaryTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ATEPrimaryTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ATEPrimaryTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setTag("text_primary");
        ATE.apply(context, this);
    }
}
