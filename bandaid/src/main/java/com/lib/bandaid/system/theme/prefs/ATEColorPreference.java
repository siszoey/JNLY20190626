package com.lib.bandaid.system.theme.prefs;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;

import com.lib.bandaid.R;
import com.lib.bandaid.system.theme.utils.ATE;


/**
 * @author Aidan Follestad (afollestad)
 */
public class ATEColorPreference extends Preference {

    private View mView;
    private int color;
    private int border;

    public ATEColorPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ATEColorPreference(Context context) {
        this(context, null, 0);
    }

    public ATEColorPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutResource(R.layout.theme_preference_custom);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        mView = view;
        ATE.apply(view.getContext(), view);
        invalidateColor();
    }

    public void setColor(int color, int border) {
        this.color = color;
        this.border = border;
        invalidateColor();
    }

    private void invalidateColor() {
        if (mView != null) {
            BorderCircleView circle = (BorderCircleView) mView.findViewById(R.id.circle);
            if (this.color != 0) {
                circle.setVisibility(View.VISIBLE);
                circle.setBackgroundColor(color);
                circle.setBorderColor(border);
            } else {
                circle.setVisibility(View.GONE);
            }
        }
    }
}