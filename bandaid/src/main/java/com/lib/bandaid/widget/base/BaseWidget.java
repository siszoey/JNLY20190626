package com.lib.bandaid.widget.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lib.bandaid.R;
import com.lib.bandaid.activity.BaseActivity;
import com.lib.bandaid.activity.IOnActivityResult;
import com.lib.bandaid.utils.MeasureScreen;
import com.lib.bandaid.widget.snackbar.Snackbar;
import com.lib.bandaid.widget.snackbar.SnackbarManager;

import java.util.zip.Inflater;


/**
 * Created by zy on 2018/7/31.
 */

public abstract class BaseWidget implements IWidget, IOnActivityResult {
    /**
     *
     */
    protected int layoutGravity = 5;
    protected float w = 1f;
    protected float h = 1f;
    /**
     * 屏幕尺寸
     */
    protected int screenWidth;
    /**
     *
     */
    protected int screenHeight;
    /**
     * 组件的View
     */
    protected View view;
    /**
     *
     */
    protected Context context;
    /**
     *
     */
    protected Activity activity;


    public BaseWidget(Context context) {
        init(context);
    }


    protected void init(Context context) {
        beforeInitialize();
        this.context = context;
        this.activity = (Activity) context;

        /*if (activity instanceof BaseAppCompatActivity) {
            ((BaseAppCompatActivity) context).addIOnActivityResults(this);
        } else */
        if (activity instanceof BaseActivity) {
            ((BaseActivity) context).addIOnActivityResults(this);
        }

        screenWidth = MeasureScreen.getScreenWidth(context);
        screenHeight = MeasureScreen.getScreenHeight(context);
    }

    protected void setContentView(int layoutId) {
        view = View.inflate(context, layoutId, null);
        {
            if (view != null && layoutGravity != -1) {
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((int) (MeasureScreen.getScreenWidth(context) * w), (int) (MeasureScreen.getScreenHeight(context) * h));
                view.setLayoutParams(params);
            }
        }

        initialize();
        afterInitialize();
        registerEvent();
        initClass();
    }


    public void beforeInitialize() {

    }

    public void afterInitialize() {

    }

    public abstract void initialize();

    public abstract void registerEvent();

    public abstract void initClass();

    public void onDestroy() {

    }


    public <T extends View> T $(int resId) {
        return (T) view.findViewById(resId);
    }

    public View getView() {
        return view;
    }

    public void show() {
        view.setVisibility(View.VISIBLE);
    }

    public void hide() {
        view.setVisibility(View.GONE);
    }

    public void startActivityByRightSlide(Intent intent) {
        context.startActivity(intent);
        activity.overridePendingTransition(R.anim.in_from_right, 0);
    }

    public void startActivity(Intent intent) {
        context.startActivity(intent);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    public void finish() {
        ((Activity) context).finish();
    }

    public void overridePendingTransition(int arg0, int arg1) {
        activity.overridePendingTransition(arg0, arg1);
    }

    @Override
    public void widgetReady() {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    /**
     * 测量宽高
     *
     * @return
     */
    public int[] measure() {
        return new int[]{view.getWidth(), view.getHeight()};
    }

    public int getGravity() {
        return layoutGravity;
    }

    public void setLayoutGravity(int gravity) {
        layoutGravity = gravity;
    }


    /**
     * *********************************************************************************************
     * 提示框
     * *********************************************************************************************
     */
    public void showToast(Object info) {
        Toast.makeText(context, info + "", Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(Object info) {
        Toast.makeText(context, info + "", Toast.LENGTH_LONG).show();
    }

    public void showSnackBar(Object info) {
        SnackbarManager.show(
                Snackbar.with(context)
                        .text(info + "").duration(500));
    }

    public void showLongSnackBar(Object info) {
        SnackbarManager.show(
                Snackbar.with(context).position(Snackbar.SnackbarPosition.TOP)
                        .text(info + "").duration(1000)
        );
    }

    public void showError(Object info) {
        SnackbarManager.show(
                Snackbar.with(context).
                        position(Snackbar.SnackbarPosition.TOP)
                        .text(info + "")
                        .leftIcon(R.mipmap.widget_snackbar_common_error)
                        .duration(2000)
                        //.color(Color.argb(150, 255, 0, 0))
                        .textColor(Color.WHITE)
        );
    }

    public void showSuccess(Object info) {
        SnackbarManager.show(
                Snackbar.with(context).
                        position(Snackbar.SnackbarPosition.TOP)
                        .text(info + "")
                        .leftIcon(R.mipmap.widget_snackbar_common_success)
                        .duration(2000)
                        //.color(Color.argb(150, 0, 255, 0))
                        .textColor(Color.WHITE)
        );
    }

    public void showMsg(Object info) {
        SnackbarManager.show(
                Snackbar.with(context).
                        position(Snackbar.SnackbarPosition.TOP)
                        .text(info + "")
                        .leftIcon(R.mipmap.widget_snackbar_common_msg)
                        .duration(2000)
                        //.color(Color.argb(150, 0, 0, 255))
                        .textColor(Color.WHITE)
        );
    }

    public void showWarn(Object info) {
        SnackbarManager.show(
                Snackbar.with(context)
                        .position(Snackbar.SnackbarPosition.TOP)
                        .text(info + "")
                        .leftIcon(R.mipmap.widget_snackbar_common_warn)
                        .duration(2000)
                        //.color(Color.argb(150, 255, 128, 0))
                        .textColor(Color.WHITE)
        );
    }
}
