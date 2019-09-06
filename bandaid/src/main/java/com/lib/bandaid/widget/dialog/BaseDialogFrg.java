package com.lib.bandaid.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.appbar.AppBarLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lib.bandaid.R;
import com.lib.bandaid.util.DialogFactory;
import com.lib.bandaid.util.MeasureScreen;
import com.lib.bandaid.util.ViewUtil;
import com.lib.bandaid.widget.dialog.i.IView;
import com.lib.bandaid.widget.layout.RootStatusView;


/**
 * Created by zy on 2019/4/21.
 */

public abstract class BaseDialogFrg extends DialogFragment implements IView {

    protected Context context;

    private int layoutId;

    private View layout;

    MaterialDialog dialog;
    /**
     * 对话框计数器
     */
    Integer dialogCount = 0;

    protected float w = 0.85f;
    protected float h = 0.6f;

    protected AppBarLayout _appBarLayout;
    protected Toolbar _toolbar;
    protected TextView _tvToolbarName;
    protected RootStatusView _frameLayout;
    protected View _contentView;
    protected Button _btnRight;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public void onStart() {
        super.onStart();
        adjustActivitySize(w, h);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.widget_base_dialog_fragment, null);
        if (_contentView == null) _contentView = inflater.inflate(layoutId, null);
        init();
        initialize();
        registerEvent();
        initClass();
        return layout;
    }

    private void init() {
        _appBarLayout = $(R.id._appBarLayout);
        _toolbar = $(R.id._toolbar);
        _tvToolbarName = $(R.id._tvToolbarName);
        _frameLayout = $(R.id._frameLayout);
        _btnRight = $(R.id.btnRight);
        _frameLayout.addView(_contentView);
        _appBarLayout.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));

        _toolbar.setTitle("");
        _tvToolbarName.setGravity(gravity);
        _tvToolbarName.setText(name);
        if (leftIcon == null) leftIcon = R.drawable.ic_close;
        _toolbar.setNavigationIcon(leftIcon);
        _toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    Integer leftIcon;
    String name;
    int gravity;

    protected void initTitle(Integer leftIcon, String name, int gravity) {
        this.leftIcon = leftIcon;
        this.name = name;
        this.gravity = gravity;

    }

    protected abstract void initialize();

    protected abstract void registerEvent();

    protected abstract void initClass();

    public void setContentView(@LayoutRes int id) {
        this.layoutId = id;
    }

    public void setContentView(View view) {
        this._contentView = view;
    }

    public <T extends View> T $(int resId) {
        return ViewUtil.findViewById(layout, resId);
    }


    protected void showWaitDialog() {
        synchronized (dialogCount) {
            dialogCount++;
        }
        if (dialogCount == 1 && !dialog.isShowing() && !isHidden()) {
            dialog.show();
        }
    }

    protected void hideWaitDialog() {
        synchronized (dialogCount) {
            if (dialogCount > 0) dialogCount--;
        }
        if (dialogCount == 0 && dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public BaseDialogFrg show(@NonNull FragmentManager manager) {
        if (manager == null) return this;
        super.show(manager, "");
        return this;
    }

    public BaseDialogFrg show(@NonNull Context context) {
        FragmentManager manager = null;
        if (context instanceof FragmentActivity) {
            manager = ((FragmentActivity) context).getSupportFragmentManager();
        }
        if (manager == null) return this;
        super.show(manager, "");
        return this;
    }

    protected void adjustActivitySize(float w, float h) {
        Dialog dialog = getDialog();
        if (dialog == null) return;
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();

        if (w < 0) {
            lp.width = (int) w;
        } else {
            lp.width = (int) (MeasureScreen.getScreenWidth(context) * w);
        }
        if (h < 0) {
            lp.height = (int) h;
        } else {
            lp.height = (int) (MeasureScreen.getScreenHeight(context) * h);
        }
        if (window != null) window.setLayout(lp.width, lp.height);
    }

    //----------------------------------------------------------------------------------------------
    protected void setOnRetryClickListener(View.OnClickListener listener) {
        _frameLayout.setOnRetryClickListener(listener);
    }

    protected void setOnViewStatusChangeListener(RootStatusView.OnViewStatusChangeListener listener) {
        _frameLayout.setOnViewStatusChangeListener(listener);
    }

    //----------------------------------------------------------------------------------------------
    @Override
    public void showContent() {
        _frameLayout.showContent();
    }

    @Override
    public void showNoNetwork() {
        _frameLayout.showNoNetwork();
    }

    @Override
    public void showEmpty() {
        _frameLayout.showEmpty();
    }

    @Override
    public void showLoading() {
        _frameLayout.showLoading();
    }

    @Override
    public void showError() {
        _frameLayout.showError();
    }

    @Override
    public void dialogLoading() {
        DialogFactory.getFactory().show(getActivity());
    }

    @Override
    public void dialogHiding() {
        DialogFactory.getFactory().dismiss(getActivity());
    }


    //----------------------------------------------------------------------------------------------

    public interface ICallBack<T> {
        public void callback(T t);
    }

    protected ICallBack iCallBack;

    public void setCallBack(ICallBack iCallBack) {
        this.iCallBack = iCallBack;
    }
}
