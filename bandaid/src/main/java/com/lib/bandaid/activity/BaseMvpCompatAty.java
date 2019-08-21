package com.lib.bandaid.activity;

import android.app.Activity;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.bandaid.data.remote.core.INetRequest;
//import com.lib.bandaid.utils.DialogFactory;
import com.lib.bandaid.utils.DialogFactory;
import com.trello.rxlifecycle2.LifecycleTransformer;

import javax.inject.Inject;

public abstract class BaseMvpCompatAty<T extends INetRequest.BasePresenter> extends BaseAppCompatActivity implements INetRequest.BaseView {

    protected MaterialDialog waitDialog;

    @Inject
    protected T presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        waitDialog = DialogFactory.createDialogUnCancel(this);
        attachView();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        detachView();
    }

    private void attachView() {
        if (presenter != null) presenter.attachView(this);
    }

    /**
     * 分离view
     */
    private void detachView() {
        if (presenter != null) presenter.detachView();
    }

    @Override
    public void showLoading() {
        waitDialog.show();
        //DialogFactory.getFactory().show(this);
    }

    @Override
    public void showLoading(String msg) {
        waitDialog.show();
        //DialogFactory.getFactory().show(this);
    }

    @Override
    public void hideLoading() {
        waitDialog.dismiss();
        //DialogFactory.getFactory().dismiss(this);
    }

    @Override
    public void showSuccess(String message) {
        ToastUtils.showShort(message);
    }

    @Override
    public void showFail(String message) {
        ToastUtils.showShort(message);
    }

    @Override
    public void showNoNet() {
        ToastUtils.showShort("无网络!");
    }

    @Override
    public void onRetry() {

    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
