package com.lib.bandaid.system.theme.prefs;

import android.content.Context;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;
import android.view.View;

import com.lib.bandaid.R;
import com.lib.bandaid.system.theme.utils.ATE;


/**
 * @author Aidan Follestad (afollestad)
 */
public class ATECheckBoxPreference extends CheckBoxPreference {

    public ATECheckBoxPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ATECheckBoxPreference(Context context) {
        this(context, null, 0);
    }

    public ATECheckBoxPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutResource(R.layout.theme_preference_custom);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        ATE.apply(view.getContext(), view);
    }
}