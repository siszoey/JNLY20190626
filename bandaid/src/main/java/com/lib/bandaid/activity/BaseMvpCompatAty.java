package com.lib.bandaid.activity;

import android.app.Activity;
import android.os.Bundle;

import com.blankj.utilcode.util.ToastUtils;
import com.lib.bandaid.data.remote.core.INetRequest;
import com.lib.bandaid.utils.DialogFactory;
import com.trello.rxlifecycle2.LifecycleTransformer;

import javax.inject.Inject;

public abstract class BaseMvpCompatAty<T extends INetRequest.BasePresenter> extends BaseAppCompatAty implements INetRequest.BaseView {

    @Inject
    protected T presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachView();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        detachView();
    }

    /**
     * 分离view
     */
    private void detachView() {
        if (presenter != null) {
            presenter.detachView();
        }
    }

    private void attachView() {
        if (presenter != null) {
            presenter.attachView(this);
        }
    }

    @Override
    public void showLoading() {
        DialogFactory.getFactory().get(this).show();
    }

    @Override
    public void showLoading(String msg) {
        DialogFactory.getFactory().get(this).show();
    }

    @Override
    public void hideLoading() {
        DialogFactory.getFactory().get(this).dismiss();
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
