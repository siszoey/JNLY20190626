package com.lib.bandaid.system.theme.aty;

import android.os.Bundle;
import androidx.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import com.lib.bandaid.system.theme.utils.ATE;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;


/**
 * @author Aidan Follestad (afollestad)
 */
public class ATEActivity extends RxAppCompatActivity {

    private long updateTime = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ATE.preApply(this);
        super.onCreate(savedInstanceState);
    }

    private void apply() {
        ATE.apply(this);
        updateTime = System.currentTimeMillis();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        apply();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        apply();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ATE.didValuesChange(this, updateTime))
            recreate();
    }
}