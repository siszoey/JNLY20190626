package com.lib.bandaid.activity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.lib.bandaid.R;
import com.lib.bandaid.widget.snackbar.Snackbar;
import com.lib.bandaid.widget.snackbar.SnackbarManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by zy on 2018/7/24.
 */

public abstract class BaseAppCompatActivity extends BaseAppCompatAty {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        /**
         * 公交车 注册
         */
        EventBus.getDefault().register(this);
    }

    //接收消息
    @Subscribe
    public void onEventMainThread(Object object) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            /**
             * 公交车 取消注册
             */
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * *********************************************************************************************
     * 提示框
     * *********************************************************************************************
     */
    public void showToast(Object info) {
        Toast.makeText(this, info + "", Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(Object info) {
        Toast.makeText(this, info + "", Toast.LENGTH_LONG).show();
    }

    public void showSnackBar(Object info) {
        /*SnackbarManager.show(
                Snackbar.with(this)
                        .text(info + "").duration(500), _frameLayout);*/
        SnackbarManager.show(
                Snackbar.with(this)
                        .text(info + "").duration(500));
    }

    public void showLongSnackBar(Object info) {
        /*SnackbarManager.show(
                Snackbar.with(this).position(Snackbar.SnackbarPosition.TOP)
                        .text(info + "").duration(1000), _frameLayout
        );*/
        SnackbarManager.show(
                Snackbar.with(this).position(Snackbar.SnackbarPosition.TOP)
                        .text(info + "").duration(1000)
        );
    }

    public void showError(Object info) {
        /*SnackbarManager.show(
                Snackbar.with(this).
                        position(Snackbar.SnackbarPosition.TOP)
                        .text(info + "")
                        .leftIcon(R.mipmap.widget_snackbar_common_error)
                        .duration(2000)
                        //.color(Color.argb(150, 255, 0, 0))
                        .textColor(Color.WHITE)
                , _frameLayout
        );*/
        SnackbarManager.show(
                Snackbar.with(this).
                        position(Snackbar.SnackbarPosition.TOP)
                        .text(info + "")
                        .leftIcon(R.mipmap.widget_snackbar_common_error)
                        .duration(2000)
                        //.color(Color.argb(150, 255, 0, 0))
                        .textColor(Color.WHITE)
        );
    }

    public void showSuccess(Object info) {
        /*SnackbarManager.show(
                Snackbar.with(this).
                        position(Snackbar.SnackbarPosition.TOP)
                        .text(info + "")
                        .leftIcon(R.mipmap.widget_snackbar_common_success)
                        .duration(2000)
                        //.color(Color.argb(150, 0, 255, 0))
                        .textColor(Color.WHITE)
                , _frameLayout
        );*/
        SnackbarManager.show(
                Snackbar.with(this).
                        position(Snackbar.SnackbarPosition.TOP)
                        .text(info + "")
                        .leftIcon(R.mipmap.widget_snackbar_common_success)
                        .duration(2000)
                        //.color(Color.argb(150, 0, 255, 0))
                        .textColor(Color.WHITE)
        );
    }

    public void showMsg(Object info) {
        /*SnackbarManager.show(
                Snackbar.with(this).
                        position(Snackbar.SnackbarPosition.TOP)
                        .text(info + "")
                        .leftIcon(R.mipmap.widget_snackbar_common_msg)
                        .duration(2000)
                        //.color(Color.argb(150, 0, 0, 255))
                        .textColor(Color.WHITE)
                , _frameLayout
        );*/
        SnackbarManager.show(
                Snackbar.with(this).
                        position(Snackbar.SnackbarPosition.TOP)
                        .text(info + "")
                        .leftIcon(R.mipmap.widget_snackbar_common_msg)
                        .duration(2000)
                        //.color(Color.argb(150, 0, 0, 255))
                        .textColor(Color.WHITE)
        );
    }

    public void showWarn(Object info) {
        /*SnackbarManager.show(
                Snackbar.with(this)
                        .position(Snackbar.SnackbarPosition.TOP)
                        .text(info + "")
                        .leftIcon(R.mipmap.widget_snackbar_common_warn)
                        .duration(2000)
                        //.color(Color.argb(150, 255, 128, 0))
                        .textColor(Color.WHITE)
                , _frameLayout, true
        );*/
        SnackbarManager.show(
                Snackbar.with(this)
                        .position(Snackbar.SnackbarPosition.TOP)
                        .text(info + "")
                        .leftIcon(R.mipmap.widget_snackbar_common_warn)
                        .duration(2000)
                        //.color(Color.argb(150, 255, 128, 0))
                        .textColor(Color.WHITE)
        );
    }
}
