package com.lib.bandaid.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.bandaid.activity.i.ITipView;
import com.lib.bandaid.data.remote.core.INetRequest;
//import com.lib.bandaid.utils.DialogFactory;
import com.lib.bandaid.util.DialogFactory;
import com.lib.bandaid.widget.layout.RootStatusView;
import com.trello.rxlifecycle2.LifecycleTransformer;

import javax.inject.Inject;

public abstract class BaseMvpCompatAty<T extends INetRequest.BasePresenter>
        extends BaseAppCompatActivity{

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



    //**********************************************************************************************
    protected void setOnRetryClickListener(View.OnClickListener listener) {
        rootStatusView.setOnRetryClickListener(listener);
    }

    protected void setOnViewStatusChangeListener(RootStatusView.OnViewStatusChangeListener listener) {
        rootStatusView.setOnViewStatusChangeListener(listener);
    }

    @Override
    public void showContent() {
        rootStatusView.showContent();
    }

    @Override
    public void showNoNetwork() {
        rootStatusView.showNoNetwork();
    }

    @Override
    public void showEmpty() {
        rootStatusView.showEmpty();
    }

    @Override
    public void showLoading() {
        rootStatusView.showLoading();
    }

    @Override
    public void showError() {
        rootStatusView.showError();
    }

    @Override
    public void dialogLoading() {
        waitDialog.show();
    }

    @Override
    public void dialogHiding() {
        waitDialog.dismiss();
    }
    //**********************************************************************************************

}
