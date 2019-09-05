package com.lib.bandaid.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lib.bandaid.R;
import com.lib.bandaid.util.ViewUtil;
import com.lib.bandaid.widget.snackbar.Snackbar;
import com.lib.bandaid.widget.snackbar.SnackbarManager;

/**
 * Created by zy on 2019/6/18.
 */

public abstract class BaseFragment extends Fragment {

    private Context context;
    private Activity activity;
    private int layoutId;
    private View layoutView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setContentView(@LayoutRes int id) {
        this.layoutId = id;
    }

    protected void setContentView(@NonNull View view) {
        this.layoutId = view.getId();
        this.layoutView = view;
    }

    protected abstract void initialize();

    protected abstract void registerEvent();

    protected abstract void initClass();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (layoutView == null) {
            context = getContext();
            activity = getActivity();
            layoutView = inflater.inflate(layoutId, null);
            initialize();
            registerEvent();
            initClass();
        }
        return layoutView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public <T extends View> T $(int resId) {
        return ViewUtil.findViewById(layoutView, resId);
    }

    public <T extends View> T findViewById(int resId) {
        return ViewUtil.findViewById(layoutView, resId);
    }


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
