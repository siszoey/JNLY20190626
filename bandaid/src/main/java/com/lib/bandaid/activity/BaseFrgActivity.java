package com.lib.bandaid.activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.lib.bandaid.R;
import com.lib.bandaid.utils.ViewUtil;
import com.lib.bandaid.widget.snackbar.Snackbar;
import com.lib.bandaid.widget.snackbar.SnackbarManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2019/5/31.
 */

public abstract class BaseFrgActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 禁止横屏
        setVolumeControlStream(AudioManager.STREAM_MUSIC);// 使得音量键控制媒体声音
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initialize();
        registerEvent();
        initClass();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iOnActivityResults != null) {
            iOnActivityResults.clear();
        }
        iOnActivityResults = null;
    }

    protected abstract void initialize();

    protected abstract void registerEvent();

    protected abstract void initClass();


    /**
     * *********************************************************************************************
     * activity结果回调
     * *********************************************************************************************
     */
    private List<IOnActivityResult> iOnActivityResults;

    public void addIOnActivityResults(IOnActivityResult iOnActivityResult) {
        if (iOnActivityResults == null) iOnActivityResults = new ArrayList<>();
        if (iOnActivityResults.contains(iOnActivityResult)) return;
        iOnActivityResults.add(iOnActivityResult);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (iOnActivityResults != null) {
            for (IOnActivityResult iOnActivityResult : iOnActivityResults) {
                iOnActivityResult.onActivityResult(requestCode, resultCode, data);
            }
        }
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
        SnackbarManager.show(
                Snackbar.with(this)
                        .text(info + "").duration(500));
    }

    public void showLongSnackBar(Object info) {
        SnackbarManager.show(
                Snackbar.with(this).position(Snackbar.SnackbarPosition.TOP)
                        .text(info + "").duration(1000)
        );
    }

    public void showError(Object info) {
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

    public <T extends View> T $(int resId) {
        return ViewUtil.findViewById(this, resId);
    }

}
