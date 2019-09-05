package com.lib.bandaid.widget.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.lib.bandaid.system.theme.views.ATEProgressBar;
import com.lib.bandaid.util.MeasureScreen;

/**
 * Created by zy on 2019/4/24.
 */

public class WaitingDialog extends ProgressDialog {

    int width = 0;
    int height = 0;

    Integer count = 0;

    public WaitingDialog(Context context) {
        super(context);
    }

    public WaitingDialog(Context context, int theme) {
        super(context, theme);
    }

    public WaitingDialog(Context context, int width, int height) {
        super(context);
        this.width = width;
        this.height = height;
    }

    public WaitingDialog(Context context, int theme, int width, int height) {
        super(context, theme);
        this.width = width;
        this.height = height;
    }

    public WaitingDialog setH() {
        show();
        return this;
    }

    public void setProgress(double d) {
        setProgress((int) d);
    }

    public WaitingDialog setR() {
        setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(getContext());
    }

    private void init(Context context) {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(new ATEProgressBar(context));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = width == 0 ? WindowManager.LayoutParams.WRAP_CONTENT : MeasureScreen.dip2px(context, width);
        params.height = height == 0 ? WindowManager.LayoutParams.WRAP_CONTENT : MeasureScreen.dip2px(context, height);
        getWindow().setAttributes(params);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        super.dismiss();
    }

    @Override
    public void show() {
        synchronized (count) {
            count++;
        }
        if (count == 1) {
            super.show();
        }
    }

    @Override
    public void dismiss() {
        synchronized (count) {
            if (count > 0) count--;
        }
        if (count == 0) {
            super.dismiss();
        }
    }
}
